package com.andcreations.ae.studio.plugins.ui.main.view.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.ae.studio.plugins.ui.common.quickopen.QuickOpenDialog;
import com.andcreations.ae.studio.plugins.ui.common.quickopen.QuickOpenItem;
import com.andcreations.ae.studio.plugins.ui.common.quickopen.QuickOpenUtil;
import com.andcreations.ae.studio.plugins.ui.common.quickopen.SeparatorQuickOpenMatcher;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.ae.studio.plugins.ui.main.view.View;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewManager;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewProvider;
import com.andcreations.resources.BundleResources;

/**
 * Dialog which allows to go to a dialog in a group.
 * 
 * @author Mikolaj Gucki
 */
public class GoToView {
    /** */
    private static BundleResources res = new BundleResources(GoToView.class); 
    
    /** */
    private static List<QuickOpenItem> createViewItems(Collection<View> views) {
        List<QuickOpenItem> items = new ArrayList<>();
        
    // for each view
        for (View view:views) {
            items.add(new QuickOpenItem(view.getIcon(),
                view.getTitle(),view.getTitle(),view));
        }
        
        return items;
    }
    
    private static List<QuickOpenItem> createProviderItems(
        List<ViewProvider> providers) {
    //
        List<QuickOpenItem> items = new ArrayList<>();
        
        String dark = UIColors.htmlDark();
    // for each provider
        for (ViewProvider provider:providers) {
            items.add(new QuickOpenItem(provider.getIcon(),
                provider.getTitle(),
                res.getStr("provider.text",provider.getTitle()),
                res.getStr("provider.html",provider.getTitle(),dark),
                provider));
        }
        
        return items;
    }
    
    /** */
    private static void goToView(List<QuickOpenItem> items) {
    // sort
        QuickOpenUtil.sortBySearchValue(items);
        
    // create dialog
        QuickOpenDialog dialog = new QuickOpenDialog(MainFrame.get(),
            res.getStr("go.to.view"),items,true);
        int height = Math.min(48,24 + items.size());
        dialog.setRelativeMinimumSize(16,height);
        
    // matcher
        SeparatorQuickOpenMatcher matcher = new SeparatorQuickOpenMatcher(
            " ",dialog.getMatcher(),true);
        dialog.setMatcher(dialog.getMatcherLabel(),matcher);        
        
    // show
        dialog.showOptionDialog();
        
    // selected view
        QuickOpenItem item = dialog.getSelectedItem();        
        if (item != null) {
            Object object = item.getObject();
            if (object instanceof View) {
                MainFrame.get().getViewManager().showView((View)object);
            }
            if (object instanceof ViewProvider) {
                ViewProvider provider = (ViewProvider)object;
                provider.showView();
            }
        }        
    }
    
    /** */
    public static void go(View view) {
        List<View> siblings =
            MainFrame.get().getViewManager().getSiblingViews(view);
        if (siblings == null) {
            return;
        }
        
        goToView(createViewItems(siblings));
    }
    
    /** */
    public static void go() {
        ViewManager manager = MainFrame.get().getViewManager();
        List<ViewProvider> providers =
            new ArrayList<>(manager.getViewProviders());
        
    // remove the providers for which view is already shown
        while (true) {
            ViewProvider providerToRemove = null;
            for (ViewProvider provider:providers) {
                if (manager.getViewByTitle(provider.getTitle()) != null) {
                    providerToRemove = provider;
                    break;
                }
            }
            if (providerToRemove == null) {
                break;
            }
            providers.remove(providerToRemove);
        }
        
        List<QuickOpenItem> items = createProviderItems(providers);
        items.addAll(createViewItems(manager.getViews()));
        
        goToView(items);
    }
}