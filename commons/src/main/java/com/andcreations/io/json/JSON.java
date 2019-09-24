package com.andcreations.io.json;

import java.io.File;
import java.io.IOException;

import com.andcreations.resources.BundleResources;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author Mikolaj Gucki
 */
public class JSON {
    /** */
    private static BundleResources res = new BundleResources(JSON.class);    
    
    /** */
    public static <T> T read(String src,Class<T> clazz) throws JSONException {
        T value;
        try {
            ObjectMapper mapper = new ObjectMapper();
            value = mapper.readValue(src,clazz);
        } catch (JsonProcessingException exception) {
            throw new JSONException(res.getStr("json.read.error",
                exception.getMessage()),exception);
        } catch (IOException exception) {
            throw new JSONException(res.getStr("json.read.error",
                exception.getMessage()),exception);
        }
        
        return value;        
    }
    
    /** */
    public static <T> T read(File file,Class<T> clazz) throws JSONException {
        T value;
        try {
            ObjectMapper mapper = new ObjectMapper();
            value = mapper.readValue(file,clazz);
        } catch (JsonProcessingException exception) {
            throw new JSONException(res.getStr("json.read.error",
                file.getAbsolutePath(),exception.getMessage()),exception);
        } catch (IOException exception) {
            throw new JSONException(res.getStr("json.read.error",
                file.getAbsolutePath(),exception.getMessage()),exception);
        }
        
        return value;
    }
    
    /** */
    public static void write(File file,Object value) throws JSONException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(file,value);
        } catch (JsonProcessingException exception) {
            throw new JSONException(res.getStr("json.write.error",
                file.getAbsolutePath(),exception.getMessage()),exception);
        } catch (IOException exception) {
            throw new JSONException(res.getStr("json.write.error",
                file.getAbsolutePath(),exception.getMessage()),exception);
        }        
    }
}