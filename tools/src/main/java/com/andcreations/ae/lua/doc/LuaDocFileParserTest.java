package com.andcreations.ae.lua.doc;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public class LuaDocFileParserTest {
    /** */
    public static void main(String[] args) throws Exception {
        /*
        File dir = new File("e:/andcreations/ae/plugins/vungle");
        DirScanner dirScanner = new DirScanner(dir,false);
        String[] filesStr = dirScanner.build();
        List<File> files = new ArrayList<>();
        for (String fileStr:filesStr) {
            if (fileStr.endsWith(".cpp")) {
                files.add(new File(dir,fileStr));
            }
        }
        
        LuaDocCppFileListParser parser = new LuaDocCppFileListParser();
        LuaDocFileDataList dataList = parser.parse(files);
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(dataList));
        */
        
        /*
        LuaDocLuaFileParser parser = new LuaDocLuaFileParser();
        LuaDocParseContext ctx = new LuaDocParseContext();
        LuaDocFileData data = parser.parse(new File(
            "d:/andcreations/ae/engine/common/anim/src/lua/ae/anim/Animator.lua"),
            "ae.anim.Animator",ctx);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(data));        
        */
        
        /*
        LuaDocLuaDirParser parser = new LuaDocLuaDirParser();
        try {
            parser.parse(new String[]{
                //"e:/andcreations/ae/engine/common/anim/src/lua"
                //"e:/andcreations/ae/engine/common/audio/src/lua"
                //"e:/andcreations/ae/engine/common/core/src/lua"
                //"e:/andcreations/ae/engine/common/engine/src/lua"         
                //"e:/andcreations/ae/engine/common/event/src/lua"                
                //"d:/andcreations/ae/engine/common/gles2/src/lua"
                //"d:/andcreations/ae/engine/common/io/src/lua"                
                //"d:/andcreations/ae/engine/common/luadebugger/src/lua"                
                //"e:/andcreations/ae/engine/common/luaprofiler/src/lua",
                //"d:/andcreations/ae/engine/common/math/src/lua"
                //"d:/andcreations/ae/engine/common/texture/src/lua"
                //"e:/andcreations/ae/engine/common/ui/src/lua"
                //"e:/andcreations/ae/engine/common/util/src/lua"
                "e:/andcreations/ae/plugins/spine/common/src/lua"
            });
        } catch (LuaDocException exception) {
            System.out.println(exception.getMessage() + " [line " +
                exception.getLine() + "]");
        }
        */
        
        
        LuaDocLuaFileParser parser = new LuaDocLuaFileParser();
        //LuaDocCppFileParser parser = new LuaDocCppFileParser();
        LuaDocParseContext context = new LuaDocParseContext();
        LuaDocFileData data = parser.parse(
            new File("test.lua"),"test",context);
        
        System.out.println("warns:");
        for (LuaDocWarning warn:data.getWarnings()) {
            System.out.println("  " + warn.getMessage());
        }
        System.out.println("errors:");
        for (LuaDocError warn:data.getErrors()) {
            System.out.println("  " + warn.getMessage());
        }
        /*
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(data));
        */
    }
}