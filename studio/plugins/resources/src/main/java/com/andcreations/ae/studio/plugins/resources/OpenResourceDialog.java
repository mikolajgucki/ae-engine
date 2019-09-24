package com.andcreations.ae.studio.plugins.resources;

import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.ae.studio.plugins.ui.common.quickopen.QuickOpenDialog;
import com.andcreations.ae.studio.plugins.ui.common.quickopen.QuickOpenItem;
import com.andcreations.ae.studio.plugins.ui.common.quickopen.QuickOpenUtil;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class OpenResourceDialog {
    /** */
    static class Item {
        /** */
        ResourceSource source;
        
        /** */
        Resource resource;
        
        private Item(ResourceSource source,Resource resource) {
            this.source = source;
            this.resource = resource;
        }
    };
    
    /** */
    private BundleResources res =
        new BundleResources(OpenResourceDialog.class); 
    
    /** */
    private static String dark;
    
    /** */
    static {
        init();
    }    
    
    /** */
    private static void init() {
        dark = UIColors.toHex(UIColors.dark());
    }    
    
    /** */
    OpenResourceDialog() {
    }
    
    /** */
    private QuickOpenItem createItem(ResourceSource source,Resource resource) {
        String name = resource.getName();
        String details = resource.getDetails();
        
        String text = null;
        String html = null;
        if (details == null) {
            text = res.getStr("text.no.details",name);
            html = res.getStr("html.no.details",name);
        }
        else {
            text = res.getStr("text",name,details,dark);
            html = res.getStr("html",name,details,dark);
        }
        
        QuickOpenItem item = new QuickOpenItem(
            Icons.getIcon(resource.getIconName()),text,html,
            new Item(source,resource));
        return item;
    }
    
    /** */
    private QuickOpenDialog createDialog(List<ResourceSource> sources) {
    // items
        List<QuickOpenItem> items = new ArrayList<>();
        for (ResourceSource source:sources) {
            List<Resource> resources = source.getResources();
            if (resources != null) {
                for (Resource resource:resources) {
                    items.add(createItem(source,resource));
                }
            }
        }
        QuickOpenUtil.sortBySearchValue(items);
        
    // dialog
        QuickOpenDialog dialog = new QuickOpenDialog(MainFrame.get(),
            res.getStr("title"),items,true);
        
        return dialog;
    }
    
    /** */
    Item show(List<ResourceSource> sources) {
    // show dialog
        QuickOpenDialog dialog = createDialog(sources);
        dialog.showOptionDialog();
        
    // selected item
        QuickOpenItem item = dialog.getSelectedItem();
        if (item == null) {
            return null;
        }        
        
        return (Item)item.getObject();
    }
}