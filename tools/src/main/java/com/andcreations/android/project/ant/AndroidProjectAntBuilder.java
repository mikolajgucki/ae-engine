package com.andcreations.android.project.ant;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.andcreations.ant.AntRunner;
import com.andcreations.ant.AntRunnerProcess;

/**
 * Builds Android project using Ant and build.xml produced by Android SDK.
 *
 * @author Mikolaj Gucki
 */
public class AndroidProjectAntBuilder {
    /** The Android project directory. */
    private File projectDir;
    
    /** The Ant runner. */
    private AntRunner antRunner;
    
    /** The stream for Ant output. */
    private OutputStream output;
    
    /** The stream for errors. */
    private OutputStream errorOutput;
    
    /**
     * Creates a AndroidProjectAntBuilder.
     *
     * @param projectDir The directory with the Android project.
     * @param antRunner The Ant runner.
     * @param output The stream for Ant output.
     * @param errorOutput The stream for errors.
     */
    public AndroidProjectAntBuilder(File projectDir,AntRunner antRunner,
        OutputStream output,OutputStream errorOutput) {
    //
        this.projectDir = projectDir;
        this.antRunner = antRunner;
        this.output = output;
        this.errorOutput = errorOutput;
    }
    
    /**
     * Calls Ant with <code>debug</code> argument.
     *
     * @throws IOException on I/O error.
     */
    public void buildDebug() throws IOException {
        antRunner.run(output,errorOutput,projectDir,"debug");
    }
    
    /**
     * Creates an Ant process runner which calls Ant with <code>debug</code>
     * argument.
     *
     * @return The Ant process runner.
     * @throws IOException on I/O error.
     */
    public AntRunnerProcess createDebugProcess() throws IOException {
        return antRunner.createProcess(output,errorOutput,projectDir,"debug");
    }
    
    /**
     * Calls Ant with <code>installd</code> argument.
     *
     * @throws IOException on I/O error.
     */
    public void buildInstallDebug() throws IOException {
        antRunner.run(output,errorOutput,projectDir,"installd");
    }
    
    /**
     * Creates an Ant process runner which calls Ant with <code>installd</code>
     * argument.
     *
     * @return The Ant process runner.
     * @throws IOException on I/O error.
     */
    public AntRunnerProcess createInstallDebugProcess() throws IOException {
    //
        return antRunner.createProcess(
            output,errorOutput,projectDir,"installd");
    }
    
    /**
     * Calls Ant with <code>uninstall</code> argument.
     *
     * @throws IOException on I/O error.
     */
    public void buildUninstall() throws IOException {
        antRunner.run(output,errorOutput,projectDir,"uninstall");
    }
    
    /**
     * Creates an Ant process runner which calls Ant with <code>uninstall</code>
     * argument.
     *
     * @return The Ant process runner.
     * @throws IOException on I/O error.
     */
    public AntRunnerProcess createUninstallProcess() throws IOException {
        return antRunner.createProcess(
            output,errorOutput,projectDir,"uninstall");
    }    
}