package com.andcreations.ae.studio.plugins.text.editor;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.andcreations.ae.studio.log.Log;

/**
 * A text area.
 *
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class TextArea extends JPanel {
    /** */
    private RSyntaxTextArea textArea;    
    
    /** */
    private RTextScrollPane textAreaScrollPane;

    /** */
    private List<TextAreaListener> listeners = new ArrayList<>();    
    
    /** */
    public TextArea(EditorSyntax syntax) {
        create(syntax);
    }
    
    /** */
    public void create(EditorSyntax syntax) {
    // text area
        textArea = new RSyntaxTextArea(10,1);
        textArea.setSyntaxEditingStyle(syntax.getTextAreaEditingStyle());
        createDocumentListener();

    // configure
        RSyntexTextAreaCfg cfg = new RSyntexTextAreaCfg(textArea);
        cfg.configure();
        
    // scroll
        textAreaScrollPane = new RTextScrollPane(textArea);
        textAreaScrollPane.setIconRowHeaderEnabled(true);
        textAreaScrollPane.getGutter().setBackground(
            Colors.getBackgroundColor());
        
        setLayout(new BorderLayout());
        add(textAreaScrollPane,BorderLayout.CENTER);
    }
    
    /** */
    private void createDocumentListener() {
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            /** */
            @Override
            public void changedUpdate(DocumentEvent event) {
            }
            
            /** */
            @Override
            public void insertUpdate(DocumentEvent event) {
                textChanged();
            }
            
            /** */
            @Override
            public void removeUpdate(DocumentEvent event) {
                textChanged();
            }
        });
    }    
    
    /** */
    public void addTextAreaListener(TextAreaListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    public void removeTextAreaListener(TextAreaListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }
    
    /** */
    private void textChanged() {
        synchronized (listeners) {
            for (TextAreaListener listener:listeners) {
                listener.textChanged(this);
            }
        }
    }
    
    /** */
    public void setText(String text) {
        Document doc = textArea.getDocument();
        try {
            doc.remove(0,doc.getLength());
            doc.insertString(0,text,null);
        } catch (BadLocationException exception) {
            Log.error(exception.toString());
        }
    }
    
    /** */
    public String getText() {
        Document doc = textArea.getDocument();
        try {
            return doc.getText(0,doc.getLength());
        } catch (BadLocationException exception) {
            Log.error(exception.toString());
            return null;
        }
    }    
}