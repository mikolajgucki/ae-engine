package com.andcreations.ae.lua.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.dist.AEDistFiles;
import com.andcreations.ae.project.AEProject;
import com.andcreations.ae.project.AEProjectHelper;
import com.andcreations.ae.project.NoSuchVariableException;
import com.andcreations.io.DirScanner;
import com.andcreations.lua.LuaUtil;
import com.andcreations.lua.runner.LuaRunner;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class ProjectLuaTestRunner {
    /** */
    private final BundleResources res =
        new BundleResources(ProjectLuaTestRunner.class); 
        
    /** */
    private File aeDistDir;
    
    /** */
    private File projectDir;
    
    /** */
    private File luaTmpFile;
    
    /** */
    private LuaTestRunnerListener[] luaTestRunnerListeners;
    
    /** */
    private AEProjectHelper projectHelper;
    
    /** */
    private LuaTestRunner luaTestRunner;
    
    /** */
    public ProjectLuaTestRunner(File aeDistDir,File projectDir,File luaTmpFile,
        LuaTestRunnerListener... luaTestRunnerListeners) {
    //
        this.aeDistDir = aeDistDir;
        this.projectDir = projectDir;
        this.luaTmpFile = luaTmpFile;
        this.luaTestRunnerListeners = luaTestRunnerListeners;
    }
    
    /** */
    private void createProjectHelper() throws IOException {
        projectHelper = new AEProjectHelper(aeDistDir,projectDir);
        File propertiesFile = projectHelper.getPropertiesFile();
        
    // initialize
        try {
            projectHelper.init();
        } catch (IOException exception) {
            throw exception;
        } catch (NoSuchVariableException exception) {
            throw new IOException(res.getStr("env.var.not.found",
                exception.getName(),exception.getReferringVarName(),
                propertiesFile.getAbsolutePath()),exception);
        }
    }
    
    /** */
    private List<File> listLuaTestFiles() {
        File luaTestsDir = new File(projectDir,AEProject.TESTS_LUA_PATH);
        DirScanner scanner = new DirScanner(luaTestsDir,false);
        String[] paths = scanner.build();
        
        List<File> luaFiles = new ArrayList<>();
    // for each path
        for (String path:paths) {
            if (LuaUtil.isLuaFile(path) == true) {
                File file = scanner.getFile(path);
                try {
                    file = file.getCanonicalFile();
                } catch (IOException exception) {
                }
                luaFiles.add(file);
            }
        }
        
        return luaFiles;
    }
    
    /** */
    private List<File> getLuaPkgPaths() {
        List<File> dirs = projectHelper.getLuaSrcDirs();
        for (int index = 0; index < dirs.size(); index++) {
            try {
                dirs.set(index,dirs.get(index).getCanonicalFile());
            } catch (IOException exception) {
            }
        }
        return dirs;
    }
    
    /** */
    public void run() throws IOException {
        createProjectHelper();
        List<File> luaFiles = listLuaTestFiles();
     
    // Lua test runner
        LuaRunner luaRunner = new LuaRunner(AEDistFiles.getLuaExec(aeDistDir));
        File aeTestSrcDir = AEDistFiles.getLuaTestSrcDir(aeDistDir);
        luaTestRunner = new LuaTestRunner(
            luaRunner,aeTestSrcDir,luaTestRunnerListeners);
        
    // run tests
        luaTestRunner.run(luaFiles,luaTmpFile,getLuaPkgPaths());
        synchronized (this) {
            luaTestRunner = null;
        }
    }
    
    /**
     * Terminates the runner if running.
     */
    public void terminate() {
        synchronized (this) {
            if (luaTestRunner != null) {
                luaTestRunner.terminate();
            }
        }
    }
}