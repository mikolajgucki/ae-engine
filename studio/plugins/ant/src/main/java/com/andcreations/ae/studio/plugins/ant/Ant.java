package com.andcreations.ae.studio.plugins.ant;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.andcreations.ae.project.AEProject;
import com.andcreations.ae.studio.plugins.ae.dist.AEDist;
import com.andcreations.ae.studio.plugins.console.DefaultConsole;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.ant.AntRunner;
import com.andcreations.io.OutputStreamLineReader;
import com.andcreations.io.SplitOutputStream;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class Ant {
    /** */
    private class AntOutputReader extends OutputStreamLineReader {
        /** */
        private StringBuilder output = new StringBuilder();
        
        /** */
        @Override           
        public synchronized void lineRead(String line) {
            output.append(line + "\n");
        }
        
        /** */
        private String getOutput() {
            return output.toString();
        }
    }
    
    /** */
    private static Ant instance;
    
    /** */
    private BundleResources res = new BundleResources(Ant.class);     
    
    /** */
    private OutputStreamLineReader consoleOutput;
    
    /** */
    void init() {
    // console output
        consoleOutput = new OutputStreamLineReader() {
            /** */
            @Override
            public void lineRead(String line) {
                DefaultConsole.get().println(line);
            }            
        };
    }
    
    /** */
    public boolean isAntAvailable() {
        File antHome = AntPreferences.get().getAntHome();
        return AntHome.validate(antHome) == null;
    }
    
    /** */
    public void showAntNotAvailableDialog() {
        CommonDialogs.error(res.getStr("no.ant.title"),
            res.getStr("no.ant.message"));
    }
    
    /** */
    public void showAntRunFailedDialog(AntRunException exception) {
        if (exception.getOutput().length() > 0) { 
            DefaultConsole.get().print(exception.getOutput());
        }
        String errorMsg = exception.getMessage();
        if (errorMsg != null) {
            errorMsg = " (" + errorMsg + ")";
        }
        else {
            errorMsg = "";
        }
        CommonDialogs.error(res.getStr("ant.run.failed.title"),
            res.getStr("ant.run.failed.message",errorMsg));
    }
    
    /** */
    public void showAntFailedDialog(IOException exception) {
    // get message
        String msg = exception.getMessage();
        if (msg == null || msg.length() == 0) {
            msg = res.getStr("ant.failed.message");
        }
     
    // show dialog
        CommonDialogs.error(res.getStr("ant.failed.title"),msg);
    }
    
    /** */
    private void setEnv(AntRunner runner) {
        runner.addEnvVar(AEProject.Env.AE_DIST,
            AEDist.get().getAEDistDir().getAbsolutePath());
    }
    
    /** */
    public AntRunner getAntRunner() throws AntNotAvailableException {
        if (isAntAvailable() == false) {
            throw new AntNotAvailableException();
        }
        
        AntRunner runner = new AntRunner(AntPreferences.get().getAntHome());
        setEnv(runner);
        
        return runner;
    }
    
    /**
     * Runs Ant.
     *
     * @param baseDir The directory in which to run Ant.
     * @param output The Ant output stream.
     * @param errorOutput The Ant error output stream.
     * @param args The arguments.
     * @throws AntNotAvailableException if Ant is not available.     
     * @throws AntRunException if it fails to run Ant.
     * @throws IOException on I/O error.
     */
    public void run(File baseDir,OutputStream output,OutputStream errorOutput,
        String... args) throws AntNotAvailableException,AntRunException,
        IOException {
    //
        AntOutputReader bufferOutput = new AntOutputReader();
        AntRunner runner = getAntRunner();
        try {
            runner.run(
                new SplitOutputStream(output,bufferOutput),
                new SplitOutputStream(errorOutput,bufferOutput),
                baseDir,args);
        } catch (IOException exception) {
            throw new AntRunException(exception,bufferOutput.getOutput());
        }
    }
    
    /**
     * Runs Ant logging to the default console.
     *
     * @param baseDir The directory in which to run Ant.
     * @param args The arguments.
     * @throws AntNotAvailableException if Ant is not available.     
     * @throws AntRunException if it fails to run Ant.
     * @throws IOException on I/O error.
     */
    public void run(File baseDir,String... args)
        throws AntNotAvailableException,AntRunException,IOException {
    //
        run(baseDir,consoleOutput,consoleOutput,args);
    }
    
    /**
     * Runs Ant logging to the default console.
     *
     * @param baseDir The directory in which to run Ant.
     * @param args The arguments.
     */
    public void runNoExceptions(File baseDir,String... args) {
        try {
            run(baseDir,args);
        } catch (AntNotAvailableException exception) {
            showAntNotAvailableDialog();
            return;
        } catch (AntRunException exception) {
            showAntRunFailedDialog(exception);
            return;
        } catch (IOException exception) {
            showAntFailedDialog(exception);
            return;
        }
    }
    
    /** */
    public static Ant get() {
        if (instance == null) {
            instance = new Ant();
        }
        
        return instance;
    }
}