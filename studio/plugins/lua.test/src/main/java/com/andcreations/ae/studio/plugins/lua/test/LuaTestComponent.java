package com.andcreations.ae.studio.plugins.lua.test;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTree;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeModel;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class LuaTestComponent extends JPanel {
    /** */
    private BundleResources res = new BundleResources(LuaTestComponent.class);  
    
    /** */
    private LabelTree tree;
    
    /** */
    private LabelTreeModel model;
    
    /** */
    private LuaTestLabelTreeNode rootNode;
    
    /** */
    private LuaTestLabelTreeNode suiteNode;
    
    /** */
    private LuaTestLabelTreeNode testNode;
    
    /** */
    LuaTestComponent() {
        create();
    }
    
    /** */
    private void create() {
        setLayout(new BorderLayout());
        
    // tree
        tree = new LabelTree(rootNode);
        model = tree.getLabelTreeModel();
        
    // scroll
        JScrollPane treeScroll = new JScrollPane(tree);
        add(treeScroll,BorderLayout.CENTER);
        
    // clear initially
        clear();
    }
    
    /** */
    void clear() {
        rootNode = new LuaTestLabelTreeNode("");
        tree.setRootNode(rootNode);
        updateRootNode(Icons.getIcon(DefaultIcons.TESTS_RUNNING),null);
    }
    
    /** */
    private void updateRootNode(ImageIcon icon,String msg) {
        String text = null;
        String html = null;
        if (msg == null) {
            text = res.getStr("root.node.text");
            html = res.getStr("root.node.html");
        }
        else {
            text = res.getStr("root.node.with.msg.text",msg);
            html = res.getStr("root.node.with.msg.html",msg);
        }
        
        rootNode.setIcon(icon);
        rootNode.setValue(text);
        rootNode.setHTMLValue(html);
        model.nodeChanged(rootNode);        
    }
    
    /** */
    void updateRootNode(String msg,boolean error) {
        ImageIcon icon = null;
        if (error == false) {
            icon = Icons.getIcon(DefaultIcons.TESTS_OK);
        }
        else {
            icon = Icons.getIcon(DefaultIcons.TESTS_FAILED);
        }        
        updateRootNode(icon,msg);
    }
    
    /** */
    private void setSuiteNodeText(String name,String msg) {
        String text = null;
        String html = null;
        if (msg == null) {
            text = res.getStr("suite.node.text",name);
            html = res.getStr("suite.node.html",name);
        }
        else {
            text = res.getStr("suite.node.with.msg.text",name,msg);
            html = res.getStr("suite.node.with.msg.html",name,msg);
        }  
        
        suiteNode.setValue(text);
        suiteNode.setHTMLValue(html);
        model.nodeChanged(suiteNode);
    }
    
    /** */
    private void addTestSuiteNode(ImageIcon icon,String name,String msg,
        File file,int line) {
    //
        int index = rootNode.getChildCount();
    // add to the root node
        suiteNode = new LuaTestLabelTreeNode(icon,null,null);
        setSuiteNodeText(name,msg);
        rootNode.add(suiteNode);
        
    // notify
        model.nodesWereInserted(rootNode,new int[]{index});
        
    // expand
        tree.expandNode(rootNode);
        tree.expandNode(suiteNode);        
    }
    
    /**
     * Adds a test suite node
     *
     * @param name The suite (module) name.
     * @param file The file.
     * @param line The line.
     */
    void addTestSuiteNode(String name,File file,int line) {
        addTestSuiteNode(Icons.getIcon(DefaultIcons.TESTS_RUNNING),
            name,null,file,line);
    }
    
    /**
     * Called when running a test suite failed.
     *
     * @param name The suite (module) name.
     * @param msg The error message.
     * @param file The Lua file.
     * @param line The line number.     
     */
    void testSuiteFailed(String name,String msg,File file,int line) {
        suiteNode.setIcon(Icons.getIcon(DefaultIcons.TESTS_FAILED));
        suiteNode.setFile(file);
        suiteNode.setLine(line);
        setSuiteNodeText(name,msg);
    }
    
    /**
     * Called when running a test suite finished.
     *
     * @param name The suite (module) name.
     */
    void testSuiteFinished(String name) {
        suiteNode.setIcon(Icons.getIcon(DefaultIcons.TESTS_OK));
        setSuiteNodeText(name,null);
    }
    
    /** */
    private void setTestNodeText(String name,String msg) {
        String text = null;
        String html = null;
        if (msg == null) {
            text = res.getStr("test.node.text",name);
            html = res.getStr("test.node.html",name);
        }
        else {
            text = res.getStr("test.node.with.msg.text",name,msg);
            html = res.getStr("test.node.with.msg.html",name,msg);
        }

        testNode.setValue(text);
        testNode.setHTMLValue(html);
        model.nodeChanged(testNode);        
    }
    
    /** */
    private void addTestNode(ImageIcon icon,String name,String msg,File file,
        int line) {
    //
        if (suiteNode == null) {
            throw new IllegalStateException("No suite node");
        }
        
        int index = suiteNode.getChildCount();
    // add to the suite node
        testNode = new LuaTestLabelTreeNode(icon,null,null);
        testNode.setFile(file);
        testNode.setLine(line);
        setTestNodeText(name,msg);
        suiteNode.add(testNode);
        
    // notify
        model.nodesWereInserted(suiteNode,new int[]{index});
        
    // expand
        tree.expandNode(suiteNode);
        tree.expandNode(testNode);        
    }
    
    /**
     * Adds a test node.
     *
     * @param name The test name.
     * @param file The test suite file.
     */
    void addTestNode(String name,File file) {
        addTestNode(Icons.getIcon(DefaultIcons.TEST_RUNNING),
            name,null,file,LuaTestLabelTreeNode.NO_LINE);
    }
    
    /**
     * Adds a test node when the test has successfully finished.
     *
     * @param name The name.
     */
    void addTestFinishedNode(String name) {
        testNode.setIcon(Icons.getIcon(DefaultIcons.TEST_OK));
    }
    
    /**
     * Adds a test node when the test has failed.
     *
     * @param name The name.
     * @param msg The fail message.
     * @param file The Lua file.
     * @param line The line number.
     */
    void addTestFailedNode(String name,String msg,File file,int line) {
        testNode.setIcon(Icons.getIcon(DefaultIcons.TEST_FAILED));
        suiteNode.setFile(file);
        suiteNode.setLine(line);
        setTestNodeText(name,msg);
    }
}