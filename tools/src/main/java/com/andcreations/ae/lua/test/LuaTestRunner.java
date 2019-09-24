package com.andcreations.ae.lua.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.andcreations.ae.lua.test.resources.R;
import com.andcreations.io.OutputStreamLineReader;
import com.andcreations.lua.LuaUtil;
import com.andcreations.lua.runner.LuaRunner;
import com.andcreations.lua.runner.LuaRunnerProcess;
import com.andcreations.velocity.VelocityUtil;

/**
 * Lua unit test runner.
 *
 * @author Mikolaj Gucki
 */
public class LuaTestRunner {
    /** */
    private OutputStreamLineReader outReader = new OutputStreamLineReader() {
        /** */
        @Override
        public void lineRead(String line) {
            output.append(line + "\n");
            LuaTestRunner.this.lineRead(line);
        }
    };   
    
    /** */
    private static final String PREFIX = "lua-test-runner>";
        
    /** */
    private static final String RUNNING_TEST_SUITE = "runningTestSuite";
    
    /** */
    private static final String FAILED_TO_RUN_TEST_SUITE =
        "failedToRunTestSuite";
    
    /** */
    private static final String SET_UP_FAILED = "setUpFailed";
    
    /** */
    private static final String TEAR_DOWN_FAILED = "tearDownFailed";
    
    /** */
    private static final String NOT_FUNCTION_ERROR = "notFunctionError";
    
    /** */
    private static final String RUNNING_TEST = "runningTest";
    
    /** */
    private static final String TEST_FINISHED = "testFinished";
    
    /** */
    private static final String TEST_FAILED = "testFailed";
    
    /** */
    private static final String TEST_SUITE_FINISHED = "testSuiteFinished";
    
    /** */
    private static final String TRACEBACK = "traceback";
    
    /** */
    private LuaRunner luaRunner;
    
    /** */
    private LuaRunnerProcess luaRunnerProcess;    
    
    /** */
    private File aeTestSrcDir;
    
    /** */
    private LuaTestRunnerListener[] listeners;
    
    /** */
    private String name;
    
    /** */
    private StringBuilder arg;
    
    /** */
    private List<String> args;
    
    /** */
    private StringBuilder output = new StringBuilder();
    
    /**
     * Creates a {@link LuaTestRunner}.
     *
     * @param luaRunner The Lua runner.
     * @param aeTestSrcDir The directory with Lua source for module
     *   <code>ae.test</code>.
     * @param listeners The listeners.
     */
    public LuaTestRunner(LuaRunner luaRunner,File aeTestSrcDir,
        LuaTestRunnerListener... listeners) {
    //
        this.luaRunner = luaRunner;
        this.aeTestSrcDir = aeTestSrcDir;
        this.listeners = listeners;
    }
    
    /** */
    private void invalidLuaOuputError() {
        // TODO Handle error properly.
        throw new RuntimeException("Invalid Lua output: " + output.toString());
    }
    
    /** */
    private void dispatch(String name,String[] args) {
    // running test suite
        if (RUNNING_TEST_SUITE.equals(name) == true) {
            if (args.length != 2) {
                invalidLuaOuputError();
                return;
            }
            File file = new File(args[1]);
            for (LuaTestRunnerListener listener:listeners) {
                listener.runningTestSuite(args[0],file);
            }
        }
        
    // failed to run test suite
        if (FAILED_TO_RUN_TEST_SUITE.equals(name) == true) {
            if (args.length != 2) {
                invalidLuaOuputError();
                return;
            }
            for (LuaTestRunnerListener listener:listeners) {
                listener.failedToRunTestSuite(args[0],args[1]);
            }
        }
        
    // set-up failed
        if (SET_UP_FAILED.equals(name) == true) {
            if (args.length != 3) {
                invalidLuaOuputError();
                return;
            }
            for (LuaTestRunnerListener listener:listeners) {
                listener.setUpFailed(args[0],args[1],args[2]);
            }
        }
        
    // tear-down failed
        if (TEAR_DOWN_FAILED.equals(name) == true) {
            if (args.length != 3) {
                invalidLuaOuputError();
                return;
            }
            for (LuaTestRunnerListener listener:listeners) {
                listener.tearDownFailed(args[0],args[1],args[2]);
            }
        }
        
    // not a function
        if (NOT_FUNCTION_ERROR.equals(name) == true) {
            if (args.length != 2) {
                invalidLuaOuputError();
                return;
            }
            for (LuaTestRunnerListener listener:listeners) {
                listener.notFunctionError(args[0],args[1]);
            }
        }
        
    // running test
        if (RUNNING_TEST.equals(name) == true) {
            if (args.length != 2) {
                invalidLuaOuputError();
                return;
            }
            for (LuaTestRunnerListener listener:listeners) {
                listener.runningTest(args[0],args[1]);
            }
        }
        
    // test finished
        if (TEST_FINISHED.equals(name) == true) {
            if (args.length != 2) {
                invalidLuaOuputError();
                return;
            }
            for (LuaTestRunnerListener listener:listeners) {
                listener.testFinished(args[0],args[1]);
            }
        }
        
    // test failed
        if (TEST_FAILED.equals(name) == true) {
            if (args.length != 3) {
                invalidLuaOuputError();
                return;
            }
            for (LuaTestRunnerListener listener:listeners) {
                listener.testFailed(args[0],args[1],args[2]);
            }
        }
        
    // test suite finished
        if (TEST_SUITE_FINISHED.equals(name) == true) {
            if (args.length != 1) {
                invalidLuaOuputError();
                return;
            }
            for (LuaTestRunnerListener listener:listeners) {
                listener.testSuiteFinished(args[0]);
            }
        }
        
    // traceback
        if (TRACEBACK.equals(name) == true) {
            /*
            if (args.length != 2) {
                invalidLuaOuputError();
                return;
            }
            */
            for (LuaTestRunnerListener listener:listeners) {
                listener.tracebackCaught(args[0],args[1]);
            }
        }
    }
    
    /** */
    private void handleStart(String line) {
        String[] tokens = line.split(",");
        name = tokens[1];
        args = new ArrayList<>();
    }
    
    /** */
    private void handleArgLine(String line) {
        if (arg == null) {
            arg = new StringBuilder();
        }
        if (arg.length() > 0) {
            arg.append("\n");
        }
        arg.append(line);
    }
    
    /** */
    private void handleArg() {
        args.add(arg.toString());
        arg = null;
    }
    
    /** */
    private void handleEnd() {
        dispatch(name,args.toArray(new String[]{}));
        name = null;
        args = null;
    }
    
    /** */
    private void lineRead(String line) {
        if (line.startsWith(PREFIX + "start") == true) {
            handleStart(line);
            return;
        }
        
        if (line.equals(PREFIX + "arg") == true) {
            handleArg();
            return;
        }
        
        if (line.equals(PREFIX + "end") == true) {
            handleEnd();
            return;
        }
        
        if (name != null) {
            handleArgLine(line);
            return;
        }
        
        for (LuaTestRunnerListener listener:listeners) {
            listener.linePrinted(line);
        }
    }
    
    /**
     * Runs all tests contained in a single file.
     *
     * @param srcFile The Lua source file with the tests.
     * @param luaTmpFile The path to a temporary Lua file.
     * @param extraPkgPaths The extra package paths.
     * @throws IOException on I/O error.
     */
    public void run(File srcFile,File luaTmpFile,List<File> extraPkgPaths)
        throws IOException {
    // package paths
        List<String> pkgPaths = new ArrayList<>();
        pkgPaths.add(LuaUtil.getPackagePath(aeTestSrcDir));
        pkgPaths.add(LuaUtil.getPackagePath(srcFile.getParentFile()));
        if (extraPkgPaths != null) {
            for (File extraPkgPath:extraPkgPaths) {
                pkgPaths.add(LuaUtil.getPackagePath(extraPkgPath));
            }
        }
        
    // path to the source file
        String srcFilePath = srcFile.getAbsolutePath();
        srcFilePath = srcFilePath.replace("\\","\\\\");
        
    // template context
        Map<String,Object> context = new HashMap<String,Object>();
        context.put("pkgPaths",pkgPaths);
        context.put("moduleName",FilenameUtils.getBaseName(srcFile.getName()));
        context.put("srcFilePath",srcFilePath);
        
    // generate Lua source which runs the tests
        String src = VelocityUtil.evaluate(R.class,"test_runner.vm",context);
        
    // write to file
        FileUtils.writeStringToFile(luaTmpFile,src,"UTF-8");
        
    // clear output
        output.delete(0,output.length());
        
    // run
        try {
            luaRunnerProcess = luaRunner.getProcess(luaTmpFile,outReader,null);
            luaRunnerProcess.start();
        } finally {
            luaRunnerProcess.cleanUp();
            synchronized (this) {
                luaRunnerProcess = null;
            }
            luaTmpFile.delete();
        }
    }
    
    /**
     * Runs all tests contained in a single file.
     *
     * @param srcFile The Lua source file with the tests.
     * @param luaTmpFile The path to a temporary Lua file.
     * @throws IOException on I/O error.
     */
    public void run(File srcFile,File luaTmpFile) throws IOException {
        run(srcFile,luaTmpFile,null);
    }
    
    /**
     * Runs all tests contained in given files.
     *
     * @param srcFiles The Lua source files with the tests.
     * @param luaTmpFile The path to a temporary Lua file.
     * @param extraPkgPaths The extra package paths.     
     * @throws IOException on I/O error.     
     */
    public void run(List<File> srcFiles,File luaTmpFile,
        List<File> extraPkgPaths) throws IOException {
    //
        for (File srcFile:srcFiles) {
            run(srcFile,luaTmpFile,extraPkgPaths);
        }
    }
    
        /**
     * Runs all tests contained in given files.
     *
     * @param srcFiles The Lua source files with the tests.
     * @param luaTmpFile The path to a temporary Lua file.
     * @throws IOException on I/O error.     
     */
    public void run(List<File> srcFiles,File luaTmpFile)
        throws IOException {
    //
        run(srcFiles,luaTmpFile,null);
    }
    
    /**
     * Terminates the runner if running.
     */
    public void terminate() {
        synchronized (this) {
            if (luaRunnerProcess != null) {
                luaRunnerProcess.terminate();
            }
        }
    }
}