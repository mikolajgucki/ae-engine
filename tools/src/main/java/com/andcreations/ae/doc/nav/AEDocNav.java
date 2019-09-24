package com.andcreations.ae.doc.nav;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.andcreations.ae.doc.nav.resources.R;
import com.andcreations.resources.ResourceLoader;

/**
 * @author Mikolaj Gucki
 */
public class AEDocNav {
    /** */
    private AEDocNavCfg cfg;
    
    /** */
    public AEDocNav(AEDocNavCfg cfg) {
        this.cfg = cfg;
    }
    
    /** */
    private List<AEDocNavEntry> readNavFile(File file) throws IOException {
    // read
        List<String> lines = FileUtils.readLines(file);
        
    // parse
        AEDocNavParser parser = new AEDocNavParser(lines);
        List<AEDocNavEntry> entries = parser.parse();
        
        return entries;
    }
    
    /** */
    private void copyResource(String name) throws IOException {
        ResourceLoader loader = new ResourceLoader(R.class);
        loader.copyResource(cfg.getDstDir(),name);        
    }
    
    /** */
    private void copyResources() throws IOException {
        copyResource("index.html");
    }
    
    /** */
    public void process() throws IOException {
    // read
        List<AEDocNavEntry> entries = new ArrayList<>();
        for (File navFile:cfg.getNavFiles()) {
            entries.addAll(readNavFile(navFile));
        }
        
    // generate
        AEDocNavGen gen = new AEDocNavGen();
        String result = gen.generate(entries);
        
    // write
        copyResources();
        FileUtils.writeStringToFile(new File(cfg.getDstDir(),"navigation.html"),
            result,"UTF-8");
    }
}