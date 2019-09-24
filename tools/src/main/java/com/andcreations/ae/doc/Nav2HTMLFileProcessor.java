package com.andcreations.ae.doc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.doc.nav.AEDocNav;
import com.andcreations.ae.doc.nav.AEDocNavCfg;

/**
 * @author Mikolaj Gucki
 */
class Nav2HTMLFileProcessor extends AEDocFileProcessor {
    /** */
    private static final String NAV_FILE_NAME = "navigation";
    
    /** */
    private List<File> navFiles;
    
    /** */
    @Override
    protected void processingStarted(AEDocCfg cfg) throws IOException {
        navFiles = new ArrayList<>();
    }    
    
    /** */
    @Override
    protected void processingStopped(AEDocCfg cfg) throws IOException {
        AEDocNavCfg navCfg = new AEDocNavCfg(navFiles,cfg.getDstDir());
        AEDocNav nav = new AEDocNav(navCfg);
        nav.process();
    }    
    
    /** */
    @Override
    public boolean canProcess(AEDocCfg cfg,AEDocFile aeDocFile) {
        return NAV_FILE_NAME.equals(aeDocFile.getFile().getName());
    }
    
    /** */ 
    @Override
    public void process(AEDocCfg cfg,AEDocFile aeDocFile) throws IOException {
        navFiles.add(aeDocFile.getFile());
    }
}