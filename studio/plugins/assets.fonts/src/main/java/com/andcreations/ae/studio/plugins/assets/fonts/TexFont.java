package com.andcreations.ae.studio.plugins.assets.fonts;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.andcreations.ae.assets.AssetsCfg;
import com.andcreations.ae.assets.AssetsUtil;
import com.andcreations.ae.assets.DefaultAssetsCfg;
import com.andcreations.ae.assets.LuaModulesCodeGenListener;
import com.andcreations.ae.assets.TexFontBuilder;
import com.andcreations.ae.assets.TexFontBuilderListener;
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
import com.andcreations.ae.studio.plugins.text.editor.DefaultTextEditor;
import com.andcreations.ae.studio.plugins.text.editor.EditorCfg;
import com.andcreations.ae.studio.plugins.text.editor.EditorSyntax;
import com.andcreations.ae.studio.plugins.text.editor.TextEditor;
import com.andcreations.ae.studio.plugins.wizards.Wizards;
import com.andcreations.ae.tex.font.TexFontListener;
import com.andcreations.io.FileUtil;
import com.andcreations.lang.ThrowableUtil;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class TexFont {
    /** */
    private static final String PROBLEMS_SOURCE_ID = TexFont.class.getName();
    
    /** */
    private static TexFont instance;
    
    /** */
    private TexFontBuilderListener texFontBuilderListener =
        new TexFontBuilderListener() {
            /** */
            @Override
            public void loadingTexFont(File file) {
                loadProblems.removeProblems(file);
            }
            
            /** */
            @Override
            public void failedToLoadTexFont(File file,Exception exception) {
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
            public void failedToLoadTexFont(File file,List<Issue> issues) {
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
            public void loadedTexFont(File file) {
                Log.trace("Loaded " + getPath(file));
            }            

            /** */
            @Override
            public void buildingTexFont(File file) {
                buildProblems.removeProblems(file);
            }
            
            /** */
            @Override
            public void texFontUpToDate(File file) {
                Log.trace("Up-to-date " + getPath(file));
                DefaultConsole.get().traceln(
                    res.getStr("up.to.date",getPath(file)));
            }                
            
            /** */
            @Override
            public void didBuildTexFont(File file) {
                Log.trace("Successfully built " + getPath(file));
                DefaultConsole.get().traceln(res.getStr("built",getPath(file)));
            }
            
            /** */
            @Override
            public void failedToBuildTexFont(File file,Exception exception) {
                String msg = ThrowableUtil.getRootCauseMessage(exception);
                Problem problem = buildProblems.addProblem(file,
                    ProblemSeverity.ERROR,msg,
                    getPath(file),null,problemType); 
                addProblemListener(file,problem);
                DefaultConsole.get().errorln(res.getStr("failed.to.build",
                    getPath(file),msg));                
            }                

            /** */
            @Override
            public void texFontExtUpToDate(File file) {
                Log.trace("Font images up-to-date " + getPath(file));
                DefaultConsole.get().traceln(
                    res.getStr("images.up.to.date",getPath(file)));
            }                
            
            /** */
            @Override
            public void didExtTexFont(File file) {
                Log.trace("Extracted font images " + getPath(file));
                DefaultConsole.get().traceln(
                    res.getStr("extracted.images",getPath(file)));
            }
            
            /** */
            @Override
            public void failedToExtTexFont(File file,Exception exception) {
                String msg = ThrowableUtil.getRootCauseMessage(exception);
                Problem problem = buildProblems.addProblem(file,
                    ProblemSeverity.ERROR,msg,
                    getPath(file),null,problemType);
                addProblemListener(file,problem);
                DefaultConsole.get().errorln(
                    res.getStr("failed.to.extract.images",getPath(file),msg));                
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
    private final BundleResources res = new BundleResources(TexFont.class);     
    
    /** */
    private AssetsCfg assetsCfg;    
    
    /** */
    private TexFontBuilder texFontBuilder; 
    
    /** */
    private TexFontAssetsBuilder assetsBuilder;
    
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
        Resources.get().addSource(new TexFontResourceSource());        
        
    // file tree
        FileTreeComponent.registerFileTreeNodeRenderer(
            new TexFontFileTreeNodeRenderer());
        FileTreeComponent.registerFileTreeNodeListener(
            new TexFontFileTreeNodeListener());        
        
    // problems
        problemType = new ProblemType(res.getStr("problem.type"));
        loadProblems = new FileProblems(PROBLEMS_SOURCE_ID);
        buildProblems = new FileProblems(PROBLEMS_SOURCE_ID);  
        
    // texfont builder
        createTexFontBuilder(projectPlugin.getProjectDir());
        
    // file listener
        createFileListener();        
        
    // assets builder
        assetsBuilder = new TexFontAssetsBuilder(
            texFontBuilder,assetsCfg,luaModulesCodeGenListener);
        Builders.get().appendBuilder(assetsBuilder);
        AssetsBuilderPipeline.get().appendBuilder(assetsBuilder);      
        
    // wizard
        Wizards.get().addWizard(new CreateTexFontWizard());        
    } 
    
    /** */
    private void createTexFontBuilder(File projectDir) {
    // files
        List<File> texFontFiles = AssetsUtil.findFiles(projectDir,
            TexFontBuilder.TEX_FONT_FILE_SUFFIX);
        
    // configuration
        assetsCfg = DefaultAssetsCfg.create(projectDir);
        
    // listener
        TexFontListener texFontListener = new TexFontListener() {
            /** */
            @Override
            public void texFontDirCreated(File texFontFile,File dir) {
                Files.get().notifyDirCreated(dir);
            }
    
            /** */
            @Override
            public void texFontFileCreated(File texFontFile,File file) {
                Files.get().notifyFileCreated(file);
            }            
        };
        
    // builder
        texFontBuilder = new TexFontBuilder(assetsCfg,texFontFiles,
            texFontBuilderListener,texFontListener);        
    }
            
    /** */
    private void createFileListener() {
        Files.get().addFileListener(new FileAdapter() {
            /** */
            @Override
            public void fileCreated(File file) {
                tryCreateTexFont(file);
            }
            
            /** */
            @Override
            public void fileChanged(File file) {
                if (TexFontBuilder.isTexFontFile(file) == true) {
                    texFontFileChanged(file);
                }
            }
                        
            /** */
            @Override
            public void fileDeleted(File file) {
                tryDeleteTexFont(file);
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
    private void tryCreateTexFont(File file) {
    // file
        if (file.isFile() == true) {
            createTexFont(file);
            return;
        }
        
    // directory
        File[] childFiles = Files.listTree(file);
        for (File childFile:childFiles) {
            if (childFile.isFile() == true) {
                createTexFont(childFile);
            }                        
        }         
    }
    
    /** */
    private void createTexFont(File file) {
        if (TexFontBuilder.isTexFontFile(file) == false) {
            return;
        }
        texFontBuilder.addTexFontFile(file);
        Log.trace("TexFont file created " + file.getAbsolutePath());
    }
    
    /** */
    private void texFontFileChanged(File file) {
        Log.trace("TexFont file changed " + file.getAbsolutePath());
        Builders.get().postBuild(new SingleTexFontBuilder(texFontBuilder,file));
    }
    
    /** */
    private void tryDeleteTexFont(File file) {
    // file
        if (file.isFile() == true) {
            deleteTexFont(file);
            return;
        }
        
    // directory
        while (true) {
            File toDelete = null;
            for (File texFontFile:texFontBuilder.getTexFontFiles()) {
                if (FileUtil.isAncestor(file,texFontFile) == true) {
                    toDelete = texFontFile;
                    break;
                }                
            }
            
            if (toDelete == null) {
                break;
            }
            deleteTexFont(toDelete);
        }        
    }
    
    /** */
    private void deleteTexFont(File file) {
        if (TexFontBuilder.isTexFontFile(file) == false) {
            return;
        }        
        loadProblems.removeProblems(file);
        buildProblems.removeProblems(file);
        texFontBuilder.removeTexFontFile(file);
        Log.trace("TexFont file deleted " + file.getAbsolutePath());
    }
    
    /** */
    private void addProblemListener(final File file,Problem problem) {
        problem.addProblemListener(new ProblemListener() {
            /** */
            @Override
            public void problemDoubleClicked(Problem problem) {
                DefaultTextEditor.get().edit(file);
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
            FontsIcons.FONT);
    }
    
    /** */
    void edit(File file) {
        EditorCfg cfg = getEditorCfg(file);
        TextEditor.get().edit(cfg);
    }    
    
    /** */
    public boolean isTexFontFile(File file) {
        return file.getName().endsWith(TexFontBuilder.TEX_FONT_FILE_SUFFIX);
    }
        
    /** */
    public List<File> getTexFontFiles() {
        return texFontBuilder.getTexFontFiles();
    }    
    
    /** */
    public static TexFont get() {
        if (instance == null) {
            instance = new TexFont();
        }
        
        return instance;
    }
}