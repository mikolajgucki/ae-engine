package com.andcreations.ae.plugin.ui.studio.plugin.layout;

import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.UIManager;

import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class LayoutComponent extends JComponent {
    /** */
    private class DrawData {
        /** */
        private ComponentLuaResult component;
        
        /** */
        private Rectangle compRect;
        
        /** */
        private String id;
        
        /** */
        private Rectangle idRect;
        
        /** */
        private DrawData(ComponentLuaResult component,Rectangle compRect) {
            this.component = component;
            this.compRect = compRect;
        }
    }
    
    /** */
    private LayoutComponentListener listener;
    
    /** */
    private final int margin;
    
    /** */
    private Color idColor;
    
    /** */
    private Color backgroundColor;
    
    /** */
    private Color componentColor;
    
    /** */
    private Color highlightColor;
    
    /** */
    private Color outlineColor;
    
    /** */
    private Stroke defaultStroke;
    
    /** */
    private Stroke outlineStroke;
    
    /** */
    private ComponentLuaResult root;
    
    /** */
    private int rootWidth;
    
    /** */
    private int rootHeight;
    
    /** */
    private List<DrawData> drawDataList = new ArrayList<>();
    
    /** */
    private Point pointerLocation = new Point();
    
    /** */
    LayoutComponent(int margin,LayoutComponentListener listener) {
        this.listener = listener;
        this.margin = margin;
        create();
    }
    
    /** */
    private void create() {
        defaultStroke = new BasicStroke();
        outlineStroke = new BasicStroke(1,BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER,10,new float[]{5,5},0);
        
        addMouseMotionListener(new MouseMotionListener() {
            /** */
            @Override
            public void mouseMoved(MouseEvent event) {
                LayoutComponent.this.mouseMoved(event.getX(),event.getY());
            }
            
            /** */
            @Override
            public void mouseDragged(MouseEvent event) {
                LayoutComponent.this.mouseMoved(event.getX(),event.getY());
            }
        });
        
        addMouseListener(new MouseAdapter() {
            /** */
            @Override
            public void mouseExited(MouseEvent event) {
                LayoutComponent.this.mouseMoved(-1,-1);
            }
        });
    }
    
    /** */
    private void mouseMoved(int x,int y) {
        pointerLocation.setLocation(x,y);
        repaint();
    }
    
    /** */
    private void getColors() {
        if (idColor != null) {
            return;
        }
        
        backgroundColor = UIColors.blend(Color.BLACK,getBackground(),0.05);
        componentColor = UIColors.blend(
            UIManager.getDefaults().getColor("Label.foreground"),
            getBackground(),0.35);
        highlightColor = UIColors.blend(
            UIManager.getDefaults().getColor("Label.foreground"),
            getBackground(),0.7);
        idColor = UIManager.getDefaults().getColor("Label.foreground");
        outlineColor = UIColors.blend(
            UIManager.getDefaults().getColor("Label.foreground"),
            getBackground(),0.1);
    }
    
    /** */
    void setRoot(ComponentLuaResult root,int rootWidth,int rootHeight) {
        this.root = root;
        this.rootWidth = rootWidth;
        this.rootHeight = rootHeight;
    }
    
    /** */
    private void drawBackground(Graphics2D graphics,int x,int y,
        int width,int height) {
    //
        graphics.setColor(backgroundColor);
        graphics.fillRect(x,y,width,height);
    }
    
    /** */
    private void drawOutline(Graphics2D graphics,int x,int y,
        int width,int height) {
    //
        graphics.setColor(outlineColor);
        graphics.setStroke(outlineStroke);
        graphics.drawRect(x,y,width - 1,height - 1);
    }
    
    /** */
    private Rectangle getComponentRect(ComponentLuaResult component,
        int x,int y,int width,int height) {
    //
        int compX = (int)(width * component.getBounds().getX());
        int compY = (int)(height * component.getBounds().getY());
        int compWidth = (int)(width * component.getBounds().getWidth());
        int compHeight = (int)(height * component.getBounds().getHeight());
        
    // upside down to fit the layout
        compY = height - compY - compHeight - 1;
        
        return new Rectangle(compX + x,compY + y,compWidth,compHeight);
    }
    
    /** */
    private void fillDrawData(Graphics2D graphics,ComponentLuaResult component,
        int x,int y,int width,int height) {
    //
        if (component.hasChildren() == false) {
        // rectangle
            Rectangle compRect = getComponentRect(component,x,y,width,height);
            
        // data
            DrawData drawData = new DrawData(component,compRect);
            drawDataList.add(drawData);
            
        // identifier
            if (component.getId() != null) {
                drawData.id = component.getId();
                char[] chars = component.getId().toCharArray();
                
            // metrics
                FontMetrics metrics = graphics.getFontMetrics();
                int idWidth = metrics.charsWidth(chars,0,chars.length);
                int idHeight = metrics.getAscent() + metrics.getDescent(); 
                
                int idX = compRect.x + (compRect.width - idWidth) / 2;
                int idY = compRect.y + (compRect.height - idHeight) / 2;
                
                drawData.idRect = new Rectangle(idX,idY,idWidth,idHeight);
            }
        }
        else {
            for (ComponentLuaResult child:component.getChildren()) {
                fillDrawData(graphics,child,x,y,width,height);
            }
        }           
    }
    
    /** */
    private void drawComponent(Graphics2D graphics,DrawData drawData) {
        graphics.drawRect(
            drawData.compRect.x,drawData.compRect.y,
            drawData.compRect.width,drawData.compRect.height);
    }
    
    /** */
    private void drawComponents(Graphics2D graphics) {
        List<ComponentLuaResult> highlighted = new ArrayList<>();
        graphics.setStroke(defaultStroke);
        
        graphics.setColor(componentColor);
        for (DrawData drawData:drawDataList) {
            if (drawData.compRect.contains(pointerLocation) == false) {
                drawComponent(graphics,drawData);
            }
        }
            
        graphics.setColor(highlightColor);
        for (DrawData drawData:drawDataList) {
            if (drawData.compRect.contains(pointerLocation)) {
                highlighted.add(drawData.component);
                drawComponent(graphics,drawData);
            }
        }
        
        listener.componentsHighlighted(highlighted);
    }
    
    /** */
    private List<DrawData> getIdIntersections(DrawData drawDataToChk) {
        List<DrawData> intersections = new ArrayList<>();
        for (DrawData drawData:drawDataList) {
            if (drawData.idRect == null) {
                continue;
            }
            
            if (drawData.idRect.intersects(drawDataToChk.idRect) == true) {
                intersections.add(drawData);
            }
        }
        
        return intersections;
    }
    
    /** */
    private void drawIds(Graphics2D graphics) {
        for (DrawData drawData:drawDataList) {
            if (drawData.id == null) {
                continue;
            }
            
        // color, stroke
            graphics.setColor(componentColor);
            graphics.setStroke(defaultStroke);
            
        // if overlap
            List<DrawData> intersections = getIdIntersections(drawData);
            if (intersections.size() > 1) {
                if (intersections.get(0) == drawData) {
                    int dx = drawData.compRect.x + drawData.compRect.width / 2;
                    int dy = drawData.compRect.y + drawData.compRect.height / 2;
                    
                // draw 3 dots
                    graphics.drawLine(dx - 2,dy,dx - 2,dy);
                    graphics.drawLine(dx,dy,dx,dy);
                    graphics.drawLine(dx + 2,dy,dx + 2,dy);
                }
                
                continue;
            }
            
        // if doesn't fit
            if (drawData.idRect.width >= drawData.compRect.width ||
                drawData.idRect.height >= drawData.compRect.height) {
            //
                int dx = drawData.compRect.x + drawData.compRect.width / 2;
                int dy = drawData.compRect.y + drawData.compRect.height / 2;
            // draw a dot
                graphics.drawLine(dx,dy,dx,dy);
            }
            else { // fits
                FontMetrics metrics = graphics.getFontMetrics();
                char[] chars = drawData.id.toCharArray();
                graphics.drawChars(chars,0,chars.length,
                    drawData.idRect.x,drawData.idRect.y + metrics.getAscent());
            }
        }
    }
    
    /** */
    @Override
    protected void paintComponent(Graphics graphics) {
        getColors();
        
    // margins
        int viewWidth = getWidth() - margin * 2;
        int viewHeight = getHeight() - margin * 2;
        
    // aspect
        double rootAspect = (double)rootHeight / (double)rootWidth;
        
    // fit
        int newRootHeight = (int)(rootAspect * viewWidth);
        int newRootWidth = viewWidth;
        if (newRootHeight > viewHeight) {
            newRootWidth = (int)(viewHeight / rootAspect);
            newRootHeight = viewHeight;
        }
        
    // rectangle in which to draw
        int x = (viewWidth - newRootWidth) / 2;
        int y = (viewHeight - newRootHeight) / 2;
        int width = newRootWidth;
        int height = newRootHeight;
        
        Graphics2D graphics2D = (Graphics2D)graphics;
    // draw data
        drawDataList.clear();
        fillDrawData(graphics2D,root,x,y,width,height);
        
    // draw
        drawBackground(graphics2D,x,y,width,height);
        drawOutline(graphics2D,x,y,width,height);
        drawComponents(graphics2D);
        drawIds(graphics2D);
    }
}