package com.andcreations.ae.studio.plugins.console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class ConsoleComponent extends JPanel {
    /** */
    private JTextPane textPane;
    
    /** */
    private FindPanel findPanel;
    
    /** */
    private Map<String,Style> styles = new HashMap<>();    
    
    /** */
    private LinkList links = new LinkList();
    
    /** */
    private boolean scrollLock;
    
    /** */
    private int maximumLines = -1;
    
    /** */
    public ConsoleComponent() {
        create();
    }
    
    /** */
    private void create() {
        setLayout(new BorderLayout());
        
    // text pane
        createTextPane();
        JScrollPane scroll = new JScrollPane(textPane);
        add(scroll,BorderLayout.CENTER);
        
    // find panel
        findPanel = new FindPanel(textPane);
        findPanel.setVisible(false);
        add(findPanel,BorderLayout.SOUTH);            
        
    // mouse listener
        createMouseListener();
    }
    
    /** */
    private void createTextPane() {
        textPane = new JTextPane();
        textPane.setEditable(false);
        
    // font
        Font font = new Font(Font.MONOSPACED,Font.PLAIN,12);
        textPane.setFont(font);
    }
    
    /** */
    private void createMouseListener() {
        textPane.addMouseListener(new MouseAdapter() {
            /** */
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 1) {
                    int position = textPane.viewToModel(event.getPoint());
                    Link link = links.find(position);
                    if (link != null) {
                        link.getLinkListener().linkClicked(new LinkEvent());
                    }
                }
            }
        });
        
        textPane.addMouseMotionListener(new MouseMotionAdapter() {
            /** */
            @Override
            public void mouseMoved(MouseEvent event) {
                int position = textPane.viewToModel(event.getPoint());
                Link link = links.find(position);
                if (link != null) {
                    textPane.setCursor(Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR));
                }
                else {
                    textPane.setCursor(Cursor.getPredefinedCursor(
                        Cursor.DEFAULT_CURSOR));
                }
            }
        });
    }
    
    /** */
    public synchronized void setMaximumLines(int maximumLines) {
        this.maximumLines = maximumLines;
    }
    
    /** */
    public void setFindPanelVisible(boolean visible) {
        findPanel.setVisible(visible);
        if (findPanel.isVisible() == false) {
            requestFocus();
        }
    }
    
    /** */
    public void addStyle(String name,Color foreground,Color background,
        boolean underline,boolean bold) {
    //
        if (styles.containsKey(name) == true) {
            throw new IllegalArgumentException(String.format(
                "Style %s already exists",name));
        }
    
        Style style = textPane.addStyle(null,null);
        if (foreground != null) {
            StyleConstants.setForeground(style,foreground);
        }
        if (background != null) {
            StyleConstants.setBackground(style,background);
        }
        StyleConstants.setUnderline(style,underline);
        StyleConstants.setBold(style,bold);
        
        styles.put(name,style);
    }
    
    /** */
    public void addStyle(String name,Color foreground,Color background,
        boolean underline) {
    //
        addStyle(name,foreground,background,underline,false);
    }
    
    /** */
    public void addStyle(String name,Color foreground,boolean underline) {
        addStyle(name,foreground,null,underline,false);
    }

    
    /** */
    public void addStyle(String name,Color foreground) {
        addStyle(name,foreground,null,false,false);
    }    
    
    /** */
    protected Style getStyle(String styleName) {
        Style style = styles.get(styleName);
        if (style == null) {
            throw new IllegalArgumentException(String.format(
                "Style %s does not exist",styleName));
        }
        
        return style;
    }
    
    /** */
    private void goToBottom() {
        if (textPane.getCaret() != null) {
            textPane.setCaretPosition(textPane.getDocument().getLength());
        }
    }
    
    /** */
    private int getLineCount() {
        Document document = textPane.getDocument();
        Element root = document.getDefaultRootElement();
        
        return root.getElementCount();
    }
    
    /** */
    private void removeFirstList() {
        Document document = textPane.getDocument();
        Element root = document.getDefaultRootElement();
        Element line = root.getElement(0);
        
        try {
            document.remove(0,line.getEndOffset());
        } catch (BadLocationException exception) {
            exception.printStackTrace();
        }        
    }
    
    /** */
    public synchronized int append(String text,Style style,
        LinkListener listener) {
    //
        int offset = -1;
        try {
            Document document = textPane.getDocument();
            offset = document.getLength();
            document.insertString(offset,text,style);
            if (scrollLock == false) {
                goToBottom();
            }
        } catch (BadLocationException exception) {
            // never happens
        }
        
        if (listener != null) {
            addLink(offset,text.length(),style,listener);
        }
        
    // limit the number of lines
        if (maximumLines > 0) {
            int count =  getLineCount() - maximumLines;
            while (count > 0) {
                removeFirstList();
                count--;
            }
        }
        
        return offset;
    }    
    
    /** */
    public int append(String text,String styleName,LinkListener listener) {
        Style style = getStyle(styleName);
        return append(text,style,listener);
    }
    
    /** */
    public int append(String text) {
        return append(text,(Style)null,null);
    }
    
    /** */
    public int append(String text,String styleName) {
        return append(text,styleName,null);
    }    
    
    /** */
    private void addLink(int offset,int length,Style style,
        LinkListener listener) {
    //
        links.add(new Link(offset,length,listener));
        textPane.getStyledDocument().setCharacterAttributes(
            offset,length,style,true);
    }    
    
    /** */
    public void addLink(int offset,int length,String styleName,
        LinkListener listener) {
    //
        Style style = getStyle(styleName);
        addLink(offset,length,style,listener);
    }
    
    /** */
    public void clear() {
        try {
            Document document = textPane.getDocument();
            document.remove(0,document.getLength());
            links.clear();
            findPanel.clear();
        } catch (BadLocationException exception) {
            // never happens
        }            
    }
    
    /** */
    public synchronized void setScrollLock(boolean scrollLock) {
        this.scrollLock = scrollLock;
    }
}
