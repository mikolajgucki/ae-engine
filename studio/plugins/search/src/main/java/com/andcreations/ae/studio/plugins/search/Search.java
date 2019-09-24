package com.andcreations.ae.studio.plugins.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.ui.common.MessageDialog;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.ae.studio.plugins.ui.main.view.DefaultViewProvider;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCategory;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class Search {
    /** The view identifier. */
    private static final String VIEW_ID = Search.class.getName();
        
    /** */
    private static Search instance;
    
    /** */
    private BundleResources res = new BundleResources(Search.class);        
    
    /** The search view provider. */
    private DefaultViewProvider viewProvider;
    
    /** The search source list. */
    private List<SearchSource> searchSourceList = new ArrayList<>();
    
    /** The search dialog. */
    private SearchDialog dialog;
    
    /** The view component. */
    private SearchViewComponent component;
    
    /** The search engine. */
    private SearchEngine engine = new SearchEngine();
    
    /** */
    private String prevSearchText;
    
    /** */
    private boolean prevMatchCase;
    
    /** */
    private boolean prevRegex;
    
    private Search() {
    }
    
    /** */
    void initUI(SearchDialog dialog) {
        this.dialog = dialog;
        
    // component
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                component = new SearchViewComponent();
            }
        });
        
        String title = res.getStr("view.title");
        ImageIcon icon = Icons.getIcon(DefaultIcons.SEARCH);
        
    // view factory
        SearchViewFactory factory = new SearchViewFactory(
            VIEW_ID,component,title,icon);
        factory.setViewCategory(ViewCategory.LOG);
        MainFrame.get().getViewManager().registerViewFactory(factory);
        
    // view provider
        viewProvider = new DefaultViewProvider(title,icon,factory);
        MainFrame.get().getViewManager().addViewProvider(viewProvider);
    }
    
    /**
     * Adds a search source.
     *
     * @param searchSource The search source.
     */
    public void addSearchSource(SearchSource searchSource) {
        searchSourceList.add(searchSource);
    }
    
    /** */
    boolean search(String searchText,boolean matchCase,boolean regex) {
        prevSearchText = searchText;
        prevMatchCase = matchCase;
        prevRegex = regex;
        
        List<SearchOccurenceGroup> groups;
        try {
            groups = engine.search(
                searchSourceList,searchText,matchCase,regex);
        } catch (IOException exception) {
            MessageDialog.error(dialog,res.getStr("search.failed.title"),
                res.getStr("search.failed",exception.getMessage()));
            return false;
        }
        
        if (groups == null) {
            return false;
        }
        
        component.setResult(searchText,groups);
        viewProvider.showView();
        
        return true;
    }
    
    /** */
    void expandAll() {
        component.expandAll();
    }
    
    /** */
    void collapseAll() {
        component.collapseAll();
    }
    
    /** */
    void searchAgain() {
        search(prevSearchText,prevMatchCase,prevRegex);
    }
    
    /** */
    public static Search get() {
        if (instance == null) {
            instance = new Search();
        }
        
        return instance;
    }
}