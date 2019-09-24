package com.andcreations.ae.studio.plugins.lua.explorer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.project.ProjectMenu;
import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.ae.studio.plugins.ui.common.quickopen.QuickOpenDialog;
import com.andcreations.ae.studio.plugins.ui.common.quickopen.QuickOpenItem;
import com.andcreations.ae.studio.plugins.ui.common.quickopen.QuickOpenUtil;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.io.FileNode;
import com.andcreations.io.FileTree;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class OpenLuaFile {
    /** */
    private static OpenLuaFile instance;
    
    /** */
    private BundleResources res = new BundleResources(OpenLuaFile.class);    
    
    /** */
    private OpenLuaFile() {
    }
    
    /** */
    void init() {
        addMenuItem();
    }
    
    private List<QuickOpenItem> getItemList() {
        FileTree tree = ProjectLuaFiles.get().getLuaSourceTree();
        List<FileNode> nodes = tree.flatten();
            
        List<QuickOpenItem> items = new ArrayList<>();
    // for each node
        for (FileNode node:nodes) {
            if (node.isFile() == false) {
                continue;
            }
            if (ProjectLuaFiles.getError(node) != null) {
                continue;
            }
            
        // path
            String path = ProjectLuaFiles.get().getLuaPath(node.getFile());
           
        // icon
            ImageIcon icon = Icons.getIcon(
                ProjectLuaFiles.get().getLuaFileIconName(node.getFile()),
                LuaFile.getDecoIconName(node.getFile()));

        // item
            items.add(new QuickOpenItem(icon,node.getName(),path,node));
        }
        
        return items;
    }
    
    /** */
    private void openLuaFile() {
    // items
        List<QuickOpenItem> items = getItemList();
        QuickOpenUtil.sortBySearchValue(items);
        
    // show dialog
        QuickOpenDialog dialog = new QuickOpenDialog(MainFrame.get(),
            res.getStr("menu.text"),items,true);
        dialog.showOptionDialog();
        
    // edit the selected file
        QuickOpenItem selectedItem = dialog.getSelectedItem();
        if (selectedItem != null) {
            FileNode node = (FileNode)selectedItem.getObject();
            LuaFile.edit(node.getFile());
        }
    }
    
    /** */
    void addMenuItem() {
        JMenuItem item = new JMenuItem(res.getStr("menu.text"));
        item.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_L,UIKeys.menuKeyMask() | KeyEvent.SHIFT_DOWN_MASK));
        item.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                openLuaFile();
            }
        });
        
        ProjectMenu.get().add(item);
    }
    
    /** */
    static OpenLuaFile get() {
        if (instance == null) {
            instance = new OpenLuaFile();
        }
        
        return instance;
    }
}
