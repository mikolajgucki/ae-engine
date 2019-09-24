package com.andcreations.ae.studio.plugins.lua.parser;

import java.io.File;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.file.LineLocation;
import com.andcreations.ae.studio.plugins.file.problems.FileProblems;
import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.problems.Problem;
import com.andcreations.ae.studio.plugins.problems.ProblemListener;
import com.andcreations.ae.studio.plugins.problems.ProblemSeverity;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;

/**
 * @author Mikolaj Gucki
 */
class LuaParserProblems extends FileProblems {
    /** */
    private static final String SOURCE_ID = LuaParserProblems.class.getName();  
    
    /** */
    LuaParserProblems() {
        super(SOURCE_ID);
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
    void addWarning(final File file,String msg,final int line) {
        Problem problem = addProblem(file,ProblemSeverity.WARNING,msg,
            getResource(file),new LineLocation(line));
        problem.addProblemListener(new ProblemListener() {
            /** */
            @Override
            public void problemDoubleClicked(Problem problem) {
                LuaFile.edit(file,line);
            }
        });
    }
    
    /** */
    void addError(final File file,String msg) {
        Problem problem = addProblem(
            file,ProblemSeverity.ERROR,msg,getResource(file));
        problem.addProblemListener(new ProblemListener() {
            /** */
            @Override
            public void problemDoubleClicked(Problem problem) {
                LuaFile.edit(file);
            }
        });        
    }
    
    /** */
    void handleParseError(File file,Exception exception) {
        clear(file);                    
    // Don't push the error to the problems view. Lua compiler will do that.
        Log.error(String.format("Failed to parse Lua file %s\n  -> %s",
            file.getAbsolutePath(),exception.getMessage()));
    }
    
    /** */
    void clear(File file) {
        removeProblems(file);
    }    
}