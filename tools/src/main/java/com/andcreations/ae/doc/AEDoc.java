package com.andcreations.ae.doc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.io.DirScanner;

/**
 * @author Mikolaj Gucki
 */
public class AEDoc {
    /** */
    private AEDocCfg cfg;
    
    /** */
    private List<AEDocFileProcessor> processors = new ArrayList<>();
    
    /** */
    public AEDoc(AEDocCfg cfg) {
        this.cfg = cfg;
        create();
    }
    
    /** */
    private void create() {
    // register processors
        processors.add(new Markdown2HTMLFileProcessor());
        processors.add(new Nav2HTMLFileProcessor());
    }
    
    /** */
    private List<AEDocFile> listDocFiles(File root) {
        DirScanner scanner = new DirScanner(root,true);
        String[] paths = scanner.build();
        
        List<AEDocFile> files = new ArrayList<>();
    // for each file
        for (String path:paths) {
            File file = new File(root,path);
            if (file.isDirectory() == false) {
                files.add(new AEDocFile(root,file,path));
            }
        }
        
        return files;
    }
    
    /** */
    private List<AEDocFile> listDocFiles() {
        List<AEDocFile> files = new ArrayList<>();
        
    // for each root
        for (File root:cfg.getRoots()) {
            files.addAll(listDocFiles(root));
        }
        
        return files;
    }
    
    /** */
    private void process(AEDocFile aeDocFile) throws IOException {
        for (AEDocFileProcessor processor:processors) {
            if (processor.canProcess(cfg,aeDocFile) == true) {
                processor.process(cfg,aeDocFile);
            }
        }
    }
    
    /** */
    public void process() throws IOException {
    // start processing
        for (AEDocFileProcessor processor:processors) {
            processor.processingStarted(cfg);
        }
        
    // process
        List<AEDocFile> aeDocFiles = listDocFiles();
        for (AEDocFile aeDocFile:aeDocFiles) {
            process(aeDocFile);
        }
        
    // stop processing
        for (AEDocFileProcessor processor:processors) {
            processor.processingStopped(cfg);
        }
    }
}