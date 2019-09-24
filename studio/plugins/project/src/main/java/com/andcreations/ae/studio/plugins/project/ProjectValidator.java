package com.andcreations.ae.studio.plugins.project;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.andcreations.ae.project.AEProject;
import com.andcreations.ae.project.AEProjectProperties;
import com.andcreations.ae.studio.plugins.ae.dist.AEDist;
import com.andcreations.ae.studio.plugins.file.FileAdapter;
import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.problems.Problem;
import com.andcreations.ae.studio.plugins.problems.ProblemSeverity;
import com.andcreations.ae.studio.plugins.problems.Problems;
import com.andcreations.io.FileNode;
import com.andcreations.io.FileTree;
import com.andcreations.resources.BundleResources;

/**
 * Validates project.
 *
 * @author Mikolaj Gucki
 */
class ProjectValidator {
    /** */
    private static final String PROBLEM_SOURCE_ID =
        ProjectValidator.class.getName();
    
    /** */
    private static final BundleResources res =
        new BundleResources(ProjectValidator.class);
        
    /** */
    private static Map<File,Problem> luaSrcDirExistProblems =
        new HashMap<File,Problem>();
        
    /** */
    private static Problem addLuaSrcDirError(File dir,String resKey) {
        String description = res.getStr(resKey,dir.getAbsolutePath());
        String location = res.getStr(
            "property",AEProjectProperties.LUA_SRC_DIRS);
        return Problems.get().add(PROBLEM_SOURCE_ID,ProblemSeverity.ERROR,
            description,AEProject.PROPERTIES_FILE,location);
    }
        
    /** */
    private static void validateLuaSrcDirs() {
        File[] dirs = ProjectProperties.get().getLuaSrcDirs();
        for (File dir:dirs) {
            if (dir.exists() == false) {
                Problem problem = luaSrcDirExistProblems.get(dir);
                if (problem == null) {
                    problem = addLuaSrcDirError(dir,"lua.src.dir.not.found");
                    luaSrcDirExistProblems.put(dir,problem);
                }
                continue;
            }
            else {
                Problem problem = luaSrcDirExistProblems.get(dir);
                if (problem != null) {
                    luaSrcDirExistProblems.remove(dir);
                    Problems.get().removeProblem(problem);
                }
            }
            
            if (dir.isDirectory() == false) {
                addLuaSrcDirError(dir,"lua.src.dir.not.dir");
                continue;
            }
        }
    }
    
    /** */
    private static void validateLuaSrcFiles() {
        FileTree luaSrcTree = ProjectLuaFiles.get().getLuaSourceTree();
        
        List<FileNode> nodes = luaSrcTree.flatten();
        for (FileNode node:nodes) {
            String error = ProjectLuaFiles.getError(node);
            if (error != null) {
                Problems.get().add(PROBLEM_SOURCE_ID,ProblemSeverity.ERROR,
                    error,node.getPath(LuaFile.MODULE_SEPARATOR));
            }
        }
    }
    
    /** */
    private static void validatePlugins() {
        for (String pluginId:ProjectProperties.get().getAllPlugins()) {
            if (AEDist.get().isPluginInstalled(pluginId) == false) {
                File pluginDir = AEDist.get().getPluginDir(pluginId);
                String location = res.getStr("plugin.not.found.location",
                    pluginDir.getAbsolutePath());
                Problems.get().add(PROBLEM_SOURCE_ID,ProblemSeverity.ERROR,
                    res.getStr("plugin.not.found",pluginId),
                    AEProject.PROPERTIES_FILE,location);
            }
        }
    }
    
    /** */
    static void init() {
        createFileListener();
        createLuaProjectFilesListener();
        validate();
    }
    
    /** */
    private static void createFileListener() {
        Files.get().addFileListener(new FileAdapter() {
            /** */
            @Override
            public void dirCreated(File dir) {
                validateLuaSrcDirs();
            }                
    
            /** */
            @Override
            public void dirDeleted(File dir) {
                validateLuaSrcDirs();
            }
            
            /** */
            @Override
            public void fileRenamed(File src,File dst) {
                if (dst.isDirectory() == true) {
                    dirDeleted(src);
                    dirCreated(dst);
                }
            }
        });
    }
    
    /** */
    private static void createLuaProjectFilesListener() {
        ProjectLuaFilesListener listener = new ProjectLuaFilesListener() {
            /** */
            @Override
            public void luaSourceFileNodeChanged(FileNode node) {
                validateLuaSrcFiles();
            }
            
            /** */
            @Override
            public void luaSourceFileNodeAdded(FileNode parentNode,
                FileNode node) {
            //
                validateLuaSrcFiles();
            }
            
            /** */
            @Override
            public void luaSourceFileNodeRemoved(FileNode parentNode,
                FileNode node,int index) {
            //
                validateLuaSrcFiles();
            }            
        };
        
        ProjectLuaFiles.get().addProjectLuaFilesListener(listener);
    }
    
    /** */
    private static void validate() {
        Problems.get().removeBySourceId(PROBLEM_SOURCE_ID);
        validateLuaSrcDirs();
        validateLuaSrcFiles();
        validatePlugins();
    }
}