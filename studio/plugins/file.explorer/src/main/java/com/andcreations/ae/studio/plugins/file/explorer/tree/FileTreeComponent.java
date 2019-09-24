package com.andcreations.ae.studio.plugins.file.explorer.tree;

import java.awt.BorderLayout;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.andcreations.ae.studio.plugins.file.FileAdapter;
import com.andcreations.ae.studio.plugins.file.FileIssue;
import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTree;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeListener;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeModel;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNode;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNodeAdapter;
import com.andcreations.io.FileNode;
import com.andcreations.io.FileTree;
import com.andcreations.io.FileUtil;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class FileTreeComponent extends JPanel {
    /** */
    private static List<FileTreeNodeRenderer> registeredRenderers =
        new ArrayList<>();
        
    /** */
    private static List<FileTreeNodeListener> registeredListeners =
        new ArrayList<>(); 
    
    /** */
    private File root;
    
    /** */
    private FileFilter fileFilter;
    
    /** */
    private DefaultFileTreeNodeRenderer defaultRenderer;
    
    /** */
    private DefaultFileTreeNodeListener defaultListener;
    
    /** */
    private FileTree fileTree;
    
    /** */
    private LabelTree tree;
    
    /** */
    private LabelTreeModel model;
    
    /** */
    private FileTreeNode rootTreeNode;    
    
    /** */
    private Map<File,FileTreeNode> treeNodes = new HashMap<>();
    
    /** */
    private List<FileTreeComponentListener> listeners = new ArrayList<>();
    
    /** */
    private JPopupMenu popup;
    
    /** */
    private DefaultPopupMenuBuilder defaultPopupMenuBuilder;
    
    /** */
    private FileTreeDnD fileTreeDnD;
    
    /** */
    public FileTreeComponent(File root,String rootName,FileFilter fileFilter) {
        this.root = root;
        this.fileFilter = fileFilter;
        create(rootName);
    }
    
    /** */
    public FileTreeComponent(FileTree fileTree,String rootName,
        FileFilter fileFilter) {
    //
        this.fileTree = fileTree;
        this.fileFilter = fileFilter;
        create(rootName);
    }
    
    /** */
    private void create(String rootName) {
        setLayout(new BorderLayout());
        
    // defaults
        defaultRenderer = new DefaultFileTreeNodeRenderer();
        defaultListener = new DefaultFileTreeNodeListener();
        
    // file tree
        if (fileTree == null) {
            fileTree = FileTree.build(root,fileFilter);
            fileTree.sort();
        }
        
    // tree
        buildTree(rootName);
        createTreeMouseListener();
        createTreeSelectionListener();
        
    // model
        model = (LabelTreeModel)tree.getModel();
        
    // tree scroll
        JScrollPane treeScroll = new JScrollPane(tree);        
        add(treeScroll,BorderLayout.CENTER);
        
    // popup
        popup = new JPopupMenu();
        defaultPopupMenuBuilder = new DefaultPopupMenuBuilder();
        
    // drag-and-drop
        fileTreeDnD = new FileTreeDnD(tree);
        
    // tree listener
        tree.addLabelTreeListener(new LabelTreeListener() {
            /** */
            @Override
            public boolean dragGestureRecognized(DragGestureEvent event) {
                return fileTreeDnD.dragGestureRecognized(event);
            }
        });                
        
    // file listener
        Files.get().addFileListener(new FileAdapter() {
            /** */
            @Override    
            public void fileIssuesChanged(File file,List<FileIssue> issues) {
                tree.repaint();
            }
            
            /** */
            @Override
            public void fileCreated(File file) {
                FileTreeComponent.this.fileCreated(file);
            }
            
            /** */
            @Override
            public void fileDeleted(File file) {                
                FileTreeComponent.this.fileDeleted(file);
            }
            
            /** */
            @Override
            public void dirCreated(File dir) {                
                FileTreeComponent.this.fileCreated(dir);
            }
            
            /** */
            @Override
            public void dirDeleted(File dir) {                
                FileTreeComponent.this.fileDeleted(dir);
            }   
            
            /** */
            @Override
            public void fileRenamed(File src,File dst) {
                FileTreeComponent.this.fileRenamed(src,dst);
            }
        });        
    }
    
    /** */
    private void addNodeToParentNode(FileTreeNode treeNode,
        FileNode parentNode) {
    //
        FileNode node = treeNode.getFileNode();
        File file = node.getFile();
        
        parentNode.addChildNode(node,fileTree.getDefaultComparator());
        LabelTreeNode parentTreeNode = treeNodes.get(file.getParentFile());
        
        int index = parentNode.indexOf(node);        
        parentTreeNode.insert(treeNode,index);
        model.nodesWereInserted(parentTreeNode,new int[]{index}); 
    }
    
    /** */
    private void addFileToParentNode(File file) {
    // file node
        FileNode parentNode = fileTree.findParentNode(file);
        FileNode node = new FileNode(file);
        
    // add to parent
        FileTreeNode treeNode = createFileTreeNode(node);
        addNodeToParentNode(treeNode,parentNode);
    }
    
    /** */
    private void fileCreated(File createdFile) {
        if (fileFilter != null && fileFilter.accept(createdFile) == false) {
            return;
        }
        
        final File file = FileUtil.canonical(createdFile);       
    // if already exists        
        if (fileTree.findNode(file) != null) {
            return;
        }        
        
        final List<File> parentDirs = new ArrayList<>();        
        File parent = file.getParentFile();
    // find the existing descendant
        while (true) {
            if (fileTree.findNode(parent) != null) {
                break;
            }
            
            parentDirs.add(0,parent);
            parent = parent.getParentFile();
            
        // if outside the tree
            if (parent == null) {
                return;
            }
        }
        
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {        
            // add non-existing parent directories
                for (File parentDir:parentDirs) {
                    addFileToParentNode(parentDir);            
                }
                
            // add the created file
                addFileToParentNode(file);
            }
        });
    }
    
    /** */
    private void fileDeleted(final File deletedFile) {
        UICommon.invoke(new Runnable() {
            /** */
            @Override
            public void run() {
                if (fileFilter != null &&
                    fileFilter.accept(deletedFile) == false) {
                //
                    return;
                }
                
                File file = FileUtil.canonical(deletedFile);
            // if doesn't exist        
                FileNode node = fileTree.findNode(file);
                if (node == null) {
                    return;
                }
                
            // remove file node
                FileNode parentNode = node.getParent();
                if (parentNode == null) {
                // attempt to remove root 
                    throw new IllegalArgumentException(
                        "Cannot remove root node");
                }
                int index = parentNode.indexOf(node);
                parentNode.removeChildNode(node);
                
            // remove tree node
                FileTreeNode parentTreeNode = treeNodes.get(
                    file.getParentFile());
                FileTreeNode treeNode = treeNodes.get(file);
                parentTreeNode.remove(index);
                model.nodesWereRemoved(parentTreeNode,new int[]{index},
                    new FileTreeNode[]{treeNode});
            }
        });
    }
    
    /** */
    private void fileRenamed(File src,File dst) {
    // delete
        fileDeleted(src);
        
        FileNode parentNode = fileTree.findParentNode(dst);
        if (parentNode != null) {
        // file tree
            FileTree fileSubtree = FileTree.build(dst,fileFilter);
            FileNode newNode = fileSubtree.getRoot();
    
        // file tree node
            FileTreeNode newTreeNode = buildFileTreeNode(newNode);
            
        // add to the parent
            addNodeToParentNode(newTreeNode,parentNode);
        }
    }
    
    /** */
    private void fileTreeNodeSelected(FileTreeNode fileTreeNode) {
        for (FileTreeNodeListener listener:registeredListeners) {
            if (listener.fileTreeNodeSelected(fileTreeNode) == true) {
                return;
            }
        }
        
    // fall back to the default listener
        defaultListener.fileTreeNodeSelected(fileTreeNode);
    }    
    
    /** */
    private void fileTreeNodeDoubleClicked(FileTreeNode fileTreeNode) {
        for (FileTreeNodeListener listener:registeredListeners) {
            if (listener.fileTreeNodeDoubleClicked(fileTreeNode) == true) {
                return;
            }
        }
        
    // fall back to the default listener
        defaultListener.fileTreeNodeDoubleClicked(fileTreeNode);
    }
    
    /** */
    private void fileTreeNodeCreated(FileNode fileNode,
        final FileTreeNode fileTreeNode) {
    //
        fileTreeNode.addLabelTreeNodeListener(new LabelTreeNodeAdapter() {
            /** */
            @Override
            public void labelTreeNodeSelected(LabelTreeNode node) {
                fileTreeNodeSelected(fileTreeNode);
            }                
                
            /** */
            @Override
            public void labelTreeNodeDoubleClicked(LabelTreeNode node) {
                fileTreeNodeDoubleClicked(fileTreeNode);
            }
            
            /** */
            @Override
            public boolean dragOver(LabelTreeNode node,
                DropTargetDragEvent event) {
            //
                return fileTreeDnD.dragOver((FileTreeNode)node,event);
            }            
            
    
            /** */
            @Override
            public boolean drop(LabelTreeNode node,DropTargetDropEvent event) {
                return fileTreeDnD.drop((FileTreeNode)node,event);
            }            
        });
        
        treeNodes.put(fileNode.getFile(),fileTreeNode);
    }
    
    /** */
    private FileTreeNode createFileTreeNode(FileNode fileNode) {
        FileTreeNode fileTreeNode = null;
    // for each renderer
        for (FileTreeNodeRenderer renderer:registeredRenderers) {
            fileTreeNode = renderer.createFileTreeNode(fileNode);
            if (fileTreeNode != null) {
                break;
            }
        }
                
    // fall back to the default renderer
        if (fileTreeNode == null) {
            fileTreeNode = defaultRenderer.createFileTreeNode(fileNode);
        }
        
        fileTreeNodeCreated(fileNode,fileTreeNode);
        return fileTreeNode;
    }
    
    /** */
    private FileTreeNode buildFileTreeNode(FileNode fileNode) {
        FileTreeNode fileTreeNode = createFileTreeNode(fileNode);
        
    // for each child node
        for (FileNode childFileNode:fileNode.getChildNodes()) {
            FileTreeNode childFileTreeNode = buildFileTreeNode(childFileNode);
            childFileTreeNode.setParentNode(fileTreeNode);
            fileTreeNode.add(childFileTreeNode);
        }
        
        return fileTreeNode;
    }
    
    /** */
    private void buildTree(String rootName) {
        rootTreeNode = buildFileTreeNode(fileTree.getRoot());        
        rootTreeNode.setValue(rootName);
        rootTreeNode.setHTMLValue(rootName);
        
    // create tree
        tree = new LabelTree(rootTreeNode);
        tree.setLargeModel(true);
        
    // selection
        tree.getSelectionModel().setSelectionMode(
            TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);          
    }
    
    /** */
    public List<FileTreeNode> getSelectedNodes() {
        List<FileTreeNode> nodes = new ArrayList<>();
        for (TreePath path:tree.getSelectionPaths()) {
            Object last = path.getLastPathComponent();
            nodes.add((FileTreeNode)last);
        }
        
        return nodes;
    }
    
    /** */
    private boolean showPopup(MouseEvent event) {
        if (event.isPopupTrigger() == false) {
            return false;
        }
        
    // selected nodes
        List<FileTreeNode> nodes = getSelectedNodes();
        
    // build the menu
        popup.removeAll();
        for (FileTreeComponentListener listener:listeners) {
            listener.appendFileTreeNodeMenuItems(this,nodes,popup);
        }
        defaultPopupMenuBuilder.appendMenuItems(rootTreeNode,nodes,popup);
        
    // show if not empty
        popup.show(tree,event.getX(),event.getY());
        
        return true;
    }
    
    /** */
    private void createTreeMouseListener() {
        tree.addMouseListener(new MouseAdapter() {
            /** */
            @Override
            public void mousePressed(MouseEvent event) {
                if (showPopup(event) == true) {
                    return;
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
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent event) {
            // selected nodes
                List<FileTreeNode> nodes = getSelectedNodes();
                
            // notify
                for (FileTreeComponentListener listener:listeners) {
                    listener.fileTreeNodeSelectionChanged(
                        FileTreeComponent.this,nodes);
                }
            }
        });
    }    
    
    /** */
    public FileTreeNode getRootNode() {
        return (FileTreeNode)tree.getRootNode();
    }
    
    /** */
    public FileTreeNode findNode(File file) {
        FileNode fileNode = fileTree.findNode(file);
        if (fileNode == null) {
            return null;
        }
        return (FileTreeNode)model.findNodeByUserObject(fileNode);
    }
    
    /** */
    public void repaintTree() {
        tree.repaint();
    }
    
    /** */
    public void reload() {
    	model.reload();
    }
    
    /** */
    public void goTo(File file) {
        FileTreeNode node = findNode(file);
        if (node == null) {
            return;
        }
        
        Object[] elements = model.getPathToRoot(node);
        TreePath path = new TreePath(elements);
        tree.expandPath(path);
        tree.setSelectionPath(path);
        tree.scrollPathToVisible(path);
    }
    
    /** */
    public void addFileTreeComponentListener(
        FileTreeComponentListener listener) {
    //
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    public static void registerFileTreeNodeRenderer(
        FileTreeNodeRenderer renderer) {
    //
        registeredRenderers.add(renderer);
    }
    
    /** */
    public static void registerFileTreeNodeListener(
        FileTreeNodeListener listener) {
    //
        registeredListeners.add(listener);
    }
}