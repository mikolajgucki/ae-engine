package com.andcreations.ae.studio.plugins.file.cache;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.andcreations.ae.studio.plugins.file.FileAdapter;
import com.andcreations.ae.studio.plugins.file.Files;

/**
 * Caches text files in memory.
 *
 * @author Mikolaj Gucki
 */
public class TextFileCache {
    /** */
    private static final String ENCODING = "UTF-8";
    
    /** */
    private static TextFileCache instance;
    
    /** */
    private Map<File,String> cache = new HashMap<>();
    
    /** */
    private TextFileCache() {
        createFileListener();
    }
    
    /** */
    private void createFileListener() {
        Files.get().addFileListener(new FileAdapter() {
            /** */
            @Override
            public void fileChanged(File file) {
                cache.remove(file);
            }
            
            /** */
            @Override
            public void fileDeleted(File file) {
                cache.remove(file);
            }
            
            /** */
            @Override
            public void fileRenamed(File src,File dst) {
                cache.remove(src);
            }
        });
    }
    
    /**
     * Removes single LFs.
     *
     * @param file The file from which the input was read.
     * @param input The input string.
     * @return The output string.
     * @throws IOException on I/O error.
     */
    private String fixLineBreaks(File file,String input) throws IOException {
        boolean fixes = false;
        StringBuilder output = new StringBuilder();
        
        for (int index = 0; index < input.length(); index++) {
            char ch = input.charAt(index);
            char nextCh = 0;
            if (index < input.length() - 1) {
                nextCh = input.charAt(index + 1);
            }
            
            if (ch == 0x0D && nextCh != 0x0A) {
                fixes = true;
            }
            else {
                output.append(ch);
            }
        }
        
        if (fixes == true) {
            write(file,output.toString());
        }
        
        return output.toString();
    }
    
    /** */
    public synchronized String read(File file) throws IOException {
        if (cache.containsKey(file) == false) {
            String content = FileUtils.readFileToString(file,ENCODING);
            cache.put(file,fixLineBreaks(file,content));
        }
        
        return cache.get(file);
    }
    
    /** */
    public synchronized void write(File file,String content)
        throws IOException {
    //
        FileUtils.writeStringToFile(file,content,ENCODING);
        cache.put(file,content);
    }
    
    /** */
    public static TextFileCache get() {
        if (instance == null) {
            instance = new TextFileCache();
        }
        
        return instance;
    }
}