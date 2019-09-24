package com.andcreations.ae.studio.plugins.lua.debug.snippet;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.andcreations.ae.studio.plugins.text.editor.EditorSyntax;
import com.andcreations.ae.studio.plugins.text.editor.TextArea;
import com.andcreations.ae.studio.plugins.text.editor.TextAreaListener;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class LuaSnippetViewComponent extends JPanel {
    /** */
    private LuaSnippetViewComponentListener listener;
        
    /** */
    private TextArea textArea;
    
    /** */
    LuaSnippetViewComponent(LuaSnippetViewComponentListener listener) {
        this.listener = listener;
        create();
    }
    
    /** */
    private void create() {
        setLayout(new BorderLayout());
        final int border = 4;
        
    // top panel
        add(createTopPanel(border),BorderLayout.NORTH);
        
    // text area
        add(createTextArea(border),BorderLayout.CENTER);
    }
    
    /** */
    private JPanel createTopPanel(int border) {
        JPanel panel = new JPanel(new BorderLayout());
        
    // border
        panel.setBorder(BorderFactory.createEmptyBorder(
            border,border,border,border));        
        
        return panel;
    }
    
    /** */
    private TextArea createTextArea(int border) {
        textArea = new TextArea(EditorSyntax.LUA);
        textArea.addTextAreaListener(new TextAreaListener() {
            /** */
            @Override
            public void textChanged(TextArea textArea) {
                listener.textChanged();
            }
        });
        
    // border
        textArea.setBorder(BorderFactory.createEmptyBorder(
            border,border,border,border));        
        
        return textArea;
    }
    
    /** */
    String getText() {
        return textArea.getText();
    }
    
    /** */
    void setText(String text) {
        textArea.setText(text);
    }
}