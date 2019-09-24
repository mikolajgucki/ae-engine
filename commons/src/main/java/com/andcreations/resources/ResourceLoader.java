package com.andcreations.resources;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;


/**
 * @author Mikolaj Gucki
 */
public class ResourceLoader {
    /** The resources. */
    private StrResources res = new BundleResources(ResourceLoader.class);

    /** The class whose package get resources from. */
    private Class<?> clazz;
    
    /**
     * Constructs a {@link ResourceLoader} object.
     * 
     * @param clazz The class whose package get resources from.
     */
    public ResourceLoader(Class<?> clazz) {
        this.clazz = clazz;
    }
    
    /**
     * Gets an input stream to a resource.
     * 
     * @param name The resource name.
     * @return The input stream to the resource.
     * @throws IOException on I/O error.
     */
    public InputStream getInputStream(String name) throws IOException {
        InputStream stream = clazz.getResourceAsStream(name);
        if (stream == null) {
            throw new IOException(res.getStr("resource.not.found",
                name,clazz.getPackage().getName()));
        }
        
        return stream;
    }
    
    /**
     * Loads a resource as string buffer.
     * 
     * @param name The resource name.
     * @return The loaded resource.
     * @throws IOException on I/O error.
     */
    public StringBuffer loadAsStringBuffer(String name) throws IOException {
        StringBuffer buffer = new StringBuffer();
        
        InputStream stream = getInputStream(name);
        InputStreamReader reader = new InputStreamReader(stream,"UTF-8");
        while (true) {
            int value = reader.read();
            if (value == -1) {
                break;
            }
                
            buffer.append((char)value);
        }
        
        return buffer;
    }
    
    /**
     * Loads a resource as string.
     *
     * @param name The resource name.
     * @return The loaded resource.
     * @throws IOException on I/O error.     
     */
    public String loadAsString(String name) throws IOException {
        return loadAsStringBuffer(name).toString();
    }
    
    /**
     * Loads a resource as string.
     *
     * @param name The resource name.
     * @return The loaded resource.
     * @throws IOException on I/O error.     
     */
    public static String loadAsString(Class<?>clazz,String name)
        throws IOException {
    //
        ResourceLoader loader = new ResourceLoader(clazz);
        return loader.loadAsString(name);
    }    
    
    /**
     * Copies a resource to a directory.
     *
     * @param dstDir The directory to which to copy.
     * @param name The name of the resource to copy.
     * @throws IOException on I/O error.
     */
    public void copyResource(File dstDir,String name) throws IOException {
        File dstFile = new File(dstDir,name);
        
        InputStream input = getInputStream(name);
        try {
            FileUtils.copyInputStreamToFile(input,dstFile);
        } catch (IOException exception) {
            throw exception;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException exception) {
                    throw exception;
                }
            }
        }
    }    
    
    /**
     * Loads an image from a resource.
     * 
     * @param name The resource name.
     * @return The loaded image.
     * @throws IOException if the image cannot be loaded.
     */
    public BufferedImage loadImage(String name) throws IOException {
        return ImageIO.read(getInputStream(name));
    }
    
    /** */
    @Override
    public String toString() {
        return clazz.getName();
    }
}
