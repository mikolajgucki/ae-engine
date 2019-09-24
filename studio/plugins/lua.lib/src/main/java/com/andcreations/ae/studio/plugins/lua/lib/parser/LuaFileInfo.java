package com.andcreations.ae.studio.plugins.lua.lib.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class LuaFileInfo {
    /** The functions. */
    private List<Func> funcs = new ArrayList<>();
    
    /** The local functions. */
    private List<Func> localFuncs = new ArrayList<>();
    
    /** The assignments. */
    private List<Assignment> assignments = new ArrayList<>();
    
    /** The local assignments. */
    private List<Assignment> localAssignments = new ArrayList<>();
    
    /** */
    void addFunc(Func func) {
        funcs.add(func);
    }
    
    /** */
    public List<Func> getFuncs() {
        return Collections.unmodifiableList(funcs);
    }
    
    /** */
    void addLocalFunc(Func func) {
        localFuncs.add(func);
    }
    
    /** */
    public List<Func> getLocalFuncs() {
        return Collections.unmodifiableList(localFuncs);
    }
    
    /** */
    void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }   
    
    /** */
    public List<Assignment> getAssignments() {
        return Collections.unmodifiableList(assignments);
    }
    
    
    /** */
    void addLocalAssignment(Assignment assignment) {
        localAssignments.add(assignment);
    }   
    
    /** */
    public List<Assignment> getLocalAssignments() {
        return Collections.unmodifiableList(localAssignments);
    }    
}