package com.andcreations.ae.studio.plugins.lua.compiler;

import java.io.File;

import com.andcreations.ae.studio.plugins.file.LineLocation;
import com.andcreations.ae.studio.plugins.file.problems.FileProblems;
import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.lua.lib.NativeLua;
import com.andcreations.ae.studio.plugins.lua.lib.NativeLuaException;
import com.andcreations.ae.studio.plugins.problems.Problem;
import com.andcreations.ae.studio.plugins.problems.ProblemListener;
import com.andcreations.ae.studio.plugins.problems.ProblemSeverity;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;

/**
 * Manages all the compilation errors.
 *
 * @author Mikolaj Gucki
 */
class LuaCompilerProblems extends FileProblems {
    /** */
    private static final String SOURCE_ID = LuaCompilerProblems.class.getName();   
    
    /** */
    LuaCompilerProblems() {
        super(SOURCE_ID);
    }

    /** */
    private void handleNativeLuaException(File file,
        NativeLuaException exception) {
    // TODO Handle the native Lua exception.
    }
    
    /** */
    private String getResource(File file) {
        String resource = ProjectLuaFiles.get().getPath(file);
        if (resource == null) {
            resource = file.getAbsolutePath();
        }
        return resource;
    }
    
    /** */
    void handleError(File file,String error) {
        addProblem(file,ProblemSeverity.ERROR,error,getResource(file),
            LuaCompiler.PROBLEM_TYPE);        
    }
    
    /** */
    void handleLuaCompilationError(final File file,String luaError) {
        clearErrors(file);
        
    // parse error
        int line;
        String message;
        try {
            line = NativeLua.getErrorLineNumber(luaError);
            message = NativeLua.getErrorMessage(luaError).trim();
        } catch (NativeLuaException exception) {
            handleNativeLuaException(file,exception);
            return;
        }
        
        final int finalLine = line;
    // listener
        ProblemListener listener = new ProblemListener() {
            /** */
            public void problemDoubleClicked(Problem problem) {
                LuaFile.edit(file,finalLine);
            }
        };
        
    // add problem
        addProblem(file,ProblemSeverity.ERROR,message,getResource(file),
            new LineLocation(line),LuaCompiler.PROBLEM_TYPE,listener);
    }
    
    /** */
    void clearErrors(File file) {
        removeProblems(file);
    }
}
