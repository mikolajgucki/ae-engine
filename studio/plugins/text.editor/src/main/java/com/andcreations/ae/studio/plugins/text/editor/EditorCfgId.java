package com.andcreations.ae.studio.plugins.text.editor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;

/**
 * @author Mikolaj Gucki
 */
class EditorCfgId {
    /** */
    private static final char SEPARATOR = ':';
    
    /** */
    private static final char ESCAPE = '\\';
    
    /** */
    private static String escape(String str) {
        return str.replace("" + SEPARATOR,"" + ESCAPE + SEPARATOR);
    }
    
    /** */
    static String toId(EditorCfg cfg) {
        // path
        String path;
        if (cfg.getProjectPath() != null) {
            path = cfg.getProjectPath();
        }
        else {
            path = cfg.getFilePath();
        }
        path = path.replace(ESCAPE,'/');
        
    // project file
        String projectFileFlag;
        if (cfg.getProjectPath() != null) {
            projectFileFlag = EditorCfg.PROJECT_FILE_FLAG;
        }
        else {
            projectFileFlag = "";
        }
        
        return String.format("%s%c%s%c%s%c%s",
            escape(path),SEPARATOR,
            escape(projectFileFlag),SEPARATOR,
            escape(cfg.getEditorSyntax().name()),SEPARATOR,
            escape(cfg.getIconName()),SEPARATOR);         
    }
    
    /** */
    private static String[] split(String id) {
        List<String> tokens = new ArrayList<>();
        
        StringBuilder token = new StringBuilder();
        boolean escape = false;
    // for each character
        for (int index = 0; index < id.length(); index++) {
            char ch = id.charAt(index);
            
        // escape character
            if (ch == ESCAPE) {
                if (escape == true) {
                    token.append(ESCAPE);
                    escape = false;
                }
                else {
                    escape = true;
                }
                continue;
            }
            
        // escaped character
            if (escape == true) {
                token.append(ch);
                escape = false;
                continue;
            }
            
        // separator
            if (ch == SEPARATOR) {
                tokens.add(token.toString());
                token = new StringBuilder();
                continue;
            }
            
        // another character
            token.append(ch);
        }
        
        if (token.length() > 0) {
            tokens.add(token.toString());
        }
        
        return tokens.toArray(new String[]{});
    }
    
    /** */
    static EditorCfg fromId(String id) {
        String[] tokens = split(id);
        
    // path
        String path = tokens[0];
        
    // project file
        String projectFileFlag = tokens[1];
        if (EditorCfg.PROJECT_FILE_FLAG.equals(projectFileFlag) == true) {
            File file = ProjectFiles.get().getFileFromRelativePath(path);
            path = file.getAbsolutePath();
        }
        
    // syntax
        String syntaxName = tokens[2];
        EditorSyntax syntax = EditorSyntax.valueOf(syntaxName);
        
    // icon name
        String iconName = tokens[3];
        
        return new EditorCfg(syntax,path,iconName);        
    }
}
