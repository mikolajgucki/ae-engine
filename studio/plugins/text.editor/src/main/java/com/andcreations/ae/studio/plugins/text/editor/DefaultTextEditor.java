package com.andcreations.ae.studio.plugins.text.editor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;

/**
 * @author Mikolaj Gucki
 */
public class DefaultTextEditor {
    /** */
    public static class SyntaxMapping {
        /** */
        private EditorSyntax syntax;
        
        /** */
        private String[] suffixes;
        
        private SyntaxMapping(EditorSyntax syntax,String... suffixes) {
            this.syntax = syntax;
            this.suffixes = suffixes;
        }
    }
    
    /** */
    private static DefaultTextEditor instance;
    
    /** */
    private List<SyntaxMapping> syntaxMappingList = new ArrayList<>();
    
    /** */
    private DefaultTextEditor() {
        create();
    }
    
    /** */
    private void create() {
        addSyntaxMapping(EditorSyntax.LUA,".lua");
        addSyntaxMapping(EditorSyntax.JSON,".json");
        addSyntaxMapping(EditorSyntax.JAVA,".java");
        addSyntaxMapping(EditorSyntax.XML,".xml");
        addSyntaxMapping(EditorSyntax.PROPERTIES,".properties");
    }
    
    /** */
    private void addSyntaxMapping(EditorSyntax syntax,String... suffixes) {
        addSyntaxMapping(new SyntaxMapping(syntax,suffixes));
    }
    
    /** */
    public void addSyntaxMapping(SyntaxMapping syntaxMapping) {
        syntaxMappingList.add(syntaxMapping);
    }
    
    /** */
    private EditorSyntax getEditorSyntax(File file) {
        for (SyntaxMapping syntaxMapping:syntaxMappingList) {            
            for (String suffix:syntaxMapping.suffixes) {
                if (file.getName().endsWith(suffix) == true) {
                    return syntaxMapping.syntax;
                }
            }
        }
        
        return EditorSyntax.NONE;
    }
    
    /** */
    public void edit(File file) {
        EditorSyntax syntax = getEditorSyntax(file);
        EditorCfg cfg = new EditorCfg(
            syntax,file.getAbsolutePath(),DefaultIcons.FILE);
        TextEditor.get().edit(cfg);
    }
    
    /** */
    public static DefaultTextEditor get() {
        if (instance == null) {
            instance = new DefaultTextEditor();
        }
        
        return instance;
    }
}