package com.andcreations.ae.studio.plugins.android;

import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.ant.Ant;
import com.andcreations.ae.studio.plugins.builders.AbstractBuilder;
import com.andcreations.ae.studio.plugins.console.DefaultConsoleErrorStream;
import com.andcreations.ae.studio.plugins.console.DefaultConsolePrintStream;
import com.andcreations.io.FileUtil;
import com.andcreations.process.ProcessRunner;
import com.andcreations.process.ProcessRunnerProcess;
import com.andcreations.resources.BundleResources;

/**
 * Runs an executable in a child process in the Android project directory.
 * 
 * @author Mikolaj Gucki
 */
abstract class ExecBuilder extends AbstractBuilder {
    /** */
    private static BundleResources res = new BundleResources(ExecBuilder.class);  
    
	/** */
	private ProcessRunnerProcess process;
	
    /** */
    private boolean canTerminate;
    
    /** */
    private boolean terminated;	
	
	/** */
	protected ExecBuilder(String id,String name,ImageIcon icon,String desc) {
		super(id,name,icon,desc);
	}
	
	/** */
	protected boolean canBuild() {
		return true;
	}
	
	/** */
	protected abstract String[] getArgs();
	
	/** */
	private void runProcess() throws IOException {
        try {
        // start the process
            process.start();
            synchronized (this) {
                canTerminate = true;
                setTerminable(true);
            }
            
        // wait for the process to finish
            process.waitFor();
            synchronized (this) {
                canTerminate = false;
                setTerminable(false);
            }            
        } finally {
            process.cleanUp();
        }
        if (process.getExitValue() != 0) {
        	throw new IOException(res.getStr("exit.value",
    			Integer.toString(process.getExitValue())));
        }
	}
	
	/** */
	@Override
	public void build() {
		if (canBuild() == false) {
			return;
		}
		
    // Android project directory
        File androidProjectDir = AndroidProjectDir.get().tryToGet();
        if (androidProjectDir == null) {
            return;
        }
        androidProjectDir = FileUtil.canonical(androidProjectDir);
        
    // output streams
        DefaultConsolePrintStream stdOutput = new DefaultConsolePrintStream();
        DefaultConsoleErrorStream errOutput = new DefaultConsoleErrorStream();
        
        try {
			process = ProcessRunner.createProcess(
				stdOutput,errOutput,androidProjectDir,getArgs());
			runProcess();
		} catch (IOException exception) {
            if (terminated == false) {
                Ant.get().showAntFailedDialog(exception);
                return;            
            }
        }
	}
	
	/** */
	@Override
	public void terminate() {
        synchronized (this) {
            if (canTerminate == true) {
                process.terminate();
                terminated = true;
            }
        }		
	}
}
