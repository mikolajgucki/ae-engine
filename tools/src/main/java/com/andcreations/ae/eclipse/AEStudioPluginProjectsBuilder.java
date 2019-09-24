package com.andcreations.ae.eclipse;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.eclipse.JavaProjectBuilder;
import com.andcreations.eclipse.JavaProjectCfg;
import com.andcreations.eclipse.LinkedFolder;

/**
 * @author Mikolaj Gucki
 */
class AEStudioPluginProjectsBuilder {
    /** */
    private AEProjectsBuilderCfg cfg;    

    /** */
    private List<AEStudioPluginProject> plugins;
    
    /** */
    private List<DependencyEntry> dependencyEntries;
    
    /** */
    AEStudioPluginProjectsBuilder(AEProjectsBuilderCfg cfg) {
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
    private URL urlFromAESrc(String path) throws MalformedURLException {
        return new File(cfg.getAESrcDir(),path).toURI().toURL();
    }
    
    /** */
    private ClassLoader createClassLoader(File[] pluginDirs)      
        throws MalformedURLException {
    //
        List<URL> urls = new ArrayList<>();
        
    // libraries
        File[] libFiles = getJavaLibs();
        for (File libFile:libFiles) {
            urls.add(libFile.toURI().toURL());
        }
        
    // plugins
        for (File pluginDir:pluginDirs) {
            File jarFile = AEStudioPluginProject.getJarFile(pluginDir);
            if (jarFile == null) {
                continue;
            }
            urls.add(jarFile.toURI().toURL());
            
            File[] pluginLibs = AEStudioPluginProject.getLibs(pluginDir);
            for (File pluginLib:pluginLibs) {
                urls.add(pluginLib.toURI().toURL());
            }
        }
        
    // commons, tools, AE studio core
        urls.add(urlFromAESrc("commons/build/ae.commons.jar"));
        urls.add(urlFromAESrc("tools/build/ae.tools.jar"));
        urls.add(urlFromAESrc("studio/core/build/ae.studio.core.jar"));
        
        return new URLClassLoader(urls.toArray(new URL[]{}),
            ClassLoader.getSystemClassLoader());        
    }    
    
    /** */
    private void addPluginsLibs(JavaProjectCfg javaCfg) {
        for (AEStudioPluginProject plugin:plugins) {
            if (plugin.hasLibs() == true) {
                String dir = "libs/" + plugin.getPluginId();
                javaCfg.addLinkedResource(new LinkedFolder(
                    dir,AEProjectsCommon.getAbsPath(plugin.getLibsDir())));  
                for (File lib:plugin.getLibs()) {
                    javaCfg.addLib(dir + "/" + lib.getName());
                }                
            }
        }
    }
    
    /** */
    private void createAEStudioPluginProject(File dstDir,
        AEStudioPluginProject plugin) throws IOException {
    // configure
        JavaProjectCfg javaCfg = new JavaProjectCfg(plugin.getPluginId());
        javaCfg.addSrcDir("src");
        javaCfg.setOutputDir("build/classes");
        javaCfg.addDir("libs");
        javaCfg.addLinkedResource(new LinkedFolder("src",
            AEProjectsCommon.getAbsPath(plugin.getSrcDir())));
        
    // AE libraries
        javaCfg.addLinkedResource(new LinkedFolder(
            "libs/ae",AEProjectsCommon.getAbsPath(getJavaLibsDir())));
        for (File lib:getJavaLibs()) {
            javaCfg.addLib("libs/ae/" + lib.getName());
        }
        
    // plugins libraries
        addPluginsLibs(javaCfg);
        
    // projects
        javaCfg.addProjects(
            AEProjectsBuilder.AE_COMMONS_PROJECT_NAME,
            AEProjectsBuilder.AE_TOOLS_PROJECT_NAME,
            AEProjectsBuilder.AE_STUDIO_CORE_PROJECT_NAME);
        for (String dependency:plugin.getDependencies()) {
            javaCfg.addProject(dependency);
        }
        
    // build
        JavaProjectBuilder builder = new JavaProjectBuilder(javaCfg);
        builder.build(new File(dstDir,plugin.getPluginId()));        
    }    
    
    /** */
    void createAEStudioPluginsProjects(File dstDir) throws IOException {
        File pluginsDir = new File(cfg.getAESrcDir(),"studio/plugins");
        
    // directories with plugins
        File[] pluginDirs = pluginsDir.listFiles(new FileFilter() {
            /** */
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        
    // class loader
        ClassLoader classLoader = createClassLoader(pluginDirs);
        
        plugins = new ArrayList<>();
    // for each directory with plugin
        for (File pluginDir:pluginDirs) {
            plugins.add(
                new AEStudioPluginProject(pluginDir,classLoader));
        }
        
        dependencyEntries = new ArrayList<>();
    // for each plugin
        for (AEStudioPluginProject plugin:plugins) {
            createAEStudioPluginProject(dstDir,plugin);
            dependencyEntries.add(new DependencyEntry(
                plugin.getPluginId(),plugin.getDependencies()));
        }
        
    // print dependencies
//        for (DependencyEntry entry:dependencyEntries) {
//            System.out.println("-> " + entry.getId());
//            for (String dependency:entry.getDependencies()) {
//                System.out.println("    " + dependency);                
//            }
//        }
    }    
    
    /** */
    void findCyclesInAEStudioPlugins() {
        CycleFinder finder = new CycleFinder();
        finder.findCycles(dependencyEntries);
    }    
}
