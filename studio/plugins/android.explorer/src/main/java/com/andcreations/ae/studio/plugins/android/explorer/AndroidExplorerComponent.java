package com.andcreations.ae.studio.plugins.android.explorer;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.andcreations.ae.studio.plugins.android.AndroidProjectDir;
import com.andcreations.ae.studio.plugins.android.AndroidSettings;
import com.andcreations.ae.studio.plugins.android.AndroidSettingsAdapter;
import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeComponent;
import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.io.FileTree;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class AndroidExplorerComponent extends JPanel {
    /** */
    private BundleResources res =
        new BundleResources(AndroidExplorerComponent.class);
        
    /** */
    private String errorColor;
    
    /** */
    private Thread initTreeThread;

    /** */
    private JLabel infoLabel;
    
    /** */
    private FileTreeComponent fileTreeComponent;
        
    /** */
    AndroidExplorerComponent() {
        create();
    }
    
    /** */
    private void create() {
        setLayout(new BorderLayout());
        
    // colors
        errorColor = UIColors.toHex(UIColors.error());
        
    // Android project directory issue
        infoLabel = new JLabel();
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        
        AndroidSettings.get().addAndroidSettingsListener(
            new AndroidSettingsAdapter() {
                /** */
                @Override
                public void androidProjectDirChanged(String dir) {
                    AndroidExplorerComponent.this.androidProjectDirChanged();
                }
            });
        
    // initialize
        androidProjectDirChanged();
    }
    
    /** */
    private void setInfo(String info) {
        if (fileTreeComponent != null) {
            remove(fileTreeComponent);
        }
        
        infoLabel.setText(info);
        add(infoLabel,BorderLayout.CENTER);
    }
    
    /** */
    private void setIssue(String issue) {
        setInfo(res.getStr("dir.issue.html",issue,errorColor));
    }
    
    /** */
    private void setFileTree(FileTree fileTree) {
        fileTreeComponent = new FileTreeComponent(fileTree,
            res.getStr("android.project"),null);
        remove(infoLabel);
        add(fileTreeComponent);
    }
    
    /** */
    private void initTree(File dir) {
        final FileTree fileTree = FileTree.build(dir,null);
        fileTree.sort();
        
        synchronized (this) {
            UICommon.invokeAndWait(new Runnable() {
                /** */
                @Override
                public void run() {
                    setFileTree(fileTree);
                }
            });
            initTreeThread = null;
        }
    }
    
    /** */
    private synchronized void androidProjectDirChanged() {
        final File dir = AndroidProjectDir.get().getAndroidProjectDir();
        
    // issue
        String issue = AndroidProjectDir.validate(dir);
        if (issue != null) {
            setIssue(issue);
            return;
        }
        
    // if already initializing
        if (initTreeThread != null) {
            return;
        }
        
    // initialize
        setInfo(res.getStr("initializing"));
        initTreeThread = new Thread(new Runnable() {
            /** */
            @Override
            public void run() {
                initTree(dir);
            }
        });
        initTreeThread.start();
    }
    
    /** */
    void viewFocusGained() {
    }
}