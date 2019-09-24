package com.andcreations.ae.studio.plugins.lua.compiler;

import java.io.File;
import java.util.List;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugins.file.FileAdapter;
import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.problems.ProblemType;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.io.FileNode;
import com.andcreations.io.FileTree;
import com.andcreations.io.FileUtil;
import com.andcreations.resources.BundleResources;

/**
 * Responsible for compiling Lua files.
 *
 * @author Mikolaj Gucki
 */
public class LuaCompiler {
    /** */
    private static final BundleResources res =
        new BundleResources(LuaCompiler.class);     
    
    /** */
    public static final ProblemType PROBLEM_TYPE =
        new ProblemType(res.getStr("problem.type"));
    
    /** */
    private LuaCompilerRunnable compilerRunnable;    
    
    /** */
    private Thread compilerThread;
    
    /** */
    public LuaCompiler() throws PluginException {
        create();
    }
    
    /** */
    private void create() {
    // file listener
        FileAdapter fileListener = new FileAdapter() {
            /** */
            @Override    
            public void fileCreated(File file) {
                tryCreate(file);
            }            
            
            /** */
            @Override    
            public void fileChanged(File file) {
                LuaCompiler.this.fileChanged(file);
            }            
            
            /** */
            @Override
            public void fileDeleted(File file) {
                tryDelete(file);
            }
            
            /** */
            @Override
            public void fileRenamed(File src,File dst) {
                fileDeleted(src);
                fileCreated(dst);
            }
        };        
        Files.get().addFileListener(fileListener);      
        
    // runnable/thread
        compilerRunnable = new LuaCompilerRunnable();
        compilerThread = new Thread(compilerRunnable,"LuaCompiler");
    }
    
    /** */
    private void postCompile(final File file) {
        if (ProjectLuaFiles.get().isLuaFile(file) == false) {
            return;
        }
        
        Runnable runnable = new Runnable() {
            /** */
            @Override
            public void run() {
                compilerRunnable.add(file);
            }
        };
        Thread thread = new Thread(runnable,
            String.format("LuaCompiler-%s",file.getName()));
        thread.start();
    }
    
    /** */
    private void tryCreate(File file) {
    // file
        if (file.isFile() == true) {
            fileCreated(file);
            return;
        }
        
    // directory
        File[] childFiles = Files.listTree(file);
        for (File childFile:childFiles) {
            if (childFile.isFile() == true) {
                fileCreated(childFile);
            }                        
        }            
    }
    
    /** */
    private void fileCreated(File file) {
        postCompile(file);
    }
    
    /** */
    private void fileChanged(File file) {
        postCompile(file);
    }
    
    /** */
    private void tryDelete(File file) {
    // file
        if (file.isFile() == true) {
            fileDeleted(file);
            return;
        }
        
    // directory
        while (true) {
            File toDelete = null;
            for (File problemFile:compilerRunnable.getProblemFiles()) {
                if (FileUtil.isAncestor(file,problemFile) == true) {
                    toDelete = problemFile;
                    break;
                }                
            }
            
            if (toDelete == null) {
                break;
            }
            fileDeleted(toDelete);
        }        
    }
    
    /** */
    private void fileDeleted(File file) {
        if (ProjectLuaFiles.get().isLuaFile(file) == false) {
            return;
        }
        compilerRunnable.clearErrors(file);
    }
    
    /** */
    private void compileAllLuaFiles() {
        Log.trace("Compiling all Lua files");
        FileTree tree = ProjectLuaFiles.get().getLuaSourceTree();
        List<FileNode> nodes = tree.flatten();
    // for each Lua file
        for (FileNode node:nodes) {
            if (node.isFile() && node.getFileCount() == 1) {
                compilerRunnable.add(node.getFile());
            }
        }
    }
    
    /** */
    public void start() {
        Log.info("Starting the Lua compiler thread");
        compilerThread.start();
        compileAllLuaFiles();
    }
    
    /** */
    public void stop() {
        compilerRunnable.stop();
        try {
            compilerThread.join();
        } catch (InterruptedException exception) {
        }
        Log.info("Stopped the Lua compiler thread");
    }
}