package com.andcreations.ae.studio.plugins.text.editor;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.UIManager;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class OverviewRuler extends JComponent {
    /** */
    private OverviewRulerListener listener;
    
    /** */
    private int scrollBarSize;
    
    /** */
    private List<EditorAnnotation> annotations = new ArrayList<>();
    
    /** */
    OverviewRuler(OverviewRulerListener listener) {
        this.listener = listener;
        create();
    }
    
    /** */
    private void create() {
    // size
        scrollBarSize = ((Integer)UIManager.get("ScrollBar.width")).intValue();
        int width = (int)(scrollBarSize * 0.75);
        setPreferredSize(new Dimension(width,scrollBarSize));
        
    // mouse listener
        addMouseListener(new MouseAdapter() {
            /** */
            @Override
            public void mouseClicked(MouseEvent event) {
                OverviewRuler.this.mouseClicked(event.getButton(),
                    event.getX(),event.getY());
            }
        });
        
    // mouse motion listener
        addMouseMotionListener(new MouseMotionListener() {
            /** */
            @Override
            public void mouseMoved(MouseEvent event) {
                OverviewRuler.this.mouseMoved(event.getX(),event.getY());
            }
            
            /** */
            @Override
            public void mouseDragged(MouseEvent event) {
            }
        });
    }
    
    /** */
    void addAnnotation(EditorAnnotation annotation) {
        annotations.add(annotation);
        repaint();
    }
    
    /** */
    void removeAnnotation(EditorAnnotation annotation) {
        annotations.remove(annotation);
        repaint();
    }
    
    /** */
    private Rectangle getAnnotationRect(EditorAnnotation annotation) {
        int height = getHeight() - scrollBarSize * 2;
        int y = scrollBarSize +
            (int)(annotation.getNormalizedPosition() * height);
            
        final int xmargin = 2;
        final int ysize = 4;
        return new Rectangle(xmargin,y - ysize / 2,
            getWidth() - xmargin * 2,ysize);
    }
    
    /** */
    private void drawAnnotation(Graphics graphics,EditorAnnotation annotation) {
        Rectangle rect = getAnnotationRect(annotation);
        graphics.setColor(annotation.getColor().getAWTColor());
        graphics.fillRect(rect.x,rect.y,rect.width,rect.height);
    }
    
    /** */
    @Override
    public void paint(Graphics graphics) {
        for (EditorAnnotation annotation:annotations) {
            drawAnnotation(graphics,annotation);
        }
    }
    
    /** */
    void repaint(List<EditorAnnotation> annotations) {
        this.annotations = annotations;
        if (this.annotations == null) {
            return;
        }
        repaint();
    }
    
    /** */
    private void mouseMoved(int x,int y) {
        if (annotations == null) {
            return;
        }
    
    // for each annotation
        for (EditorAnnotation annotation:annotations) {
            Rectangle rect = getAnnotationRect(annotation);
            if (rect.contains(x,y) == true) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setToolTipText(annotation.getTooltip());
                return;
            }
        }
        
        setCursor(Cursor.getDefaultCursor());
        setToolTipText(null);
    }

    /** */
    private void mouseClicked(int button,int x,int y) {
        if (annotations == null) {
            return;
        }
        
        if (button == MouseEvent.BUTTON1) {
        // for each annotation
            for (EditorAnnotation annotation:annotations) {
                Rectangle rect = getAnnotationRect(annotation);
                if (rect.contains(x,y) == true) {
                    listener.annotationClicked(annotation);
                    return;
                }
            }
        }
    }
}