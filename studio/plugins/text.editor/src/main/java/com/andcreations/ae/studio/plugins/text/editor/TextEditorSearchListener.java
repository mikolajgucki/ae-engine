package com.andcreations.ae.studio.plugins.text.editor;

import org.fife.rsta.ui.search.SearchEvent;
import org.fife.rsta.ui.search.SearchListener;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import org.fife.ui.rtextarea.SearchResult;

import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class FindDialogSearchListener implements SearchListener {
    /** */
    private static final BundleResources res =
        new BundleResources(FindDialogSearchListener.class);       
    
    /** */
    @Override
    public void searchEvent(SearchEvent event) {
        EditorMediator mediator = TextEditor.get().getFocusMediator();
        if (mediator == null) {
            return;
        }
        RSyntaxTextArea textArea = mediator.getComponent().getTextArea();
        
        SearchEvent.Type type = event.getType();
        SearchContext context = event.getSearchContext();
        SearchResult result = null;

        switch (type) {
            case MARK_ALL:
                result = SearchEngine.markAll(textArea,context);
                break;
                
            case FIND:
                result = SearchEngine.find(textArea,context);
                if (result.wasFound() == false) {
                    noResultsFound();
                    return;
                }
                break;
                
            case REPLACE:
                result = SearchEngine.replace(textArea,context);
                if (result.wasFound() == false) {
                    noResultsFound();
                    return;
                }
                break;
                
            case REPLACE_ALL:
                result = SearchEngine.replaceAll(textArea,context);
                /*
                JOptionPane.showMessageDialog(null, result.getCount() +
                        " occurrences replaced.");*/
                break;
            default:
        }
    }
    
    /** */
    @Override
    public String getSelectedText() {
        EditorMediator mediator = TextEditor.get().getFocusMediator();
        if (mediator == null) {
            return null;
        }
        
        RSyntaxTextArea textArea = mediator.getComponent().getTextArea();
        return textArea.getSelectedText();
    }
    
    /** */
    private static void noResultsFound() {
        CommonDialogs.info(res.getStr("not.found.title"),
            res.getStr("not.found.message"));
    }    
}