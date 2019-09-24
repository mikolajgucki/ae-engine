package com.andcreations.ae.project.template;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.andcreations.io.DirScanner;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class ProjectTemplateBuilder {    
    /** */
    private static final String TEMPLATE_PATH = "templates/project";
    
    /** */
    private final BundleResources res =
        new BundleResources(ProjectTemplateBuilder.class);     
    
    /** */
    private File aeDistDir;
    
    /** */
    private File projectDir;
    
    /** */
    private String appName;
    
    /** */
    private Map<String,String> vars = new HashMap<String,String>();
    
    /** */
    public ProjectTemplateBuilder(File aeDistDir,File projectDir,
        String appName) {
    //
        this.aeDistDir = aeDistDir;
        this.projectDir = projectDir;
        this.appName = appName;
        
        create();
    }
    
    /** */
    private void create() {
        String appId = appName.toLowerCase().replace(' ','.');
        vars.put("${ae.template.app.name}",appName);
        vars.put("${ae.template.app.id}",appId);
    }
    
    /** */
    private String[] listTemplateFiles() throws IOException {
    // template directory
        File templateDir = new File(aeDistDir,TEMPLATE_PATH);
        if (templateDir.exists() == false ||
            templateDir.isDirectory() == false) {
        //
            throw new IOException(res.getStr("template.dir.not.found",
                templateDir.getAbsolutePath()));
        }
        
    // list files
        DirScanner scanner = new DirScanner(templateDir);        
        return scanner.build();        
    }
    
    /** */
    private String replaceVars(String str) {
        for (Map.Entry<String,String> entry:vars.entrySet()) {
            str = StringUtils.replace(str,entry.getKey(),entry.getValue());
        }
        //return StringUtils.replace(str,"${ae.template.app.name}",appName);
        return str;
    }
    
    /** */
    private void copy(File srcFile,File dstFile) throws IOException {
    // directory
        if (srcFile.isDirectory() == true) {
            dstFile.mkdirs();
            return;
        }
        
    // file
        String str = FileUtils.readFileToString(srcFile,"UTF-8");
        FileUtils.writeStringToFile(dstFile,replaceVars(str),"UTF-8");
    }
    
    /** */
    public void build() throws IOException {
        String[] files = listTemplateFiles();
        File templateDir = new File(aeDistDir,TEMPLATE_PATH);
        for (String file:files) {
            copy(new File(templateDir,file),new File(projectDir,file));
        }
    }
}