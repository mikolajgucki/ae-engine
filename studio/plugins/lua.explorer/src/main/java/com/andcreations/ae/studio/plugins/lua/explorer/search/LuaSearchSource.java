package com.andcreations.ae.studio.plugins.lua.explorer.search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.search.SearchSource;
import com.andcreations.ae.studio.plugins.search.SearchSourceDocument;
import com.andcreations.ae.studio.plugins.text.editor.EditorCfg;

/**
 * @author Mikolaj Gucki
 */
public class LuaSearchSource implements SearchSource {
    /** */
    @Override
    public List<SearchSourceDocument> getDocuments() {
        List<SearchSourceDocument> documents = new ArrayList<>();
        
        List<File> files = ProjectLuaFiles.get().getLuaSourceFiles();
    // for each file
        for (File file:files) {
            String path = ProjectLuaFiles.get().getPath(file);
            documents.add(new LuaSearchSourceDocument(file,path));
        }
        
        return documents;
    }
    
    /** */
    @Override
    public void openDocument(String id,int line,int start,int end) {
        File file = ProjectLuaFiles.get().getFileByPath(id);
        if (file != null) {
            EditorCfg cfg = LuaFile.getEditorCfg(file);
            cfg.setLineRange(line,line + 8);
            cfg.setLineOffset(start);
            LuaFile.edit(cfg);
        }
    }
}