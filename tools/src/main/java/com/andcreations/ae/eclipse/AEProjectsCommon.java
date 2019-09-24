package com.andcreations.ae.eclipse;

import java.io.File;
import java.io.FileFilter;

/**
 * @author Mikolaj Gucki
 */
public class AEProjectsCommon {
    /** */
    static String getAbsPath(File file) {
        String path = file.getAbsolutePath();
        return path.replace("\\","/");
    }
    
    /** */
    static File getJavaLibsDir(AEProjectsBuilderCfg cfg) {
        return new File(cfg.getAESrcDir(),"libs/java");
    }
    
    /** */
    static File[] getJavaLibs(AEProjectsBuilderCfg cfg) {
        File javaLibsDir = getJavaLibsDir(cfg);
        return javaLibsDir.listFiles(new FileFilter() {
            /** */
            @Override
            public boolean accept(File file) {
                return file.isFile() && file.getName().endsWith(".jar");
            }
        });
    }    
}
