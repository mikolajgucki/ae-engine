package com.andcreations.ae.studio.plugins.project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.file.FileAdapter;
import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.io.FileNode;
import com.andcreations.io.FileTree;
import com.andcreations.io.FileUtil;
import com.andcreations.resources.BundleResources;

/**
 * Provides the project Lua files.
 *
 * @author Mikolaj Gucki
 */
public class ProjectLuaFiles {
    /** */
    private static final char MODULE_SEPARATOR = LuaFile.MODULE_SEPARATOR;
    
    /** */
    private static final char PATH_SEPARATOR = '/';
    
    /** */
    private static ProjectLuaFiles instance;
        
    /** */
    private static final BundleResources res =
        new BundleResources(ProjectLuaFiles.class);
    
    /** */
    private FileTree luaSourceTree;
    
    /** */
    private List<ProjectLuaFilesListener> listeners = new ArrayList<>();
    
    /** */
    void init() {
        luaSourceTree = createLuaSourceTree();
        luaSourceTree.sort();
        
        Files.get().addFileListener(new FileAdapter() {
            /** */
            @Override
            public void fileCreated(File file) {                
                ProjectLuaFiles.this.tryFileCreated(file);
            }
            
            /** */
            @Override
            public void fileDeleted(File file) {                
                ProjectLuaFiles.this.fileDeleted(file);
            }
            
            /** */
            @Override
            public void dirCreated(File dir) {                
                ProjectLuaFiles.this.tryFileCreated(dir);
            }
            
            /** */
            @Override
            public void dirDeleted(File dir) {                
                ProjectLuaFiles.this.fileDeleted(dir);
            }
            
            /** */
            @Override
            public void fileRenamed(File src,File dst) {
                ProjectLuaFiles.this.fileRenamed(src,dst);
            }
        });
    }

    /** */
    private void addFileNodeToParent(FileNode node,FileNode parentNode) {
        synchronized (parentNode) {
            parentNode.addChildNode(node,luaSourceTree.getDefaultComparator());
        }
        notifyNodeAdded(parentNode,node);
        
    }
    
    /** */
    private void addFileToParentFileNode(File file) {
    // get the parent by file
        FileNode parentNode = luaSourceTree.findParentNode(file);
        if (parentNode == null) {
        // get the parent by path 
            String parentPath = getPath(file.getParentFile(),false);
            parentNode = getNodeByPath(parentPath,false);
            
        // no parent
            if (parentNode == null) {
                throw new IllegalStateException("Could not find parent node");
            }
        }
        
        FileNode node = new FileNode(file);
        addFileNodeToParent(node,parentNode);
    }
    
    /** */
    private void fileCreated(File file) {
        file = FileUtil.canonical(file);        
    // if already exists
        if (luaSourceTree.findNode(file) != null) {
            return;
        }
        
        List<File> parentDirs = new ArrayList<>();
        File parent = file.getParentFile();
    // find the existing ancestor
        while (true) {
        // try to find the node by file
            if (luaSourceTree.findNode(parent) != null) {
                break;
            }
            
        // try to find the node by path (there might be a node of the same path,
        // but it doesn't contain the parent directory)
            String parentPath = getPath(parent,false);
            FileNode parentNode = getNodeByPath(parentPath,true);
            if (parentNode != null) {
                break;
            }
            
            parentDirs.add(0,parent);
            parent = parent.getParentFile();
            
        // if outside the tree
            if (parent == null) {
                return;
            }            
        }
        
    // add non-existing parent directories
        for (File parentDir:parentDirs) {
            addFileToParentFileNode(parentDir);            
        }
        
    // Lua module and path
        String luaModule = buildLuaModule(file);
        String luaPath = MODULE_SEPARATOR + luaModule;
        
    // check if such a module already exists
        for (FileNode node:luaSourceTree.flatten()) {
            if (node.getPath(MODULE_SEPARATOR).equals(luaPath)) {
                node.addFile(file);
                notifyNodeChanged(node);
                return;
            }
        }
        
        addFileToParentFileNode(file);        
    }
    
    /** */
    private void tryFileCreated(File file) {
        if (isLuaFile(file) == false && isLuaDir(file) == false) {
            return;
        }
        
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
        fileCreated(file);
    }
    
    /** */
    private void fileDeleted(File file) {
        file = FileUtil.canonical(file);
    // if doesn't exist
        FileNode node = luaSourceTree.findNode(file);
        if (node == null) {
            return;
        }
        
    // We must keep the file (directory) in the root node. It's essential
    // when adding a file to know that the file belongs to the tree (the
    // directory in the root is the file ancestor then).
        if (node != luaSourceTree.getRoot()) {
            node.removeFile(file);
        }
        
    // no files left - remove the node
        if (node.getFileCount() == 0) {        
            FileNode parentNode = node.getParent();
            int index = parentNode.indexOf(node);
            
        // remove
            parentNode.removeChildNode(node);
            
        // notify
            notifyNodeRemoved(parentNode,node,index);
            return;
        }
        
        notifyNodeChanged(node);
    }
    
    /** */
    private void fileRenamed(File src,File dst) {
    // delete
        fileDeleted(src);
                
        FileNode parentNode = luaSourceTree.findParentNode(dst);
    // add to parent
        if (parentNode != null) {
            FileTree subtree = FileTree.build(dst,null);
            addFileNodeToParent(subtree.getRoot(),parentNode);
        }
    }
    
    /** */
    public FileTree getLuaSourceTree() {
        return luaSourceTree;
    }
    
    /** */
    public List<File> getLuaSourceFiles() {
        List<File> files = new ArrayList<>();
        
        List<FileNode> nodes = luaSourceTree.flatten();
        for (FileNode node:nodes) {
            File file = node.getFile();
            if (file != null && node.isFile() == true) {
                files.add(file);
            }
        }
        
        return files;
    }
    
    /** */
    private FileTree createLuaSourceTree() {
        FileTree luaSrcTree = new FileTree();
        
        File[] luaSrcDirs = ProjectProperties.get().getLuaSrcDirs();
    // for each directory
        for (File luaSrcDir:luaSrcDirs) {
            FileTree tree = FileTree.build(luaSrcDir,null);
            luaSrcTree.merge(tree);
        }
                                                     
    // append source directories to the root
        for (File luaSrcDir:luaSrcDirs) {
            luaSrcTree.getRoot().addFile(Files.canonical(luaSrcDir));
        }
        
    // overwrite the root name
        luaSrcTree.getRoot().setName("");
        
        return luaSrcTree;
    }
    
    /** */
    private boolean isLuaSrcDir(File dir) {
        File[] luaSrcDirs = ProjectProperties.get().getLuaSrcDirs();
        for (File luaSrcDir:luaSrcDirs) {
            if (luaSrcDir.equals(dir) == true) {
                return true;
            }
        }
        return false;
    }
    
    /** */
    public String getLuaFileIconName(File file) {
        return LuaFile.getLuaFileIconName(file);
    }
    
    /** */
    public ImageIcon getLuaFileIcon(File file) {
        return Icons.getIcon(getLuaFileIconName(file));
    }
    
    /** */
    public String getLuaDirIconName(File dir) {
        return LuaFile.getLuaDirIconName(dir);
    }
    
    /** */
    public ImageIcon getLuaDirIcon(File dir) {
        return Icons.getIcon(getLuaDirIconName(dir));
    }
    
    /** */
    public String getLuaDirIconName(Iterable<File> dirs) {
        return LuaFile.getLuaDirIconName(dirs);
    }
    
    /** */
    public ImageIcon getLuaDirIcon(Iterable<File> dirs) {
        return Icons.getIcon(getLuaDirIconName(dirs));
    }
    
    /** */
    private boolean isLuaFile(File file,boolean checkIsFile,File[] luaSrcDirs) {
        if (checkIsFile == true && LuaFile.isLuaFile(file) == false) {
            return false;
        }
        
        for (File luaSrcDir:luaSrcDirs) {
            if (FileUtil.isAncestor(luaSrcDir,file) == true) {
                return true;
            }
        }
        
        return false;        
    }

    /** */
    private boolean isLuaFile(File file,boolean checkIsFile) {
        return isLuaFile(file,checkIsFile,
            ProjectProperties.get().getLuaSrcDirs());
    }
    
    /** */
    public boolean isLuaFile(File file) {
        return isLuaFile(file,true);
    }
    
    /** */
    public boolean isLuaDir(File dir) {
        return isLuaFile(dir,false);
    }
    
    /** */
    private boolean isLuaTestFile(File file,boolean checkIsFile) {
        return isLuaFile(file,checkIsFile,
            ProjectProperties.get().getLuaTestSrcDirs());
    }
    
    /** */
    public boolean isLuaTestFile(File file) {
        return isLuaTestFile(file,true);
    }

    /** */
    public boolean isLuaTestDir(File file) {
        return isLuaTestFile(file,false);
    }
    
    /** */
    public String getPath(File file,boolean checkLuaFile) {
        if (checkLuaFile == true && isLuaFile(file) == false) {
            return null;
        }
        String path = "";
        
        while (isLuaSrcDir(file) == false) {
            if (path.length() > 0) {
                path = PATH_SEPARATOR + path;
            }
            path = file.getName() + path;
            file = file.getParentFile();
        }
        
        return PATH_SEPARATOR + path;        
    }

    /** */
    public String getPath(File file) {
        return getPath(file,true);
    }
    
    /** */
    public String getLuaPath(File file) {
        String path = getPath(file);
        if (path == null) {
            return null;
        }
    
        if (path.length() > 0) {
            path = path.substring(1,path.length());
        }
        
        return path;
    }
    
    /** */
    public String getLuaModule(File file) {
        if (isLuaFile(file) == false) {
            return null;
        }
        String module = "";
        
        while (isLuaSrcDir(file) == false) {
            if (module.length() > 0) {
                module = MODULE_SEPARATOR + module;
            }
            module = file.getName() + module;
            file = file.getParentFile();
        }
        
        return LuaFile.removeSuffix(module);        
    }
    
    /**
     * Builds a Lua module for a file not included in the tree.
     *
     * @param file The file.
     * @return The Lua module or null if the file does not have a parent node.
     */
    private String buildLuaModule(File file) {
        FileNode parentNode = luaSourceTree.findParentNode(file);
        if (parentNode == null) {
            return null;
        }
        
    // get right of the first separator        
        String parentModule = parentNode.getPath(MODULE_SEPARATOR);
        if (parentModule.isEmpty() == false) {
            parentModule = parentModule.substring(1,parentModule.length());
        }
        
        String luaModule = "";
        if (parentModule.length() > 0) {
            luaModule += parentModule + MODULE_SEPARATOR;
        }
        
        return luaModule + file.getName();
    }
    
    /** */
    public FileNode getNodeByPath(String path,boolean ignoreError) {
        List<FileNode> nodes = luaSourceTree.flatten();
        for (FileNode node:nodes) {
            if (node.getPath().equals(path) == true) {
                if (ignoreError == true || getError(node) == null) {
                    return node;
                }
            }            
        }
        
        return null;
    }
    
    /** */
    public File getFileByPath(String path) {
        FileNode foundNode = null; 
        
        List<FileNode> nodes = luaSourceTree.flatten();
        for (FileNode node:nodes) {
            if (node.getPath().equals(path) == true && getError(node) == null) {
                if (foundNode != null) {
                    Log.error(String.format(
                        "Duplicate file for node %s:\n  %s\n  %s",
                        path,foundNode.getFile().getAbsolutePath(),
                        node.getFile().getAbsolutePath()));
                    return null;
                }
                foundNode = node;
            }
        }
        
        if (foundNode != null) {
            return foundNode.getFile();            
        }    
           
        return null;    
    }

    /** */
    private void notifyNodeChanged(FileNode node) {
        synchronized (listeners) {
            for (ProjectLuaFilesListener listener:listeners) {
                listener.luaSourceFileNodeChanged(node);
            }
        }
    }

    /** */
    private void notifyNodeAdded(FileNode parentNode,FileNode node) {
        synchronized (listeners) {
            for (ProjectLuaFilesListener listener:listeners) {
                listener.luaSourceFileNodeAdded(parentNode,node);
            }
        }
    }
    

    /** */
    private void notifyNodeRemoved(FileNode parentNode,FileNode node,
        int index) {
    //
        synchronized (listeners) {
            for (ProjectLuaFilesListener listener:listeners) {
                listener.luaSourceFileNodeRemoved(parentNode,node,index);
            }
        }
    }    
    
    /** */
    public void addProjectLuaFilesListener(ProjectLuaFilesListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /** */
    public static String getError(FileNode node) {
        List<File> files = node.getFiles();
    // single file
        if (files.size() == 1) {
            return null;
        }
        
    // file node cannot point to a file and directory at the same time
        if (node.hasFiles() && node.hasDirs()) {
            return res.getStr("file.and.dir",node.getPath(MODULE_SEPARATOR));
        }
        
    // file node cannot point to 2 or more files
        if (node.hasFiles() && node.getFileCount() > 1) {
            return res.getStr("file.n",node.getPath(MODULE_SEPARATOR));
        }      
        
        return null;
    }    
    
    /** */
    public static ProjectLuaFiles get() {
        if (instance == null) {
            instance = new ProjectLuaFiles();
        }
        
        return instance;
    }
}