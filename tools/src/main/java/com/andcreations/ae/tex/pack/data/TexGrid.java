package com.andcreations.ae.tex.pack.data;

import java.util.Map;

import com.andcreations.ae.tex.pack.TexPackException;
import com.andcreations.resources.BundleResources;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Mikolaj Gucki
 */
public class TexGrid {
    /** */
    private BundleResources res = new BundleResources(TexGrid.class);
    
    /** */
    private Map<String,String> rows;
    
    /** */
    public Map<String,String> getRows() {
        return rows;
    }
    
    /** */
    private String findRow(int rowIndex) throws TexPackException {
        for (String key:rows.keySet()) {
            int index;
            try {
                index = Integer.parseInt(key);
            } catch (NumberFormatException exception) {
                throw new TexPackException(res.getStr("invalid.row",key));                
            }
            if (index == rowIndex) {
                return rows.get(key);
            }
        }
        
        return null;
    }
    
    /** */
    @JsonIgnore
    public int getHeight() {
        return rows.size();
    }
    
    /** */
    @JsonIgnore 
    public String[] getRow(int rowIndex) throws TexPackException {
        String row = findRow(rowIndex);
        if (row == null) {
            throw new TexPackException(res.getStr("missing.row",
                Integer.toString(rowIndex))); 
        }
        
        String[] values = row.split(",");
        return values;
    }
    
    /** */
    @JsonIgnore
    public int getWidth() throws TexPackException {
        int width = -1;
        for (int rowIndex = 0; rowIndex < getHeight(); rowIndex++) {
            String[] values = getRow(rowIndex);
            if (width == -1) {
                width = values.length;
            }
            if (values.length != width) {
                throw new TexPackException(res.getStr("invalid.value.count",
                    Integer.toString(rowIndex))); 
            }
        }
        
        return width;
    }
}