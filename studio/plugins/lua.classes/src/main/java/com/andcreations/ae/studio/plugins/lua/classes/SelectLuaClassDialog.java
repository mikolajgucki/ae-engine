package com.andcreations.ae.studio.plugins.lua.classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.ui.common.quickopen.QuickOpenDialog;
import com.andcreations.ae.studio.plugins.ui.common.quickopen.QuickOpenItem;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class SelectLuaClassDialog {
    /** */
    private static SelectLuaClassDialog instance;
    
    /** */
    private final BundleResources res =
        new BundleResources(SelectLuaClassDialog.class);     
    
    /** */
    private List<QuickOpenItem> createItems(Collection<LuaClass> luaClasses) {
        List<QuickOpenItem> items = new ArrayList<>();
        
        for (LuaClass luaClass:luaClasses) {
            ImageIcon icon = LuaClassUtil.getLuaClassIcon(luaClass);
            
            items.add(new QuickOpenItem(icon,luaClass.getLuaModule(),
                luaClass.getLuaModule(),luaClass));
        }
        
        return items;
    }
    
    /** */
    LuaClass show(Collection<LuaClass> luaClasses) {
    // items
        List<QuickOpenItem> items = createItems(luaClasses);        
        
    // show dialog
        QuickOpenDialog dialog = new QuickOpenDialog(MainFrame.get(),
            res.getStr("title"),items,true);
        dialog.setMatcher(res.getStr("matcher.label"),
            new SelectLuaClassItemMatcher());
        dialog.showOptionDialog();
        
    // return the selected Lua class
        QuickOpenItem selectedItem = dialog.getSelectedItem();
        if (selectedItem != null) {
            return (LuaClass)selectedItem.getObject();
        }
        return null;
    }
    
    /** */
    static SelectLuaClassDialog get() {
        if (instance == null) {
            instance = new SelectLuaClassDialog();
        }
        
        return instance;
    }
}
