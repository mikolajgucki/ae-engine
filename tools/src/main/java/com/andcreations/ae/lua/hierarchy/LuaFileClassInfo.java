package com.andcreations.ae.lua.hierarchy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.andcreations.ae.issue.Issue;

/**
 * @author Mikolaj Gucki
 */
public class LuaFileClassInfo {
    /** */
    private boolean classFlag;
    
    /** */
    private int subclassLine;
    
    /** */
    private String superclassName;
    
    /** */
    private List<Issue> issues = new ArrayList<>();
    
    /** */
    public boolean isClass() {
        return classFlag;
    }
    
    /** */
    void setClass() {
        classFlag = true;
    }
        
    /** */
    public boolean isSubclass() {
        return superclassName != null;
    }
    
    /** */
    public int getSubclassLine() {
        return subclassLine;
    }
    
    /** */
    public String getSuperclassName() {
        return superclassName;
    }
    
    /** */
    void setSuperclass(int subclassLine,String superclassName) {
        setClass();
        this.subclassLine = subclassLine;
        this.superclassName = superclassName;
    }
    
    /** */
    public List<Issue> getIssues() {
        return Collections.unmodifiableList(issues);
    }
    
    /** */
    void addIssue(Issue issue) {
        issues.add(issue);
    }
        
    /** */
    @Override
    public String toString() {
        if (superclassName != null) {
            return "subclass of " + superclassName;
        }
        else if (classFlag == true) {
            return "class";
        }            
        
        return "no hierarchy info";
    }    
}