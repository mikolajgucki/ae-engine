package com.andcreations.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class XMLParser {
    /** */
    private static BundleResources res = new BundleResources(XMLParser.class);    
    
    /** The XML document builder factory. */
    private static DocumentBuilderFactory documentBuilderFactory;
    
    /**
     * Gets a document builder factory.
     *
     * @return The document builder factory.
     * @throws XMLException if the document builder factory cannot be
     *   instantiated.
     */
    private static DocumentBuilderFactory getDocumentBuilderFactory()
        throws XMLException {
    //
        if (documentBuilderFactory == null) {
            try {
                documentBuilderFactory = DocumentBuilderFactory.newInstance();
            } catch (FactoryConfigurationError exception) {
                throw new XMLException(res.getStr(
                    "failed.to.get.parser"),exception);
            }
        }
        
        return documentBuilderFactory;
    }
      
    /**
     * Gets a new document builder.
     *
     * @return The new document builder.
     * @throws XMLException if the document builder cannot be created.
     */
    private static DocumentBuilder getDocumentBuilder() throws XMLException {
        DocumentBuilderFactory factory = getDocumentBuilderFactory();        
        
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException exception) {
            throw new XMLException(res.getStr(
                "failed.to.get.parser"),exception);
        }
        
        return builder;
    }    
    
    /** */
    public static Document parse(InputStream stream) throws XMLException {
        DocumentBuilder documentBuilder = getDocumentBuilder();
        Document document = null;
        try {
            document = documentBuilder.parse(stream);
        } catch (Exception exception) {
            throw new XMLException(res.getStr("failed.to.parse",
                exception.getMessage()),exception);
        }
        
        return document;
    }
    
    /** */
    private static void failedToParseFile(File file,Exception exception)
        throws XMLException {
    //
        throw new XMLException(res.getStr("failed.to.parse.file",
            file.getAbsolutePath(),exception.getMessage()),
            exception);
    }
    
    /** */
    public static Document parse(File file) throws XMLException {
        FileInputStream input = null;
        Document document = null;
        try {
            input = new FileInputStream(file);
            document = parse(input);
        } catch (XMLException exception) {
            failedToParseFile(file,exception);
        } catch (IOException exception) {
            failedToParseFile(file,exception);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException exception) {
                    failedToParseFile(file,exception);
                }
            }
        }
        
        return document;
    }
}