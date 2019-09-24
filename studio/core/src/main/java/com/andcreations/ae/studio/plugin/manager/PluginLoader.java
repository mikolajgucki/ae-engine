package com.andcreations.ae.studio.plugin.manager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugin.Finalizer;
import com.andcreations.ae.studio.plugin.Initializer;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.PluginUtil;
import com.andcreations.ae.studio.plugin.Required;
import com.andcreations.ae.studio.plugin.StartAfter;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;
import com.andcreations.ae.studio.plugin.StartBefore;
import com.andcreations.ae.studio.plugin.StartBeforeAndRequire;
import com.andcreations.lang.AnnotationUtil;
import com.andcreations.resources.BundleResources;

/**
 * Responsible for loading plugins.
 *
 * @author Mikolaj Gucki
 */
class PluginLoader {
    /** */
    private final BundleResources res =
        new BundleResources(PluginLoader.class);    
        
    /** The plugin class loader. */
    private ClassLoader pluginClassLoader;
    
    /** The plugin descriptors. */
    private List<PluginDesc> pluginDescList;    
    
    /** The list of the already loaded plugins. */
    private List<PluginDesc> loadedPluginDescList;
    
    /**
     * Constructs a {@link PluginLoader}.
     *
     * @param manager The plugin manager.
     * @param pluginClassLoader The plugin class loader.
     * @param loadedPluginDescList The list of the already loaded plugins.
     */
    PluginLoader(ClassLoader pluginClassLoader,
        List<PluginDesc> pluginDescList,List<PluginDesc> loadedPluginDescList) {
    //
        this.pluginDescList = pluginDescList;
        this.pluginClassLoader = pluginClassLoader;
        this.loadedPluginDescList = loadedPluginDescList;
    }
    
    /** */
    private PluginDesc getPluginDesc(String id) {
        for (PluginDesc pluginDesc:pluginDescList) {
            if (pluginDesc.getId().equals(id) == true) {
                return pluginDesc;
            }
        }
        if (loadedPluginDescList != null) {
        	for (PluginDesc pluginDesc:loadedPluginDescList) {
	            if (pluginDesc.getId().equals(id) == true) {
	                return pluginDesc;
	            }
	        }
        }
        return null;
    }
    
    /** */
    private void getPluginClass(PluginDesc pluginDesc) {
    // get class
        Class<?> clazz = null;
        try {
            clazz = Class.forName(
                pluginDesc.getClassName(),true,pluginClassLoader);
        } catch (ClassNotFoundException exception) {
            Log.fatal(res.getStr("plugin.class.not.found",
                pluginDesc.getClassName()),exception);
            PluginManager.notifyFailedToStartPlugin(pluginDesc,exception);
        }
        
    // must be a plugin class
        if (PluginUtil.isPluginClass(clazz) == false) {
            String error =
                res.getStr("not.a.plugin.class",pluginDesc.getClassName());
            Log.fatal(error);
            PluginManager.notifyFailedToStartPlugin(pluginDesc,
                new NotPluginClassException(error));
            return;
        }

    // initializer
        if (clazz.getAnnotation(Initializer.class) != null) {
            pluginDesc.setInitializer(true);
        }

    // finalizer
        if (clazz.getAnnotation(Finalizer.class) != null) {
            pluginDesc.setFinalizer(true);
        }
        
        pluginDesc.setPluginClass(clazz);    
    }
    
    /** */
    private void getStateClass(PluginDesc pluginDesc) {
        Class<?> pluginClass = pluginDesc.getPluginClass();
        
    // getState()
        Class<?> getStateClass = null;
        try {
            Method getStateMethod = pluginClass.getMethod("getState");
            getStateClass = getStateMethod.getReturnType();            
        } catch (Exception exception) {
            Log.fatal(exception.toString(),exception);
            PluginManager.notifyFailedToStartPlugin(pluginDesc,exception);
            return;            
        }
        
        if (getStateClass.equals(PluginState.class)) {
            return;
        }
        
    // setState()
        try {
            pluginClass.getMethod("setState",getStateClass);
        } catch (NoSuchMethodException exception) {
            Log.fatal(res.getStr("invalid.set.state.param",pluginDesc.getId()),
                exception);
            PluginManager.notifyFailedToStartPlugin(pluginDesc,exception);
            return;            
        } catch (SecurityException exception) {
            Log.fatal(exception.toString(),exception);
            PluginManager.notifyFailedToStartPlugin(pluginDesc,exception);
            return;            
        }
        
        pluginDesc.setStateClass(getStateClass);
    }
    
    /**
     * Loads a plugin.
     *
     * @param pluginDesc The plugin descriptor.
     */
    private void getPluginDependencies(PluginDesc pluginDesc) {
    // for each field
        Field[] fields = pluginDesc.getPluginClass().getDeclaredFields();
        for (Field field:fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation:annotations) {
            // required
                if (AnnotationUtil.is(annotation,Required.class)) {
                    PluginDependency dependency = new PluginDependency(
                        (String)AnnotationUtil.getValue(annotation,"id"),field);
                    Log.info(res.getStr("requires",dependency.getPluginId()));
                    pluginDesc.addDependency(dependency);
                }
            }
        }
    }
    
    /** */
    private void instantiatePlugin(PluginDesc pluginDesc) {
        Object instance = null;
        try {
            instance = pluginDesc.getPluginClass().newInstance();
        } catch (Exception exception) {
            Log.fatal(res.getStr("failed.to.instantiate.plugin",
                pluginDesc.getId(),exception.getMessage()),exception);
            PluginManager.notifyFailedToStartPlugin(pluginDesc,exception);
            return;            
        }      
        
        pluginDesc.setInstance(instance);
    }     
    
    /** */
    private void addStartAfter(PluginDesc pluginDesc,String[] idList) {
        for (String id:idList) {
            if (getPluginDesc(id) == null) {
                String error = 
                    res.getStr("no.start.after",id,pluginDesc.getId());
                Log.fatal(error);
                PluginManager.notifyFailedToStartPlugin(pluginDesc,
                    new PluginDependencyException(error));
                return; 
            }
            pluginDesc.addStartDependency(id);
        }
    }
    
    /** */
    private void addStartBefore(PluginDesc pluginDesc,String[] idList) {
        for (String id:idList) {
            PluginDesc beforePluginDesc = getPluginDesc(id);
            if (beforePluginDesc == null) {
                String error = 
                    res.getStr("no.start.before",id,pluginDesc.getId());
                Log.fatal(error);
                PluginManager.notifyFailedToStartPlugin(pluginDesc,
                    new PluginDependencyException(error));
                return; 
            }
            beforePluginDesc.addStartDependency(pluginDesc.getId());
        }
    }
    
    /** */
    private void getPluginStartOrder(PluginDesc pluginDesc) {
        Annotation[] annotations = pluginDesc.getPluginClass().getAnnotations();
        for (Annotation annotation:annotations) {
        // start after
            if (AnnotationUtil.is(annotation,StartAfter.class) ||
                AnnotationUtil.is(annotation,StartAfterAndRequire.class)) {
            //
                String[] idList =
                    (String[])AnnotationUtil.getValue(annotation,"id");
                for (String id:idList) {
                    Log.info(res.getStr("start.after",id));
                }
                addStartAfter(pluginDesc,idList);
            }            
            
        // start before
            if (AnnotationUtil.is(annotation,StartBefore.class) ||
                AnnotationUtil.is(annotation,StartBeforeAndRequire.class)) {
            //
                String[] idList =
                    (String[])AnnotationUtil.getValue(annotation,"id");
                for (String id:idList) {
                    Log.info(res.getStr("start.before",id));
                }
                addStartBefore(pluginDesc,idList);
            }            
        }
    }
    
    /** */
    private void injectDependency(PluginDesc pluginDesc,
        PluginDependency dependency) {
    // get dependency
        Field field = dependency.getField();
        PluginDesc dependencyPluginDesc = getPluginDesc(
            dependency.getPluginId());
        if (dependencyPluginDesc == null) {
            String error = res.getStr("no.dependency",dependency.getPluginId(),
                pluginDesc.getId());
            PluginManager.notifyFailedToStartPlugin(pluginDesc,
                new PluginDependencyException(error));
            return; 
        }
    
    // inject
        Object instance = pluginDesc.getInstance();
        field.setAccessible(true);
        try {
            field.set(instance,dependencyPluginDesc.getInstance());
        } catch (Exception exception) {
            Log.fatal(res.getStr("failed.to.inject",dependency.getPluginId(),
                pluginDesc.getId(),exception.getMessage()),exception);
            PluginManager.notifyFailedToStartPlugin(pluginDesc,exception);
            return; 
        }
    }
    
    /** */
    private void injectDependencies(PluginDesc pluginDesc) {
        for (PluginDependency dependency:pluginDesc.getDependencies()) {
            injectDependency(pluginDesc,dependency);
        }
    }      
    
    /** */
    void loadPlugins() {
        Log.info(res.getStr("loading.plugins"));
    // for each plugin
        for (PluginDesc pluginDesc:pluginDescList) {
            Log.info(res.getStr("loading.plugin",pluginDesc.getId()));
            getPluginClass(pluginDesc);
            getStateClass(pluginDesc);
            instantiatePlugin(pluginDesc);
        }
        
        Log.info(res.getStr("resolving.dependencies"));
    // for each plugin
        for (PluginDesc pluginDesc:pluginDescList) {
            Log.info(res.getStr("resolving.plugin.dependencies",
                pluginDesc.getId()));            
            if (pluginDesc.isInitializer() == true) {
                Log.info(res.getStr("initializer"));
            }
            if (pluginDesc.isFinalizer() == true) {
                Log.info(res.getStr("finalizer"));
            }
            getPluginDependencies(pluginDesc);
            getPluginStartOrder(pluginDesc);
            injectDependencies(pluginDesc);
        }
    }
}