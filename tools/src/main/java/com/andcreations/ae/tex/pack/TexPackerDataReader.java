package com.andcreations.ae.tex.pack;

import java.io.File;
import java.io.IOException;

import com.andcreations.ae.tex.pack.data.TexPack;
import com.andcreations.io.json.JSON;
import com.andcreations.io.json.JSONException;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class TexPackerDataReader {
    /** */
    private BundleResources res = new BundleResources(
        TexPackerDataReader.class);
    
    /** */
    public TexPack read(File file) throws IOException {
        TexPack data;
    // parse
        try {
            data = JSON.read(file,TexPack.class);
        } catch (JSONException exception) {
            throw new IOException(res.getStr("json.error",
                file.getAbsolutePath(),exception.getMessage()),exception);
        }
        
        return data;
    }
}