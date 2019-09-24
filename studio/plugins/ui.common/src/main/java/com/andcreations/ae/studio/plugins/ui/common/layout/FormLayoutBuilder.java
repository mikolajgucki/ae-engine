package com.andcreations.ae.studio.plugins.ui.common.layout;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Mikolaj Gucki
 */
public class FormLayoutBuilder {
    /** */
    private List<String[]> data = new ArrayList<>();    
    
    /** */
    private int width;
    
    /** */
    private int height;
    
    /** */
    private PanelBuilder builder;
    
    /** */
    private CellConstraints cc;
    
    /** */
    public FormLayoutBuilder(String src) throws IOException {
        parse(src);
        create();
    }
    
    /** */
    private void parse(String src) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(src));
        
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            
            String[] tokens = line.split(",");
            data.add(tokens);
        }
        
        width = data.get(0).length;
        height = data.size();
    }
    
    /** */
    private void create() {
        FormLayout layout = new FormLayout(getColumnsSpec(),getRowsSpec());
        builder = new PanelBuilder(layout);
        cc = new CellConstraints();
    }
    
    /** */
    private String getColumnsSpec() {
        String spec = "";
        for (String cell:data.get(0)) {
            if (spec.isEmpty() == false) {
                spec += ",";
            }
            spec += cell;
        }
        
        return spec;
    }
    
    /** */
    private String getRowsSpec() {
        String spec = "";
        
        for (int index = 1; index < data.size(); index++) {
            String[] row = data.get(index);
            if (spec.isEmpty() == false) {
                spec += ",";
            }
            spec += row[0];
        }
        
        return spec;
    }
    
    /** */
    private String[] getCellValues(int x,int y) {
        String[] row = data.get(y);
        if (x < row.length) {
            return row[x].trim().split(":");
        }
        
        return null;        
    }
    
    /** */
    private boolean cellContains(int x,int y,String... values) {
        String[] cellValues = getCellValues(x,y);
        if (cellValues != null) {
            // ignore the identifier
            for (int index = 1; index < cellValues.length; index++) {
                String cellValue = cellValues[index];
                for (String value:values) {
                    if (cellValue.equals(value)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /** */
    private String getId(int x,int y) {
        String[] values = getCellValues(x,y);
        return values != null ? values[0] : null;
    }
    
    /** */
    private CellConstraints.Alignment getHAlignment(int x,int y) {
        if (cellContains(x,y,"r")) {
            return CellConstraints.RIGHT;
        }
        if (cellContains(x,y,"l")) {
            return CellConstraints.LEFT;
        }
        if (cellContains(x,y,"c")) {
            return CellConstraints.CENTER;
        }
        
        return CellConstraints.DEFAULT;
    }
    
    /** */
    private CellConstraints.Alignment getVAlignment(int x,int y) {
        if (cellContains(x,y,"b")) {
            return CellConstraints.BOTTOM;
        }
        if (cellContains(x,y,"t")) {
            return CellConstraints.TOP;
        }
        if (cellContains(x,y,"m")) {
            return CellConstraints.CENTER;
        }
        
        return CellConstraints.DEFAULT;
    }
    
    /** */
    public CellConstraints cc(String id) {
        Point min = null;
        Point max = null;
                
        for (int y = 1; y < height; y++) {
            for (int x = 1; x < width + 1; x++) {
                String cellId = getId(x,y);
                if (cellId != null) {
                    if (cellId.equals(id)) {
                    // min
                        if (min == null) {
                            min = new Point(x,y);
                        }
                        if (x < min.x) {
                            min.x = x;
                        }
                        if (y < min.y) {
                            min.y = y;
                        }
                        
                    // max
                        if (max == null) {
                            max = new Point(x,y);
                        }
                        if (x > max.x) {
                            max.x = x;
                        }
                        if (y > max.y) {
                            max.y = y;
                        }
                    }
                }
            }
        }
        
        if (min == null || max == null) {
            throw new IllegalStateException(String.format(
                "Cell(s) of identifier %s not found",id)); 
        }
        
        return cc.xywh(min.x,min.y,max.x - min.x + 1,max.y - min.y + 1,
            getHAlignment(min.x,min.y),getVAlignment(min.x,min.y));
    }
    
    /** */
    public JLabel addLabel(String label,String id) {
        return builder.addLabel(label,cc(id));
    }   
    
    /** */
    public void addSeparator(String label,String id) {
        builder.addSeparator(label,cc(id));
    }
    
    /** */
    public void add(JComponent component,String id) {
        builder.add(component,cc(id));
    }
    
    /** */
    public JPanel getPanel() {
        return builder.getPanel();
    }
}