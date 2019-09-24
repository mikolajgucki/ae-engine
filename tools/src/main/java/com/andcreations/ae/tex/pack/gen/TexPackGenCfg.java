package com.andcreations.ae.tex.pack.gen;

import java.io.File;
import java.util.List;

import com.andcreations.ae.tex.pack.TexPackerCfg;

/**
 * @author Mikolaj Gucki
 */
public class TexPackGenCfg {
    /** */
    private TexPackerCfg cfg;
    
    /** */
    private List<File> dirs;
    
    /** */
    public TexPackGenCfg(TexPackerCfg cfg,List<File> dirs) {
        this.cfg = cfg;
        this.dirs = dirs;
    }
    
    /** */
    public TexPackerCfg getTexPackerCfg() {
        return cfg;
    }
    
    /** */
    public List<File> getDirs() {
        return dirs;
    }
}