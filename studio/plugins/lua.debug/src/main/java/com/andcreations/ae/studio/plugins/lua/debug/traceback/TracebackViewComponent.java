package com.andcreations.ae.studio.plugins.lua.debug.traceback;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.TreePath;

import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTree;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeModel;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNode;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNodeAdapter;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class TracebackViewComponent extends JPanel {
    /** */
    private BundleResources res =
        new BundleResources(TracebackViewComponent.class); 
    
    /** */
    private TracebackItem noTracebackItem;
    
    /** */
    private LabelTree tree;
    
    /** */
    TracebackViewComponent() {
        create();
    }
    
    /** */
    private void create() {
        setLayout(new BorderLayout());
        
    // no traceback item
        noTracebackItem = new TracebackItem(null,res.getStr("no.traceback"));
        
    // tree
        tree = new LabelTree(noTracebackItem);
        tree.setLargeModel(true);
        tree.setToggleClickCount(0);        
        
    // tree scroll
        JScrollPane treeScroll = new JScrollPane(tree);
        add(treeScroll,BorderLayout.CENTER);
    }
    
    /** */
    void setRootItem(TracebackItem rootItem) {
        if (rootItem != noTracebackItem) {
            addLabelTreeNodeListener(rootItem);
        }
        tree.setRootNode(rootItem);        
    } 
    
    /** */
    void clear() {
        setRootItem(noTracebackItem);
    }    
    
    /** */
    void expand(TracebackItem item) {
        LabelTreeModel model = (LabelTreeModel)tree.getModel();
        TreePath path = new TreePath(model.getPathToRoot(item));
        tree.expandPath(path);
    }

    /** */
    public void addChildItem(TracebackItem parent,TracebackItem item) {
    // add
        addLabelTreeNodeListener(item);
        parent.add(item);
        
    // notify
        int index = parent.getIndex(item);
        LabelTreeModel model = (LabelTreeModel)tree.getModel();
        model.nodesWereInserted(parent,new int[]{index});        
    }
    
    /** */    
    public List<TracebackItem> getAllItems() {
        List<TracebackItem> items = new ArrayList<>();
        
        LabelTreeModel model = (LabelTreeModel)tree.getModel();
    // for each node
        for (LabelTreeNode node:model.flatten()) {
            items.add((TracebackItem)node);
        }
        return items;
    }
    
    /** */
    private void addLabelTreeNodeListener(final TracebackItem item) {
        item.addLabelTreeNodeListener(new LabelTreeNodeAdapter() {
            /** */
            @Override
            public void labelTreeNodeSelected(LabelTreeNode node) {
                item.notifySelected();
            }
            
            /** */
            @Override
            public void labelTreeNodeDoubleClicked(LabelTreeNode node) {
                item.notifyDoubleClicked();
            }
            
            /** */
            @Override
            public void labelTreeNodeExpanded(LabelTreeNode node) {
                item.notifyExpanded();
            }
        });
        
        for (LabelTreeNode node:item.getChildrenAsList()) {
            addLabelTreeNodeListener((TracebackItem)node);
        }
    }
}