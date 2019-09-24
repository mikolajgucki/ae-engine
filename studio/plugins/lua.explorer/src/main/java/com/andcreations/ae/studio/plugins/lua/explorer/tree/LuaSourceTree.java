package com.andcreations.ae.studio.plugins.lua.explorer.tree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.jdesktop.swingx.JXTree;

import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFilesListener;
import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.main.StatusBar;
import com.andcreations.io.FileNode;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class LuaSourceTree extends JXTree {
    /** */
    private BundleResources res = new BundleResources(LuaSourceTree.class);
        
    /** */
    private LuaSourceTreeModel model;
    
    /** */
    private List<LuaSourceTreeListener> listeners = new ArrayList<>();
    
    /** */
    private LuaSourceTreePopupBuilder popupBuilder;
    
    /** */
    public LuaSourceTree() {
        create();
    }
    
    /** */
    private void create() {
    // root
        LuaSourceTreeRootNode root = new LuaSourceTreeRootNode(
            ProjectLuaFiles.get().getLuaSourceTree().getRoot());
        root.setUserObject(res.getStr("root.node.text"));
        
    // model
        model = new LuaSourceTreeModel(root);
        setModel(model);
        setLargeModel(true);
        
    // selection
        getSelectionModel().setSelectionMode(
            TreeSelectionModel.SINGLE_TREE_SELECTION);
        
    // renderer
        setCellRenderer(new LuaSourceTreeCellRenderer());
        
    // listeners
        createTreeMouseListener();
        createTreeSelectionListener();
        createLuaProjectFilesListener();
        
    // tooltip
        ToolTipManager.sharedInstance().registerComponent(this);
        
    // popup
        popupBuilder = new LuaSourceTreePopupBuilder();
    }
    
    /** */
    private void createTreeMouseListener() {
        addMouseListener(new MouseAdapter() {
            /** */
            @Override
            public void mousePressed(MouseEvent event) {
                if (showPopup(event) == true) {
                    return;
                }
                
                LuaSourceTreeNode node = getNodeAt(event.getX(),event.getY());
                if (node == null || node.getFileNode() == null) {
                    return;
                }
                if (event.getClickCount() == 2) {
                    notifyFileNodeDoubleClicked(node.getFileNode());
                }
            }
            
            /** */
            @Override
            public void mouseReleased(MouseEvent event) {
                if (showPopup(event) == true) {
                    return;
                }
            }
        });
    }
    
    /** */
    private void createTreeSelectionListener() {
        addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent event) {
                LuaSourceTreeNode node =
                    (LuaSourceTreeNode)getLastSelectedPathComponent();
                if (node == null) {
                    return;
                }        
                luaSourceTreeNodeSelected(node);
            }
        });
    }

    /** */
    private void createLuaProjectFilesListener() {
        ProjectLuaFilesListener listener = new ProjectLuaFilesListener() {
            /** */
            @Override
            public void luaSourceFileNodeChanged(FileNode node) {
                repaint();
            }
            
            /** */
            @Override
            public void luaSourceFileNodeAdded(FileNode parentNode,
                FileNode node) {
            //
                fileNodeAdded(parentNode,node);
            }
            
            /** */
            @Override
            public void luaSourceFileNodeRemoved(FileNode parentNode,
                FileNode node,int index) {
            //
                fileNodeRemoved(parentNode,node,index);
            }
        };
        
        ProjectLuaFiles.get().addProjectLuaFilesListener(listener);
    }
    
    /** */
    private void setStatusBar(LuaSourceTreeNode node) {
        if (node.getFileNode() == null) {
            return;
        }
        
    // error
        String error = ProjectLuaFiles.getError(node.getFileNode());
        if (error != null) {
            StatusBar.get().setInfo(error,UIColors.error());
        }
        else {
        // no error            
            File file = node.getFileNode().getFile();
            if (file != null) {
                StatusBar.get().setInfo(file.getAbsolutePath());
            }
            else {
                String count = Integer.toString(
                    node.getFileNode().getFileCount());
                if (node.getFileNode().isDirectory() == true) {
                    StatusBar.get().setInfo(
                        res.getStr("n.dirs",count));                                    
                }
                else if (node.getFileNode().isFile() == true) {
                    StatusBar.get().setInfo(
                        res.getStr("n.files",count));                                    
                }
                else {
                    StatusBar.get().setInfo(
                        res.getStr("n.files.dirs",count));                                    
                }
            }
        }  
    }
    
    /** */
    private void luaSourceTreeNodeSelected(LuaSourceTreeNode node) {
        setStatusBar(node);
    }
    
    /** */
    private void fileNodeAdded(final FileNode parentNode,final FileNode node) {
        UICommon.invoke(new Runnable() {
            /** */
            @Override
            public void run() {        
                int index = parentNode.indexOf(node);
                if (index < 0) {
                    throw new IllegalStateException("Invalid file node index");
                }
                
            // create
                LuaSourceTreeNode parentTreeNode = model.findNode(parentNode);
                LuaSourceTreeNode treeNode = new LuaSourceTreeNode(node);
                
            // build tree
                model.buildTree(treeNode);
                
            // insert, notify
                parentTreeNode.insert(treeNode,index);
                model.nodesWereInserted(parentTreeNode,new int[]{index});
            }
        });
    }
    
    /** */
    private void fileNodeRemoved(final FileNode parentNode,final FileNode node,
        final int index) {
    //
        UICommon.invoke(new Runnable() {
            /** */
            @Override
            public void run() {
            // find
                LuaSourceTreeNode parentTreeNode = model.findNode(parentNode);
                LuaSourceTreeNode treeNode = model.findNode(node);     
                
            // remove, notify
                parentTreeNode.remove(index);
                model.nodesWereRemoved(parentTreeNode,new int[]{index},
                    new LuaSourceTreeNode[]{treeNode});
            }
        });
    }
    
    /** */
    private LuaSourceTreeNode getNodeAt(int x,int y) {
        TreePath path = getPathForLocation(x,y);
        if (path == null) {
            return null;
        }
        return (LuaSourceTreeNode)path.getLastPathComponent();
    }
    
    /** */
    private boolean showPopup(MouseEvent event) {
        if (event.isPopupTrigger() == false) {
            return false;
        }
        
        LuaSourceTreeNode node = getNodeAt(event.getX(),event.getY());
        JPopupMenu popup = popupBuilder.build(node);
        popup.show(this,event.getX(),event.getY());
        
        return true;
    }
    
    /** */
    public LuaSourceTreeModel getLuaSourceTreeModel() {
        return model;
    }
    
    /** */
    public void addLuaSourceTreeListener(LuaSourceTreeListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    private void notifyFileNodeDoubleClicked(FileNode fileNode) {
        synchronized (listeners) {
            for (LuaSourceTreeListener listener:listeners) {
                listener.fileNodeDoubleClicked(fileNode);
            }
        }
    }
    
    /** */
    public void goTo(File file) {
        LuaSourceTreeNode node = model.findNode(file);
        if (node == null) {
            return;
        }
        
        Object[] elements = model.getPathToRoot(node);
        TreePath path = new TreePath(elements);
        expandPath(path);
        setSelectionPath(path);
        scrollPathToVisible(path);
    }
    
    /** */
    public void viewFocusGained() {        
        TreePath path = getSelectionPath();
        if (path == null) {
            return;
        }
        Object last = path.getLastPathComponent();
        if (last == null) {
            return;
        }
        
        LuaSourceTreeNode node = (LuaSourceTreeNode)last;
        setStatusBar(node);
    }
}