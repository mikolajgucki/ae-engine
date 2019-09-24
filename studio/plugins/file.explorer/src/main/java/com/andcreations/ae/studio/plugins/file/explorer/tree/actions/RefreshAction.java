package com.andcreations.ae.studio.plugins.file.explorer.tree.actions;

import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeNode;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.io.FileNodeSync;
import com.andcreations.io.FileNodeSyncDiff;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class RefreshAction extends AbstractPopupAction {
    /** */
    private BundleResources res = new BundleResources(RefreshAction.class);
    
    /** */
    RefreshAction() {
        create();
    }
    
    /** */
    private void create() {
        setText(res.getStr("action.name"));        
        setIcon(Icons.getIcon(DefaultIcons.REFRESH));
    }
    
    /** */
    @Override
    public boolean canPerform(FileTreeNode root,List<FileTreeNode> nodes) {
        return nodes.size() == 1;
    }
    
    /** */
    private List<FileNodeSyncDiff> sync(FileTreeNode node) {
        List<FileNodeSyncDiff> diffs = new ArrayList<>();
        node.getFileNode().syncTree(diffs);
        FileNodeSync.buildFullTree(diffs);
        FileNodeSync.sort(diffs);
        
        return diffs;
    }
    
    /** */
    @Override
    public void perform(FileTreeNode root,List<FileTreeNode> nodes) {
        List<FileNodeSyncDiff> diffs = sync(nodes.get(0));
        for (FileNodeSyncDiff diff:diffs) {
            if (diff.getType() == FileNodeSyncDiff.Type.ADDED) {
                if (diff.getFile().isFile() == true) {
                    Files.get().notifyFileCreated(diff.getFile());
                }
                if (diff.getFile().isDirectory() == true) {
                    Files.get().notifyDirCreated(diff.getFile());
                }
            }
            if (diff.getType() == FileNodeSyncDiff.Type.DELETED) {
                Files.get().notifyFileDeleted(diff.getFile());
            }
        }
    }
}