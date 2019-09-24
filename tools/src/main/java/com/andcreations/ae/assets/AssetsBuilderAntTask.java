package com.andcreations.ae.assets;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.tools.ant.BuildException;

import com.andcreations.ae.issue.Issue;
import com.andcreations.ant.AETask;
import com.andcreations.ant.AntPath;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class AssetsBuilderAntTask extends AETask {
    /** The string resources. */
    private BundleResources res =
        new BundleResources(AssetsBuilderAntTask.class);   
        
    /** */
    private AntPath projectDir;
        
    /** */
    private TexFontBuilderListener texFontBuilderListener =
        new TexFontBuilderListener() {
            /** */
            @Override
            public void loadingTexFont(File file) {
            }
            
            /** */
            @Override
            public void loadedTexFont(File file) {
            }
            
            /** */
            @Override
            public void failedToLoadTexFont(File file,Exception exception) {
                throw new BuildException(res.getStr("failed.to.load.tex.font",
                    file.getAbsolutePath(),exception.getMessage()));
            }
            
            /** */
            @Override
            public void failedToLoadTexFont(File file,List<Issue> issues) {
                throw new BuildException(res.getStr("failed.to.load.tex.font",
                    file.getAbsolutePath(),issues.get(0).getMessage()));
            }
            
            /** */
            @Override
            public void buildingTexFont(File file) {
            }

            /** */
            @Override
            public void texFontUpToDate(File file) {
            }                
            
            /** */
            @Override
            public void didBuildTexFont(File file) {
                log(res.getStr("did.build.tex.font",file.getName()));
            }
            
            /** */
            @Override
            public void failedToBuildTexFont(File file,Exception exception) {
                throw new BuildException(res.getStr("failed.to.build.tex.font",
                    file.getAbsolutePath(),exception.getMessage()));
            }                

            /** */
            @Override
            public void texFontExtUpToDate(File file) {
            }                
            
            /** */
            @Override
            public void didExtTexFont(File file) {
                log(res.getStr("did.ext.tex.font",file.getName()));
            }
            
            /** */
            @Override
            public void failedToExtTexFont(File file,Exception exception) {
                throw new BuildException(res.getStr("failed.to.ext.tex.font",
                    file.getAbsolutePath(),exception.getMessage()));
            }
        };
        
    /** */
    private TexPackBuilderListener texPackBuilderListener = 
        new TexPackBuilderListener() {
            /** */
            @Override
            public void loadingTexPack(File file) {
            }
            
            /** */
            @Override
            public void failedToLoadTexPack(File file,Exception exception) {
                throw new BuildException(res.getStr("failed.to.load.tex.pack",
                    file.getAbsolutePath(),exception.getMessage()));
            }
            
            /** */
            @Override
            public void failedToLoadTexPack(File file,List<Issue> issues) {
                throw new BuildException(res.getStr("failed.to.load.tex.pack",
                    file.getAbsolutePath(),issues.get(0).getMessage()));
            }                
            
            /** */
            @Override
            public void loadedTexPack(File file) {
            }
            
            /** */
            @Override
            public void buildingTexPack(File file) {
            }
            
            /** */
            @Override
            public void texPackUpToDate(File file) {
            }
            
            /** */
            @Override
            public void didBuildTexPack(File file) {
                log(res.getStr("did.build.tex.pack",file.getName()));
            }
            
            /** */
            @Override
            public void failedToBuildTexPack(File file,Exception exception) {
                throw new BuildException(res.getStr("failed.to.build.tex.pack",
                    file.getAbsolutePath(),exception.getMessage()));
            }
        };
    
    /** */
    private LuaModulesCodeGenListener luaModulesCodeGenListener =
        new LuaModulesCodeGenListener() {
            /** */
            @Override
            public void luaModuleCodeGenDirCreated(File dir) {
            }
            
            /** */
            @Override
            public void luaModuleCodeGenFileCreated(File file) {
            }
            
            /** */
            @Override
            public void failedToGenLuaModulesCode(File file,
                IOException exception) {
            //
                log(res.getStr("failed.to.generate.lua.code",
                    file.getAbsolutePath(),exception.getMessage()));
            }
            
            /** */
            @Override
            public void generatedLuaModuleCode(File file) {
                log(res.getStr("gen.lua.modules",file.getAbsolutePath()));
            }
        };
        
    /** */
    public AntPath createProjectDir() {
        if (projectDir != null) {
            duplicatedElement("projectdir");
        }
        
        projectDir = new AntPath();
        return projectDir;
    }
    
    /** */
    private void buildAssets(File root) {
        AssetsCfg assetsCfg = DefaultAssetsCfg.create(root);
        assetsCfg.mkdirs();
        AssetsBuilderCfg builderCfg = new AssetsBuilderCfg(
            texFontBuilderListener,texPackBuilderListener,
            luaModulesCodeGenListener);
        AssetsBuilder builder = new AssetsBuilder(assetsCfg,builderCfg);
        builder.build();        
    }
    
    /** */
    @Override
    public void execute() {
        verifyElementSet(projectDir,"projectdir");
        
        File root = new File(projectDir.getPath());
        buildAssets(root);
    }        
}