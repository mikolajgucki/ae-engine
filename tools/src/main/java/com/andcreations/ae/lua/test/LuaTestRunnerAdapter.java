package com.andcreations.ae.lua.test;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public class LuaTestRunnerAdapter implements LuaTestRunnerListener {
    /** */
    @Override
    public void runningTestSuite(String moduleName,File file) {
    }
    
    /** */
    @Override
    public void failedToRunTestSuite(String moduleName,String msg) {
    }
    
    /** */
    @Override
    public void setUpFailed(String moduleName,String testName,String msg) {
    }
    
    /** */
    @Override
    public void tearDownFailed(String moduleName,String testName,String msg) {
    }
    
    /** */
    @Override
    public void notFunctionError(String moduleName,String testName) {
    }
    
    /** */
    @Override
    public void runningTest(String moduleName,String testName) {
    }    
    
    /** */
    @Override
    public void testFinished(String moduleName,String testName) {
    }
    
    /** */
    @Override
    public void testFailed(String moduleName,String testName,String msg) {
    }
    
    /** */
    @Override
    public void testSuiteFinished(String moduleName) {
    }
    
    /** */
    @Override
    public void linePrinted(String line) {
    }

    /** */
    @Override
    public void tracebackCaught(String traceback,String msg) {
    }
}
