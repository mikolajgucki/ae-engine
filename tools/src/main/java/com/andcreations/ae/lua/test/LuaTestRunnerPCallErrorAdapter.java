package com.andcreations.ae.lua.test;

import com.andcreations.lua.PCallError;

/**
 * @author Mikolaj Gucki
 */
public class LuaTestRunnerPCallErrorAdapter extends LuaTestRunnerAdapter {
    /** */
    public void failedToRunTestSuite(String moduleName,PCallError error) {
    }
    
    /** */
    @Override
    public void failedToRunTestSuite(String moduleName,String msg) {
        failedToRunTestSuite(moduleName,PCallError.parse(msg));
    }
    
    /** */
    public void setUpFailed(String moduleName,String testName,
        PCallError error) {
    }
    
    /** */
    @Override
    public void setUpFailed(String moduleName,String testName,String msg) {
        setUpFailed(moduleName,testName,PCallError.parse(msg));
    }
    
    /** */
    public void tearDownFailed(String moduleName,String testName,
        PCallError error) {
    }
    
    /** */
    @Override
    public void tearDownFailed(String moduleName,String testName,String msg) {
        tearDownFailed(moduleName,testName,PCallError.parse(msg));
    }
    
    /** */
    public void testFailed(String moduleName,String testName,PCallError error) {
    }
    
    /** */
    @Override
    public void testFailed(String moduleName,String testName,String msg) {
        testFailed(moduleName,testName,PCallError.parse(msg));
    }
}
