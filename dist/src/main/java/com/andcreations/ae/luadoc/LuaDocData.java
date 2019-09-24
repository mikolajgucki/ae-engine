package com.andcreations.ae.luadoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.andcreations.resources.BundleResources;
import com.andcreations.resources.StrResources;

/**
 * Contains the parsed data.
 *
 * @author Mikolaj Gucki
 */
public class LuaDocData {
    /** The string resources. */
    private static StrResources res = new BundleResources(LuaDocData.class);     
    
    /** The modules. */
    private List<LuaDocModule> modules;
    
    /** The groups of modules. */
    private List<LuaDocGroup> groups;
    
    /**
     * Constructs a {@link LuaDocData}.
     *
     * @param modules The modules.     
     */
    public LuaDocData(List<LuaDocModule> modules) {
        this.modules = modules;
        create();
    }
    
    /** */
    private void create() {
        groups = LuaDocGroup.getGroups(modules);
        sortModules(modules);
        sortGroups();
        sortGroupModules();
    }
    
    /** */
    private void sortModules(List<LuaDocModule> modules) {
        Collections.sort(modules,new Comparator<LuaDocModule>() {
            /** */
            @Override
            public int compare(LuaDocModule a,LuaDocModule b) {
                return a.getName().compareToIgnoreCase(b.getName());
            }
                
            /** */
            @Override
            public boolean equals(Object obj) {
                return obj == this;
            }
        });        
    }
    
    /** */
    private void sortGroups() {
        Collections.sort(groups,new Comparator<LuaDocGroup>() {
            @Override
            public int compare(LuaDocGroup a,LuaDocGroup b) {
                if (a.getName() == null && b.getName() != null) {
                    return -1;
                }
                if (a.getName() != null && b.getName() == null) {
                    return 1;
                }
                
                return a.getName().compareToIgnoreCase(b.getName());
            }
                
            @Override
            public boolean equals(Object obj) {
                return obj == this;
            }
        });        
    }
    
    /** */
    private void sortGroupModules() {
        for (LuaDocGroup group:groups) {
            sortModules(group.getModules());
        }
    }
    
    /**
     * Gets the modules.
     *
     * @return The modules.
     */
    public List<LuaDocModule> getModules() {
        return modules;
    }
    
    /**
     * Gets the groups.
     *
     * @return The groups.
     */
    public List<LuaDocGroup> getGroups() {
        return groups;
    }
    
    /**
     * Gets a module by name.
     *
     * @param name The name.
     * @return The module or <code>null</code> if there is no such module.
     */
    private LuaDocModule getModule(String name) {
        for (LuaDocModule module:modules) {
            if (module.getName().equals(name) == true) {
                return module;
            }
        }
        
        return null;
    }
    
    /**
     * Validates that the referenced supermodules exist.
     *
     * @throws LuaDocException if a supermodule does not exist.
     */
    public void validateSuperModules() throws LuaDocException {
        for (LuaDocModule module:modules) {
            if (module.getSuperModuleName() != null &&
                getModule(module.getSuperModuleName()) == null) {
            //
                throw new LuaDocException(res.getStr("unknown.super.module",
                    module.getSuperModuleName(),module.getSrcFilename(),
                    Integer.toString(module.getSrcLine())));
            }
        }
    }
    
    /**
     * Gets the supermodules of a module.
     *
     * @param module The module.
     * @return The supermodules.
     */
    public List<LuaDocModule> getSuperModules(LuaDocModule module) {
        List<LuaDocModule> supermodules = new ArrayList<LuaDocModule>();
        
        while (module.getSuperModuleName() != null) {
            module = getModule(module.getSuperModuleName());
            supermodules.add(module);
        }
        
        return supermodules;
    }
    
    /**
     * Gets the direct submodules.
     *
     * @param module The module.
     * @return The direct submodules. 
     */
    public List<LuaDocModule> getDirectSubModules(LuaDocModule module) {
        List<LuaDocModule> submodules = new ArrayList<LuaDocModule>();
        
        for (LuaDocModule itr:modules) {
            if (module.getName().equals(itr.getSuperModuleName()) == true) {
                submodules.add(itr);
            }
        }
        
        return submodules;
    }
    
    /**
     * Checks if a module has variables (including supermodules).
     *
     * @param module The module to check.
     * @return <code>true</code> if has variables, <code>false</code> otherwise.
     */
    public boolean hasVars(LuaDocModule module) {
        List<LuaDocModule> supermodules = getSuperModules(module);        
        for (LuaDocModule supermodule:supermodules) {
            if (supermodule.hasVars() == true) {
                return true;
            }
        }
        
        return module.hasVars();
    }
    
    /**
     * Checks if a module has functions (including supermodules).
     *
     * @param module The module to check.
     * @return <code>true</code> if has functions, <code>false</code> otherwise.
     */
    public boolean hasFuncs(LuaDocModule module) {
        List<LuaDocModule> supermodules = getSuperModules(module);        
        for (LuaDocModule supermodule:supermodules) {
            if (supermodule.hasFuncs() == true) {
                return true;
            }
        }
        
        return module.hasFuncs();        
    }
}