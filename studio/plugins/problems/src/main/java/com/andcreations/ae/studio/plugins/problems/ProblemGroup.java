package com.andcreations.ae.studio.plugins.problems;

import java.util.ArrayList;
import java.util.List;

import com.andcreations.resources.BundleResources;

/**
 * Represents a group of problems of the same severity.
 *
 * @author Mikolaj Gucki
 */
class ProblemGroup {
    /** */
    private BundleResources res =
        new BundleResources(ProblemGroup.class);       
    
    /** */
    private ProblemSeverity severity;
    
    /** */
    private List<Problem> problems = new ArrayList<>();
    
    /** */
    ProblemGroup(ProblemSeverity severity) {
        this.severity = severity;
    }
    
    /** */
    ProblemSeverity getSeverity() {
        return severity;
    }
    
    /** */
    void add(Problem problem) {
        if (severity != problem.getSeverity()) {
            throw new IllegalArgumentException("Invalid severity");
        }
        // TODO Insert alphabetically by description
        problems.add(problem);
    }
    
    /** */
    int remove(Problem problem) {
        int index = indexOf(problem);
        if (index >= 0) {
            problems.remove(problem);
        }
        
        return index;
    }
    
    /** */
    int size() {
        return problems.size();
    }
    
    /** */
    Problem get(int index) {
        return problems.get(index);
    }
    
    /** */
    int indexOf(Problem problem) {
        return problems.indexOf(problem);
    }
    
    /** */
    String getDescription() {
        String key = severity.name().toLowerCase();
        int size = size();
        if (size == 0) {
            key += ".0";
        }
        else if (size == 1) {
            key += ".1";
        }
        else {
            key += ".n";
        }
        
        return res.getStr(key,Integer.toString(size()));
    }
}