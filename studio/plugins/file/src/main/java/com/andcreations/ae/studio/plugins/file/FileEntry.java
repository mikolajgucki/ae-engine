package com.andcreations.ae.studio.plugins.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a file.
 *
 * @author Mikolaj Gucki
 */
class FileEntry {
    /** The file represented by this entry. */
    private File file;
    
    /** The issues associated with the file. */
    private List<FileIssue> issues = new ArrayList<>();
    
    /** */
    FileEntry(File file) {
        this.file = file;
    }
    
    /** */
    File getFile() {
        return file;
    }
    
    /** */
    void addIssue(FileIssue issue) {
        issues.add(issue);
    }
    
    /** */
    boolean removeIssue(FileIssue issue) {
        return issues.remove(issue);
    }
    
    /** */
    List<FileIssue> getIssues() {
        return Collections.unmodifiableList(issues);
    }
    
    /** */
    boolean hasIssues() {
        return issues.isEmpty() == false;
    }
    
    /** */
    boolean hasIssues(FileIssueSeverity severity) {
        for (FileIssue issue:issues) {
            if (issue.getSeverity() == severity) {
                return true;
            }
        }
        
        return false;
    }
}