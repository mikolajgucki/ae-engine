package com.andcreations.ae.studio.plugins.outline;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTree;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNode;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNodeAdapter;
import com.andcreations.resources.BundleResources;

/**
 * The outline view top component.
 *
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class OutlineViewComponent extends JPanel {
    /** */
    private BundleResources res =
        new BundleResources(OutlineViewComponent.class);  
    
    /** */
    private OutlineItem noOutlineItem;
    
    /** */
    private LabelTree tree;
    
    /** */
    OutlineViewComponent() {
        create();
    }
    
    /** */
    private void create() {
        setLayout(new BorderLayout());
        
    // no outline item
        noOutlineItem = new OutlineItem(null,res.getStr("no.outline"));
        
    // tree
        tree = new LabelTree(buildRootNode(noOutlineItem));
        
    // tree scroll
        JScrollPane treeScroll = new JScrollPane(tree);
        add(treeScroll,BorderLayout.CENTER);
    }
    
    /** */
    void setRootItem(OutlineItem rootItem) {
        tree.setRootNode(buildRootNode(rootItem));
    }
    
    /** */
    void clear() {
        setRootItem(noOutlineItem);
    }
    
    /** */
    private LabelTreeNode createNode(OutlineItem item) {
        LabelTreeNode node = new LabelTreeNode(item.getIcon(),item.getValue(),
            item.getHTMLValue());
        node.setUserObject(item);
        node.addLabelTreeNodeListener(new LabelTreeNodeAdapter() {
            /** */
            @Override
            public void labelTreeNodeSelected(LabelTreeNode node) {
                OutlineItem item = (OutlineItem)node.getUserObject();
                item.notifySelected();
            }
        });
        
        return node;
    }
    
    /** */
    private void addNodes(LabelTreeNode node,OutlineItem item) {
        for (OutlineItem childItem:item.getChildItems()) {
            LabelTreeNode childNode = createNode(childItem);
            node.add(childNode);
            addNodes(childNode,childItem);
        }
    }    
    
    /** */
    private LabelTreeNode buildRootNode(OutlineItem rootItem) {
        LabelTreeNode rootNode = createNode(rootItem);
        addNodes(rootNode,rootItem);
        return rootNode;        
    }
}