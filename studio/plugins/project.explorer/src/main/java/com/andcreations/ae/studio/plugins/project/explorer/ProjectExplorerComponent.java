package com.andcreations.ae.studio.plugins.project.explorer;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileFilter;
import java.util.List;

import javax.swing.JPanel;

import com.andcreations.ae.studio.plugins.appicon.AppIcon;
import com.andcreations.ae.studio.plugins.appicon.AppIconListener;
import com.andcreations.ae.studio.plugins.file.EditedFile;
import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeComponent;
import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeComponentAdapter;
import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeNode;
import com.andcreations.ae.studio.plugins.project.ProjectProperties;
import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;
import com.andcreations.ae.studio.plugins.ui.main.StatusBar;
import com.andcreations.io.FileUtil;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class ProjectExplorerComponent extends JPanel {
    /** */
    private BundleResources res =
        new BundleResources(ProjectExplorerComponent.class);
    
    /** */
    private FileTreeComponent fileTreeComponent;
    
    /** */
    ProjectExplorerComponent(File root) {
        create(root);
    }
    
    /** */
    private void create(File root) {
        setLayout(new BorderLayout());
        
    // project file filter
        FileFilter fileFilter = new FileFilter() {
            /** */
            public boolean accept(File file) {
                if (file.getName().equals(ProjectFiles.STORAGE_DIR)) {
                    return false;
                }
                if (FileUtil.hasAncestor(file,ProjectFiles.STORAGE_DIR)) {
                    return false;
                }
                
                return true;
            }
        };
        
        String appName = ProjectProperties.get().getAppName();
    // tree
        fileTreeComponent = new FileTreeComponent(root,appName,fileFilter);
        add(fileTreeComponent,BorderLayout.CENTER);
        
    // listener
        fileTreeComponent.addFileTreeComponentListener(
            new FileTreeComponentAdapter() {
                /** */
                @Override
                public void fileTreeNodeSelectionChanged(
                    FileTreeComponent component,List<FileTreeNode> nodes) {
                //
                    fileTreeNodesSelected(nodes);
                }
            }
        );
        
        AppIcon.get().addAppIconListener(new AppIconListener() {
            /** */
            @Override
            public void appIconChanged() {
            }
        });
    }
    
    /** */
    private void setStatusBar(List<FileTreeNode> nodes) {
        if (nodes.isEmpty() == true) {
            StatusBar.get().setInfo("");
        }
        else if (nodes.size() == 1) {
            FileTreeNode node = nodes.get(0);
            File file = node.getFile();
            StatusBar.get().setInfo(file.getAbsolutePath());
        }
        else {
            StatusBar.get().setInfo(res.getStr("n.files.dirs",
                Integer.toString(nodes.size())));
        }
    }
    
    /** */
    private void fileTreeNodesSelected(List<FileTreeNode> nodes) {
        setStatusBar(nodes);
    }
    
    /** */
    void viewFocusGained() {
        setStatusBar(fileTreeComponent.getSelectedNodes());
    }
    
    /** */
    void syncWithEditor() {
        File file = EditedFile.get().getLastEditedFile();
        if (file == null) {
            return;
        }
        fileTreeComponent.goTo(file);
    }
}