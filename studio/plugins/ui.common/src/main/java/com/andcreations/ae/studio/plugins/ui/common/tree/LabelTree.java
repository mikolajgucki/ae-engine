package com.andcreations.ae.studio.plugins.ui.common.tree;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.jdesktop.swingx.JXTree;

/**
 * @author Mikolaj Gucki    
 */
@SuppressWarnings("serial")
public class LabelTree extends JXTree {
    /** */
    private LabelTreeModel model;
    
    /** */
    private LabelTreeNode currentRoot;
    
    /** */
    private List<LabelTreeListener> listeners = new ArrayList<>();
    
    /** */
    public LabelTree(LabelTreeNode root) {
        create(root);
    }
    
    /** */
    private void create(LabelTreeNode root) {
        setLargeModel(true);
        currentRoot = root;
        
    // model
        model = new LabelTreeModel(root);
        setModel(model);
        
    // renderer
        setCellRenderer(new LabelTreeCellRenderer());
        
    // selection
        getSelectionModel().setSelectionMode(
            TreeSelectionModel.SINGLE_TREE_SELECTION);  
       
    // drag and...
        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(this,
            DnDConstants.ACTION_COPY_OR_MOVE,
            new LabelTreeDragGestureListener(this));        
        
    // ...drop
        DropTarget dropTarget = new DropTarget(this,
            DnDConstants.ACTION_COPY_OR_MOVE,
            new LabelTreeDropTargetListener(this));
        setDropTarget(dropTarget);
        
    // listeners
        createSelectionListener();
        createMouseListener();
        createExpansionListener();
    }
    
    /** */
    private void createSelectionListener() {
        addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent event) {
                LabelTreeNode node =
                    (LabelTreeNode)getLastSelectedPathComponent();
                if (node == null) {
                    return;
                }
                node.notifySelected();
            }
        });
    }
    
    /** */
    private void createMouseListener() {
        addMouseListener(new MouseAdapter() {
            /** */
            @Override
            public void mousePressed(MouseEvent event) {
                LabelTreeNode node =
                    (LabelTreeNode)getLastSelectedPathComponent();
                if (node == null) {
                    return;
                }
                if (event.getClickCount() == 2) {
                    node.notifyDoubleClicked();                
                }                
            }
        });
    }
    
    /** */
    private void createExpansionListener() {
        addTreeExpansionListener(new TreeExpansionListener() {
            /** */
            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                LabelTreeNode node =
                    (LabelTreeNode)event.getPath().getLastPathComponent();
                node.notifyCollapsed();
            }
            
            /** */
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                LabelTreeNode node =
                    (LabelTreeNode)event.getPath().getLastPathComponent();
                node.notifyExpanded();
            }
        });
    }
    
    /** */
    public void addLabelTreeListener(LabelTreeListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    public void setRootNode(LabelTreeNode root) {
        if (root == currentRoot) {
            return;
        }
        currentRoot = root;        
        model.setRoot(root);
    }
    
    /** */
    public LabelTreeNode getRootNode() {
        return (LabelTreeNode)model.getRoot();
    }
    
    /** */
    public void expandNode(LabelTreeNode node) {
        expandPath(new TreePath(model.getPathToRoot(node)));
    }
    
    /** */
    void dragGestureRecognized(DragGestureEvent event) {
        TreePath[] paths = getSelectionPaths();
    // if no path is selected
        if (paths == null) {
            return;
        }
                
        synchronized (listeners) {
            for (LabelTreeListener listener:listeners) {
                if (listener.dragGestureRecognized(event) == true) {
                    break;
                }
            }
        }
    }
    
    /** */
    public LabelTreeModel getLabelTreeModel() {
        return model;
    }
}