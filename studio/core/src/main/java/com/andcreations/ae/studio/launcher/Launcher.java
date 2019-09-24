package com.andcreations.ae.studio.launcher;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * Launches AE Studio.
 *
 * @author Mikolaj Gucki
 */
public class Launcher {
    /** */
    private static final String AE_DIST_DIR_PROPERTY = "ae.dist.dir";
    
    /** */
    private File aeDistDir;
     
    /** */
    private PrintWriter logWriter;
    
    /** */
    private Object processMutex = new Object();
    
    /** */
    private void log(String msg) {
        System.out.println(msg);
        logWriter.println(msg);
        logWriter.flush();
    }
    
    /** */
    private void fatal(String msg) {
        if (logWriter != null) {
            log(msg);
        }
        JOptionPane.showMessageDialog(null,msg);
        System.exit(-1);
    }
    
    /** */
    private void getAEDistDir() {
        String aeDistDirStr = System.getProperty(AE_DIST_DIR_PROPERTY);
        if (aeDistDirStr == null) {
            fatal(String.format("Property %s not set",AE_DIST_DIR_PROPERTY));
        }
        
        aeDistDir = new File(aeDistDirStr);
        try {
            aeDistDir = aeDistDir.getCanonicalFile();
        } catch (IOException exception) {
        // ignored, simply couldn't obtain the canonical file
        }
    }
    
    /** */
    private File getStorageDir() {
        File storageDir = new File(aeDistDir,".ae.studio");
        storageDir.mkdirs();
        return storageDir;
    }
    
    /** */
    private File getStudioPluginsDir() {
        return new File(aeDistDir,"libs/studio/plugins");
    }
    
    /** */
    private File getBinDir() {
        return new File(aeDistDir,"bin");
    }
    
    /** */
    private File getEnginePluginsDir() {
        return new File(aeDistDir,"plugins");
    }
    
    /** */
    private void createLogFile() {
        File logFile = new File(getStorageDir(),"ae.studio.log");
        try {
            logWriter = new PrintWriter(logFile);
        } catch (IOException exception) {
            fatal(String.format("Failed to create log file %s: %s",
                logFile.getAbsolutePath(),exception.getMessage()));
            return;
        }
    }
    
    /** */
    private void addFileList(List<File> list,File dir,
        FilenameFilter filter) {
    //
        if (dir.exists() == false || dir.isDirectory() == false) {
            fatal(String.format("Directory %s not found or is not a directory",
                dir.getAbsolutePath()));
            return;
        }
    
        File[] files = dir.listFiles(filter);
        if (files != null) {
            list.addAll(Arrays.asList(files));
        }
    }
    
    /** */
    private List<File> getEnginePluginsStudioDirs() {
        List<File> dirs = new ArrayList<>();
        if (getEnginePluginsDir().exists() == false) {
            return dirs;
        }
        
        File[] enginePluginsDirs = getEnginePluginsDir().listFiles();
        for (File enginePluginDir:enginePluginsDirs) {
            File dir = new File(enginePluginDir,"studio");
            if (dir.exists() == true) {
                dirs.add(dir);
            }
        }
        
        return dirs;
    }
    
    /** */
    private List<File> getClasspath() {
        List<File> classpath = new ArrayList<>();
        FilenameFilter jarFilter = new FilenameFilter() {
            /** */
            @Override
            public boolean accept(File dir,String name) {
                return name.endsWith(".jar");
            }
        };
                
        addFileList(classpath,new File(aeDistDir,"libs"),jarFilter);
        addFileList(classpath,new File(aeDistDir,"libs/studio"),jarFilter);
        
    // add the plugin JARs to classpath as some libs don't load via URL
    // classloader
        addFileList(classpath,getStudioPluginsDir(),jarFilter);
        
    // engine plugins
        for (File dir:getEnginePluginsStudioDirs()) {
            addFileList(classpath,dir,jarFilter);
        }
        
        return classpath;
    }
    
    /** */
    private String getClasspathStr() {
        List<File> classpath = getClasspath();
        StringBuilder classpathStr = new StringBuilder();
        
        for (File file:classpath) {
            if (classpathStr.length() > 0) {
                classpathStr.append(File.pathSeparator);
            }
            classpathStr.append(file);
        }
        
        return classpathStr.toString();
    }
    
    /** */
    private File getJavaExec() {
        File javaHome = new File(System.getProperty("java.home"));        
        return new File(javaHome,"bin" + File.separator + "java");
    }
    
    /** */
    private boolean isOSX() {
        String name = System.getProperty("os.name").toLowerCase();
        return name.contains("mac");
    }
    
    /** */
    private String getPluginsProperty() {
        StringBuilder dirs = new StringBuilder();
        dirs.append(getStudioPluginsDir().getAbsolutePath());
        
    // engine plugins
        for (File dir:getEnginePluginsStudioDirs()) {
            dirs.append(Main.PROPERTY_PLUGINS_SEPARATOR);
            dirs.append(dir.getAbsolutePath());
        }
        
        return dirs.toString();
    }
    
    /** */
    private String getPluginsDyldLibraryPath() {
        if (getEnginePluginsDir().exists() == false) {
            return null;
        }
        
        StringBuilder path = null;
        File[] enginePluginsDirs = getEnginePluginsDir().listFiles();
        for (File enginePluginDir:enginePluginsDirs) {
            File dir = new File(enginePluginDir,"desktop/osx");
            if (dir.exists() == true) {
                if (path == null) {
                    path = new StringBuilder();
                }
                else {
                    path.append(":");
                }
                path.append(dir.getAbsolutePath());
            }
        }
        
        return path == null ? null : path.toString();
    }
    
    /** */
    private String appendDyldLibPath(String dyldLibPath,String path) {
        if (dyldLibPath.length() > 0) {
            dyldLibPath += ":";
        }
        return dyldLibPath + path;
    }
    
    /** */
    private String appendDyldLibPath(String dyldLibPath,File dir) {
        return appendDyldLibPath(dyldLibPath,dir.getAbsolutePath());
    }
    
    /** */
    private ProcessBuilder buildProcess() {
        List<String> args = new ArrayList<>();
        
    // java exec
        args.add(getJavaExec().getAbsolutePath());
        
    // classpath
        args.add("-cp");
        args.add(getClasspathStr());
        
    // plugins directory
        args.add(String.format("-D%s=%s",Main.PROPERTY_PLUGINS,
            getPluginsProperty()));
        
    // native library path
        args.add(String.format("-D%s=%s","java.library.path",
            getStudioPluginsDir().getAbsolutePath()));
        
    // stored directory
        args.add(String.format("-D%s=%s",Main.PROPERTY_STORAGE,
            getStorageDir().getAbsolutePath()));
        
    // icon
        String dockIcon = System.getProperty("dock.icon");
        if (dockIcon != null) {
            args.add(String.format("-Xdock:icon=%s",dockIcon));
        }
        
    // main class
        args.add(Main.class.getName());

    // build the process
        ProcessBuilder builder = new ProcessBuilder(args);
        builder.directory(aeDistDir);        
        
    // library path (OS X)
        if (isOSX()) {
            String dyldLibPath = System.getenv("DYLD_LIBRARY_PATH");
            if (dyldLibPath == null) {
                dyldLibPath = "";
            }
            
            String pluginsDyldLibPath = getPluginsDyldLibraryPath();
            if (pluginsDyldLibPath != null) {
                dyldLibPath = appendDyldLibPath(dyldLibPath,pluginsDyldLibPath);
            }
            
            dyldLibPath = appendDyldLibPath(dyldLibPath,getStudioPluginsDir());
            dyldLibPath = appendDyldLibPath(dyldLibPath,getBinDir());
            builder.environment().put("DYLD_LIBRARY_PATH",dyldLibPath);
        }
        
    // log arguments
        StringBuilder argsStr = new StringBuilder();
        for (String arg:args) {
            if (argsStr.length() > 0) {
                argsStr.append(" ");
            }            
            argsStr.append(arg);
        }
        log(String.format("Executing: %s",argsStr.toString()));
        
        return builder;
    }
    
    /** */
    private void startWaitForThread(final Process process) {
        Thread thread = new Thread(new Runnable() {
            /** */  
            @Override
            public void run() {
                try {
                    process.waitFor();
                } catch (InterruptedException exception) {
                }
                
                synchronized (processMutex) {
                    processMutex.notify();
                }
            }
        });  
        thread.setName("AEStudioWaitFor");
        thread.start();
    }    
    
    /** */
    private void startStreamThread(final String name,
        final InputStream inputStream) {
    //
        Thread thread = new Thread(new Runnable() {
            /** */
            @Override
            public void run() {
                StringBuilder msg = new StringBuilder();
                
                while (true) {                        
                    try {
                        int value = inputStream.read();
                        if (value == -1) {
                            break;
                        }
                        if (value == 13) {
                            continue;
                        }
                        if (value != 10) {
                            msg.append((char)value);
                        }
                        else {
                            log(msg.toString());
                            msg.delete(0,msg.length());
                        }
                    } catch (IOException exception) {
                        log(String.format("IO error in stream thread %s: %s",
                            name,exception.getMessage()));
                        break;
                    }
                }
                
                log(String.format("Stream thread %s stopped",name));
            }
        });
        thread.setName(name);
        thread.start();
    }    
    
    /** */
    private void run() {
        getAEDistDir();
        createLogFile();
        log(String.format("AE distribution directory is %s",
            aeDistDir.getAbsolutePath()));
        
        while (true) {
            ProcessBuilder processBuilder = buildProcess();
            synchronized (processMutex) {
                Process process = null;
                try {
                    process = processBuilder.start();
                } catch (IOException exception) {
                    fatal(exception.getMessage());
                    return;
                }
                startWaitForThread(process);
                startStreamThread("stdout",process.getInputStream());
                startStreamThread("stderr",process.getErrorStream());
                
            // wait for the process to exit
                try {
                    processMutex.wait();
                } catch (InterruptedException exception) {
                    return;
                }
                
                int exitValue = process.exitValue();
                log(String.format("AE studio process stopped with value %d",
                    exitValue));                
                if (exitValue != 1) {
                    break;
                }
            }
        }
    }
    
    /** */
    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        launcher.run();        
    }
}