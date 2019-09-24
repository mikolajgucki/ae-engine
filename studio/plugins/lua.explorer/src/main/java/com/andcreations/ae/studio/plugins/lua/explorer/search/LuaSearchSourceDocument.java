package com.andcreations.ae.studio.plugins.lua.explorer.search;

import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.file.cache.TextFileCache;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.search.SearchSourceDocument;

/**
 * @author Mikolaj Gucki
 */
class LuaSearchSourceDocument implements SearchSourceDocument {
    /** */
    private File file;
    
    /** */
    private String path;
    
    /** */
    LuaSearchSourceDocument(File file,String path) {
        this.file = file;
        this.path = path;
    }
    
    /** */
    @Override
    public String getId() {
        return path;
    }
    
    /** */
    @Override
    public ImageIcon getIcon() {
        return ProjectLuaFiles.get().getLuaFileIcon(file);
    }
    
    /** */
    @Override
    public String getName() {
        return path;
    }
    
    /** */
    @Override
    public String getText() throws IOException {
        return TextFileCache.get().read(file);
    }
    
    /** */
    @Override
    public String toString() {
        return file.getAbsolutePath();
    }
}