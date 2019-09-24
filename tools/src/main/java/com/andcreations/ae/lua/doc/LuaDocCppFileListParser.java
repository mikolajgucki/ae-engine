package com.andcreations.ae.lua.doc;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class LuaDocCppFileListParser {
    /** */
    private LuaDocCppFileParser cppFileParser = new LuaDocCppFileParser();
    
    /** */
    public LuaDocFileDataList parse(List<File> files) throws IOException {
    //
        LuaDocFileDataList dataList = new LuaDocFileDataList();
        
    // for each file
        for (File file:files) {
            LuaDocFileData fileData = cppFileParser.parse(
                file,new LuaDocParseContext());
            fileData.setFile(file);
            if (fileData.isEmpty() == false) {
                String definedIn = String.format("C/C++ (%s)",file.getName());
                for (LuaDocVar var:fileData.getVars()) {
                    var.setDefinedIn(definedIn);
                }
                for (LuaDocFunc func:fileData.getFuncs()) {
                    func.setDefinedIn(definedIn);
                }
                
                dataList.addFileData(fileData);
            }
        }
        
        return dataList;
    }
}
