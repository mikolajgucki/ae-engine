package com.andcreations.ae.studio.plugins.search;

import java.util.List;

/**
 * The interface for a search source.
 *
 * @author Mikolaj Gucki
 */
public interface SearchSource {
    /**
     * Gets all the documents to search.
     *
     * @return The documents.
     */
    List<SearchSourceDocument> getDocuments();
    
    /**
     * Opens a document and highlights an occurence.
     *
     * @param id The document identifier.
     * @param line The line number.
     * @param start The index of the begin character.
     * @param end The index of the last character.
     */
    void openDocument(String id,int line,int start,int end);
}