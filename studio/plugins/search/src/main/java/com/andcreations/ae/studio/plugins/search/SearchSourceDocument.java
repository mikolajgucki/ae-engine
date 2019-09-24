package com.andcreations.ae.studio.plugins.search;

import java.io.IOException;

import javax.swing.ImageIcon;

/**
 * The interface for a search source document.
 *
 * @author Mikolaj Gucki
 */
public interface SearchSourceDocument {
    /**
     * Gets the document identifier.
     *
     * @return The document identifier.
     */
    String getId();
    
    /**
     * Gets the document name (displayed in results).
     *
     * @return The document name.
     */
    String getName();
    
    /**
     * Gets the icon which depicts the document.
     *
     * @return The icon.
     */
    ImageIcon getIcon();
    
    /**
     * Gets the text to search.
     *
     * @return The document text.
     * @throws IOException on I/O error.
     */
    String getText() throws IOException;
}