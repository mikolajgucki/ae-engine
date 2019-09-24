package com.andcreations.ae.luadoc;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a group of modules.
 *
 * @author Mikolaj Gucki
 */
public class LuaDocGroup {
    /** The group name. */
    private String name;
    
    /** The modules in this group. */
    private List<LuaDocModule> modules = new ArrayList<LuaDocModule>();
    
    /**
     * Constructs a {@link LuaDocGroup}.
     *
     * @param name The group name.
     */
    public LuaDocGroup(String name) {
        this.name = name;
    }
    
    /**
     * Gets the group name.
     *
     * @return The group name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Adds a module to the group.
     *
     * @param module The module.
     */
    public void addModule(LuaDocModule module) {
        modules.add(module);
    }
    
    /**
     * Gets the modules belonging to this group.
     *
     * @return The modules.
     */
    public List<LuaDocModule> getModules() {
        return modules;
    }
    
    /** */
    private static LuaDocGroup findGroup(List<LuaDocGroup> groups,String name) {
        for (LuaDocGroup group:groups) {
            if (name == null && group.getName() == null) {
                return group;
            }
            if (name != null && name.equals(group.getName())) {
                return group;
            }
        }
        
        return null;
    }
    
    /**
     * Gets groups from modules.
     *
     * @param modules The modules.
     * @return The groups.
     */
    public static List<LuaDocGroup> getGroups(List<LuaDocModule> modules) {
        List<LuaDocGroup> groups = new ArrayList<LuaDocGroup>();

        for (LuaDocModule module:modules) {
            LuaDocGroup group = findGroup(groups,module.getGroup());
            if (group == null) {
                group = new LuaDocGroup(module.getGroup());
                groups.add(group);
            }
            
            group.addModule(module);
        }
        
        return groups;
    }
}