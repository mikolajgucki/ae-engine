package com.andcreations.ae.studio.plugins.lua.classes.hierarchy;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultTreeModel;

import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.lua.classes.LuaClass;
import com.andcreations.ae.studio.plugins.lua.classes.LuaClassTree;
import com.andcreations.ae.studio.plugins.lua.classes.LuaClassTreeNode;
import com.andcreations.ae.studio.plugins.lua.classes.LuaClassUtil;
import com.andcreations.ae.studio.plugins.lua.classes.LuaClasses;
import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTree;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNode;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNodeAdapter;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.StatusBar;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class LuaHierarchyComponent extends JPanel {
    /** */
    private BundleResources res =
        new BundleResources(LuaHierarchyComponent.class);
        
    /** */
    private LabelTree tree;
    
    /** */ 
    private String dark;     
    
    /** */
    LuaHierarchyComponent() {
        create();
    }
    
    /** */
    private void create() {
        setLayout(new BorderLayout());
        
    // colors
        dark = UIColors.toHex(UIColors.dark());        
        
    // tree
        LabelTreeNode rootNode = buildRootNode();
        tree = new LabelTree(rootNode);
        tree.setLargeModel(true);
        tree.setToggleClickCount(0);
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        model.nodeChanged(rootNode);
        
        JScrollPane treeScroll = new JScrollPane(tree);
        add(treeScroll,BorderLayout.CENTER);
    }
    
    /** */
    private LabelTreeNode buildTreeNode(LuaClassTreeNode classNode) {
        final LuaClass luaClass = classNode.getLuaClass();
        
    // prefix, name
        String prefix = null;
        String name = null;
        if (luaClass != null) {
            prefix = luaClass.getPrefix();
            name = luaClass.getName();
        }
        if (prefix == null) {
            prefix = "";
        }
        else {
            prefix += ".";
        }
        
        String text = res.getStr("node.text",name,prefix);
        String html = res.getStr("node.html",name,prefix,dark);
        
    // node
        LabelTreeNode node = new LabelTreeNode(
            LuaClassUtil.getLuaClassIcon(luaClass),text,html);
        
    // node listener
        if (luaClass != null) {
            node.addLabelTreeNodeListener(new LabelTreeNodeAdapter() {
                /** */
                @Override
                public void labelTreeNodeDoubleClicked(LabelTreeNode node) {
                    LuaFile.edit(luaClass.getFile());
                }              
                
                /** */
                @Override
                public void labelTreeNodeSelected(LabelTreeNode node) {
                    StatusBar.get().setInfo(
                        luaClass.getFile().getAbsolutePath());
                }            
            });
        }
        
    // for each child node
        for (LuaClassTreeNode childClassNode:classNode.getChildNodes()) {
            LabelTreeNode childNode = buildTreeNode(childClassNode);
            node.add(childNode);
        }
        
        return node;
    }
    
    /** */
    private LabelTreeNode buildRootNode() {
        LuaClassTree classTree = LuaClasses.get().buildTree();
        if (classTree == null) {
            String errorColor = UIColors.toHex(UIColors.error());   
            LabelTreeNode rootNode = new LabelTreeNode(
                Icons.getIcon(DefaultIcons.ERROR),
                res.getStr("root.node.error",errorColor));
            return rootNode;
        }            
        
    // root
        LabelTreeNode rootNode = buildTreeNode(classTree.getRoot());
        rootNode.setValue(res.getStr("root.node.text"));
        rootNode.setHTMLValue(res.getStr("root.node.text"));
        
        return rootNode;
    }
    
    /** */
    void refresh() {
        LabelTreeNode rootNode = buildRootNode();
        tree.setRootNode(rootNode);
    }
}