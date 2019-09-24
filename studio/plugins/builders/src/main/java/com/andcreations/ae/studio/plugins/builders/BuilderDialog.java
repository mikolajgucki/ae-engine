package com.andcreations.ae.studio.plugins.builders;

import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.ae.studio.plugins.ui.common.quickopen.QuickOpenDialog;
import com.andcreations.ae.studio.plugins.ui.common.quickopen.QuickOpenItem;
import com.andcreations.ae.studio.plugins.ui.common.quickopen.SeparatorQuickOpenMatcher;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class BuilderDialog {
    /** */
    private BundleResources res = new BundleResources(BuilderDialog.class); 
    
    /** */
    BuilderDialog() {
    }
    
    /** */
    private QuickOpenDialog createDialog() {
    // items
        List<QuickOpenItem> items = new ArrayList<>();
        for (Builder builder:Builders.get().getBuilders()) {
            String name = builder.getName();
            String desc = builder.getDesc();
            
            String text = res.getStr("text",name,desc);
            String html = res.getStr("html",name,desc,UIColors.htmlDark());

            QuickOpenItem item = new QuickOpenItem(builder.getIcon(),
                builder.getName(),text,html,builder);
            items.add(item);
        }
        
    // dialog
        QuickOpenDialog dialog = new QuickOpenDialog(MainFrame.get(),
            res.getStr("title"),items,true);
        dialog.setRelativeMinimumSize(16,32);
        
    // matcher
        SeparatorQuickOpenMatcher matcher = new SeparatorQuickOpenMatcher(
            " ",dialog.getMatcher(),true);
        dialog.setMatcher(dialog.getMatcherLabel(),matcher);
        
        return dialog;
    }
    
    /** */
    Builder show() {
    // show dialog
        QuickOpenDialog dialog = createDialog();
        dialog.showOptionDialog();
        
    // selected builder
        QuickOpenItem item = dialog.getSelectedItem();
        if (item == null) {
            return null;
        }        
        return (Builder)item.getObject();
    }
}