package com.andcreations.eclipse;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.andcreations.eclipse.resources.R;
import com.andcreations.velocity.VelocityUtil;

/**
 * @author Mikolaj Gucki
 */
public abstract class ProjectBuilder {
    /** The project configuration. */
    private ProjectCfg cfg;
    
    /**
     * Creates a {@link ProjectBuilder}.
     *
     * @param cfg The project configuration.
     */
    public ProjectBuilder(ProjectCfg cfg) {
        this.cfg = cfg;
    }
    
    /**
     * Gets the project configuration.
     *
     * @return The project configuration.
     */
    public ProjectCfg getProjectCfg() {
        return cfg;
    }
    
    /** */
    protected void write(File file,String str) throws IOException {
        FileUtils.writeStringToFile(file,str,"UTF-8");
    }
    
    /** */
    protected void writeTemplate(File dstDir,String fileName,
        String templateName) throws IOException {
    // context
        Map<String,Object> context = new HashMap<String,Object>();
        context.put("cfg",cfg);
        
    // evalute and write
        String str = VelocityUtil.evaluate(R.class,templateName,context);
        write(new File(dstDir,fileName),str);    
    }
    
    /**
     * Builds the project.
     *
     * @param dstDir The destination directory.
     */
    public void build(File dstDir) throws IOException {
        dstDir.mkdirs();
        for (String dirName:cfg.getDirs()) {
            File dir = new File(dstDir,dirName);
            dir.mkdirs();
        }
    }
}