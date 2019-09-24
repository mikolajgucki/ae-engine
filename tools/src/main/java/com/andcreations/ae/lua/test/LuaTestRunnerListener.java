package com.andcreations.ae.lua.test;

import java.io.File;

/**
 * The Lua unit test runner listener.
 *
 * @author Mikolaj Gucki
 */
public interface LuaTestRunnerListener {
    /** 
     * Called when a test suite is running.
     *
     * @param moduleName The name of the module which represents the suite.
     * @param file The file with the test suite source.
     */
    void runningTestSuite(String moduleName,File file);
    
    /** 
     * Called when a test suite failed to run.
     *
     * @param moduleName The name of the module which represents the suite.
     * @param msg The error message.
     */
    void failedToRunTestSuite(String moduleName,String msg);
    
    /**
     * Called when the set-up function failed.
     *
     * @param moduleName The name of the module which represents the suite.
     * @param testName The test name.
     * @param msg The error message.
     */
    void setUpFailed(String moduleName,String testName,String msg);    
    
    /**
     * Called when the tear-down function failed.
     *
     * @param moduleName The name of the module which represents the suite.
     * @param testName The test name.
     * @param msg The error message.
     */
    void tearDownFailed(String moduleName,String testName,String msg);    
    
    /**
     * Called when a field in a test suite (module) is not a function.
     *
     * @param moduleName The name of the module which represents the suite.
     * @param testName The test name.
     */
    void notFunctionError(String moduleName,String testName);
    
    /**
     * Called when a test is running.
     *
     * @param moduleName The name of the module which represents the suite.
     * @param testName The test name.
     */
    void runningTest(String moduleName,String testName);
    
    /**
     * Called when a test successfully finished.
     *
     * @param moduleName The name of the module which represents the suite.
     * @param testName The test name.
     */
    void testFinished(String moduleName,String testName);
    
    /**
     * Called when a test has failed.
     *
     * @param moduleName The name of the module which represents the suite.
     * @param testName The test name.
     * @param msg The message.
     */
    void testFailed(String moduleName,String testName,String msg);
    
    /** 
     * Called when a test suite has finished.
     *
     * @param moduleName The name of the module which represents the suite.
     */
    void testSuiteFinished(String moduleName);    
    
    /**
     * Called when a line has been printed.
     *
     * @param line The line.
     */
    void linePrinted(String line);
    
    /**
     * Called when a traceback on error has been caught.
     *
     * @param traceback The traceback.
     * @param msg The error msg.
     */
    void tracebackCaught(String traceback,String msg);
}
