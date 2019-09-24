package com.andcreations.ae.tex.font;

import java.io.File;
import java.io.IOException;

import com.andcreations.ae.tex.font.data.TexFontData;
import com.andcreations.io.json.JSON;
import com.andcreations.io.json.JSONException;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class TexFontDataReader {
    /** */
    private BundleResources res = new BundleResources(TexFontDataReader.class);
    
    /** */
    public TexFontData read(File file) throws IOException {
        TexFontData data;
    // parse
        try {
            data = JSON.read(file,TexFontData.class);
        } catch (JSONException exception) {
            throw new IOException(res.getStr("json.error",
                file.getAbsolutePath(),exception.getMessage()),exception);
        }
        
        return data;
    }
}