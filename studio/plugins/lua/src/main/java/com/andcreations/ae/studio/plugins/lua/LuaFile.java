package com.andcreations.ae.studio.plugins.lua; 

import java.io.File;

import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;
import com.andcreations.ae.studio.plugins.text.editor.EditorCfg;
import com.andcreations.ae.studio.plugins.text.editor.EditorSyntax;
import com.andcreations.ae.studio.plugins.text.editor.TextEditor;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.lua.LuaUtil;

/**
 * @author Mikolaj Gucki
 */
public class LuaFile {
    /** */
    public static final String SUFFIX = LuaUtil.LUA_FILE_SUFFIX;
    
    /** */
    public static final String DOT_SUFFIX = LuaUtil.LUA_FILE_DOT_SUFFIX;
    
    /** */
    public static final String IDENTIFIER_REGEX = "[a-zA-Z_0-9]+";
    
    /** */
    public static final char MODULE_SEPARATOR = '.';
        
    /** */
    static void init() {
    }
        
    /** */
    public static boolean isLuaFile(File file) {
        return LuaUtil.isLuaFile(file);
    }

    /** */
    public static String getDecoIconName(File file) {
        if (Files.get().hasErrors(file) == true) {
            return DefaultIcons.DECO_ERROR;
        }
        
        if (Files.get().hasWarnings(file) == true) {
            return DefaultIcons.DECO_WARNING;
        }
        
        return null;        
    }
    
    /** */
    public static String getLuaFileIconName(File file) {
        if (ProjectFiles.get().isProjectFile(file) == true) {
            return LuaIcons.LUA_PROJECT_FILE;
        }
        
        return LuaIcons.LUA_FILE;
    }
    
    /** */
    public static String getLuaDirIconName(File dir) {
        if (ProjectFiles.get().isProjectFile(dir) == true) {
            return DefaultIcons.PROJECT_DIRECTORY;
        }
        
        return DefaultIcons.DIRECTORY;
    }
    
    /** */
    public static String getLuaDirIconName(Iterable<File> dirs) {
        if (dirs == null) {
            return DefaultIcons.DIRECTORY;
        }
        
    // for each directory
        for (File dir:dirs) {
            if (ProjectFiles.get().isProjectFile(dir) == false) {
                return DefaultIcons.DIRECTORY;
            }
        }
        
        return DefaultIcons.PROJECT_DIRECTORY;
    }
    
    /** */
    public static String removeSuffix(String filename) {
        if (filename.endsWith(DOT_SUFFIX) == true) {
            return filename.substring(0,
                filename.length() - DOT_SUFFIX.length());
        }
        
        return filename;
    }
    
    /**
     * Gets the path which can be appended to <code>package.path</code>.
     *
     * @param dir The directory.
     * @return The package path corresponding to the directory.
     */
    public static String getPackagePath(File dir) {
        return LuaUtil.getPackagePath(dir);
    }
    
    /** */
    public static EditorCfg getEditorCfg(File file) {
        return new EditorCfg(EditorSyntax.LUA,file.getAbsolutePath(),
            getLuaFileIconName(file));
    }
    
    /** */
    public static void edit(EditorCfg cfg) {
        TextEditor.get().edit(cfg);        
    }
    
    /** */
    public static void edit(File file,int line) {
        EditorCfg cfg = getEditorCfg(file);
        cfg.setLine(line);
        edit(cfg);        
    }
    
    /** */
    public static void edit(File file,int startLine,int endLine) {
        EditorCfg cfg = getEditorCfg(file);
        cfg.setLineRange(startLine,endLine);
        edit(cfg);        
    }
    
    /** */
    public static void edit(File file) {
        edit(file,1);
    }
}