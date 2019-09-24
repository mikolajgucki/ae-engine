package com.andcreations.ae.studio.plugins.file.problems;

import java.io.File;

import com.andcreations.ae.studio.plugins.file.FileIssue;
import com.andcreations.ae.studio.plugins.problems.Problem;

/**
 * @author Mikolaj Gucki
 */
class FileProblem {
    /** */
    private File file;
    
    /** */
    private Problem problem;
    
    /** */
    private FileIssue issue;
    
    /** */
    FileProblem(File file,Problem problem,FileIssue issue) {
        this.file = file;
        this.problem = problem;
        this.issue = issue;
    }    
    
    /** */
    File getFile() {
        return file;
    }
    
    /** */
    Problem getProblem() {
        return problem;
    }
    /** */
    FileIssue getIssue() {
        return issue;
    }
}