package com.andcreations.ae.eclipse;

import java.util.List;
import java.util.Stack;

/**
 * @author Mikolaj Gucki
 */
class CycleFinder {
    /** */
    private List<DependencyEntry> entries;
    
    /** */
    private DependencyEntry findEntry(String id) {
        for (DependencyEntry entry:entries) {
            if (id.equals(entry.getId()) == true) {
                return entry;
            }
        }
        return null;
    }
    
    /** */
    private String stackToString(Stack<String> stack) {
        StringBuilder str = new StringBuilder();
        
        for (String item:stack) {
            if (str.length() > 0) {
                str.append(" <- ");
            }
            str.append(item);
        }
        
        return str.toString();
    }
    
    /** */
    private void findCycle(DependencyEntry entry,Stack<String> stack) {
        if (stack.contains(entry.getId()) == true) {
            throw new RuntimeException("Found cycle " + entry.getId() + ": " +
                stackToString(stack));
        }
        stack.push(entry.getId());
        
        if (entry.getDependencies() != null) {
            for (String dependencyId:entry.getDependencies()) {
                DependencyEntry dependency = findEntry(dependencyId);
                if (dependency == null) {
                    throw new RuntimeException(
                        "Unknown dependency " + dependencyId);
                }
                findCycle(dependency,stack);
            }
        }
        
        stack.pop();
    }
    
    
    /** */
    private void findCycle(DependencyEntry entry) {
        findCycle(entry,new Stack<String>());
    }
    
    /** */
    void findCycles(List<DependencyEntry> entries) {
        this.entries = entries;
        for (DependencyEntry root:entries) {
            findCycle(root);
        }
    }
}
