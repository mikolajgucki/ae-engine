package com.andcreations.ae.studio.plugins.search;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.andcreations.ae.studio.plugins.search.common.SearchOccurence;
import com.andcreations.ae.studio.plugins.ui.common.JTreeUtil;
import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTree;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNode;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNodeAdapter;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.lang.Strings;
import com.andcreations.resources.BundleResources;

/**
 * The search view component.
 *
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class SearchViewComponent extends JPanel {
    /** */
    private BundleResources res =
        new BundleResources(SearchViewComponent.class);     
    
    /** */
    private LabelTree tree;
        
    /** */ 
    private String dark;    
    
    /** */
    private String green;
    
    /** */
    SearchViewComponent() {
        create();
    }
    
    /** */
    private void create() {        
        setLayout(new BorderLayout());
        
    // colors
        dark = UIColors.toHex(UIColors.dark());
        green = UIColors.toHex(UIColors.green());
        
    // tree
        tree = new LabelTree(null);
        
    // tree scroll
        JScrollPane treeScroll = new JScrollPane(tree);
        add(treeScroll,BorderLayout.CENTER);        
    }
    
    /** */
    private LabelTreeNode createOccurenceNode(final SearchOccurenceGroup group,
        final SearchOccurence occurence) {
    //
        String lineNo = Integer.toString(occurence.getLineNo());
        String prefix = Strings.ltrim(occurence.getPrefix());
        String match = occurence.getMatch();
        String suffix = Strings.rtrim(occurence.getSuffix());        
        
        LabelTreeNode node = new LabelTreeNode(null,
            res.getStr("occurence.node.text",lineNo,prefix,match,suffix),
            res.getStr("occurence.node.html",lineNo,prefix,match,suffix,dark));
        node.addLabelTreeNodeListener(new LabelTreeNodeAdapter() {
            /** */
            @Override
            public void labelTreeNodeDoubleClicked(LabelTreeNode node) {
                group.getSource().openDocument(group.getDocumentId(),
                    occurence.getLineNo(),occurence.getStart(),
                    occurence.getEnd());                    
            }
        });
        
        return node;
    }
    
    /** */
    private LabelTreeNode createGroupNode(final SearchOccurenceGroup group) {
        String matchesStr = Integer.toString(group.getOccurences().size());
        LabelTreeNode node = new LabelTreeNode(group.getIcon(),            
            res.getStr("group.node.text",group.getName(),matchesStr),
            res.getStr("group.node.html",group.getName(),matchesStr,green));             
        for (SearchOccurence occurence:group.getOccurences()) {
            node.add(createOccurenceNode(group,occurence));
        }
        
        return node;
    }
    
    /** */
    private void appendGroupNodes(LabelTreeNode node,
        List<SearchOccurenceGroup> groups) {
    //
        for (SearchOccurenceGroup group:groups) {
            node.add(createGroupNode(group));
        }
    }
    
    /** */
    void setResult(String searchText,List<SearchOccurenceGroup> groups) {
        int matches = 0;
        for (SearchOccurenceGroup group:groups) {
            matches += group.getOccurences().size();
        }
        String matchesStr = Integer.toString(matches);
        
        LabelTreeNode rootNode = new LabelTreeNode(
            Icons.getIcon(DefaultIcons.SEARCH),
            res.getStr("search.node.text",searchText,matchesStr),
            res.getStr("search.node.html",searchText,matchesStr,green,dark));
        appendGroupNodes(rootNode,groups);
        tree.setRootNode(rootNode);
    }
    
    /** */
    void expandAll() {
        JTreeUtil.expandAll(tree);
    }
    
    /** */
    void collapseAll() {
        JTreeUtil.collapseAll(tree);
        tree.expandRow(0); // expand the first level nodes
    }
    
    /** */
    void clear() {
        tree.setRootNode(null);
    }
}