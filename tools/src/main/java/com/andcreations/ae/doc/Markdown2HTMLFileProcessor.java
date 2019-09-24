package com.andcreations.ae.doc;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.andcreations.ae.doc.resources.R;
import com.andcreations.markdown.MarkdownProcessor;

/**
 * @author Mikolaj Gucki
 */
class Markdown2HTMLFileProcessor extends AEDocFileProcessor {
    /** */
    private static final String MD_SUFFIX = ".md";
    
    /** */
    private static final String HTML_SUFFIX = ".html";
    
    /** */
    @Override
    protected void processingStopped(AEDocCfg cfg) throws IOException {
        copyResource(cfg,R.class,"markdown.css");
    }
    
    /** */
    @Override
    public boolean canProcess(AEDocCfg cfg,AEDocFile aeDocFile) {
        return aeDocFile.getFile().getName().endsWith(MD_SUFFIX);
    }
    
    /** */
    private File getOutputFile(AEDocCfg cfg,AEDocFile aeDocFile) {
        String path = aeDocFile.getPath().substring(
            0,aeDocFile.getPath().length() - MD_SUFFIX.length());
        return new File(cfg.getDstDir(),path + HTML_SUFFIX);
    }
    
    /** */
    private String getTitle(List<String> lines) {
        return lines.get(0);
    }
    
    /** */ 
    @Override
    public void process(AEDocCfg cfg,AEDocFile aeDocFile) throws IOException {
    // read, process
        String input = FileUtils.readFileToString(aeDocFile.getFile(),"UTF-8");
        List<String> lines = FileUtils.readLines(aeDocFile.getFile());
        String body = MarkdownProcessor.process(input);
        
    // title
        String title = getTitle(lines);
        
    // template
        String template = loadResource(R.class,"markdown.html");
        template = template
            .replace("${body}",body)
            .replace("${title}",title);
        
    // write
        File outputFile = getOutputFile(cfg,aeDocFile);
        FileUtils.writeStringToFile(outputFile,template,"UTF-8");
    }
}