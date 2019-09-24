package com.andcreations.ae.studio.plugins.file.problems;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.andcreations.ae.issue.Issue;
import com.andcreations.ae.issue.IssueSeverity;
import com.andcreations.ae.studio.plugins.file.FileIssue;
import com.andcreations.ae.studio.plugins.file.FileIssueSeverity;
import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.problems.Problem;
import com.andcreations.ae.studio.plugins.problems.ProblemListener;
import com.andcreations.ae.studio.plugins.problems.ProblemSeverity;
import com.andcreations.ae.studio.plugins.problems.ProblemType;
import com.andcreations.ae.studio.plugins.problems.Problems;

/**
 * Manages problems related to files.
 *
 * @author Mikolaj Gucki
 */
public class FileProblems {
    /** */
    private String sourceId;
    
    /** */
    private Map<File,List<FileProblem>> problems =
        new HashMap<File,List<FileProblem>>();
     
    /** */
    public FileProblems(String sourceId) {
        this.sourceId = sourceId;
    }
    
    /** */
    private FileIssueSeverity getFileIssueSeverity(ProblemSeverity severity) {
        if (severity == ProblemSeverity.ERROR) {
            return FileIssueSeverity.ERROR;
        }
        if (severity == ProblemSeverity.WARNING) {
            return FileIssueSeverity.WARNING;
        }
        
        return null;
    }
        
    /** */
    private List<FileProblem> getProblemList(File file) {
        List<FileProblem> list = problems.get(file);
        if (list == null) {
            list = new ArrayList<>();
            problems.put(file,list);
        }
        
        return list;
    }
        
    /** */
    public Problem addProblem(File file,ProblemSeverity severity,
        String description,String resource,Object location,ProblemType type,
        ProblemListener listener) {
    // problem
        Problem problem = new Problem(sourceId,severity,description,type);
        problem.setResource(resource);
        if (location != null) {
            problem.setLocation(location.toString());
        }
        if (listener != null) {
            problem.addProblemListener(listener);
        }
        Problems.get().addProblem(problem);
        
    // issue
        FileIssue issue = new FileIssue(getFileIssueSeverity(severity),
            sourceId,description,location);
        Files.get().addIssue(file,issue);
        
    // add
        List<FileProblem> fileProblems = getProblemList(file);
        fileProblems.add(new FileProblem(file,problem,issue));
        
        return problem;
    }
    
    /** */
    public Problem addProblem(File file,ProblemSeverity severity,
        String description,String resource,Object location,
        ProblemListener listener) {
    //
        return addProblem(
            file,severity,description,resource,location,null,listener);
    }
    
    /** */
    public Problem addProblem(File file,ProblemSeverity severity,
        String description,String resource,Object location) {
    //
        return addProblem(file,severity,description,resource,location,
            null,null);
    }
    
    
    /** */
    public Problem addProblem(File file,ProblemSeverity severity,
        String description,String resource,Object location,ProblemType type) {
    //
        return addProblem(file,severity,description,resource,location,type,null);
    }    
        
    /** */
    public Problem addProblem(File file,ProblemSeverity severity,
        String description,String resource) {
    //
        return addProblem(file,severity,description,resource,null,null,null);
    }    
    
    /** */
    public Problem addProblem(File file,Issue issue,String resource,
        Object location,ProblemType type) {
    //
        ProblemSeverity severity = null;
        if (issue.getSeverity() == IssueSeverity.WARNING) {
            severity = ProblemSeverity.WARNING;
        }
        else if (issue.getSeverity() == IssueSeverity.ERROR) {
            severity = ProblemSeverity.ERROR;
        }
        
        return addProblem(
            file,severity,issue.getMessage(),resource,location,type);
    }  
    
    /** */
    public void removeProblems(File file) {
        List<FileProblem> fileProblems = getProblemList(file);
        for (FileProblem fileProblem:fileProblems) {
            Problems.get().removeProblem(fileProblem.getProblem());
            Files.get().removeIssue(file,fileProblem.getIssue());
        }
        
        problems.remove(file);
    }
    
    /** */
    public boolean hasProblems(File file,ProblemSeverity severity) {
        List<FileProblem> fileProblems = getProblemList(file);
        for (FileProblem fileProblem:fileProblems) {
            if (fileProblem.getProblem().getSeverity() == severity) {
                return true;
            }
        }
        
        return false;
    }
    
    /** */
    public boolean hasWarnings(File file) {
        return hasProblems(file,ProblemSeverity.WARNING);
    }
    
    /** */
    public boolean hasErrors(File file) {
        return hasProblems(file,ProblemSeverity.ERROR);
    }
    
    /** */
    public boolean hasProblems(ProblemSeverity severity) {
        for (File file:problems.keySet()) {
            if (hasProblems(file,severity) == true) {            
                return true;
            }            
        }
        
        return false;
    }
    
    /** */
    public boolean hasWarnings() {
        return hasProblems(ProblemSeverity.WARNING);
    }
    
    /** */
    public boolean hasErrors() {
        return hasProblems(ProblemSeverity.ERROR);
    }
    
    /** */
    public Set<File> getFiles() {
        return Collections.unmodifiableSet(problems.keySet());
    }
}