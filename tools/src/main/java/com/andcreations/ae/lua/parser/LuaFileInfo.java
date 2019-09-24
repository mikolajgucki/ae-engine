package com.andcreations.ae.lua.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class LuaFileInfo {
    /** The functions. */
    private List<LuaFunc> funcs = new ArrayList<>();
    
    /** The local functions. */
    private List<LuaLocalFunc> localFuncs = new ArrayList<>();
    
    /** The assignments. */
    private List<LuaAssignment> assignments = new ArrayList<>();
    
    /** The local assignments. */
    private List<LuaLocalAssignment> localAssignments = new ArrayList<>();
    
    /** The returns. */
    private List<LuaReturn> returns = new ArrayList<>();
    
    /** The single line comments. */
    private List<LuaSingleLineComment> singleLineComments = new ArrayList<>();
    
    /** */
    void addFunc(LuaFunc func) {
        funcs.add(func);
    }
    
    /** */
    public List<LuaFunc> getFuncs() {
        return Collections.unmodifiableList(funcs);
    }
    
    /** */
    void addLocalFunc(LuaLocalFunc func) {
        localFuncs.add(func);
    }
    
    /** */
    public List<LuaLocalFunc> getLocalFuncs() {
        return Collections.unmodifiableList(localFuncs);
    }
    
    /** */
    void addAssignment(LuaAssignment assignment) {
        assignments.add(assignment);
    }   
    
    /** */
    public List<LuaAssignment> getAssignments() {
        return Collections.unmodifiableList(assignments);
    }
    
    
    /** */
    void addLocalAssignment(LuaLocalAssignment assignment) {
        localAssignments.add(assignment);
    }   
    
    /** */
    public List<LuaLocalAssignment> getLocalAssignments() {
        return Collections.unmodifiableList(localAssignments);
    }    
    
    /** */
    void addReturn(LuaReturn rtrn) {
        returns.add(rtrn);
    }
    
    /** */
    public List<LuaReturn> getReturns() {
        return Collections.unmodifiableList(returns);
    }
    
    /** */
    void addSingleLineComment(LuaSingleLineComment comment) {
        singleLineComments.add(comment);
    }
    
    /** */
    public List<LuaSingleLineComment> getSingleLineComments() {
        return singleLineComments;
    }
    
    /** */
    public List<LuaElement> getElements() {
        List<LuaElement> elements = new ArrayList<>();
        
    // add all
        elements.addAll(funcs);
        elements.addAll(localFuncs);
        elements.addAll(assignments);
        elements.addAll(localAssignments);
        elements.addAll(returns);
        elements.addAll(singleLineComments);
        
    // sort
        Collections.sort(elements,new LuaElementComparator());
        
        return elements;
    }
    
    /** */
    public LuaFunc getFuncByName(String name) {
        for (LuaFunc func:funcs) {
            if (func.getName().equals(name) == true) {
                return func;
            }
        }
        
        return null;
    }
}