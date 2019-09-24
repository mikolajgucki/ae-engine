package com.andcreations.ae.desktop;

import java.io.File;
import java.io.OutputStream;

import com.andcreations.system.OS;

/**
 * @author Mikolaj Gucki
 */
public class DesktopEngineCfg {
    /** */
    static final int NO_RESOLUTION = -1;
    
    /** The executable file. */
    private File execFile;
    
    /** The project directory. */
    private File projectDir;
    
    /** The output for the data read from the process standard error stream. */
    private OutputStream errorOutputStream;
    
    /** The width. */
    private int width = NO_RESOLUTION;
    
    /** The height. */
    private int height = NO_RESOLUTION;
    
    /** The file for the debug log. */
    private File debugLogFile;

    /** */
    public DesktopEngineCfg(File execFile,File projectDir) {
        this.execFile = execFile;
        this.projectDir = projectDir;
    }
    
    /** */
    public File getExecFile() {
        return execFile;
    }
    
    /** */
    public File getProjectDir() {
        return projectDir;
    }
    
    /** */
    public void setErrorOutputStream(OutputStream errorOutputStream) {
        this.errorOutputStream = errorOutputStream;
    }
    
    /** */
    OutputStream getErrorOutputStream() {
        return errorOutputStream;
    }
    
    /** */
    public void setResolution(int width,int height) {
        this.width = width;
        this.height = height;
    }
    
    /** */
    boolean hasResolution() {
        return width != NO_RESOLUTION && height != NO_RESOLUTION;
    }
    
    /** */
    public int getWidth() {
        return width;
    }
    
    /** */
    public int getHeight() {
        return height;
    }
    
    /** */
    public void setDebugLogFile(File debugLogFile) {
        this.debugLogFile = debugLogFile;
    }
    
    /** */
    public File getDebugLogFile() {
        return debugLogFile;
    }
    
    /**
     * Gets the executable file from a distribution directory.
     *
     * @param aeDistDir The distribution directory.
     * @return The executable file or null if it cannot be determined.
     */
    public static File getExecFile(String aeDistDir) {
        String os = OS.getOS();
        if (os == OS.WINDOWS) {
            return new File(aeDistDir,"bin/ae.exe");
        }
        if (os == OS.LINUX || os == OS.OSX) {
            return new File(aeDistDir,"bin/ae");
        }
        
        return null;
    }
}