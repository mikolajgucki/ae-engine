package com.andcreations.ae.studio.plugins.lua.autocompletion.luadoc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.andcreations.ae.lua.doc.LuaDocDescription;
import com.andcreations.ae.lua.doc.LuaDocFileData;
import com.andcreations.ae.lua.doc.LuaDocFileDataList;
import com.andcreations.ae.lua.doc.LuaDocFunc;
import com.andcreations.ae.lua.doc.LuaDocVar;
import com.andcreations.ae.studio.plugins.ae.dist.AEDist;
import com.andcreations.ae.studio.plugins.problems.ProblemSeverity;
import com.andcreations.ae.studio.plugins.problems.Problems;
import com.andcreations.ae.studio.plugins.text.editor.EditorMediator;
import com.andcreations.ae.studio.plugins.text.editor.autocomplete.AutocomplSource;
import com.andcreations.ae.studio.plugins.text.editor.autocomplete.FuncAutocompl;
import com.andcreations.resources.BundleResources;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Provides the LuaDoc from the C/C++ files.
 *
 * @author Mikolaj Gucki
 */
public class CApiAutocompletion {    
    /** The path to the JSON in the distribution. */
    private static final String C_API_PATH = "doc/api/lua-c-api.json";
    
    /** */
    private static final String PROBLEM_SOURCE_ID =
        CApiAutocompletion.class.getName();
    
    /** */
    private static CApiAutocompletion instance;
    
    /** */
    private static final BundleResources res =
        new BundleResources(CApiAutocompletion.class);     
    
    /** */
    private List<LuaDocFileDataList> dataLists = new ArrayList<>();
     
    /** */
    private void createSource(LuaDocFileDataList dataList,
        AutocomplSource source) {
    // for each element
        for (LuaDocFileData luaDocData:dataList.getDataList()) {
        // for each function
            for (LuaDocFunc luaDocFunc:luaDocData.getFuncs()) {
                List<FuncAutocompl> funcs =
                    LuaDocFuncConverter.convert(luaDocFunc);
                for (FuncAutocompl func:funcs) {
                    source.addFunc(func);
                }
            }            
            
        // for each variable
            for (LuaDocVar luaDocVar:luaDocData.getVars()) {
                source.addVar(LuaDocVarConverter.convert(luaDocVar));
            }
        }
    }
    
    /** */
    private AutocomplSource createSource() {
        AutocomplSource source = new AutocomplSource();
        
    // for each data list
        for (LuaDocFileDataList dataList:dataLists) {
            createSource(dataList,source);
        }
        
        return source;
    }
    
    /** */
    public void install(EditorMediator editor) {
        if (dataLists == null || dataLists.isEmpty() == true) {
            return;
        }
        
        editor.getAutocompletion().addSource(createSource());
    }
    
    /** */
    private void failedToRead(File file,Exception exception) {
        Problems.get().add(PROBLEM_SOURCE_ID,ProblemSeverity.ERROR,
            res.getStr("failed.to.read",exception.getMessage()),
            file.getAbsolutePath());
    }
    
    /** */
    private void readLuaDoc(File file) {
    // read
        String src;
        try {
            src = FileUtils.readFileToString(file,"UTF-8");
        } catch (IOException exception) {
            failedToRead(file,exception);
            return;
        }
        
        LuaDocFileDataList dataList = null;
    // parse
        try {
            Gson gson = new Gson();
            dataList = gson.fromJson(src,LuaDocFileDataList.class);
        } catch (JsonSyntaxException exception) {
            failedToRead(file,exception);
            return;
        }
        
    // process descriptions
        try {
            LuaDocDescription desc = new LuaDocDescription();
            desc.setLuaDocLinksEnabled(false);
            desc.process(dataList);
        } catch (IOException exception) {
            failedToRead(file,exception);
            return;
        }
        
        dataLists.add(dataList);
    }
    
    /** */
    private void readLuaDoc() {
    // AE distribution
        readLuaDoc(new File(AEDist.get().getAEDistDir(),C_API_PATH));
        
    // TODO Limit plugins to the ones used in the project.
    // plugins
        List<File> pluginDirs = AEDist.get().getPluginDirs();
        for (File pluginDir:pluginDirs) {
            File apiFile = new File(pluginDir,C_API_PATH);
            if (apiFile.exists() == false) {
                continue;
            }
            readLuaDoc(apiFile);
        }
    }
    
    /** */
    public void init() {
        readLuaDoc();
    }
    
    /** */
    public static CApiAutocompletion get() {
        if (instance == null) {
            instance = new CApiAutocompletion();
        }
        
        return instance;
    }
}