package com.andcreations.ae.appicon;

import java.io.IOException;

import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class AppIconLauncher {
    /** */
    private final BundleResources res =
        new BundleResources(AppIconLauncher.class);     
    
    /** */
    private String[] args;
    
    /** */
    private String srcFile;
    
    /** */
    private String dstDir;
        
    /** */
    private AppIconLauncher(String[] args) {
        this.args = args;
    }
    
    /** */
    private void printHelp() {
        System.out.println(res.getStr("help"));
    }
    
    /** */
    private void parseArgs() {
        if (args.length != 2) {
            System.out.println(res.getStr("invalid.arg.count"));
            printHelp();
            System.exit(-1);
        }
        
        srcFile = args[0];
        dstDir = args[1];
    }
    
    /** */
    private void generate(AppIcon appIcon) throws IOException {
        appIcon.generate(srcFile,dstDir);
    }
    
    /** */
    private void run() {
        parseArgs();
        try {
            generate(AppIcon.ios());
            generate(AppIcon.android());
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
            System.exit(-1);
        }
    }
    
    /** */
    public static void main(String[] args) {
        AppIconLauncher launcher = new AppIconLauncher(args);
        launcher.run();
    }
}