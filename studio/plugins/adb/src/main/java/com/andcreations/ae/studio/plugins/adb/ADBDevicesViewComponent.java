package com.andcreations.ae.studio.plugins.adb;

import java.awt.BorderLayout;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.android.AndroidIcons;
import com.andcreations.ae.studio.plugins.android.AndroidPreferences;
import com.andcreations.ae.studio.plugins.android.AndroidPreferencesListener;
import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTree;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNode;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.android.adb.ADBDevice;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class ADBDevicesViewComponent extends JPanel {
    /** */
    private BundleResources res =
        new BundleResources(ADBDevicesViewComponent.class);
        
    /** */
    private LabelTree tree;    
    
    /** */
    private LabelTreeNode rootNode;
    
    /** */
    private DefaultTreeModel treeModel;
    
    /** */
    private Map<String,ADBDeviceTreeNode> deviceNodes =
        new HashMap<String,ADBDeviceTreeNode>(); 
    
    /** */
    ADBDevicesViewComponent() {
        create();
    }
    
    /** */
    private void create() {
        setLayout(new BorderLayout());
        
    // tree
        rootNode = createRootNode();
        tree = new LabelTree(rootNode);
        tree.setLargeModel(true);
        treeModel = (DefaultTreeModel)tree.getModel();
        updateRootNode();
        
    // tree scroll
        JScrollPane treeScroll = new JScrollPane(tree);
        add(treeScroll,BorderLayout.CENTER);

    // Android preferences listener
        AndroidPreferencesListener listener = new AndroidPreferencesListener() {
            /** */
            @Override
            public void androidSDKDirChanged(File oldSDKDir,File newSDKDir) {
                updateRootNode();
            }
        };
        AndroidPreferences.get().addAndroidPreferencesListener(listener);
    }
    
    /** */
    private void updateRootNode() {
        if (AndroidPreferences.get().getAndroidSDKDir() == null) {
            rootNode.setIcon(Icons.getIcon(
                AndroidIcons.ANDROID_DEVICES_WARNING));
            rootNode.setValue(res.getStr("root.text.no.sdk.dir"));
            rootNode.setHTMLValue(res.getStr("root.html.no.sdk.dir",
                UIColors.toHex(UIColors.warning())));
        }
        else {
            rootNode.setIcon(Icons.getIcon(AndroidIcons.ANDROID_DEVICES));
            rootNode.setValue(res.getStr("root.text"));
            rootNode.setHTMLValue(res.getStr("root.html"));
        }
        
        treeModel.nodeChanged(rootNode);
    }
    
    /** */
    private LabelTreeNode createRootNode() {
        LabelTreeNode rootNode = new LabelTreeNode(
            Icons.getIcon(AndroidIcons.ANDROID_DEVICES),
            res.getStr("root.text"),res.getStr("root.html"));
        return rootNode;
    }
    
    /** */
    private ADBDeviceTreeNode createDeviceNode(ADBDevice device) {
        String text = device.getSerialNumber();
        ADBDeviceTreeNode deviceNode = new ADBDeviceTreeNode(
            Icons.getIcon(AndroidIcons.ANDROID_DEVICE),text,text);
        return deviceNode;
    }
    
    /** */
    void deviceConnected(ADBDevice device) {
        ADBDeviceTreeNode deviceNode = createDeviceNode(device);
        deviceNodes.put(device.getSerialNumber(),deviceNode);
        treeModel.insertNodeInto(deviceNode,rootNode,rootNode.getChildCount());
        tree.expandPath(new TreePath(rootNode));
    }
    
    /** */
    void deviceDisconnected(ADBDevice device) {
        ADBDeviceTreeNode deviceNode = deviceNodes.get(
            device.getSerialNumber());
        if (deviceNode == null) {
            Log.warning(String.format(
                "Attempt to remove nodes of unknown ADB device %s",
                device.getSerialNumber()));
            return;
        }
        treeModel.removeNodeFromParent(deviceNode);
    }    
}