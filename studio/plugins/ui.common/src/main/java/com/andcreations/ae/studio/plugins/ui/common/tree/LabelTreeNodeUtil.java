package com.andcreations.ae.studio.plugins.ui.common.tree;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Mikolaj Gucki    
 */
public class LabelTreeNodeUtil {
    /** */
    public static void sortByValueIgnoreCase(List<LabelTreeNode> nodes) {
        Collections.sort(nodes,new Comparator<LabelTreeNode>() {
            /** */
            @Override
            public int compare(LabelTreeNode a,LabelTreeNode b) {
                return a.getValue().compareToIgnoreCase(b.getValue());
            }
        });
        
        for (LabelTreeNode node:nodes) {
            sortByValueIgnoreCase(node);
        }
    }
    
    /** */
    public static void sortByValueIgnoreCase(LabelTreeNode node) {
    // sort
        List<LabelTreeNode> children = node.getChildrenAsList();
        sortByValueIgnoreCase(children);
        
    // remove all and add again
        node.removeAllChildren();
        for (LabelTreeNode childNode:children) {
            node.add(childNode);
        }
    }
}