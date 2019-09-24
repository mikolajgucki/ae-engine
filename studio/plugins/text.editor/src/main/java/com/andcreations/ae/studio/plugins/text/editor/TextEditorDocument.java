package com.andcreations.ae.studio.plugins.text.editor;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class TextEditorDocument extends RSyntaxDocument {
    /** */
    private TextEditorDocumentListener listener;
    
    /** */
    TextEditorDocument(String syntaxStyle,TextEditorDocumentListener listener) {
        super(syntaxStyle);
        this.listener = listener;
    }
    
    /** */
    int getLineStartOffset(int line) throws BadLocationException {
        Element map = getDefaultRootElement();
        if (line < 0) {
            throw new BadLocationException("Negative line",-1);
        }
        else if (line >= map.getElementCount()) {
            throw new BadLocationException("No such line",getLength() + 1);
        }
        else {
            Element lineElement = map.getElement(line);
            return lineElement.getStartOffset();
        }
    }
    
    /** */
    @Override
    public void remove(int offset,int length) throws BadLocationException { 
        String text = getText(offset,length);
        listener.removingText(offset,length,text);
        super.remove(offset,length);
    }
}
