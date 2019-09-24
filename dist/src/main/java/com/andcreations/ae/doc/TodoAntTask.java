package com.andcreations.ae.doc;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.andcreations.ae.doc.resources.R;
import com.andcreations.ant.AETask;
import com.andcreations.ant.AntPath;
import com.andcreations.ant.FileSetHelper;
import com.andcreations.resources.BundleResources;
import com.andcreations.resources.StrResources;

/**
 * Generates TO-DO page.
 *
 * @author Mikolaj Gucki
 */
public class TodoAntTask extends AETask {
    /** The string resources. */
    private StrResources res = new BundleResources(TodoAntTask.class); 
    
    /** The source files. */
    private FileSet srcFileSet;
    
    /** The path to the destination file. */
    private AntPath dstFile;
    
    /** */
    public FileSet createSrcFiles() {
        if (srcFileSet != null) {
            duplicatedElement("srcfiles");
        }
        
        srcFileSet = new FileSet();
        srcFileSet.setProject(getProject());
        
        return srcFileSet;
    }
    
    /** */
    public AntPath createDstFile() {
        if (dstFile != null) {
            duplicatedElement("dstfile");
        }
        
        dstFile = new AntPath();
        return dstFile;
    }
    
    /**
     * Parses a file.
     *
     * @param file The file.
     * @return The read TO-DOs.
     */
    private List<Todo> parse(File file) {
        List<String> lines;
        try {
            lines = FileUtils.readLines(file,"UTF-8");
        } catch (IOException exception) {
            throw new BuildException(res.getStr("failed.to.read.file",
                file.getAbsolutePath(),exception.getMessage()));
        }
        
        TodoParser parser = new TodoParser();
        List<Todo> todos = parser.parse(file.getName(),lines);
        
        return todos;
    }
    
    /**
     * Reads the to-do list.
     *
     * @param files The files from which to read.
     * @return The read TODOs.
     */
    private List<Todo> read(File[] files) {
        List<Todo> todos = new ArrayList<Todo>();
        for (File file:files) {
            List<Todo> fileTodos = parse(file);
            todos.addAll(fileTodos);
        }
        
        return todos;
    }
    
    /**
     * Writes the destination file.
     *
     * @param content The file content.
     * @return The destination file.
     */
    private File writeDstFile(String content) {
        File file = new File(dstFile.getPath());        
            
    // write
        try {
            FileUtils.write(file,content,"UTF-8");
        } catch (IOException exception) {
            throw new BuildException(res.getStr("failed.to.write.file",
                file.getAbsolutePath(),exception.getMessage()));
        }
        
        return file;
    }
    
    /**
     * Generates the content of the destination file.
     *
     * @param todos The TO-DOs.
     * @return The file content.
     */
    private String generate(List<Todo> todos) {
        VelocityContext context = new VelocityContext();
        context.put("todos",todos);
        
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context,writer,"doc",
            loadResourceAsString(R.class,"todo.vm"));
        
        return writer.toString();         
    }
    
    /** */
    @Override
    public void execute() {
        verifyElementSet(srcFileSet,"srcfiles");
        verifyElementSet(dstFile,"dstfile");
        
        File[] files = FileSetHelper.getFiles(srcFileSet);
        List<Todo> todos = read(files);
        String content = generate(todos);
        File file = writeDstFile(content);
        log(res.getStr("processed.files",Integer.toString(files.length),
            file.getAbsolutePath()));        
    }
}