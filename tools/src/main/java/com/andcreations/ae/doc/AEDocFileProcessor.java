package com.andcreations.ae.doc;

import java.io.IOException;

import com.andcreations.resources.ResourceLoader;

/**
 * @author Mikolaj Gucki
 */
abstract class AEDocFileProcessor {
    /** */
    protected void copyResource(AEDocCfg cfg,Class<?> clazz,String name)
        throws IOException {
    //
        ResourceLoader loader = new ResourceLoader(clazz);
        loader.copyResource(cfg.getDstDir(),name);
    }
    
    /** */
    protected String loadResource(Class<?> clazz,String name)
        throws IOException {
    //
        ResourceLoader loader = new ResourceLoader(clazz);
        return loader.loadAsString(name);
    }
    
    /** */
    protected void processingStarted(AEDocCfg cfg) throws IOException {
    }
    
    /** */
    protected void processingStopped(AEDocCfg cfg) throws IOException {
    }
    
    /** */
    abstract boolean canProcess(AEDocCfg cfg,AEDocFile aeDocFile);
    
    /** */ 
    abstract void process(AEDocCfg cfg,AEDocFile aeDocFile) throws IOException;
}