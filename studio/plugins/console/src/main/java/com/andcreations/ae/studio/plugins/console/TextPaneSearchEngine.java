package com.andcreations.ae.studio.plugins.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;

import com.andcreations.ae.studio.plugins.search.common.SearchMatcher;
import com.andcreations.ae.studio.plugins.search.common.SearchOccurence;
import com.andcreations.ae.studio.plugins.ui.common.text.TextComponentUtil;

/**
 * @author Mikolaj Gucki
 */
class TextPaneSearchEngine {
    /** */
    private JTextPane textPane;
    
    /** */
    private DefaultHighlightPainter painter;
    
    /** */
    public TextPaneSearchEngine(JTextPane textPane) {
        this.textPane = textPane;
        create();
    }
    
    /** */
    private void create() {
        painter = new DefaultHighlightPainter(
            textPane.getBackground().brighter().brighter());
    }
    
    /** */
    List<SearchOccurence> search(SearchMatcher matcher) {
        List<SearchOccurence> allOccurences = new ArrayList<>();
        
        Document document = textPane.getDocument();
        String text = null;
        try {
            text = document.getText(0,document.getLength());
        } catch (BadLocationException exception) {
            // never thrown
        }
        
        BufferedReader reader = new BufferedReader(new StringReader(text));
        int lineNo = 0;
        
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                List<SearchOccurence> occurences = matcher.match(line,lineNo);
                allOccurences.addAll(occurences);
                lineNo++;
            }
        } catch (IOException exception) {
            // never thrown
        }
        
        return allOccurences;
    }
    
    /** */
    void clear() {
        Highlighter highlighter = textPane.getHighlighter();
        highlighter.removeAllHighlights();
    }
    
    /** */
    void highlight(List<SearchOccurence> occurences) {
        clear();
        Highlighter highlighter = textPane.getHighlighter();
        
    // for each occurence
        for (SearchOccurence occurence:occurences) {
            try {
                int offset = TextComponentUtil.getLineStartOffset(
                    textPane,occurence.getLineNo());
                int start = offset + occurence.getStart();
                int end = offset + occurence.getEnd();
                highlighter.addHighlight(start,end,painter);
            } catch (BadLocationException exception) {
                // the document must have changed since the search
            }
        }
    }
    
    /** */
    boolean goTo(SearchOccurence occurence) {
        try {
            int offset = TextComponentUtil.getLineStartOffset(
                textPane,occurence.getLineNo()) + occurence.getStart();
            textPane.setCaretPosition(offset);
        } catch (BadLocationException exception) {
            return false;
        }
        
        return true;
    }
}