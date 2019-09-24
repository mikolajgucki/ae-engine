package com.andcreations.ae.doc.api;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;

import com.andcreations.ae.lua.doc.LuaDocCppFileListParser;
import com.andcreations.ae.lua.doc.LuaDocError;
import com.andcreations.ae.lua.doc.LuaDocFileData;
import com.andcreations.ae.lua.doc.LuaDocFileDataList;
import com.andcreations.ae.lua.doc.LuaDocWarning;
import com.andcreations.ant.AETask;
import com.andcreations.ant.AntPath;
import com.andcreations.ant.FileSetHelper;
import com.andcreations.resources.BundleResources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Generates the document with the Lua C/C++ API.
 *
 * @author Mikolaj Gucki
 */
public class LuaCApiAntTask extends AETask {
    /** */
    private BundleResources res = new BundleResources(LuaCApiAntTask.class); 
    
    /** The source files. */
    private FileSet srcFileSet;    
    
    /** The destinstion file. */
    private AntPath dstFile;
    
    /** */
    public FileSet createSrcFiles() {
        if (srcFileSet != null) {
            duplicatedElement("srcfiles");
        }
        
        srcFileSet = new FileSet();
        srcFileSet.setProject(getProject());
        
        return srcFileSet;
    }    
    
    /** */
    public AntPath createDstFile() {
        if (dstFile != null) {
            duplicatedElement("dstfile");
        }
        
        dstFile = new AntPath();
        return dstFile;
    }    
    
    /** */
    private void readLuaDoc(List<File> files) {
    // parse the files
        LuaDocCppFileListParser parser = new LuaDocCppFileListParser();
        LuaDocFileDataList dataList = null;
        try {
            dataList = parser.parse(files);
        } catch (IOException exception) {
            throw new BuildException(res.getStr("failed.to.parse",
                exception.getMessage()),exception);
        }
        
        boolean issues = false;
    // check warnings, errors
        for (LuaDocFileData fileData:dataList.getDataList()) {
            if (fileData.getWarnings().isEmpty() == true &&
                fileData.getErrors().isEmpty() == true) {
            //
                continue;
            }
            issues = true;
            
            log(fileData.getFile().getAbsolutePath());
            for (LuaDocWarning warning:fileData.getWarnings()) {
                log(String.format("  Warning[line %d]: %s",
                    warning.getLine(),warning.getMessage()));
            }
            for (LuaDocError error:fileData.getErrors()) {
                log(String.format("  Error[line %d]: %s",
                    error.getLine(),error.getMessage()));
            }
        }
        if (issues == true) {
            throw new BuildException(res.getStr("issues"));
        }
        
    // JSON
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(dataList);
        
    // save
        File file = new File(dstFile.getPath());
        try {
            FileUtils.write(file,json,"UTF-8");
        } catch (IOException exception) {
            throw new BuildException(res.getStr("failed.to.write.json",
                file.getAbsolutePath(),exception.getMessage()),exception);
        }
    }
    
    /** */
    @Override
    public void execute() {
        verifyElementSet(srcFileSet,"srcfiles");
        verifyElementSet(dstFile,"dstfile");
        
        File[] files = FileSetHelper.getFiles(srcFileSet);
        readLuaDoc(Arrays.asList(files));
    }
}