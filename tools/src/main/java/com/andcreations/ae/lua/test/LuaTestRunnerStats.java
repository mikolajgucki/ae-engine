package com.andcreations.ae.lua.test;

import java.io.File;

/**
 * The Lua unit test runner listener which collects stats.
 *
 * @author Mikolaj Gucki
 */
public class LuaTestRunnerStats extends LuaTestRunnerAdapter {
    /** */
    private int suiteCount;
    
    /** */
    private int suiteErrorCount;
    
    /** */
    private int successCount;
    
    /** */
    private int failureCount;
    
    /** */
    private int errorCount;
    
    /** */
    @Override
    public void runningTestSuite(String moduleName,File file) {
        suiteCount++;
    }
        
    /** */
    @Override
    public void failedToRunTestSuite(String moduleName,String msg) {
        suiteErrorCount++;
    }
    
    /** */
    @Override
    public void setUpFailed(String moduleName,String testName,String msg) {
        errorCount++;
    }
    
    /** */
    @Override
    public void tearDownFailed(String moduleName,String testName,String msg) {
        errorCount++;
    }
    
    /** */
    @Override
    public void notFunctionError(String moduleName,String testName) {
        errorCount++;
    }
    
    /** */
    @Override
    public void testFinished(String moduleName,String testName) {
        successCount++;
    }
    
    /** */
    @Override
    public void testFailed(String moduleName,String testName,String msg) {
        failureCount++;
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
    public int getSuiteCount() {
        return suiteCount;
    }
    
    /** */
    public int getSuiteErrors() {
        return suiteErrorCount;
    }
    
    /** */
    public int getSuccesses() {
        return successCount;
    }
    
    /** */
    public int getFailures() {
        return failureCount;
    }
    
    /** */
    public int getErrors() {
        return errorCount;
    }
    
    /** */
    public int getTestCount() {
        return successCount + failureCount + errorCount;
    }
    
    /** */
    public boolean isSuccessful() {
        return suiteErrorCount + failureCount + errorCount == 0;
    }
    
    /** */
    public void reset() {
        suiteCount = 0;
        suiteErrorCount = 0;
        successCount = 0;
        failureCount = 0;
        errorCount = 0;
    }
}
