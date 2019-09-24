package com.andcreations.ae.assets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.io.DirScanner;

/**
 * @author Mikolaj Gucki
 */
public class AssetsUtil {
    /** */
    public static List<File> findFiles(File root,String suffix) {
        DirScanner scanner = new DirScanner(root,false);
        String[] files = scanner.build();
        
        List<File> matchedFiles = new ArrayList<>();
        for (String file:files) {
            if (file.endsWith(suffix) == true) {
                matchedFiles.add(new File(root,file));
            }
        }
        
        return matchedFiles;
    }    
}