package com.andcreations.ae.studio.plugins.text.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.GutterIconInfo;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.file.cache.TextFileCache;
import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.resources.BundleResources;

/**
 * The component for the text editor view.
 *
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class EditorComponent extends JPanel {
    /** */
    private final BundleResources res =
        new BundleResources(EditorComponent.class);      
    
    /** */
    private EditorCfg cfg;

    /** */
    private TextEditorDocument editorDocument;
    
    /** */
    private EditorComponentListener listener;
    
    /** */
    private File file;
    
    /** */
    private JLabel errorLabel;
    
    /** */
    private RSyntaxTextArea textArea;
    
    /** */
    private RTextScrollPane textAreaScrollPane;
    
    /** */
    private GutterIconList gutterIconList;
    
    /** */
    private OverviewRuler overviewRuler;
    
    /** */
    private List<EditorAnnotation> annotations = new ArrayList<>();    

    /** */
    EditorComponent(EditorCfg cfg) {
        this.cfg = cfg;
        create();
    }
    
    /** */
    private void createErrorPanel() {
        errorLabel = new JLabel();
        errorLabel.setForeground(UIColors.error());
        errorLabel.setIcon(Icons.getIcon(DefaultIcons.ERROR));
        final int border = 4;
        errorLabel.setBorder(BorderFactory.createEmptyBorder(
            border,border,border,border));
        add(errorLabel,BorderLayout.NORTH);
    }
    
    /** */
    private void createTextAreaPanel() {
    // document
        TextEditorDocumentListener documentListener = 
            new TextEditorDocumentListener() {
                /** */
                @Override
                public void removingText(int offset,int length,String text)
                    throws BadLocationException {
                //
                }
            };
        editorDocument = new TextEditorDocument(
            cfg.getEditorSyntax().getTextAreaEditingStyle(),documentListener);
        
    // text area
        textArea = new RSyntaxTextArea(1,1);
        textArea.setDocument(editorDocument);
        textArea.setSyntaxEditingStyle(
            cfg.getEditorSyntax().getTextAreaEditingStyle());
        ToolTipManager.sharedInstance().registerComponent(textArea);
        
    // caret listener
        textArea.addCaretListener(new CaretListener() {
            /** */
            @Override
            public void caretUpdate(CaretEvent event) {
                updateCurrentLineHighlight();
            }
        });
        
    // configure
        RSyntexTextAreaCfg cfg = new RSyntexTextAreaCfg(textArea);
        cfg.configure();
        
    // scroll
        textAreaScrollPane = new RTextScrollPane(textArea);
        textAreaScrollPane.setIconRowHeaderEnabled(true);
        textAreaScrollPane.getGutter().setBackground(
            Colors.getBackgroundColor());
        add(textAreaScrollPane,BorderLayout.CENTER);
        
        textAreaScrollPane.setIconRowHeaderEnabled(true);
        textAreaScrollPane.getGutter().setBookmarkingEnabled(true);        
        
    // overview ruler
        overviewRuler = new OverviewRuler(new OverviewRulerListener() {
            /** */
            @Override
            public void annotationClicked(EditorAnnotation annotation) {
                EditorComponent.this.annotationClicked(annotation);
            }
        });
        add(overviewRuler,BorderLayout.EAST);
    }
    
    /** */
    private void createKeyListener() {
        KeyAdapter keyListener = new KeyAdapter() {
            /** */
            @Override
            public void keyPressed(KeyEvent event) {
                if (UIKeys.isMenuOnly(event) &&
                    listener.keyPressed(event) == true) {
                //
                    return;
                }
                
            // go to line
                if (UIKeys.isMenuOnly(event) &&
                    event.getKeyCode() == KeyEvent.VK_L) {
                //
                    TextEditorGoToLineDialog.showDialog();
                    return;
                }
                
            // find
                if (UIKeys.isMenuOnly(event) &&
                    event.getKeyCode() == KeyEvent.VK_F) {
                //
                    TextEditor.get().showFindDialog();
                    return;
                }
                
            // replace
                if (UIKeys.isMenuOnly(event) &&
                    event.getKeyCode() == KeyEvent.VK_R) {
                //
                    TextEditor.get().showReplaceDialog();
                    return;
                }
                
            // save
                if (UIKeys.isMenuOnly(event) &&
                    event.getKeyCode() == KeyEvent.VK_S) {
                //
                    saveFile();
                    return;
                }
            }
            
            /** */
            @Override
            public void keyReleased(KeyEvent event) {
                if (UIKeys.isMenuOnly(event) &&
                    listener.keyReleased(event) == true) {
                //
                    return;
                }
            }
            
            /** */
            @Override
            public void keyTyped(KeyEvent event) {
                if (UIKeys.isMenuOnly(event) &&
                    listener.keyTyped(event) == true) {
                //
                    return;
                }
            }            
        };
        
        textArea.addKeyListener(keyListener);        
        addKeyListener(keyListener);
    }
    
    /** */
    private void createDocumentListener() {
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            /** */
            @Override
            public void changedUpdate(DocumentEvent event) {
            }
            
            /** */
            @Override
            public void insertUpdate(DocumentEvent event) {
                textChanged();
            }
            
            /** */
            @Override
            public void removeUpdate(DocumentEvent event) {
                textChanged();
            }
        });
    }
    
    /** */
    private void createGutterIconList() {
        gutterIconList = new GutterIconList(new GutterIconListListener() {
            /** */
            @Override
            public void removedGutterIcon(GutterIcon icon) {
                try {
                    updateGutterIcons();
                } catch (BadLocationException exception) {
                }                
                listener.gutterIconRemoved(icon);
            }
        });
    }
    
    /** */
    private void create() {
        setLayout(new BorderLayout());
        
        createErrorPanel();
        createTextAreaPanel();
        createKeyListener();
        createDocumentListener();
        createGutterIconList();
        
        file = new File(cfg.getFilePath());        
        try {
            file = file.getCanonicalFile();
        } catch (IOException exception) {
        }
        loadFile();
        
        textArea.discardAllEdits();
        textArea.requestFocus();
    }
    
    /** */
    void setEditorComponentListener(EditorComponentListener listener) {
        this.listener = listener;
    }
    
    /** */
    private void setError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        
        textAreaScrollPane.setVisible(false);
        requestFocus();
    }
    
    /** */
    private void setText(String text) {
        Document doc = textArea.getDocument();
        try {
            doc.remove(0,doc.getLength());
            doc.insertString(0,text,null);
        } catch (BadLocationException exception) {
            Log.error(exception.toString());
        }
        textAreaScrollPane.setVisible(true);
        errorLabel.setVisible(false);
    }
    
    /** */
    private String getText() {
        Document doc = textArea.getDocument();
        try {
            return doc.getText(0,doc.getLength());
        } catch (BadLocationException exception) {
            Log.error(exception.toString());
            return null;
        }
    }
    
    /** */
    private void loadFile() {
        String text;
        try {
            text = TextFileCache.get().read(file);
        } catch (FileNotFoundException exception) {
            setError(res.getStr("file.not.found",file.getAbsolutePath()));
            return;            
        } catch (IOException exception) {
            setError(res.getStr("failed.to.load",file.getAbsolutePath(),
                exception.toString()));
            return;            
        }        
        
        setText(text);
    }
    
    /** */
    boolean saveFile() {
        try {
            TextFileCache.get().write(file,getText());
        } catch (IOException exception) {
            CommonDialogs.error(
                res.getStr("save.failed.title",file.getAbsolutePath()),
                res.getStr("save.failed.msg",exception.getMessage()));
            return false;            
        }        
        
        if (listener != null) {
            listener.fileSaved();
        }
        return true;
    }
    
    /** */
    private void textChanged() {
    // gutter icons
        try {
            updateGutterIconLines();
        } catch (BadLocationException exception) {
        }
        
    // annotations
        updateAnnotations();
        repaintAnnotations();
        
    // listener
        if (listener != null) {
            listener.textChanged();
        }        
    }
    
    /** */
    void clearMarkAllSelection() {
        SearchContext context = new SearchContext("");
        SearchEngine.find(textArea,context);
    }
    
    /** */
    void viewFocusGained() {
        setFocusable(true);
        requestFocusInWindow();
        
        if (textAreaScrollPane.isVisible() == true) {
            textArea.requestFocusInWindow();
        }
        else {
            requestFocus();
        }
    }
    
    /** */
    void viewFocusLost() {
    }
    
    /** */
    void viewShown() {
        goToLine(1);
    }
    
    /** */
    int getLineOfOffset(int offset) throws BadLocationException {
        return textArea.getLineOfOffset(offset) + 1;
    }

    /** */
    private int getLineStartOffset(int line) throws BadLocationException {
        return editorDocument.getLineStartOffset(line);
    }
    
    /** */
    private int getMaxLine() {
        try {
            return getLineOfOffset(textArea.getDocument().getLength());
        } catch (BadLocationException exception) {
            // ignored as the location is always valid
        }
        return -1;
    }
    
    /**
     * Goes to position.
     *
     * @param position The position.
     */
    void goToPosition(int position) {        
        if (position < 0) {
            return;
        }
        
        try {
            Rectangle rect = textArea.modelToView(position);
            if (rect == null) {
                return;
            }
            textArea.scrollRectToVisible(rect);
        } catch (BadLocationException exception) {
            return;
        }
        textArea.getCaret().setDot(position);
    }
    
    /** */
    private int getLineNumber(int line) {
        line--;
        if (line < 0) {
            return 0;
        }
        
        int maxLine = getMaxLine();
        if (line > maxLine) {
            return maxLine;
        }
            
        return line;
    }
    
    /**
     * Goes to line.
     *
     * @param line The line number (starting from 1).
     * @param lineOffset The offset in the line.
     */
    void goToLine(int line,int lineOffset) {
        int offset;
        try {
            offset = getLineStartOffset(getLineNumber(line)) + lineOffset;
        } catch (BadLocationException exception) { 
            return;
        }
        
        int length = textArea.getDocument().getLength();
        if (offset > length) {
            offset = length;
        }            
        
        textArea.setCaretPosition(offset);
        textArea.requestFocusInWindow();
    }
    
    /**
     * Goes to line.
     *
     * @param line The line number (starting from 1).
     */
    void goToLine(int line) {
        goToLine(line,0);
    }
    
    /** */
    void goToLineRange(int beginLine,int endLine,int lineOffset) {
        try {
            int beginOffset = getLineStartOffset(getLineNumber(beginLine));
            Rectangle beginRect = textArea.modelToView(beginOffset);
            
            int endOffset = getLineStartOffset(getLineNumber(endLine));
            Rectangle endRect = textArea.modelToView(endOffset);
            
            beginRect.add(endRect);
            textArea.scrollRectToVisible(beginRect);
        } catch (BadLocationException exception) {
        }
        
        goToLine(beginLine,lineOffset);
    }
    
    /** */
    void goToLineRange(int beginLine,int endLine) {
        goToLineRange(beginLine,endLine,0);
    }
    
    /** */
    Object addLineHighlight(int line,Color color) throws BadLocationException {
        return textArea.addLineHighlight(line - 1,color); // zero based here
    }
    
    /** */
    void removeLineHighlight(Object tag) {
        textArea.removeLineHighlight(tag);        
    }
    
    /** */
    void updateCurrentLineHighlight() {
        int line = textArea.getCaretLineNumber() + 1;
        boolean hasHighlight = LineHighlights.get().hasHighlight(file,line);
        textArea.setHighlightCurrentLine(!hasHighlight);        
    }
    
    /** */
    GutterIcon addGutterIcon(int line,String iconName,String tooltip,
        EditorAnnotation.Priority priority) throws BadLocationException {
    //    
        GutterIcon gutterIcon = new GutterIcon(line,iconName,tooltip,priority);
        gutterIconList.add(gutterIcon);
        
        updateGutterIcons();
        return gutterIcon;
    }
    
    /** */
    void removeGutterIcon(GutterIcon gutterIcon) throws BadLocationException {      
        gutterIconList.remove(gutterIcon);
        updateGutterIcons();
    }
    
    /** */
    private void updateAnnotations() {
        int maxLine = getMaxLine();
        for (EditorAnnotation annotation:annotations) {
            annotation.setNormalizedPosition(
                (double)annotation.getLine() / maxLine);
        }
    }
    
    /** */
    private void repaintAnnotations() {
        overviewRuler.repaint(annotations);
    }
    
    /** */
    EditorAnnotation addAnnotation(int line,String tooltip,
        com.andcreations.ae.color.Color color,
        EditorAnnotation.Priority priority) throws BadLocationException {
    // create
        EditorAnnotation annotation = new EditorAnnotation(
            line,tooltip,color,priority);
        annotations.add(annotation);
        
    // update,paint
        updateAnnotations();
        repaintAnnotations();
        
        return annotation;
    }
    
    /** */
    void removeAnnotation(EditorAnnotation annotation) {
        annotations.remove(annotation);
        repaintAnnotations();
    }
    
    /** */
    private void updateGutterIconLines() throws BadLocationException {
        boolean changed = false;
        for (GutterIcon icon:gutterIconList.getGutterIcons()) {
            GutterIconInfo info = icon.getGutterIconInfo();
            if (info != null) {
                int line = getLineOfOffset(info.getMarkedOffset());
                if (line != icon.getLine()) {
                    icon.setLine(line);
                    changed = true;
                }
            }
        }
        
        gutterIconList.deduplicate();
        if (changed == true) {
            listener.gutterIconLinesUpdated();
        }
    }
    
    /** */
    private String getGutterIconTooltip(List<GutterIcon> gutterIcons) {
        StringBuilder str = new StringBuilder();
        str.append("<html>");
        
    // for each icon
        for (GutterIcon gutterIcon:gutterIcons) {
            str.append(String.format("<p>%s</p>",gutterIcon.getTooltip()));
        }
        
        str.append("</html>");
        return str.toString();
    }
    
    /** */
    private void updateGutterIcons() throws BadLocationException {
        Gutter gutter = textAreaScrollPane.getGutter();
        gutter.removeAllTrackingIcons();
        
        Set<Integer> lines = gutterIconList.getLines();
    // for each line with gutter icons
        for (Integer line:lines) {
            List<GutterIcon> gutterIcons =
                gutterIconList.getGutterIcons(line.intValue());
            String tooltip = getGutterIconTooltip(gutterIcons);
                
            List<String> iconNames = new ArrayList<>();
        // for each gutter icon
            for (GutterIcon gutterIcon:gutterIcons) {
                iconNames.add(gutterIcon.getIconName());
            }            
                    
            ImageIcon icon = Icons.getIcon(iconNames);
        // gutter icon info
            GutterIconInfo gutterIconInfo = gutter.addLineTrackingIcon(
                line.intValue() - 1,icon,tooltip);
            for (GutterIcon gutterIcon:gutterIcons) {
                gutterIcon.setGutterIconInfo(gutterIconInfo);
            }
        }
    }
    
    /** */
    private void annotationClicked(EditorAnnotation annotation) {
        goToLine(annotation.getLine());
    }
    
    /** */
    RSyntaxTextArea getTextArea() {
        return textArea;
    }
    
    /** */
    String getContent() {
        Document document = textArea.getDocument();        
        try {
            return document.getText(0,document.getLength());
        } catch (BadLocationException exception) {
        // never happens
            return null;
        }        
    }
    
    /** */
    int getLine() {
        return textArea.getCaretLineNumber() + 1;
    }
    
    /** */
    void insertAtCaret(String text) {
        int location = textArea.getCaret().getDot();        
        Document document = textArea.getDocument();        
        try {
            document.insertString(location,text,null);
        } catch (BadLocationException exception) {
        // never happens
        }        
    }
    
    /** */
    void setContent(String content) {
        setText(content);
    }
    
    /** */
    void addPopupSeparator() {
        textArea.getPopupMenu().addSeparator();
    }
    
    /** */
    public void addPopupItem(JMenuItem item) {
        textArea.getPopupMenu().add(item);
    }
    
    /** */
    String getSelectedText() {
        return textArea.getSelectedText();
    }
    
    /** */
    int getSelectionStart() {
        return textArea.getSelectionStart();
    }
    
    /** */
    int getSelectionEnd() {
        return textArea.getSelectionEnd();
    }
    
    /** */
    void replace(int start,int end,String str) {
        Document document = textArea.getDocument();
        try {
            document.remove(start,end - start);
            document.insertString(start,str,null);
        } catch (BadLocationException exception) {
            throw new IllegalArgumentException("Invalid start/end position");
        }
    }
}