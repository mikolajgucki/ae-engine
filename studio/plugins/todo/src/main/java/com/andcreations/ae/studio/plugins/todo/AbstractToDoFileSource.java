package com.andcreations.ae.studio.plugins.todo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.andcreations.ae.studio.plugins.file.FileAdapter;
import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.problems.Problem;
import com.andcreations.ae.studio.plugins.problems.ProblemSeverity;
import com.andcreations.ae.studio.plugins.problems.Problems;
import com.andcreations.io.FileUtil;

/**
 * Todo source reading from a number of files.
 *
 * @author Mikolaj Gucki
 */
public abstract class AbstractToDoFileSource {
    /** */
    private class Entry {
        /** */
        private File file;
        
        /** */
        private List<ToDoItem> todos;
        
        /** */
        private Problem parseProblem;
        
        /** */
        private Entry(File file) {
            this.file = file;
        }            
    }
    
    /** */
    private static final String PROBLEM_SOURCE_ID = 
        AbstractToDoFileSource.class.getName();
    
    /** */
    private Map<File,Entry> entries = new HashMap<>();
    
    /**
     * Constructs an {@link AbstractToDoFileSource}.
     *
     * @param files The files from which to get the todo items.
     * @param refreshOnChange Indicates if to parse again if a file changes.
     */
    protected AbstractToDoFileSource(File[] files,boolean refreshOnChange) {
        create(files,refreshOnChange);
    }
    
    /** */
    private void create(File[] files,boolean refreshOnChange) {
        for (File file:files) {
            entries.put(file,new Entry(file));
        }
        parseAll();
        
        if (refreshOnChange == true) {
            createFileListener();
        }
    }
    
    /** */
    private void createFileListener() {
        Files.get().addFileListener(new FileAdapter() {
            /** */
            @Override
            public void fileCreated(File file) {
                tryAddFile(file);
            }
            
            /** */
            @Override
            public void fileChanged(File file) {
                Entry entry = entries.get(file);
                if (entry != null) {
                    clear(entry);
                    parse(entry);
                }
            }                
            
            /** */
            @Override
            public void fileDeleted(File file) {
                tryRemoveFile(file);
            }
            
            /** */
            @Override
            public void fileRenamed(File src,File dst) {
                fileDeleted(src);
                fileCreated(dst);                
            }
        });
    }
    
    /** */
    private void clear(Entry entry) {
        if (entry.todos != null) {
            for (ToDoItem todo:entry.todos) {
                ToDo.get().removeItem(todo);
            }
        }
    }
    
    /** */
    private void parse(Entry entry) {
	// remove problem
    	if (entry.parseProblem != null) {
    		Problems.get().removeProblem(entry.parseProblem);
    		entry.parseProblem = null;
    	}
    	
    // parse
        try {
            entry.todos = parse(entry.file,ToDo.get().getTags());
        } catch (IOException exception) {
            entry.parseProblem = Problems.get().add(PROBLEM_SOURCE_ID,
                ProblemSeverity.WARNING,"Failed to parse"); // TODO Description
        }
        
    // add
        if (entry.todos != null) {
            for (ToDoItem todo:entry.todos) {
                ToDo.get().addItem(todo);
            }
        }
    }
    
    /** */
    private void parseAll() {
        for (File file:entries.keySet()) {
            Entry entry = entries.get(file);
            parse(entry);
        }
    }
    
    /** */
    private void tryAddFile(File file) {
    // file
        if (file.isFile() == true && shouldParse(file) == true) {
            addFile(file);
            return;
        }
        
    // directory
        File[] childFiles = Files.listTree(file);
        for (File childFile:childFiles) {
            if (childFile.isFile() == true && shouldParse(childFile) == true) {
                addFile(childFile);
            }                        
        }        
    }
    
    /**
     * Adds a file to be parsed.
     *
     * @param file The file.
     */
    protected void addFile(File file) {
        Entry entry = new Entry(file);
        entries.put(file,entry);
        parse(entry);        
    }
    
    /** */
    private void tryRemoveFile(File file) {
    // file
        if (file.isFile() == true) {
            removeFile(file);
            return;
        }
        
    // directory
        while (true) {
            File toRemove = null;
            for (File toDoFile:entries.keySet()) {
                if (FileUtil.isAncestor(file,toDoFile) == true) {
                    toRemove = toDoFile;
                    break;
                }                
            }
            
            if (toRemove == null) {
                break;
            }
            removeFile(toRemove);
        }        
    }
    
    /**
     * Removes a file.
     *
     * @param file The file.
     */
    protected void removeFile(File file) {
        Entry entry = entries.get(file);
        if (entry != null) {
            if (entry.todos != null) {
                for (ToDoItem todo:entry.todos) {
                    ToDo.get().removeItem(todo);
                }
            }
            entries.remove(file);
        }
    }
    
    /**
     * Indicates if a file should be parsed.
     *
     * @param file The file.
     * @return <code>true</code> if should, <code>false</code> otherwise.
     */
    protected abstract boolean shouldParse(File file);
    
    /**
     * Parses a file and gets all the todo items.
     *
     * @param file The file to parse.
     * @param tags The todo tags.
     * @return The list of todo items.
     * @throws IOException if parsing fails.
     */
    protected abstract List<ToDoItem> parse(File file,String[] tags)
        throws IOException;
}