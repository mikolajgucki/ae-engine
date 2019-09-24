package com.andcreations.ae.studio.plugins.ui.common.text;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

/**
 * @author Mikolaj Gucki
 */
public class TextComponentUtil {
    /** */
    public static int getLineOfOffset(JTextComponent component,int offset)
        throws BadLocationException {
    //
        Document document = component.getDocument();
        if (offset < 0 || offset > document.getLength()) {
            throw new BadLocationException("Invalid offset",offset);
        }
        
        Element map = document.getDefaultRootElement();
        return map.getElementIndex(offset);
    }

    /** */
    public static int getLineStartOffset(JTextComponent component,int line)
        throws BadLocationException {
    //
        Element map = component.getDocument().getDefaultRootElement();
        if (line < 0 || line >= map.getElementCount()) {
            throw new BadLocationException("Invalid line",-1);
        }
        
        Element lineElement = map.getElement(line);
        return lineElement.getStartOffset();
    }
}