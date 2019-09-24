package com.andcreations.ae.doc;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.markdown4j.Markdown4jProcessor;

import com.andcreations.ae.doc.resources.R;
import com.andcreations.ant.AETask;
import com.andcreations.ant.AntPath;
import com.andcreations.ant.FileSetHelper;
import com.andcreations.resources.BundleResources;
import com.andcreations.resources.ResourceLoader;
import com.andcreations.resources.StrResources;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Generates HTML pages from markdown files.
 *
 * @author Mikolaj Gucki 
 */
public class MarkdownAntTask extends AETask {
    /** The name of the resource with the HTML template. */
    private static final String MD_TEMPLATE_NAME = "markdown.html";
    
    /** The name of the resource with the index template. */
    private static final String INDEX_TEMPLATE_NAME = "index.vm";
    
    /** The name of the generated index file. */
    private static final String INDEX_FILE_NAME = "index.html";
    
    /** The name of the resource with the navigation template. */
    private static final String NAV_TEMPLATE_NAME = "navigation.vm";
    
    /** The name of the generated navigation file. */
    private static final String NAV_FILE_NAME = "navigation.html";
    
    /** The string resources. */
    private StrResources res = new BundleResources(MarkdownAntTask.class); 
    
    /** The source files. */
    private FileSet srcFileSet;
    
    /** The path to the configuration file. */
    private AntPath cfgFile;
    
    /** The destinstion directory. */
    private AntPath dstDir;
    
    /** The HTML template. */
    private StringBuffer template;    
    
    /** The markdown processor. */
    private Markdown4jProcessor markdown4jProcessor = new Markdown4jProcessor(); 
    
    /** The number of files processed. */
    private int fileCount;
    
    /** The map of document identifiers and titles. */
    private Map<String,String> docIdTitleMap = new HashMap<String,String>();
    
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
    public AntPath createCfgFile() {
        if (cfgFile != null) {
            duplicatedElement("cfgfile");
        }
        
        cfgFile = new AntPath();
        return cfgFile;
    }
    
    /** */
    public AntPath createDstDir() {
        if (dstDir != null) {
            duplicatedElement("dstdir");
        }
        
        dstDir = new AntPath();
        return dstDir;
    }
    
    /**
     * Loads the HTML template.
     */
    private void loadTemplate() {
        ResourceLoader loader = new ResourceLoader(R.class);
        try {
            template = loader.loadAsStringBuffer(MD_TEMPLATE_NAME);
        } catch (IOException exception) {
            throw new BuildException(res.getStr("failed.to.load.template",
                MD_TEMPLATE_NAME,exception.getMessage()));
        }
    }
    
    /**
     * Preprocesses a markdown source.
     *
     * @param lines The lines of the source markdown.
     * @return The result preprocessed markdown.
     */
    private String preprocess(List<String> lines) {
        String markdown = "";
        
        int index = 0;
        while (index < lines.size()) {
            String line = lines.get(index);
            index++;
            
            if (line.endsWith("\\") == false) {
                markdown += line + "\n";
            }
            else {
                markdown += line.substring(0,line.length() - 1);
            }
        }
        
        return markdown;
    }
    
    /**
     * Gets the document identifier.
     *
     * @param srcFile The source file.
     * @return The document identifier.
     */
    private String getDocId(File srcFile) {
        return FilenameUtils.getBaseName(srcFile.getName());
    }
    
    /**
     * Gets the document title.
     *
     * @param lines The lines of the source markdown.
     * @return The document title.
     */
    private String getTitle(List<String> lines) {
        return lines.get(0);
    }
    
    /**
     * Generates a single file.
     *
     * @param srcFile The source markdown file.
     * @param dstDir The destination directory for the generated HTML pages.
     */
    private void generate(File srcFile,File dstDir) {
        verbose(res.getStr("processing.file",srcFile.getPath()));
        
    // read
        List<String> lines = null;
        try {
            lines = FileUtils.readLines(srcFile);
        } catch (IOException exception) {
            throw new BuildException(res.getStr("failed.to.read.file",
                srcFile.getAbsolutePath(),exception.getMessage()));
        }
        
    // id, title map
        String id = getDocId(srcFile);
        String title = getTitle(lines);
        docIdTitleMap.put(id,title);
        
    // preprocess
        String markdown = preprocess(lines);
        
    // process
        String body = null;
        try {
            body = markdown4jProcessor.process(markdown);
        } catch (IOException exception) {
            throw new BuildException(res.getStr("failed.to.process.file",
                srcFile.getAbsolutePath(),exception.getMessage()));
        }
        
    // html
        String html = template.toString()
            .replace("${title}",title)
            .replace("${body}",body);
        
    // write
        File htmlFile = new File(dstDir,
            FilenameUtils.getBaseName(srcFile.getName()) + ".html");
        try {
            FileUtils.write(htmlFile,html);
        } catch (IOException exception) {
            throw new BuildException(res.getStr("failed.to.write.file",
                htmlFile.getAbsolutePath(),exception.getMessage()));
        }
        
        fileCount++;
    }
    
    /**
     * Generates the documents.
     *     
     * @param srcDirs The list of the directories with source markdown files.
     * @param dstDir The destination directory for the generated HTML pages.
     */
    private void generate(File[] srcFiles) {
        for (File srcFile:srcFiles) {
            generate(srcFile,new File(dstDir.getPath()));
        }
    }
    
    /**
     * Copies a resource to a directory.
     *
     * @param dstDir The directory to which to copy.
     * @param name The name of the resource to copy.
     */
    private void copyResource(File dstDir,String name) {
        ResourceLoader loader = new ResourceLoader(R.class);
        try {
            loader.copyResource(dstDir,name);            
        } catch (IOException exception) {
            throw new BuildException(exception.getMessage());
        }
    }    
    
    /**
     * Copies the markdown files.
     */
    private void copyFiles() {
        copyResource(new File(dstDir.getPath()),"markdown.css");
    }
    
    /**
     * Reads the configuration.
     *
     * @return The configuration.
     */
    private DocCfg readCfg() {
    // read the file
        File file = new File(cfgFile.getPath());
        String str = null;
        try {
            str = FileUtils.readFileToString(file,"UTF-8");
        } catch (IOException exception) {
            throw new BuildException(res.getStr("failed.to.read.nav.source",
                file.getAbsolutePath(),exception.getMessage()));
        }
        
    // parse
        DocCfg cfg;
        try {
            Gson gson = new Gson();
            cfg = gson.fromJson(str,DocCfg.class);   
        } catch (JsonSyntaxException exception) {
            throw new BuildException(res.getStr("failed.to.read.nav.source",
                file.getAbsolutePath(),exception.getMessage()));
        }
        
        return cfg;
    }
    
    /**
     * Validates the navigation.
     *
     * @param nav The navigation.
     */
    private void validateNavigation(NavGroup[] nav) {
        for (NavGroup group:nav) {
            if (group.getEntries() != null) {
                for (NavEntry entry:group.getEntries()) {
                // empty entry
                    if (entry == null) {
                        throw new BuildException(res.getStr("empty.nav.entry"));
                    }
                    
                // unknown document
                    String doc = entry.getDoc();
                    if (doc != null &&
                        docIdTitleMap.containsKey(doc) == false) {
                    //
                        throw new BuildException(res.getStr(
                            "unknown.doc.in.nav",doc));                    
                    }
                }
            }
        }
    }
    
    /**
     * Updates the navigation.
     *
     * @param nav The navigation.
     */
    private void updateNavigation(NavGroup[] nav) {
        for (NavGroup group:nav) {
            if (group.getEntries() != null) {
                for (NavEntry entry:group.getEntries()) {
                    String doc = entry.getDoc();
                // title and URL from document
                    if (doc != null) {
                        entry.setTitle(docIdTitleMap.get(doc));
                        entry.setURL(doc + ".html");
                    }
                }
            }
        }
    }
    
    /**
     * Generates a file from a template.
     *
     * @param cfg The configuration.
     * @param templateName The template name.
     * @return The navigation.
     */
    private String generateFile(DocCfg cfg,String templateName) {
        VelocityContext context = new VelocityContext();
        context.put("cfg",cfg);
        
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context,writer,"doc",
            loadResourceAsString(R.class,templateName));
        
        return writer.toString();          
    }
    
    /**
     * Writes a file in the destination directory.
     *
     * @param filename The name of the file.
     * @param str The file content.     
     */
    private void writeFile(String str,String filename) {
        File dstFile = new File(dstDir.getPath(),filename);
        
    // write
        try {
            FileUtils.write(dstFile,str,"UTF-8");
        } catch (IOException exception) {
            throw new BuildException(res.getStr("failed.to.write.file",
                dstFile.getAbsolutePath(),exception.getMessage()));
        }  
    }
    
    /** */
    @Override
    public void execute() {
        verifyElementSet(srcFileSet,"srcfiles");
        verifyElementSet(dstDir,"dstdir");
        
        loadTemplate();
        File[] files = FileSetHelper.getFiles(srcFileSet);
        generate(files);
        copyFiles();
        
        if (cfgFile != null) {
            DocCfg cfg = readCfg();
        // index
            String indexStr = generateFile(cfg,INDEX_TEMPLATE_NAME);
            writeFile(indexStr,INDEX_FILE_NAME);
            
        // navigation
            validateNavigation(cfg.getNavigation());
            updateNavigation(cfg.getNavigation());
            String navStr = generateFile(cfg,NAV_TEMPLATE_NAME);
            writeFile(navStr,NAV_FILE_NAME);
        }
        
    // log
        File dir = new File(dstDir.getPath());
        log(res.getStr("processed.files",Integer.toString(fileCount),
            dir.getAbsolutePath()));
    }
}