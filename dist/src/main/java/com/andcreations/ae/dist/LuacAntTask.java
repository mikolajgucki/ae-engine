package com.andcreations.ae.dist;

import java.io.File;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;

import com.andcreations.ant.AETask;
import com.andcreations.ant.AntPath;
import com.andcreations.ant.FileSetHelper;
import com.andcreations.resources.BundleResources;
import com.andcreations.resources.StrResources;

/**
 * @author Mikolaj Gucki
 */
public class LuacAntTask extends AETask {
    /** The string resources. */
    private StrResources res = new BundleResources(LuacAntTask.class); 
    
    /** The path to Lua compiler (luac) executable. */
    private AntPath luacExec;

    /** The path to the destination directory. */
    private AntPath dstDir;
    
    /** The source files. */
    private FileSet srcFileSet;
    
    /** */
    public AntPath createLuacExec() {
        if (luacExec != null) {
            duplicatedElement("luacexec");
        }
        
        luacExec = new AntPath();
        return luacExec;
    }
    
    /** */
    public AntPath createDstDir() {
        if (dstDir != null) {
            duplicatedElement("dstdir");
        }
        
        dstDir = new AntPath();
        return dstDir;
    }
    
    /** */
    public FileSet createSrcFiles() {
        if (srcFileSet != null) {
            duplicatedElement("srcfiles");
        }
        
        srcFileSet = new FileSet();
        return srcFileSet;
    }    
    
    /**
     * Gets the source files.
     *
     * @return The source files.
     */
    private File[] getSrcFiles() {
        File[] files = FileSetHelper.getFiles(srcFileSet);
        return files;
    }    
    
    /**
     * Compiles a source file.
     *
     * @param srcFile The source file.
     */
    private void compile(File srcFile) {
        File dstFile = new File(dstDir.getPath(),srcFile.getName());
        
    // command
        CommandLine cmd = new CommandLine(luacExec.getPath());
        cmd.addArgument("-o");
        cmd.addArgument(dstFile.getAbsolutePath());
        cmd.addArgument(srcFile.getAbsolutePath());
        
    // execute
        try {
            DefaultExecutor executor = new DefaultExecutor();
            int exitValue = executor.execute(cmd);
            if (exitValue != 0) {
                throw new BuildException(res.getStr("luac.failed.exit.value",
                    Integer.toString(exitValue)));
            }
        } catch (Exception exception) {
            throw new BuildException(exception.getMessage());
        }
        
    }
    
    /**
     * Compiles source files.
     *
     * @param srcFiles The source files.
     */
    private void compile(File[] srcFiles) {
        for (File srcFile:srcFiles) {
            compile(srcFile);
        }
    }
    
    /** */
    @Override
    public void execute() {
        verifyElementSet(luacExec,"luacexec");
        verifyElementSet(dstDir,"dstDir");
        verifyElementSet(srcFileSet,"srcfiles");
        
        File[] srcFiles = getSrcFiles();
        compile(srcFiles);
    }
}