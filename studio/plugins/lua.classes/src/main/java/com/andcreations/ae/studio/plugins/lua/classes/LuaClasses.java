package com.andcreations.ae.studio.plugins.lua.classes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.andcreations.ae.issue.Issue;
import com.andcreations.ae.lua.hierarchy.LuaFileClassInfo;
import com.andcreations.ae.lua.hierarchy.LuaFileClassParser;
import com.andcreations.ae.lua.parser.LuaFileInfo;
import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.file.FileAdapter;
import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.file.LineLocation;
import com.andcreations.ae.studio.plugins.file.problems.FileProblems;
import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.lua.parser.LuaParsedFile;
import com.andcreations.ae.studio.plugins.lua.parser.LuaParser;
import com.andcreations.ae.studio.plugins.problems.Problem;
import com.andcreations.ae.studio.plugins.problems.ProblemListener;
import com.andcreations.ae.studio.plugins.problems.ProblemSeverity;
import com.andcreations.ae.studio.plugins.problems.ProblemType;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.io.FileUtil;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class LuaClasses {
    /** */
    private static final String FILE_PROBLEMS_SOURCE_ID =
        LuaClasses.class.getName();
        
    /** */
    private static final BundleResources res =
        new BundleResources(LuaClasses.class);        
        
    /** */
    private static LuaClasses instance;
    
    /** */
    private final ProblemType problemType = new ProblemType(
        res.getStr("problem.type"));
    
    /** */
    private FileProblems fileProblems = new FileProblems(
        FILE_PROBLEMS_SOURCE_ID);
    
    /** */
    private FileProblems moduleProblems = new FileProblems(
        FILE_PROBLEMS_SOURCE_ID);
    
    /** */
    private FileProblems dependencyProblems = new FileProblems(
        FILE_PROBLEMS_SOURCE_ID);
    
    /** */
    private LuaFileClassParser luaClassParser = new LuaFileClassParser();
    
    /** */
    private Map<String,LuaClass> luaClasses = new HashMap<>();
    
    /** */
    private List<LuaClassesListener> listeners = new ArrayList<>();
    
    /** */
    private boolean containsClass(File file) {
        String luaModule = ProjectLuaFiles.get().getLuaModule(file);
        return luaClasses.containsKey(luaModule);
    }
    
    /** */
    private void process(final File file,LuaFileInfo info) {
        Log.trace(String.format("Getting Lua class info %s",
            file.getAbsolutePath()));
        
    // class info
        LuaFileClassInfo luaClassInfo = luaClassParser.getLuaClassInfo(info);
        if (luaClassInfo == null) {
            clear(file);
            return;
        }
        
    // issues
        for (final Issue issue:luaClassInfo.getIssues()) {
            String resource = ProjectLuaFiles.get().getLuaPath(file); 
            LineLocation location = new LineLocation(issue.getLine());
            
        // problem
            Problem problem = fileProblems.addProblem(
                file,issue,resource,location,problemType);
            
        // problem listener
            problem.addProblemListener(new ProblemListener() {
                /** */
                @Override
                public void problemDoubleClicked(Problem problem) {
                    LuaFile.edit(file,issue.getLine());
                }
            });
        }
        
    // if not a Lua class
        if (luaClassInfo.isClass() == false) {
            return;
        }
        
        boolean contains = containsClass(file);
    // keep
        String luaModule = ProjectLuaFiles.get().getLuaModule(file);
        luaClasses.put(luaModule,new LuaClass(file,luaModule,luaClassInfo));
        if (contains == false) {
            // TODO Notify added.
        }
        else {
            // TODO Notify changed.
        }
        validateLuaClasses();
    }
    
    /** */
    private List<LuaClass> getUnknownSuperclasses() {
        List<LuaClass> unknown = new ArrayList<>();

        for (LuaClass luaClass:luaClasses.values()) {
            String superclass = luaClass.getLuaClassInfo().getSuperclassName();
            if (superclass != null &&
                luaClasses.containsKey(superclass) == false) {
            //
                unknown.add(luaClass);
            }
        }
        
        return unknown;
    }
    
    /** */
    private boolean hasUnknownSuperclasses() {
        List<LuaClass> unknown = getUnknownSuperclasses();
        return unknown.isEmpty() == false;
    }
        
    /** */
    private void addUnkownSuperclassProblem(LuaClass luaClass) {
        final File file = luaClass.getFile();
        
        String msg = res.getStr("unknown.superclass",
            luaClass.getLuaClassInfo().getSuperclassName());
        String resource = ProjectLuaFiles.get().getLuaPath(file);
        
        final int line = luaClass.getLuaClassInfo().getSubclassLine();
        LineLocation location = new LineLocation(line);
        
    // problem
        Problem problem = moduleProblems.addProblem(
            file,ProblemSeverity.ERROR,msg,resource,location,problemType);
        
    // problem listener
        problem.addProblemListener(new ProblemListener() {
            /** */
            @Override
            public void problemDoubleClicked(Problem problem) {
                LuaFile.edit(file,line);
            }
        });
    }    
    
    /** */
    private void validateSuperclasses() {
    // remove problems
        for (LuaClass luaClass:luaClasses.values()) {
            moduleProblems.removeProblems(luaClass.getFile());
        }
                
        List<LuaClass> unknown = getUnknownSuperclasses();
        for (LuaClass luaClass:unknown) {
            addUnkownSuperclassProblem(luaClass);
        }
    }
    
    /** */
    private boolean hasCircularDependency(LuaClass luaClass) {
        int count = 0;
        while (luaClass != null) {
            count++;
            if (count > luaClasses.size()) {
                return true;
            }
            
            String superclass = luaClass.getSuperclassName();
            if (superclass == null) {
                break;
            }
            
            luaClass = luaClasses.get(superclass);
        }
        
        return false;
    }
    
    /** */
    private List<LuaClass> getCircularDependencies() {
        List<LuaClass> circular = new ArrayList<>();

        for (LuaClass luaClass:luaClasses.values()) {
            if (hasCircularDependency(luaClass) == true) {
                circular.add(luaClass);
            }
        }
        
        return circular;
    }
    
    /** */
    private boolean hasCircularDependency() {
        List<LuaClass> circular = getCircularDependencies();
        return circular.isEmpty() == false;
    }
    
    /** */
    private void addCircularDependencyProblem(LuaClass luaClass) {
        final File file = luaClass.getFile();
        
        String msg = res.getStr("circular.dependency",luaClass.getLuaModule());
        String resource = ProjectLuaFiles.get().getLuaPath(file);
        
        final int line = luaClass.getLuaClassInfo().getSubclassLine();
        LineLocation location = new LineLocation(line);
        
    // problem
        Problem problem = dependencyProblems.addProblem(
            file,ProblemSeverity.ERROR,msg,resource,location,problemType);
        
    // problem listener
        problem.addProblemListener(new ProblemListener() {
            /** */
            @Override
            public void problemDoubleClicked(Problem problem) {
                LuaFile.edit(file,line);
            }
        });
    }     
    
    /** */
    private void validateCircularDependencies() {
    // remove problems
        for (LuaClass luaClass:luaClasses.values()) {
            dependencyProblems.removeProblems(luaClass.getFile());
        }

        List<LuaClass> circular = getCircularDependencies();
        for (LuaClass luaClass:circular) {
            addCircularDependencyProblem(luaClass);
        }                
    }
    
    /** */
    private void validateLuaClasses() {
        validateSuperclasses();
        validateCircularDependencies();
    }
    
    /** */
    public LuaClassTree buildTree() {
        if (hasCircularDependency() == true ||
            hasUnknownSuperclasses() == true) {
        //
            return null;
        }
        
        LuaClassTree tree = LuaClassTree.build(luaClasses.values());
        tree.sortByName();
        return tree;
    }
    
    /** */
    void init() {
        for (LuaParsedFile parsedFile:LuaParser.get().getParsedFiles()) {
            process(parsedFile.getFile(),parsedFile.getInfo());
        }
        validateLuaClasses();
        buildTree();
        createFileListener();
    }
    
    /** */
    private void createFileListener() {
        Files.get().addFileListener(new FileAdapter() {
            /** */
            @Override
            public void fileChanged(File file) {
                tryParse(file);
            }
            
            /** */
            @Override
            public void fileCreated(File file) {
                tryParse(file);
            }
            
            /** */
            @Override
            public void fileDeleted(File file) {
                tryClear(file);
                validateLuaClasses();
            }
            
            /** */
            @Override
            public void fileRenamed(File src,File dst) {
                fileDeleted(src);
                fileCreated(dst);
            }
        });
    }
        
    /** */
    private void tryClear(File file) {
    // file
        if (file.isFile() == true) {
            clear(file);
            return;
        }
        
    // directory
        while (true) {
            LuaClass toRemove = null;
            for (LuaClass luaClass:luaClasses.values()) {
                if (FileUtil.isAncestor(file,luaClass.getFile()) == true) {
                    toRemove = luaClass;
                    break;                    
                }
            }
            
            if (toRemove == null) {
                break;
            }
            clear(toRemove.getFile());            
        }
    }    
    
    /** */
    private void clear(File file) {
        if (containsClass(file) == false) {
            return;
        }
        String luaModule = ProjectLuaFiles.get().getLuaModule(file);
        luaClasses.remove(luaModule);
        fileProblems.removeProblems(file);
        
        // TODO notify removed
    }    
    
    /** */
    private void tryParse(File file) {
    // file
        if (file.isFile() == true) {
            parse(file);
            return;
        }
        
    // directory
        File[] childFiles = Files.listTree(file);
        for (File childFile:childFiles) {
            if (childFile.isFile() == true &&
                ProjectLuaFiles.get().isLuaFile(childFile) == true) {
            //
                parse(childFile);
            }                        
        }
    }
    
    /** */
    private void parse(File file) {
        if (ProjectLuaFiles.get().isLuaFile(file) == false) {
            return;
        }
        
        try {
            LuaParsedFile parsedFile = LuaParser.get().parse(file,null);
            process(parsedFile.getFile(),parsedFile.getInfo());
        } catch (Exception exception) {
            clear(file);
        }
        validateLuaClasses();        
    }
    
    /**
     * Checks if a Lua class is defined.
     * 
     * @param moduleName The name of the module which represents the class.
     * @return <code>true</code> if the class is defined,
     *   <code>false</code> otherwise.
     */
    public boolean hasLuaClass(String moduleName) {
        return luaClasses.containsKey(moduleName);
    }
    
    /** */
    public void addLuaClassesListener(LuaClassesListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    public LuaClass selectLuaClass() {
        return SelectLuaClassDialog.get().show(luaClasses.values());
    }
    
    /** */
    public static LuaClasses get() {
        if (instance == null) {
            instance = new LuaClasses();
        }
        
        return instance;
    }
}