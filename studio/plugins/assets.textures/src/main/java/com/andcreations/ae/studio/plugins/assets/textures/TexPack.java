package com.andcreations.ae.studio.plugins.assets.textures;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.andcreations.ae.assets.AssetsCfg;
import com.andcreations.ae.assets.AssetsUtil;
import com.andcreations.ae.assets.DefaultAssetsCfg;
import com.andcreations.ae.assets.LuaModulesCodeGenListener;
import com.andcreations.ae.assets.TexPackBuilder;
import com.andcreations.ae.assets.TexPackBuilderListener;
import com.andcreations.ae.issue.Issue;
import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.assets.AssetsBuilderPipeline;
import com.andcreations.ae.studio.plugins.builders.Builders;
import com.andcreations.ae.studio.plugins.console.DefaultConsole;
import com.andcreations.ae.studio.plugins.file.FileAdapter;
import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeComponent;
import com.andcreations.ae.studio.plugins.file.problems.FileProblems;
import com.andcreations.ae.studio.plugins.problems.Problem;
import com.andcreations.ae.studio.plugins.problems.ProblemListener;
import com.andcreations.ae.studio.plugins.problems.ProblemSeverity;
import com.andcreations.ae.studio.plugins.problems.ProblemType;
import com.andcreations.ae.studio.plugins.project.ProjectPlugin;
import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;
import com.andcreations.ae.studio.plugins.resources.Resources;
import com.andcreations.ae.studio.plugins.text.editor.EditorCfg;
import com.andcreations.ae.studio.plugins.text.editor.EditorSyntax;
import com.andcreations.ae.studio.plugins.text.editor.TextEditor;
import com.andcreations.ae.studio.plugins.wizards.Wizards;
import com.andcreations.ae.tex.pack.TexPackerListener;
import com.andcreations.io.FileUtil;
import com.andcreations.lang.ThrowableUtil;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class TexPack {
    /** */
    private static final String PROBLEMS_SOURCE_ID = TexPack.class.getName();
    
    /** */
    private static TexPack instance;
    
    /** */
    TexPackBuilderListener texPackBuilderListener = 
        new TexPackBuilderListener() {
            /** */
            @Override
            public void loadingTexPack(File file) {
                loadProblems.removeProblems(file);
            }
            
            /** */
            @Override
            public void failedToLoadTexPack(File file,Exception exception) {
                String msg = ThrowableUtil.getRootCauseMessage(exception);
                Problem problem = loadProblems.addProblem(file,
                    ProblemSeverity.ERROR,msg,                    
                    getPath(file),null,problemType);
                addProblemListener(file,problem);
                DefaultConsole.get().errorln(
                    res.getStr("failed.to.load",getPath(file),msg));
            }
            
            /** */
            @Override
            public void failedToLoadTexPack(File file,List<Issue> issues) {
                StringBuilder str = new StringBuilder();
                str.append("\n");
            // for each issue                
                for (Issue issue:issues) {
                    Problem problem = loadProblems.addProblem(
                        file,issue,getPath(file),null,problemType);
                    addProblemListener(file,problem);
                    str.append(res.getStr("issue",issue.getMessage()));
                    str.append("\n");
                }
                DefaultConsole.get().error(
                    res.getStr("failed.to.load",getPath(file),str.toString()));                                
            }                
            
            /** */
            @Override
            public void loadedTexPack(File file) {
                Log.trace("Loaded " + getPath(file));
            }
            
            /** */
            @Override
            public void buildingTexPack(File file) {
                buildProblems.removeProblems(file);
            }
            
            /** */
            @Override
            public void texPackUpToDate(File file) {
                Log.trace("Up-to-date " + getPath(file));
                DefaultConsole.get().traceln(
                    res.getStr("up.to.date",getPath(file)));
            }
            
            /** */
            @Override
            public void didBuildTexPack(File file) {
                Log.trace("Successfully built " + getPath(file));
                DefaultConsole.get().traceln(res.getStr("built",getPath(file)));
            }
            
            /** */
            @Override
            public void failedToBuildTexPack(File file,Exception exception) {
                String msg = ThrowableUtil.getRootCauseMessage(exception);
                Problem problem = buildProblems.addProblem(file,
                    ProblemSeverity.ERROR,msg,
                    getPath(file),null,problemType);
                addProblemListener(file,problem);
                DefaultConsole.get().errorln(res.getStr("failed.to.build",
                    getPath(file),msg));                
            }            
        };    
    
    /** */
    private LuaModulesCodeGenListener luaModulesCodeGenListener =
        new LuaModulesCodeGenListener() {
            /** */
            public void luaModuleCodeGenDirCreated(File dir) {
                Files.get().notifyDirCreated(dir);
            }
            
            /** */
            public void luaModuleCodeGenFileCreated(File file) {
                Files.get().notifyFileCreated(file);
            }
            
            /** */
            @Override
            public void failedToGenLuaModulesCode(File file,
                IOException exception) {
            //
                DefaultConsole.get().errorln(res.getStr(
                    "failed.to.gen.lua.modules",getPath(file),
                    exception.getMessage()));                
            }
            
            /** */
            @Override
            /** */
            public void generatedLuaModuleCode(File file) {
                DefaultConsole.get().traceln(res.getStr("gen.lua.modules",
                    getPath(file)));
            }
        };
        
    /** */
    private final BundleResources res = new BundleResources(TexPack.class);     
    
    /** */
    private AssetsCfg assetsCfg;
    
    /** */
    private TexPackBuilder texPackBuilder; 
    
    /** */
    private TexPackAssetsBuilder assetsBuilder;
    
    /** */
    private ProblemType problemType;
    
    /** */
    private FileProblems loadProblems;
    
    /** */
    private FileProblems buildProblems;
    
    /** */
    void init(Object projectPluginObject) {
        ProjectPlugin projectPlugin = (ProjectPlugin)projectPluginObject;
        
    // resources
        Resources.get().addSource(new TexPackResourceSource());
        
    // file tree
        FileTreeComponent.registerFileTreeNodeRenderer(
            new TexPackFileTreeNodeRenderer());
        FileTreeComponent.registerFileTreeNodeListener(
            new TexPackFileTreeNodeListener());
        
    // problems
        problemType = new ProblemType(res.getStr("problem.type"));
        loadProblems = new FileProblems(PROBLEMS_SOURCE_ID);
        buildProblems = new FileProblems(PROBLEMS_SOURCE_ID);

    // configuration
        assetsCfg = DefaultAssetsCfg.create(projectPlugin.getProjectDir());
        
    // texpack builder
        createTexPackBuilder(projectPlugin.getProjectDir());
        
    // file listener
        createFileListener();
        
    // assets builder
        assetsBuilder = new TexPackAssetsBuilder(
            texPackBuilder,assetsCfg,luaModulesCodeGenListener);
        Builders.get().appendBuilder(assetsBuilder);
        AssetsBuilderPipeline.get().appendBuilder(assetsBuilder);
        
    // wizard
        Wizards.get().addWizard(new CreateTexPackWizard());
    }
    
    /** */
    private void createTexPackBuilder(File projectDir) {
    // files
        List<File> texPackFiles = AssetsUtil.findFiles(projectDir,
            TexPackBuilder.TEX_PACK_FILE_SUFFIX);
        
    // listener
        TexPackerListener texPackerListener = new TexPackerListener() {
            /** */
            @Override
            public void texPackDirCreated(File texPackFile,File dir) {
                Files.get().notifyDirCreated(dir);
            }
    
            /** */
            @Override
            public void texPackFileCreated(File texPackFile,File file) {
                Files.get().notifyFileCreated(file);
            }
        };
        
    // packer
        texPackBuilder = new TexPackBuilder(assetsCfg,texPackFiles,
            texPackBuilderListener,texPackerListener);
    }
    
    /** */
    private void createFileListener() {
        Files.get().addFileListener(new FileAdapter() {
            /** */
            @Override
            public void fileCreated(File file) {
                tryCreateTexPack(file);
            }
            
            /** */
            @Override
            public void fileChanged(File file) {
                if (TexPackBuilder.isTexPackFile(file) == true) {
                    texPackFileChanged(file);
                }
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
        });
    }
    
    /** */
    private void tryCreateTexPack(File file) {
    // file
        if (file.isFile() == true) {
            createTexPack(file);
            return;
        }
        
    // directory
        File[] childFiles = Files.listTree(file);
        for (File childFile:childFiles) {
            if (childFile.isFile() == true) {
                createTexPack(childFile);
            }                        
        }    
    }
    
    /** */
    private void createTexPack(File file) {
        if (TexPackBuilder.isTexPackFile(file) == false) {
            return;
        }
        texPackBuilder.addTexPackFile(file);
        Log.trace("TexPack file created " + file.getAbsolutePath());
    }
    
    /** */
    private void texPackFileChanged(File file) {
        Log.trace("TexPack file changed " + file.getAbsolutePath());
        Builders.get().postBuild(new SingleTexPackBuilder(texPackBuilder,file));
    }
    
    /** */
    private void tryDelete(File file) {
    // file
        if (file.isFile() == true) {
            deleteTexPack(file);
            return;
        }
        
    // directory
        while (true) {
            File toDelete = null;
            for (File texPackFile:texPackBuilder.getTexPackFiles()) {
                if (FileUtil.isAncestor(file,texPackFile) == true) {
                    toDelete = texPackFile;
                    break;
                }                
            }
            
            if (toDelete == null) {
                break;
            }
            deleteTexPack(toDelete);
        }
    }
    
    /** */
    private void deleteTexPack(File file) {
        if (TexPackBuilder.isTexPackFile(file) == false) {
            return;
        }        
        loadProblems.removeProblems(file);
        buildProblems.removeProblems(file);
        texPackBuilder.removeTexPackFile(file);
        Log.trace("TexPack file deleted " + file.getAbsolutePath());
    }
    
    /** */
    private void addProblemListener(final File file,Problem problem) {
        problem.addProblemListener(new ProblemListener() {
            /** */
            @Override
            public void problemDoubleClicked(Problem problem) {
                edit(file);
            }
        });
    }
    
    /** */
    private String getPath(File file) {
        return ProjectFiles.get().getRelativePath(file);
    }    
    
    /** */
    boolean hasWarnings(File file) {
        return loadProblems.hasWarnings(file) ||
            buildProblems.hasWarnings(file);
    }
    
    /** */
    boolean hasErrors(File file) {
        return loadProblems.hasErrors(file) ||
            buildProblems.hasErrors(file);
    }    
    
    /** */
    boolean hasWarnings() {
        return loadProblems.hasWarnings() || buildProblems.hasWarnings();
    }
    
    /** */
    boolean hasErrors() {
        return loadProblems.hasErrors() || buildProblems.hasErrors();
    }   
    
    /** */
    private EditorCfg getEditorCfg(File file) {
        return new EditorCfg(EditorSyntax.JSON,file.getAbsolutePath(),
            TexturesIcons.TEXTURE);
    }
    
    /** */
    void edit(File file) {
        EditorCfg cfg = getEditorCfg(file);
        TextEditor.get().edit(cfg);
    }
    
    /** */
    public boolean isTexPackFile(File file) {
        return file.getName().endsWith(TexPackBuilder.TEX_PACK_FILE_SUFFIX);
    }
    
    /** */
    public List<File> getTexPackFiles() {
        return texPackBuilder.getTexPackFiles();
    }
    
    /** */
    public static TexPack get() {
        if (instance == null) {
            instance = new TexPack();
        }
        
        return instance;
    }
}