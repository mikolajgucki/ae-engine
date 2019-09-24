package com.andcreations.eclipse;

import java.io.File;

public class Test {
    public static void main(String[] args) throws Exception {
        JavaProjectCfg cfg = new JavaProjectCfg("myproject");
        cfg.addSrcDir("src");
        cfg.addSrcDir("src2");
        cfg.addLib("libs/test.jar");
        cfg.addLib("libs/test2.jar");
        cfg.addLib("libs/test3.jar");
        cfg.setOutputDir("build/classes");
        cfg.addLinkedResource(new LinkedFolder("src","c:\\test\\src"));
        JavaProjectBuilder b = new JavaProjectBuilder(cfg);
        b.build(new File("ectest"));
    }
}