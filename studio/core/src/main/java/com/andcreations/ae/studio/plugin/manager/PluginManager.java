package com.andcreations.ae.studio.plugin.manager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.andcreations.ae.studio.jar.JarList;
import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugin.PluginJar;
import com.andcreations.resources.BundleResources;

/**
 * Responsible for managing plugins.
 *
 * @author Mikolaj Gucki
 */
public class PluginManager {
    /** The section of a JAR manifest with plugin information. */
    public static final String JAR_SECTION_PLUGIN = "AEStudio-Plugin";
    
    /** The attributes of a JAR manifest with plugin class name. */
    public static final String JAR_PLUGIN_CLASS_NAME = "Plugin-Class";
    
    /** */
    private static final int NO_EXIT_VALUE = -127;
    
    /** */
    private final BundleResources res =
        new BundleResources(PluginManager.class);    
    
    /** The configuration. */
    private PluginManagerCfg cfg;
    
    /** The directories with the extra plugins. */
    private List<File> extraPluginDirs = new ArrayList<>();
    
    /** The plugin storage. */
    private PluginStorage storage;
    
    /** The plugin starter. */
    private PluginStarter pluginStarter;
    
    /** The extra plugin starter. */
    private PluginStarter extraPluginStarter;
    
    /** The finalizers run after the plugins have been started. */
    private List<Runnable> pluginFinalizers = new ArrayList<>();
    
    /** The main class loader. */
    private ClassLoader defaultMainClassLoader;
    
    /** The default plugins. */
    List<PluginDesc> defaultPluginDescList;
    
    /** The listeners. */
    private static List<PluginManagerListener> listeners = new ArrayList<>();
    
    /** The class instance. */
    private static PluginManager instance;
    
    /**
     * Constructs a {@link PluginManager}.
     *
     * @param pluginDirs The directories with plugins.
     */
    private PluginManager(PluginManagerCfg cfg) {
        this.cfg = cfg;
    }
    
    /**
     * Gets the plugin storage.
     * 
     * @return The plugin storage.
     */
    PluginStorage getStorage() {
        return storage;
    }
    
    /** */
    private void createStorage() {
        storage = new PluginStorage();
        initStorage(cfg.getStorageDir());
    }
    
    /**
     * Initializes the storage.
     * 
     * @param storageDir The storage directory.
     */
    public static void initStorage(String storageDir) {
    	instance.storage.init(storageDir);
    }
    
    /** */
    private List<File> getJarFiles(String[] dirs) {
    // get JAR file list        
        JarList jarList = new JarList(dirs);
        List<File> jarFiles = jarList.getJarFiles();
        
        return jarFiles;
    }
    
    /** */
    private List<File> getJarFiles() {
        Log.info(res.getStr("found.jars"));
        
        List<File> jarFiles = getJarFiles(cfg.getPluginDirs());
    // log JAR file list
        for (File jarFile:jarFiles) {
            Log.info(res.getStr("found.jar",jarFile.getName()));
        }
        
        return jarFiles;
    }
    
    
    /** */
    private List<File> getExtraJarFiles() {
        Log.info(res.getStr("found.extra.jars"));
        
    // directories
        String[] dirs = new String[extraPluginDirs.size()];
        for (int index = 0; index < dirs.length; index++) {
            dirs[index] = extraPluginDirs.get(index).getAbsolutePath();
        }
        
        List<File> extraJarFiles = getJarFiles(dirs);
    // log JAR file list
        for (File jarFile:extraJarFiles) {
            Log.info(res.getStr("found.jar",jarFile.getName()));
        }
        
        return extraJarFiles;
    }
    
    /**
     * Finds the JAR files with plugins.
     * 
     * @param jarFiles The JAR files.
     * @return The plugin descriptors.
     */
    private List<PluginDesc> findPluginJars(List<File> jarFiles) {
        List<PluginDesc> pluginDescList = new ArrayList<>();
        
        for (File jarFile:jarFiles) {            
            PluginJar pluginJar = new PluginJar(jarFile);
            String className = null;
            try {
                if (pluginJar.hasPluginSection() == false)
                {
                    continue;
                }
                className = pluginJar.getClassName();
            } catch (IOException exception) {
                Log.fatal(res.getStr("failed.to.load.plugin",
                    jarFile.getAbsolutePath(),exception.getMessage()),
                    exception);
                continue;
            }
            
        // identifier
            String id = pluginJar.getPluginId();
            
        // package
            int indexOf = className.lastIndexOf('.');
            String pkgName = className.substring(0,indexOf);
            if (pkgName.equals(id) == false) {
                String error =
                    res.getStr("invalid.jar.name",jarFile.getAbsolutePath());
                Log.fatal(error);
                notifyFailedToStartPlugins(
                    new InvalidPluginJarNameException(error)); 
                continue;
            }
            
        // descriptor
            pluginDescList.add(new PluginDesc(id,className));
        }
        
        return Collections.unmodifiableList(pluginDescList);
    }
   
    /** */
    private URLClassLoader createPluginClassLoader(List<File> jarFiles,
		ClassLoader parentClassLoader) {
	//
        List<URL> urls = new ArrayList<>();
        for (File jarFile:jarFiles) {
            try {
                urls.add(jarFile.toURI().toURL());
            } catch (MalformedURLException exception) {
                notifyFailedToStartPlugins(exception);
                return null;
            }
        }
        
        return new URLClassLoader(urls.toArray(new URL[]{}),parentClassLoader);
    }
    
    /** */
    private void loadPlugins(List<PluginDesc> pluginDescList,
        ClassLoader pluginClassLoader,List<PluginDesc> loadedPluginDescList) {
    //
        PluginLoader pluginLoader = new PluginLoader(
            pluginClassLoader,pluginDescList,loadedPluginDescList);
        pluginLoader.loadPlugins();
    }
    
    /** */
    private PluginStarter startPlugins(List<PluginDesc> pluginDescList,
		List<PluginDesc> loadedPluginDescList) {
	//
        PluginStarter pluginStarter = new PluginStarter(
    		this,pluginDescList,loadedPluginDescList);
        pluginStarter.startPlugins();
        
        return pluginStarter;
    }
    
    /** */
    private void runPluginFinalizers() {
        for (Runnable finalizer:pluginFinalizers) {
            finalizer.run();
        }
    }    
    
    /** */
	private void doStartExtraPlugins() {
	// get JAR files and the related stuff
	    List<File> jarFiles = getExtraJarFiles();
	    List<PluginDesc> pluginDescList = findPluginJars(jarFiles);
	    ClassLoader extraClassLoader = createPluginClassLoader(
			jarFiles,defaultMainClassLoader);
	    
	// load, start
	    loadPlugins(pluginDescList,extraClassLoader,defaultPluginDescList);
	    extraPluginStarter = startPlugins(pluginDescList,defaultPluginDescList);        
	}

	/**
     * Loads and starts plugins.
     */
    private void doStart() {
    // storage
        createStorage();
        
    // get JAR files and the related stuff
        List<File> jarFiles = getJarFiles();
        defaultPluginDescList = findPluginJars(jarFiles);
        defaultMainClassLoader = createPluginClassLoader(
    		jarFiles,ClassLoader.getSystemClassLoader());
        
    // load, start
        loadPlugins(defaultPluginDescList,defaultMainClassLoader,null);
        pluginStarter = startPlugins(defaultPluginDescList,null);
        
    // start extra plugins
        doStartExtraPlugins();
    
    // run finalizers
        runPluginFinalizers();
    }
    
    /** */
    private void doStop(int exitValue) {
        extraPluginStarter.stopPlugins();
        pluginStarter.stopPlugins();
        
        if (exitValue != NO_EXIT_VALUE) {
            System.exit(exitValue);
        }
    }
    
    /** */
    static void notifyStartingPlugin(PluginDesc pluginDesc,int index,
        int count) {
    //
        synchronized (listeners) {
            for (PluginManagerListener listener:listeners) {
                listener.startingPlugin(pluginDesc,index,count);
            }
        }
    }

    /** */
    static void notifyFailedToStartPlugins(Exception exception) {
        synchronized (listeners) {
            for (PluginManagerListener listener:listeners) {
                listener.failedToStartPlugins(exception);
            }
        }
    }

    
    /** */
    static void notifyPluginsStarted() {
        synchronized (listeners) {
            for (PluginManagerListener listener:listeners) {
                listener.pluginsStarted();
            }
        }
    }
    
    /** */
    static void notifyFailedToStartPlugin(PluginDesc plugin,
        Exception exception) {
    //
        synchronized (listeners) {
            for (PluginManagerListener listener:listeners) {
                listener.failedToStartPlugin(plugin,exception);
            }
        }
        
    }
    
    /** */
    public static void addPluginManagerListener(
        PluginManagerListener listener) {
    //
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    public static void addPluginFinalizer(Runnable finalizer) {
        checkPluginManagerRunning();       
        instance.pluginFinalizers.add(finalizer);        
    }
    
    /** */
    public static void addExtraPluginDir(File extraPluginDir) {
        instance.extraPluginDirs.add(extraPluginDir);
    }
    
    /** */
    private static void checkPluginManagerRunning() {
        if (instance == null) {
            throw new IllegalStateException("Plugin manager not running");
        }         
    }
    
    /**
     * Starts the plugin manager and plugins.
     * 
     * @param cfg The configuration.
     */
    public static void start(PluginManagerCfg cfg) {
        if (instance != null) {
            throw new IllegalStateException("Plugin manager already running");
        }
        
        instance = new PluginManager(cfg);        
        instance.doStart();
    }
    
    /**
     * Stops the plugins and the manager. 
     *
     * @param exitValue The exit value.
     */
    public static void stop(int exitValue) {
        checkPluginManagerRunning();       
        instance.doStop(exitValue);
    }
    
    /**
     * Stops the plugins and manager in a separate thread.
     *
     * @param exitValue The exit value.
     */
    public static void postStop(final int exitValue) {
        Runnable pluginStopRunnable = new Runnable() {
            public void run() {
                PluginManager.stop(exitValue);
            }
        };
        Thread thread = new Thread(pluginStopRunnable);
        thread.start();        
    }
    
    /**
     * Stops the plugins and manager in a separate thread.
     */
    public static void postStop() {
        postStop(NO_EXIT_VALUE);
    }
}