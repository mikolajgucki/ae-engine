package com.andcreations.ae.studio.plugins.text.editor;

import org.fife.ui.rtextarea.GutterIconInfo;

/**
 * Handle to a gutter icon.
 *
 * @author Mikolaj Gucki
 */
public class GutterIcon {
    /** */
    private int line;
    
    /** */
    private String iconName;
    
    /** */
    private String tooltip;
    
    /** */
    private EditorAnnotation.Priority priority;
    
    /** */
    private GutterIconInfo gutterIconInfo;
    
    /** */
    GutterIcon(int line,String iconName,String tooltip,
        EditorAnnotation.Priority priority) {
    //
        this.line = line;
        this.iconName = iconName;
        this.tooltip = tooltip;
        this.priority = priority;
    }
    
    /** */
    public int getLine() {
        return line;
    }
    
    /** */
    void setLine(int line) {
        this.line = line;
    }
    
    /** */
    String getIconName() {
        return iconName;
    }
    
    /** */
    String getTooltip() {
        return tooltip;
    }
    
    /** */
    EditorAnnotation.Priority getPriority() {
        return priority;
    }
    
    /** */
    void setGutterIconInfo(GutterIconInfo gutterIconInfo) {
        this.gutterIconInfo = gutterIconInfo;
    }
    
    /** */
    GutterIconInfo getGutterIconInfo() {
        return gutterIconInfo;
    }
    
    /** */
    @Override
    public String toString() {
        return iconName + ":" + line + " (" + tooltip + ")";
    }
}