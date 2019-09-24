package com.andcreations.ae.eclipse;

import java.io.File;
import java.io.IOException;

import com.andcreations.eclipse.JavaProjectBuilder;
import com.andcreations.eclipse.JavaProjectCfg;
import com.andcreations.eclipse.LinkedFolder;

/**
 * @author Mikolaj Gucki
 */
public class AEProjectsBuilder {
    /** */
    static final String AE_COMMONS_PROJECT_NAME = "ae.commons";
    
    /** */
    static final String AE_TOOLS_PROJECT_NAME = "ae.tools";
    
    /** */
    static final String AE_DIST_PROJECT_NAME = "ae.dist";
    
    /** */
    static final String AE_STUDIO_CORE_PROJECT_NAME =
        "com.andcreations.ae.studio";
    
    /** */
    private AEProjectsBuilderCfg cfg;
    
    /** */
    public AEProjectsBuilder(AEProjectsBuilderCfg cfg) {
        this.cfg = cfg;
    }
    
    /** */
    private File getJavaLibsDir() {
        return AEProjectsCommon.getJavaLibsDir(cfg);
    }
    
    /** */
    private File[] getJavaLibs() {
        return AEProjectsCommon.getJavaLibs(cfg);
    }
    
    /** */
    private void addJavaLibs(JavaProjectCfg javaCfg) {
        File[] libs = getJavaLibs();
        for (File file:libs) {
            javaCfg.addLib("libs/ae/" + file.getName());
        }
    }
    
    /** */
    private File getAntLibDir() {
        return new File(cfg.getAntHome(),"lib");
    }
    
    /** */
    private void appendAntLibs(JavaProjectCfg javaCfg) {
        javaCfg.addLib("libs/ant/ant.jar");
    }
    
    /** */
    private void createJavaProject(File dstDir,String name,File srcDir,
        String[] projects) throws IOException {
    // configure
        JavaProjectCfg javaCfg = new JavaProjectCfg(name);
        javaCfg.addSrcDir("src");
        javaCfg.setOutputDir("build/classes");
        javaCfg.addDir("libs");
        javaCfg.addLinkedResource(new LinkedFolder("src",
            AEProjectsCommon.getAbsPath(srcDir)));
        
    // AE libraries
        javaCfg.addLinkedResource(new LinkedFolder(
            "libs/ae",AEProjectsCommon.getAbsPath(getJavaLibsDir())));
        addJavaLibs(javaCfg);
        
    // Ant libraries
        javaCfg.addLinkedResource(new LinkedFolder(
            "libs/ant",AEProjectsCommon.getAbsPath(getAntLibDir())));
        appendAntLibs(javaCfg);
     
    // projects
        if (projects != null) {
            for (String project:projects) {
                javaCfg.addProject(project);
            }
        }
        
    // build
        JavaProjectBuilder builder = new JavaProjectBuilder(javaCfg);
        builder.build(new File(dstDir,name));
    }
    
    /** */
    private void createBasicProjects(File dstDir) throws IOException {
    // commons
        createJavaProject(dstDir,AE_COMMONS_PROJECT_NAME,
            new File(cfg.getAESrcDir(),"commons/src/main/java"),null);
        
    // tools
        createJavaProject(dstDir,AE_TOOLS_PROJECT_NAME,
            new File(cfg.getAESrcDir(),"tools/src/main/java"),
            new String[]{AE_COMMONS_PROJECT_NAME,AE_STUDIO_CORE_PROJECT_NAME});
    // dist
        createJavaProject(dstDir,AE_DIST_PROJECT_NAME,
            new File(cfg.getAESrcDir(),"dist/src/main/java"),
            new String[]{AE_COMMONS_PROJECT_NAME,AE_TOOLS_PROJECT_NAME});
    }
    
    /** */
    private void createAEStudioPluginsProjects(File dstDir) throws IOException {
        AEStudioPluginProjectsBuilder builder =
            new AEStudioPluginProjectsBuilder(cfg);
        builder.createAEStudioPluginsProjects(dstDir);
    }
    
    /** */
    private void createAEStudioProjects(File dstDir) throws IOException {
    // core
        createJavaProject(dstDir,AE_STUDIO_CORE_PROJECT_NAME,
            new File(cfg.getAESrcDir(),"studio/core/src/main/java"),
            new String[]{AE_COMMONS_PROJECT_NAME});
        
    // plugins
        createAEStudioPluginsProjects(dstDir);
    }
    
    /** */
    public void createProjects(File dstDir) throws IOException {
        createBasicProjects(dstDir);
        createAEStudioProjects(dstDir);
    }
}