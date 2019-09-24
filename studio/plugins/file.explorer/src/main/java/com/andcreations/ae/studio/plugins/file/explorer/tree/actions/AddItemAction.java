package com.andcreations.ae.studio.plugins.file.explorer.tree.actions;

import java.io.File;
import java.util.List;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeNode;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;

/**
 * @author Mikolaj Gucki
 */
abstract class AddItemAction extends AbstractPopupAction {
    /** */
    private AddItemDialog dialog;
    
    /** */
    protected AddItemAction() {
    }
    
    /** */
    protected void create(String actionName,ImageIcon actionIcon,
        final String dialogTitle) {
    // action
        setText(actionName);
        setIcon(actionIcon);

    // dialog
        UICommon.invokeAndWait(new Runnable() {
        	/** */
        	@Override
        	public void run() {
        		dialog = new AddItemDialog(dialogTitle);
        	}
        });
    }
    
    /** */
    @Override
    public boolean canPerform(FileTreeNode root,List<FileTreeNode> nodes) {
        return nodes == null || nodes.isEmpty() == true || nodes.size() == 1;
    }
    
    /** */
    private FileTreeNode getParentNode(FileTreeNode root,
        List<FileTreeNode> nodes) {
    //
        if (nodes == null || nodes.isEmpty() == true) {
            return root;
        }
        
        FileTreeNode node = nodes.get(0);
        File file = node.getFile();
        
        if (file.isDirectory() == true) {
            return node;
        }
        return node.getParentNode();
    }
    
    /** */
    protected abstract void addItem(File file);
    
    /** */
    public void perform(FileTreeNode root,List<FileTreeNode> nodes) {
        FileTreeNode parentNode = getParentNode(root,nodes);
        File parentDir = parentNode.getFile();
        
        String name = dialog.showDialog(parentDir.getAbsolutePath());
        if (name != null) {
            File file = new File(parentDir,name);
            addItem(file);
        }            
    }
}