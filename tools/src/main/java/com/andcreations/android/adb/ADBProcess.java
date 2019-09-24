package com.andcreations.android.adb;

/**
 * @author Mikolaj Gucki
 */
public class ADBProcess {
    /** */
    private String pid;
    
    /** */
    private String ppid;
    
    /** */
    private String name;
    
    /** */
    ADBProcess() {        
    }
    
    /** */
    void setPid(String pid) {
        this.pid = pid;
    }
    
    /** */
    void setPPid(String ppid) {
        this.ppid = ppid;        
    }
    
    /** */
    void setName(String name) {
        this.name = name;
    }
    
    /** */
    boolean isZombie() {
        return "1".equals(ppid);
    }
    
    /** */
    public String getPid() {
        return pid;
    }
    
    /** */
    public String getPPid() {
        return ppid;
    }
    
    /** */
    public String getName() {
        return name;
    }
}