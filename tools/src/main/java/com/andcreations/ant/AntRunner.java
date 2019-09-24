package com.andcreations.ant;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class AntRunner {
    /** The default name of an Ant build file. */
    public static final String BUILD_FILE = "build.xml";
    
    /** */
    private static BundleResources res = new BundleResources(AntRunner.class);     
    
    /** */
    private File antHome;
    
    /** */
    private Map<String,String> env = new HashMap<String,String>();
    
    /** */
    public AntRunner(File antHome) {
        this.antHome = antHome;
    }
    
    /**
     * Adds an environment variable.
     *
     * @param name The name.
     * @param value The value.
     */
    public void addEnvVar(String name,String value) {
        env.put(name,value);
    }
    
    /** */
    private ProcessBuilder buildProcess(File baseDir,String[] args)
        throws IOException {
    // the arguments below are taken from ant.run from Ant
        List<String> processArgs = new ArrayList<>();

    // java home
        Map<String,String> sysenv = System.getenv();
        String javaHome = sysenv.get("JAVA_HOME");
        if (javaHome == null) {
            throw new IOException(res.getStr("no.java.home"));
        }

    // exec
        File exec = new File(javaHome,"bin/java");
        processArgs.add(exec.getAbsolutePath());
        
    // classpath
        processArgs.add("-classpath");
        File antLauncherJar = new File(antHome,"lib/ant-launcher.jar");
        processArgs.add(antLauncherJar.getAbsolutePath());
        
    // main class
        processArgs.add("org.apache.tools.ant.launch.Launcher");
        
    // arguments
        processArgs.addAll(Arrays.asList(args));
        
    // build the process
        ProcessBuilder builder = new ProcessBuilder(processArgs);
        builder.directory(baseDir);
        
    // java home
        if (javaHome != null) {
            builder.environment().put("JAVA_HOME",javaHome);
        }
        
    // environment
        for (String name:env.keySet()) {
            builder.environment().put(name,env.get(name));
        }
        
        return builder;
    }
    
    /**
     * Creates a process which can run an Ant script.
     *
     * @param stdOutput The output connected to the process standard stream.
     * @param errOutput The output connected to the process error stream.
     * @param baseDir The directory containing the build file.
     * @param args The arguments.
     * @return The process.
     */
    public synchronized AntRunnerProcess createProcess(OutputStream stdOutput,
        OutputStream errOutput,File baseDir,String... args) throws IOException {
    //
        ProcessBuilder processBuilder = buildProcess(baseDir,args);
        return new AntRunnerProcess(processBuilder,stdOutput,errOutput);
    }
    
    /**
     * Runs an Ant script in a separate process.
     *
     * @param stdOutput The output connected to the process standard stream.
     * @param errOutput The output connected to the process error stream.
     * @param baseDir The directory containing the build file.
     * @param args The arguments.
     */
    public synchronized void run(OutputStream stdOutput,OutputStream errOutput,
        File baseDir,String... args) throws IOException {
    // create the process
        AntRunnerProcess process = createProcess(
            stdOutput,errOutput,baseDir,args);
        
    // run the process
        try {
            process.start();
            process.waitFor();
        } finally {
            process.cleanUp();
        }
        process.checkExitValue();
    }
}