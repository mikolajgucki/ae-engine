package com.andcreations.ae.studio.plugins.search.common;

/**
 * @author Mikolaj Gucki
 */
public class SearchOccurence {
    /** */
    private String text;
    
    /** */
    private int lineNo;
    
    /** */
    private int start;

    /** */
    private int length;

    /** */
    SearchOccurence(int lineNo,String text,int start,int length) {
        this.lineNo = lineNo;
        this.text = text;
        this.start = start;
        this.length = length;
    }
    
    /** */
    public String getText() {
        return text;
    }
    
    /** */
    public int getLineNo() {
        return lineNo;
    }
    
    /** */
    public int getStart() {
        return start;
    }
    
    /** */
    public int getLength() {
        return length;
    }
    
    /** */
    public int getEnd() {
        return start + length;
    }
    
    /** */
    public String getPrefix() {
        return text.substring(0,start);
    }
    
    /** */
    public String getMatch() {
        return text.substring(start,getEnd());
    }
    
    /** */
    public String getSuffix() {
        return text.substring(getEnd(),text.length());
    }
}