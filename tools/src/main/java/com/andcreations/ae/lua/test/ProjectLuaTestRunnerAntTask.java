package com.andcreations.ae.lua.test;

import java.io.File;

import org.apache.tools.ant.BuildException;

import com.andcreations.ant.AETask;
import com.andcreations.ant.AntPath;
import com.andcreations.lua.PCallError;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class ProjectLuaTestRunnerAntTask extends AETask {
    /** */
    private final BundleResources res =
        new BundleResources(ProjectLuaTestRunnerAntTask.class); 
    
    /** */
    LuaTestRunnerListener listener = new LuaTestRunnerPCallErrorAdapter() {
        /** */
        @Override
        public void failedToRunTestSuite(String moduleName,PCallError error) {
            log(res.getStr("failed.to.run.test.suite",
                moduleName,error.getMessage()));
        }
        
        /** */
        @Override
        public void runningTestSuite(String moduleName,File file) {
            log(res.getStr("running.suite",moduleName));
        }
        
        /** */
        @Override
        public void notFunctionError(String moduleName,String testName) {
            log(res.getStr("not.a.func",testName));
        }
        
        /** */
        @Override
        public void setUpFailed(String moduleName,String testName,
            PCallError error) {
        //
            log(res.getStr("set.up.failed",testName,error.getMessage()));
        }
        
        /** */
        @Override
        public void tearDownFailed(String moduleName,String testName,
            PCallError error) {
        //
            log(res.getStr("tear.down.failed",testName,error.getMessage()));
        }
        
        /** */
        @Override
        public void testFinished(String moduleName,String testName) {
            log(res.getStr("finished",testName));
        }
        
        /** */
        @Override
        public void testFailed(String moduleName,String testName,
            PCallError error) {
        //
            log(res.getStr("failed",testName,error.getMessage()));
        }
        
        /** */
        @Override
        public void testSuiteFinished(String moduleName) {
            log(""); // separator
        }
        
        /** */
        @Override
        public void linePrinted(String line) {
            log(line);
        }
    };    
    
    /** */
    private AntPath aeDistDir;
    
    /** */
    private AntPath projectDir;
    
    /** */
    private LuaTestRunnerStats stats = new LuaTestRunnerStats();
    
    /** */
    public AntPath createAEDistDir() {
        if (aeDistDir != null) {
            duplicatedElement("aedistdir");
        }
        
        aeDistDir = new AntPath();
        return aeDistDir;
    }
    
    /** */
    public AntPath createProjectDir() {
        if (projectDir != null) {
            duplicatedElement("projectdir");
        }
        
        projectDir = new AntPath();
        return projectDir;
    }    
    
    /** */
    @Override
    public void execute() {
        verifyElementSet(projectDir,"projectdir");
        verifyElementSet(aeDistDir,"aedistdir");
        
    // temporary file
        File tmpFile;
        try {
            tmpFile = File.createTempFile("test","lua");
        } catch (Exception exception) {
            throw new BuildException(exception);
        }
        
    // create test runner
        ProjectLuaTestRunner runner = new ProjectLuaTestRunner(
            new File(aeDistDir.getPath()),new File(projectDir.getPath()),
            tmpFile,listener,stats);
        
    // run the tests
        try {
            runner.run();
        } catch (Exception exception) {
            throw new BuildException(exception);
        }
        
    // stats
        log(res.getStr("suite.summary",Integer.toString(stats.getSuiteCount()),
            Integer.toString(stats.getSuiteErrors())));
        log(res.getStr("test.summary",Integer.toString(stats.getTestCount()),
            Integer.toString(stats.getFailures()),
            Integer.toString(stats.getErrors())));
        
    // fail on failures or errors
        if (stats.getFailures() > 0 || stats.getErrors() > 0 ||
            stats.getSuiteErrors() > 0) {
        //
            throw new BuildException(res.getStr("test.failed"));
        }
    }
}