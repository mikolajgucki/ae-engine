package com.andcreations.ae.eclipse;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.studio.plugin.PluginJar;
import com.andcreations.ae.studio.plugin.PluginUtil;
import com.andcreations.ae.studio.plugin.Required;
import com.andcreations.ae.studio.plugin.RequiredPlugins;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;
import com.andcreations.ae.studio.plugin.StartBeforeAndRequire;
import com.andcreations.lang.AnnotationUtil;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class AEStudioPluginProject {
    /** */
    private final BundleResources res =
        new BundleResources(AEStudioPluginProject.class);  
    
    /** The directory with the plugin sources. */
    private File pluginDir;
    
    /** The class loader. */
    private ClassLoader classLoader;
    
    /** The plugin identifier. */
    private String pluginId;
    
    /** The dependencies. */
    private List<String> dependencies = new ArrayList<>();

    /**
     * @throws IOException  */
    public AEStudioPluginProject(File pluginDir,ClassLoader classLoader)
        throws IOException {
    //
        this.pluginDir = pluginDir;
        this.classLoader = classLoader;
        create();
    }
    
    /** */
    File getSrcDir() {
        return new File(pluginDir,"src/main/java");
    }
    
    /** */
    File getLibsDir() {
        return new File(pluginDir,"libs");
    }
    
    /** */
    File[] getLibs() {
        return getLibs(pluginDir);
    }
    
    /** */
    boolean hasLibs() {
        File[] libs = getLibs();
        return libs != null && libs.length > 0;
    }
    
    /** */
    String getPluginId() {
        return pluginId;
    }
    
    /** */
    List<String> getDependencies() {
        return dependencies;
    }

    /** */
    private Class<?> getPluginClass(String className,File jarFile)
        throws IOException {
    // class
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className,true,classLoader);
        } catch (ClassNotFoundException exception) {
            throw new IOException(
                res.getStr("plugin.class.not.found",className));
        }
        if (PluginUtil.isPluginClass(clazz) == false) {
            throw new IOException(res.getStr("not.a.plugin.class",className));
        }
        
        return clazz;
    }
    
    private void addDependency(String dependency) {
        if (dependencies.contains(dependency) == true) {
            return;
        }
        dependencies.add(dependency);
    }
    
    /** */
    private void findDependencies(Class<?> clazz) {
    // Required
        Field[] fields = clazz.getDeclaredFields();
        for (Field field:fields) {        
            for (Annotation annotation:field.getAnnotations()) {
                if (AnnotationUtil.is(annotation,Required.class)) {
                    addDependency((String)AnnotationUtil.getValue(
                        annotation,"id"));
                }
            }
        }
        
    // StartBefore, StartAfter, RequiredPlugins
        for (Annotation annotation:clazz.getAnnotations()) {
        // required plugins
            if (AnnotationUtil.is(annotation,RequiredPlugins.class)) {
                String[] idList =
                    (String[])AnnotationUtil.getValue(annotation,"id");
                for (String id:idList) {
                    addDependency(id);
                }                
            }
            
        // start before
            if (AnnotationUtil.is(annotation,StartBeforeAndRequire.class)) {
                String[] idList =
                    (String[])AnnotationUtil.getValue(annotation,"id");
                for (String id:idList) {
                    addDependency(id);
                }
            }
            
        // start after
            if (AnnotationUtil.is(annotation,StartAfterAndRequire.class)) {
                String[] idList =
                    (String[])AnnotationUtil.getValue(annotation,"id");
                for (String id:idList) {
                    addDependency(id);
                }
            }            
        }
    }

    
    /** */
    private void create() throws IOException {
        File jarFile = getJarFile(pluginDir);
        
    // jar
        PluginJar pluginJar = new PluginJar(jarFile);
        if (pluginJar.hasPluginSection() == false)
        {
            return;
        }
        
    // class
        String className = pluginJar.getClassName();
        Class<?> clazz = getPluginClass(className,jarFile);
        if (clazz == null) {
            return;
        }
        
    // dependencies
        findDependencies(clazz);
    
    // project
        pluginId = pluginJar.getPluginId();
    }

    /** */
    static File getJarFile(File pluginDir) {
        File buildDir = new File(pluginDir,"build");
        File[] jarFiles = buildDir.listFiles(new FileFilter() {
            /** */
            @Override
            public boolean accept(File file) {
                return file.isFile() && file.getName().endsWith(".jar");
            }
        });
        
    // there must be exactly one jar file
        if (jarFiles == null || jarFiles.length != 1) {
            return null;
        }
        
        return jarFiles[0];
    }

    /** */
    static File[] getLibs(File pluginDir) {
        File libsDir = new File(pluginDir,"libs");
        return libsDir.listFiles(new FileFilter() {
            /** */
            @Override
            public boolean accept(File file) {
                return file.isFile() && file.getName().endsWith(".jar");
            }
        });
    }
}
