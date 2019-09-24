package com.andcreations.ae.studio.plugin.manager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.resources.BundleResources;

/**
 * Responsible for starting plugins.
 *
 * @author Mikolaj Gucki
 */
class PluginStarter {
    /** */
    private class StartEntry {
        /** */
        private PluginDesc plugin;
        
        /** */
        private List<StartDependency> dependencies = new ArrayList<>();
        
        /** */
        private StartEntry(PluginDesc plugin) {
            this.plugin = plugin;
            this.dependencies.addAll(plugin.getStartDependencies());
        }
        
        /** */
        private void removeDependency(String pluginId) {
            while (true) {
                StartDependency toRemove = null;
                for (StartDependency dependency:dependencies) {
                    if (dependency.getPluginId().equals(pluginId))
                    {
                        toRemove = dependency;
                        break;
                    }
                }
                
                if (toRemove == null) {
                    break;
                }
                
                dependencies.remove(toRemove);
            }
        }
    }
    
    /** */
    private static final String PLUGIN_START_METHOD_NAME = "start";
    
    /** */
    private static final String PLUGIN_CAN_STOP_METHOD_NAME = "canStop";
    
    /** */
    private static final String PLUGIN_STOP_METHOD_NAME = "stop";
    
    /** */
    private final BundleResources res =
        new BundleResources(PluginStarter.class);   
        
    /** The plugin manager. */
    private PluginManager manager;
    
    /** The plugin descriptors. */
    private List<PluginDesc> pluginDescList; 
    
    /** The list of the already loaded plugins. */
    private List<PluginDesc> loadedPluginDescList;    
    
    /** The start order. */
    private List<PluginDesc> startOrder;
        
    /**
     * Constructs a {@link PluginStarter}.
     *
     * @param manager The plugin manager.
     * @param pluginDescList The plugin descriptors.
     * @param loadedPluginDescList The list of the already loaded plugins.
     */    
    PluginStarter(PluginManager manager,List<PluginDesc> pluginDescList,
		List<PluginDesc> loadedPluginDescList) {
	//
        this.manager = manager;
        this.pluginDescList = pluginDescList;
        this.loadedPluginDescList = loadedPluginDescList;
    }

    /** */
    private boolean isAlreadyLoaded(String pluginId) {
    	if (loadedPluginDescList == null) {
    		return false;
    	}
    	
    	for (PluginDesc plugin:loadedPluginDescList) {
    		if (pluginId.equals(plugin.getId()) == true) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /** */
    private String cycleToString(String topId,Stack<String> stack) {
        StringBuilder str = new StringBuilder();
        str.append(topId);
        for (String id:stack) {
            str.append(" <- " + id);
        }
        
        return str.toString();
    }
    
    /** */
    private void findCycle(List<StartEntry> entries,StartEntry entry,
        Stack<String> stack) throws CycleException {
    //
        if (stack.contains(entry.plugin.getId())) {
            throw new CycleException(res.getStr("cycle",
                cycleToString(entry.plugin.getId(),stack)));
        }
        stack.push(entry.plugin.getId());
        for (StartDependency dependency:entry.dependencies) {
            StartEntry dependencyEntry = null;
            for (StartEntry ientry:entries) {
                if (ientry.plugin.getId().equals(dependency.getPluginId())) {
                    dependencyEntry = ientry;
                    break;
                }
            }
            boolean isDependencyLoaded =
        		isAlreadyLoaded(dependency.getPluginId());
            
            if (dependencyEntry == null && isDependencyLoaded == false) {
                throw new CycleException(res.getStr("cycle.unknown.plugin",
                    dependency.getPluginId()));
            }
            
            if (isDependencyLoaded == false) { 
            	findCycle(entries,dependencyEntry,stack);
            }
        }
        
        stack.pop();
    }

    
    /** */
    private void findCycle(List<StartEntry> entries) throws CycleException {
        for (StartEntry entry:entries) {
            findCycle(entries,entry,new Stack<String>());
        }
    }
    
    /** */
    private void sortById(List<StartEntry> entries) {
        Collections.sort(entries,new Comparator<StartEntry>() {
            /** */
            @Override
            public int compare(StartEntry a,StartEntry b) {
                return a.plugin.getId().compareTo(b.plugin.getId());
            }
        });
    }
    
    /** */
    private List<PluginDesc> determineStartOrder(List<PluginDesc> plugins,
        List<PluginDesc> ignorePlugins) throws CycleException {
    //
        List<StartEntry> entries = new ArrayList<>();
        for (PluginDesc plugin:plugins) {
            StartEntry entry = new StartEntry(plugin);
            if (ignorePlugins != null) {
                for (PluginDesc ignorePlugin:ignorePlugins) {
                    entry.removeDependency(ignorePlugin.getId());
                }
            }
            if (loadedPluginDescList != null) {
	            for (PluginDesc loadedPlugin:loadedPluginDescList) {
	            	entry.removeDependency(loadedPlugin.getId());
	            }
            }
            entries.add(entry);            
        }
        
    // find cycles
        findCycle(entries);
        
        List<PluginDesc> startOrder = new ArrayList<>();
        while (entries.isEmpty() == false) {
        // find the roots (without dependencies)
            List<StartEntry> roots = new ArrayList<>();
            for (StartEntry entry:entries) {
                if (entry.dependencies.isEmpty() == true) {
                    roots.add(entry);
                }
            }
            
            if (roots.isEmpty() == true) {
            	// TODO Failed to determine start order.
            }
            
        // sort
            sortById(entries);
            
        // remove the roots
            for (StartEntry root:roots) {
                entries.remove(root);
                startOrder.add(root.plugin);
            }
            
            for (StartEntry entry:entries) {
                for (StartEntry root:roots) {
                    entry.removeDependency(root.plugin.getId());
                }
            }
        }
        
        return startOrder;
    }
    
    /** */
    private List<PluginDesc> determineStartOrder() throws CycleException {
        List<PluginDesc> initializers = new ArrayList<>();
        List<PluginDesc> inners = new ArrayList<>();
        List<PluginDesc> finalizers = new ArrayList<>();
        
    // build the lists of initializers, inners, finalizers
        for (PluginDesc plugin:pluginDescList) {
            if (plugin.isInitializer() == true &&
                plugin.isFinalizer() == true) {
            //
                String error = res.getStr("initializer.and.finalizer.error",
                    plugin.getId());
                Log.error(error);
                PluginManager.notifyFailedToStartPlugin(plugin,
                    new PluginDependencyException(error));
                continue;
            }
            
            if (plugin.isInitializer() == true) {
                initializers.add(plugin);
            }
            else if (plugin.isFinalizer() == true) {
                finalizers.add(plugin);
            }
            else {
                inners.add(plugin);
            }
        }    
        
    // non-initializers
        List<PluginDesc> nonInitializers = new ArrayList<>();
        nonInitializers.addAll(inners);
        nonInitializers.addAll(finalizers);
        
    // non-inners
        List<PluginDesc> nonInners = new ArrayList<>();
        nonInners.addAll(initializers);
        nonInners.addAll(finalizers);
        
    // non-finalizers
        List<PluginDesc> nonFinalizers = new ArrayList<>();
        nonFinalizers.addAll(initializers);
        nonFinalizers.addAll(inners);
        
    // verify non-initializer plugin does not require a initializer to be started
        for (PluginDesc nonInitializer:nonInitializers) {
            for (StartDependency dep:nonInitializer.getStartDependencies()) {
                for (PluginDesc initializer:initializers) {
                    if (initializer.getId().equals(dep.getPluginId())) {
                        String error =
                            res.getStr("non.initializer.dependency.error",
                            nonInitializer.getId(),initializer.getId());
                        Log.warning(error);
                    }
                }
            }
        }
        
    // verify non-finalizer plugin does not require a finalizer to be started
        for (PluginDesc nonFinalizer:nonFinalizers) {
            for (StartDependency dep:nonFinalizer.getStartDependencies()) {
                for (PluginDesc finalizer:finalizers) {
                    if (finalizer.getId().equals(dep.getPluginId())) {
                        String error =
                            res.getStr("non.finalizer.dependency.error",
                            nonFinalizer.getId(),finalizer.getId());
                        Log.error(error);
                        PluginManager.notifyFailedToStartPlugin(nonFinalizer,
                            new PluginDependencyException(error));
                    }
                }
            }
        }
        
    // determine the start order
        List<PluginDesc> startOrder = new ArrayList<>();
        startOrder.addAll(determineStartOrder(initializers,nonInitializers));
        startOrder.addAll(determineStartOrder(inners,nonInners));
        startOrder.addAll(determineStartOrder(finalizers,nonFinalizers));
        
        return startOrder;
    }
    
    /** */
    private void setStorageDir(PluginDesc plugin) {
        File storageDir = manager.getStorage().getPluginDir(plugin);
        
        try {
            Method method = plugin.getPluginClass().getMethod(
                "setStorageDir",File.class);
            method.invoke(plugin.getInstance(),storageDir);
        } catch (Exception exception) {
            Log.fatal(res.getStr("failed.to.restore.state",plugin.getId(),
                exception.toString()),exception);
            PluginManager.notifyFailedToStartPlugin(plugin,exception);
            return;
        }
    }
    
    /** */
    private void restoreState(PluginDesc plugin) {
        if (plugin.getStateClass() == null) {
            return;
        }
        
        Object state = null;
        try {
            state = manager.getStorage().restoreState(plugin);
        } catch (IOException exception) {
            Log.fatal(res.getStr("failed.to.restore.state",plugin.getId(),
                exception.toString()),exception);
            PluginManager.notifyFailedToStartPlugin(plugin,exception);
            return;
        }
        
        try {
            Method method = plugin.getPluginClass().getMethod("setState",
                plugin.getStateClass());
            method.invoke(plugin.getInstance(),state);
        } catch (Exception exception) {
            Log.fatal(res.getStr("failed.to.restore.state",plugin.getId(),
                exception.toString()),exception);
            PluginManager.notifyFailedToStartPlugin(plugin,exception);
            return;
        }
    }    
    
    /** */
    private void startPlugin(PluginDesc plugin) {
        Log.info(res.getStr("starting.plugin",plugin.getId()));
        
        try {
            Method startMethod = plugin.getPluginClass().getMethod(
                PLUGIN_START_METHOD_NAME);
            startMethod.invoke(plugin.getInstance());
        } catch (Exception exception) {
            Log.error(res.getStr("failed.to.start.plugin",plugin.getId(),
                exception.toString()),exception);
            PluginManager.notifyFailedToStartPlugin(plugin,exception);
            return;
        }
    }
    
    /** */  
    void startPlugins() {
    // start order
        Log.info(res.getStr("start.order"));
        try {
            startOrder = determineStartOrder();
        } catch (CycleException exception) {
            PluginManager.notifyFailedToStartPlugins(exception);
            return;
        }
    // for each plugin
        for (PluginDesc plugin:startOrder) {
            Log.info(res.getStr("start.order.plugin",plugin.getId()));
        }
        
    // start
        Log.info(res.getStr("starting.plugins"));
        int count = startOrder.size();
    // for each plugin
        for (int index = 0; index < count; index++) {
            PluginDesc plugin = startOrder.get(index);
            PluginManager.notifyStartingPlugin(plugin,index,count);
            setStorageDir(plugin);
            restoreState(plugin);
            startPlugin(plugin);
        }
        PluginManager.notifyPluginsStarted();
    }
    
    /** */
    private void storeState(PluginDesc plugin) {
        try {
            Method getStateMethod =
                plugin.getPluginClass().getMethod("getState");
            Object state = getStateMethod.invoke(plugin.getInstance());
            manager.getStorage().storeState(plugin,state);
        } catch (Exception exception) {
            Log.error(res.getStr("failed.to.store.state",plugin.getId(),
                exception.toString()),exception);
        }
    }
    
    /** */
    private boolean canStopPlugin(PluginDesc plugin) {
	    try {
	        Method canStopMethod = plugin.getPluginClass().getMethod(
	            PLUGIN_CAN_STOP_METHOD_NAME);
	        Boolean result = (Boolean)canStopMethod.invoke(
        		plugin.getInstance());
	        return result.booleanValue();
	    } catch (NoSuchMethodException exception) {
	    	return true;
	    } catch (Exception exception) {
	        Log.fatal(res.getStr("failed.to.stop.plugin",plugin.getId(),
	            exception.toString()),exception);
	        System.exit(-1);
	    }
	    return true;
    }
    
    /** */
    private boolean canStopPlugins() {
        Log.info(res.getStr("checking.if.can.stop.plugins"));
    // for each plugin
        for (int index = startOrder.size() - 1; index >= 0; index--) {
            PluginDesc plugin = startOrder.get(index);
            if (canStopPlugin(plugin) == false) {
            	return false;
            }
        }
    	
    	return true;
    }
    
    /** */
	private void stopPlugin(PluginDesc plugin) {
	    Log.info(res.getStr("stopping.plugin",plugin.getId()));
	
	    try {
	        Method stopMethod = plugin.getPluginClass().getMethod(
	            PLUGIN_STOP_METHOD_NAME);
	        stopMethod.invoke(plugin.getInstance());
	    } catch (Exception exception) {
	        Log.fatal(res.getStr("failed.to.stop.plugin",plugin.getId(),
	            exception.toString()),exception);
	        System.exit(-1);
	    }
	    
	    if (plugin.getStateClass() != null) {
	        storeState(plugin);
	    }    
	}

	/** */
    void stopPlugins() {
    // check if the plugins can be stopped
        if (canStopPlugins() == false) {
        	return;
        }
        
        Log.info(res.getStr("stopping.plugins"));
    // for each plugin
        for (int index = startOrder.size() - 1; index >= 0; index--) {
            PluginDesc plugin = startOrder.get(index);
            stopPlugin(plugin);
        }
    }
}