package com.andcreations.ae.studio.plugins.lua.debug.globals;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTree;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeModel;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNode;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNodeAdapter;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNodeUtil;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class GlobalsViewComponent extends JPanel {
    /** */
    private BundleResources res =
        new BundleResources(GlobalsViewComponent.class); 
        
    /** */
    private GlobalsItem noGlobalsItem;

    /** */
    private LabelTree tree;
    
    /** */
    GlobalsViewComponent() {
        create();
    }
    
    /** */
    private void create() {
        setLayout(new BorderLayout());
        
    // no globals item
        noGlobalsItem = new GlobalsItem(null,res.getStr("no.globals"));
        
    // tree
        tree = new LabelTree(noGlobalsItem);
        tree.setLargeModel(true);
        tree.setToggleClickCount(0);        
        
    // tree scroll
        JScrollPane treeScroll = new JScrollPane(tree);
        add(treeScroll,BorderLayout.CENTER);     
    }
    
    /** */
    void setRootItem(GlobalsItem rootItem) {
        if (rootItem != noGlobalsItem) {
            addLabelTreeNodeListener(rootItem);
        }        
        LabelTreeNodeUtil.sortByValueIgnoreCase(rootItem);
        tree.setRootNode(rootItem);
    }    
    
    /** */
    void clear() {
        setRootItem(noGlobalsItem);
    }
    
    /** */
    public void addChildItem(GlobalsItem parent,GlobalsItem item) {
    // add
        addLabelTreeNodeListener(item);
        parent.add(item);
        
    // notify
        int index = parent.getIndex(item);
        LabelTreeModel model = (LabelTreeModel)tree.getModel();
        model.nodesWereInserted(parent,new int[]{index});        
    }    
    
    /** */    
    public List<GlobalsItem> getAllItems() {
        List<GlobalsItem> items = new ArrayList<>();
        
        LabelTreeModel model = (LabelTreeModel)tree.getModel();
    // for each node
        for (LabelTreeNode node:model.flatten()) {
            items.add((GlobalsItem)node);
        }
        return items;
    }
    
    /** */
    private void addLabelTreeNodeListener(final GlobalsItem item) {
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
            addLabelTreeNodeListener((GlobalsItem)node);
        }
    }    
}