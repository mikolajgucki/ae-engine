package com.andcreations.ae.studio.plugins.lua.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.lua.test.LuaTestRunnerListener;
import com.andcreations.ae.lua.test.LuaTestRunnerPCallErrorAdapter;
import com.andcreations.ae.lua.test.LuaTestRunnerStats;
import com.andcreations.ae.lua.test.ProjectLuaTestRunner;
import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.ae.dist.AEDist;
import com.andcreations.ae.studio.plugins.console.DefaultConsole;
import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.lua.PCallError;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class LuaTest implements LuaTestViewListener {
    /** */
    private static final String TMP_FILENAME = "lua_test.lua";
    
    /** */
    private BundleResources res = new BundleResources(LuaTest.class);      
    
    /** */
    private static LuaTest instance;
    
    /** */
    private LuaTestComponent component;
    
    /** */
    private Thread runnerThread;
    
    /** */
    private File testSuiteFile;
    
    /** */
    private int testSuiteErrors;
    
    /** */
    private LuaTestRunnerStats stats = new LuaTestRunnerStats();
    
    /** */
    private List<LuaTestListener> listeners = new ArrayList<>();
    
    /** */
    private ProjectLuaTestRunner projectLuaTestRunner;
    
    /** */
    private LuaTestRunnerListener listener =
        new LuaTestRunnerPCallErrorAdapter() {
        /** */
        @Override
        public void runningTestSuite(String moduleName,File file) {
            component.addTestSuiteNode(moduleName,file,-1);
            testSuiteFile = file;
            testSuiteErrors = 0; // reset
        }
        
        /** */
        @Override
        public void failedToRunTestSuite(String moduleName,PCallError error) {
            component.testSuiteFailed(moduleName,getErrorMessage(error),
                testSuiteFile,-1); //getFile(error),getLine(error));
        }
        
        /** */
        @Override
        public void setUpFailed(String moduleName,String testName,
            PCallError error) {
        //
            component.addTestFailedNode(moduleName,getErrorMessage(error),
                testSuiteFile,-1); //getFile(error),getLine(error));
            testSuiteErrors++;
        }
        
        /** */
        @Override
        public void tearDownFailed(String moduleName,String testName,
            PCallError error) {
        //
            component.addTestFailedNode(moduleName,getErrorMessage(error),
                testSuiteFile,-1); //getFile(error),getLine(error));
            testSuiteErrors++;
        }
        
        /** */
        @Override
        public void notFunctionError(String moduleName,String testName) {
            component.addTestFailedNode(testName,res.getStr("not.a.function"),
                testSuiteFile,-1); //null,-1);
            testSuiteErrors++;
        }
        
        /** */
        @Override
        public void runningTest(String moduleName,String testName) {
            component.addTestNode(testName,testSuiteFile);
        }
        
        /** */
        @Override
        public void testFinished(String moduleName,String testName) {
            component.addTestFinishedNode(testName);
        }
        
        /** */
        @Override
        public void testFailed(String moduleName,String testName,
            PCallError error) {
        //
            component.addTestFailedNode(testName,getErrorMessage(error),
                testSuiteFile,-1);//getFile(error),getLine(error));
            testSuiteErrors++;
        }
        
        /** */
        @Override
        public void testSuiteFinished(String moduleName) {
            if (testSuiteErrors > 0) {
                component.testSuiteFailed(moduleName,null,null,-1);
            }
            else {
                component.testSuiteFinished(moduleName);
            }
        }
        
        /** */
        @Override
        public void linePrinted(String line) {
            DefaultConsole.get().println(line);
        }
        
        /** */
        @Override
        public void tracebackCaught(String traceback,String msg) {
            if (msg != null) {
                DefaultConsole.get().println(msg);
            }
            DefaultConsole.get().println(traceback);
        }        
    };
    
    /** */
    void init(LuaTestView view) {
        this.component = view.getComponent();
    }
    
    /** */
    void addLuaTestListener(LuaTestListener listener) {
        listeners.add(listener);
    }
    
    /** */
    private String getErrorMessage(PCallError error) {
        return res.getStr("error",error.getMessage());
    }
    
    /** */
    private void updateStats() {
        String summary = res.getStr("test.summary",
            Integer.toString(stats.getTestCount()),
            Integer.toString(stats.getFailures()),
            Integer.toString(stats.getErrors()));
        component.updateRootNode(summary,stats.isSuccessful() == false);
    }
    
    /** */
    private void handleError(Throwable exception) {
        Log.error("Failed to run Lua tests",exception);
        CommonDialogs.error(res.getStr("error.dialog.title"),
            res.getStr("error.dialog.msg",exception.getMessage()));
    }
    
    /** */
    synchronized void runAllTests() {
        if (runnerThread != null) {
            return;
        }
        
        Runnable runnable = new Runnable() {
            /** */
            @Override
            public void run() {
                stats.reset();
                
            // notify starting
                for (LuaTestListener listener:listeners) {
                    listener.startingLuaTests();
                }
                
            // run tests
                projectLuaTestRunner = new ProjectLuaTestRunner(
                    AEDist.get().getAEDistDir(),
                    ProjectFiles.get().getProjectDir(),
                    Files.get().getTmpFile(TMP_FILENAME),listener,stats);
                try {
                    projectLuaTestRunner.run();
                } catch (IOException exception) {
                    handleError(exception);
                    return;
                } catch (Throwable exception) {
                    handleError(exception);
                    return;
                } finally {
                    synchronized (LuaTest.this) {
                        projectLuaTestRunner = null;
                    }
                }
                
            // stats
                updateStats();
                
            // notify finished
                for (LuaTestListener listener:listeners) {
                    listener.luaTestsFinished();
                }
                
                runnerThread = null;
            }
        };
        
    // clear the view
        component.clear();
        
    // run tests
        runnerThread = new Thread(runnable,"LuaTestRunner");
        runnerThread.start();
    }
    
    /** */
    @Override
    public void terminateLuaTests() {
        synchronized (this) {
            if (projectLuaTestRunner != null) {
                Log.info("Terminating Lua tests");
                projectLuaTestRunner.terminate();
            }
        }
    }
    
    /** */
    public static LuaTest get() {
        if (instance == null) {
            instance = new LuaTest();
        }
        
        return instance;
    }
}