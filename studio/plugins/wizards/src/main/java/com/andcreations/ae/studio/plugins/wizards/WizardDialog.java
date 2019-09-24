package com.andcreations.ae.studio.plugins.wizards;

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
class WizardDialog {
    /** */
    private BundleResources res = new BundleResources(WizardDialog.class);
    
    /** */
    WizardDialog() {
    }
    
    /** */
    private QuickOpenDialog createDialog() {
    // items
        List<QuickOpenItem> items = new ArrayList<>();
        for (Wizard wizard:Wizards.get().getWizards()) {
            String name = wizard.getName();
            String desc = wizard.getDesc();
            
            String text = res.getStr("text",name,desc);
            String html = res.getStr("html",name,desc,UIColors.htmlDark());

            QuickOpenItem item = new QuickOpenItem(wizard.getIcon(),
                wizard.getName(),text,html,wizard);
            items.add(item);
        }
        
    // dialog
        QuickOpenDialog dialog = new QuickOpenDialog(MainFrame.get(),
            res.getStr("title"),items,true);
        dialog.setRelativeMinimumSize(16,24);
        
    // matcher
        SeparatorQuickOpenMatcher matcher = new SeparatorQuickOpenMatcher(
            " ",dialog.getMatcher(),true);
        dialog.setMatcher(dialog.getMatcherLabel(),matcher);        
        
        return dialog;    
    }
    
    /** */
    Wizard show() {
    // show dialog
        QuickOpenDialog dialog = createDialog();
        dialog.showOptionDialog();
        
    // selected builder
        QuickOpenItem item = dialog.getSelectedItem();
        if (item == null) {
            return null;
        }        
        return (Wizard)item.getObject();
    }    
}