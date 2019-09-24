package com.andcreations.ae.studio.plugins.lua.debug.breakpoints;

import java.io.File;

import com.andcreations.ae.studio.plugins.text.editor.EditorMediator;
import com.andcreations.ae.studio.plugins.text.editor.GutterIcon;
import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class LuaBreakpoint {
    /** */
    private BundleResources res = new BundleResources(LuaBreakpoint.class);      
    
    /** The file in which the breakpoint is set. */
    private File file;
    
    /** The name of the sorce in which the breakpoint is set. */
    private String source;
    
    /** The line in which the breakpoint is set. */
    private int line;    
    
    /** Indicates if the breakpoint is enabled. */
    private boolean enabled;
    
    /** The editor associated with the breakpoint. */
    private EditorMediator editor;
    
    /** The gutter icon associated with this breakpoint. */
    private GutterIcon gutterIcon;
    
    /** */
    LuaBreakpoint(File file,String source,int line,boolean enabled) {
        this.file = file;
        this.source = source;
        this.line = line;
        this.enabled = enabled;
    }
    
    /** */
    LuaBreakpoint(File file,String source,int line) {
        this(file,source,line,true);
    }
    
    /**
     * Gets the file in which the breakpoint is set.
     *
     * @return The file.
     */
    public File getFile() {
        return file;
    }
    
    /**
     * Gets the name of the source in which the breakpoint is set.
     *
     * @return The source file.
     */
    public String getSource() {
        return source;
    }
    
    /**
     * Gets the line in which the breakpoint is set.
     *
     * @return The line number.
     */
    public int getLine() {
        return line;
    }
    
    /** */
    int updateLine() {
        int oldLine = line;
        line = gutterIcon.getLine();
        
        return oldLine;
    }
    
    /** */
    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /**
     * Checks if the breakpoint is enabled.
     *
     * @return <code>true</code> if enabled, <code>false</code> otherwise.
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /** */
    void setGutterIcon(GutterIcon gutterIcon) {
        this.gutterIcon = gutterIcon;
    }
    
    /** */
    GutterIcon getGutterIcon() {
        return gutterIcon;
    }
    
    /** */
    void setEditor(EditorMediator editor) {
        this.editor = editor;
    }
    
    /** */
    EditorMediator getEditor() {
        return editor;
    }
    
    /** */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LuaBreakpoint) {
            LuaBreakpoint breakpoint = (LuaBreakpoint)obj;
            return source.equals(breakpoint.getSource()) == true &&
                line == breakpoint.getLine();
        }
        
        return false;
    }
    
    /** */
    String getText() {
        return res.getStr("text",source,Integer.toString(line));
    }
    
    /** */
    String getHtml() {
        String dark = UIColors.toHex(UIColors.dark());
        return res.getStr("html",source,Integer.toString(line),dark);
    }
    
    /** */
    @Override
    public String toString() {
        return source + ":" + line;
    }
}