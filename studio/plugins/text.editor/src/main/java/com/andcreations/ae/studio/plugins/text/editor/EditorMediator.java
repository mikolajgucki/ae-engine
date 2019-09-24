package com.andcreations.ae.studio.plugins.text.editor;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.text.BadLocationException;

import com.andcreations.ae.studio.plugins.file.FileAdapter;
import com.andcreations.ae.studio.plugins.file.FileIssue;
import com.andcreations.ae.studio.plugins.file.FileListener;
import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.file.LineLocation;
import com.andcreations.ae.studio.plugins.problems.Problem;
import com.andcreations.ae.studio.plugins.problems.ProblemListener;
import com.andcreations.ae.studio.plugins.problems.ProblemSeverity;
import com.andcreations.ae.studio.plugins.problems.Problems;
import com.andcreations.ae.studio.plugins.ui.common.OptionDialog.Option;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.ae.studio.plugins.ui.main.StatusBar;
import com.andcreations.ae.studio.plugins.ui.main.view.View;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewAdapter;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewClosingListener;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewListener;
import com.andcreations.io.FileUtil;
import com.andcreations.resources.BundleResources;

/**
 * The mediator between the component and corresponding view. 
 *
 * @author Mikolaj Gucki
 */
public class EditorMediator {
    /** */
    private static final String PROBLEM_SOURCE_ID =
        "com.andcreations.ae.studio.plugins.text.editor";
        
    /** */
    private final BundleResources res =
        new BundleResources(EditorMediator.class);           
        
    /** */
    private TextEditorCache cache;
    
    /** */
    private EditorCfg cfg;
    
    /** */
    private EditorComponent component;
    
    /** */
    private View view;
    
    /** */
    private FileListener fileListener;
    
    /** */
    private Problem fileNotFoundProblem;
    
    /** */
    private boolean fileDeleted;
    
    /** */
    private boolean dirty;
    
    /** */
    private List<GutterIcon> fileIssueGutterIcons = new ArrayList<>();
    
    /** */
    private List<EditorAnnotation> fileIssueAnnotations = new ArrayList<>();
    
    /** */
    private EditorAutocompletion autocompletion;
    
    /** */
    EditorMediator(TextEditorCache cache,EditorCfg cfg,
        EditorComponent component,View view) {
    //
        this.cache = cache;
        this.cfg = cfg;
        this.component = component;
        this.view = view;
        
        create();
    }
    
    /** */
    private void create() {
        component.setEditorComponentListener(new EditorComponentListener() {
            /** */
            @Override
            public void textChanged() {
                setDirty(true);
                TextEditor.get().textChanged(EditorMediator.this);
            }
            
            /** */
            @Override
            public void fileSaved() {
                EditorMediator.this.fileSaved();
            }                 
            
            /** */
            @Override
            public boolean keyPressed(KeyEvent event) {
                return TextEditor.get().notifyKeyPressed(event);
            }
            
            /** */
            @Override
            public boolean keyReleased(KeyEvent event) {
                return TextEditor.get().notifyKeyReleased(event);
            }
            
            /** */
            @Override
            public boolean keyTyped(KeyEvent event) {
                return TextEditor.get().notifyKeyTyped(event);
            }
            
            /** */
            @Override
            public void gutterIconRemoved(GutterIcon gutterIcon) {
                TextEditor.get().gutterIconRemoved(
                    EditorMediator.this,gutterIcon);
            }
            
            /** */
            @Override
            public void gutterIconLinesUpdated() {
                TextEditor.get().gutterIconLinesUpdated(EditorMediator.this);
            }
        });
        
    // view listener
        ViewListener viewListener = new ViewAdapter() {
            /** */
            @Override
            public void viewFocusLost(View view) { 
                EditorMediator.this.viewFocusLost();
            }
            
            /** */
            @Override
            public void viewFocusGained(View view) { 
                EditorMediator.this.viewFocusGained();
            }
            
            /** */
            @Override
            public void viewShown(View view) {
                EditorMediator.this.viewShown();
            }
                
            /** */
            @Override
            public void viewClosed(View view) {
                EditorMediator.this.viewClosed();
            }
        };
        view.addViewListener(viewListener);
        
    // view closing listener
        ViewClosingListener viewClosingListener = new ViewClosingListener() {
            /** */
            @Override
            public boolean viewClosing(View view) {
                return EditorMediator.this.viewClosing();
            }
        };
        view.setViewClosingListener(viewClosingListener);
            
    // file listener
        fileListener = new FileAdapter() {
            /** */
            @Override    
            public void fileIssuesChanged(File file,List<FileIssue> issues) {
                updateIcon();
                updateFileIssuesHighlights();
            }          
            
            /** */
            @Override
            public void fileChanged(File file) {
                if (file.equals(cfg.getFile()) == false) {
                    return;
                }
                EditorMediator.this.fileChanged();
            }
            
            /** */
            @Override
            public void fileDeleted(File file) {
                EditorMediator.this.fileDeleted(file);
            }
            
            /** */
            @Override
            public void fileRenamed(File src,File dst) {
                EditorMediator.this.fileRenamed(src,dst);
            }
        };        
        Files.get().addFileListener(fileListener);
        
        checkFileExists();
        updateIcon();
        updateFileIssuesHighlights();
        createAutocompletion();
    }
    
    /** */
    private void fileSaved() {
        setDirty(false);
        Files.get().notifyFileChanged(cfg.getFile());
        TextEditor.get().fileSaved(this);
    }
    
    /** */
    private void fileDeleted(File file) {
        if (Files.equal(cfg.getFile(),file) == true) {
            fileDeleted = true;
            UICommon.invoke(new Runnable() {
                /** */
                @Override
                public void run() {
                    MainFrame.get().getViewManager().closeView(view);
                }
            });
        }
    }
    
    /** */
    private void fileChanged() {
    }
    
    /** */
    private void fileRenamed(File src,File dst) {
    // file
        if (dst.isFile() == true) {
            fileDeleted(src);
            return;
        }
        
    // directory
        if (FileUtil.isAncestor(src,cfg.getFile()) == true) {
            fileDeleted(cfg.getFile());
        }
    }
    
    /** */
    private void checkFileExists() {
        File file = cfg.getFile(); 
        if (file.exists() == false || file.isFile() == false) {
            fileNotFoundProblem = Problems.get().add(PROBLEM_SOURCE_ID,
                ProblemSeverity.ERROR,"File not found",file.getAbsolutePath());
            fileNotFoundProblem.addProblemListener(new ProblemListener() {
                /** */
                @Override
                public void problemDoubleClicked(Problem problem) {
                    MainFrame.get().getViewManager().showView(view);
                }
            });            
        }
    }
    
    /** */
    private void viewShown() {
        component.viewShown();
    }
    
    /** */
    private void viewClosed() {
        Thread thread = new Thread(new Runnable() {
            /** */
            @Override
            public void run() {
                Files.get().removeFileListener(fileListener);
            }
        });
        thread.start();
        cache.remove(cfg);
        
        if (fileNotFoundProblem != null) {
            Problems.get().removeProblem(fileNotFoundProblem);
        }
        
        TextEditor.get().textEditorClosed(this);
    }
    
    /** */
    private boolean viewClosing() {
        if (dirty == false || fileDeleted == true) {
            return true;
        }
        
        Option option = CommonDialogs.yesNoCancel(
            res.getStr("close.dialog.title"),
            res.getStr("close.dialog.message",cfg.getFile().getName()));
        if (option == Option.CANCEL || option == null) {
            return false;
        }
        if (option == Option.YES) {
            if (component.saveFile() == false) {
                return false;
            }
        }
        return true;
    }
    
    /** */
    private void setDirty(boolean dirty) {
        this.dirty = dirty;
        updateIcon();
    }
    
    /** */
    private void updateIcon() {
        List<String> names = new ArrayList<>();
        names.add(cfg.getIconName());
        if (dirty == true) {
            names.add(DefaultIcons.DECO_MODIFIED);
        }
        
        File file = cfg.getFile();
        if (file.exists() == false ||
            file.isFile() == false ||
            Files.get().hasErrors(file)) {
        //
            names.add(DefaultIcons.DECO_ERROR);
        }
        else if (Files.get().hasWarnings(file)) {
            names.add(DefaultIcons.DECO_WARNING);
        }
        
        view.setIcon(Icons.getIcon(names));
    }
    
    /** */
    private void clearFileIssueGutterIcons() {
        for (GutterIcon gutterIcon:fileIssueGutterIcons) {
            try {
                component.removeGutterIcon(gutterIcon);
            } catch (BadLocationException exception) {
            }
        }
        fileIssueGutterIcons.clear();
    }
    
    /** */
    private void clearFileIssueAnnotations() {
        for (EditorAnnotation annotation:fileIssueAnnotations) {
            component.removeAnnotation(annotation);
        }
        fileIssueAnnotations.clear();
    }
    
    /** */
    private com.andcreations.ae.color.Color getFileIssueColor(FileIssue issue) {
        switch (issue.getSeverity()) {
            case ERROR:
                return Icons.red();
            case WARNING:
                return Icons.orange();
            default:
        }
        
        return null;
    }
    
    /** */
    private void updateFileIssuesHighlights() {
    // clear
        clearFileIssueGutterIcons();
        clearFileIssueAnnotations();
        
        List<FileIssue> issues = Files.get().getIssues(cfg.getFile());        
    // for each issue
        for (FileIssue issue:issues) {
            Object location = issue.getLocation();
            
        // if we know the line number
            if (location instanceof LineLocation) {
                LineLocation lineLocation = (LineLocation)location;
                int line = lineLocation.getLine();
                try {
                // gutter
                    GutterIcon gutterIcon = component.addGutterIcon(line,
                        issue.getSeverity().getIconName(),issue.getMessage(),
                        EditorAnnotation.Priority.MEDIUM);
                    fileIssueGutterIcons.add(gutterIcon);
                    
                // annotation
                    EditorAnnotation annotation = component.addAnnotation(line,
                        issue.getMessage(),getFileIssueColor(issue),
                        EditorAnnotation.Priority.MEDIUM);
                    fileIssueAnnotations.add(annotation);
                } catch (BadLocationException exception) {
                }
            }
        }
    }
    
    /** */
    private void createAutocompletion() {
        autocompletion = new EditorAutocompletion(this);
    }
    
    /** */
    private void viewFocusGained() {
        StatusBar.get().setInfo(cfg.getFile().getAbsolutePath());
        component.viewFocusGained();
        TextEditor.get().setFocusMediator(this);
    }
    
    /** */
    private void viewFocusLost() {
        component.viewFocusLost();
        TextEditor.get().setFocusMediator(null);
    }
    
    /** */
    public void focus() {
        MainFrame.get().getViewManager().focus(view);
    }
    
    /**
     * Gets the file being edited.
     *
     * @return The file.
     */
    public File getFile() {
        return cfg.getFile();
    }
    
    /**
     * Gets the current content of the editor.
     *
     * @return The current editor content.
     */
    public String getContent() {
        return component.getContent();
    }
    
    /**
     * Gets the line number with the caret.
     *
     * @return The current line number.
     */
    public int getLine() {
        return component.getLine();
    }
    
    /**
     * Inserts a text at the caret location.
     *
     * @param text The text.
     */
    public void insertAtCaret(String text) {
        component.insertAtCaret(text);
    }
    
    /**
     * Sets the editor content.
     *
     * @return The editor content.
     */
    public void setContent(String content) {
        component.setContent(content);
    }
    
    /**
     * Goes to line.
     *
     * @param line The line number (starting from 1).
     */    
    public void goToLine(int line) {
        component.goToLine(line);
    }
    
    /** */
    public void goToLineRange(int beginLine,int endLine) {
        component.goToLineRange(beginLine,endLine);
    }
    
    /** */
    public GutterIcon addGutterIcon(int line,String iconName,String tooltip,
        EditorAnnotation.Priority priority) throws BadLocationException {
    //
        return component.addGutterIcon(line,iconName,tooltip,priority);
    }
    
    /** */
    public void removeGutterIcon(GutterIcon gutterIcon)
        throws BadLocationException {
    //
        component.removeGutterIcon(gutterIcon);
    }
    
    /** */
    public EditorAnnotation addAnnotation(int line,String tooltip,
        com.andcreations.ae.color.Color color,
        EditorAnnotation.Priority priority)
        throws BadLocationException {
    //
        return component.addAnnotation(line,tooltip,color,priority);
    }
    
    /** */
    public void removeAnnotation(EditorAnnotation annotation) {
        component.removeAnnotation(annotation);
    }
    
    /** */
    public void addPopupSeparator() {
        component.addPopupSeparator();
    }
    
    /** */
    public void addPopupItem(JMenuItem item) {
        component.addPopupItem(item);
    }
    
    /** */
    public EditorAutocompletion getAutocompletion() {
        return autocompletion;
    }
    
    /** */
    public boolean isDirty() {
        return dirty;
    }
    
    /** */
    public String getSelectedText() {
        return component.getSelectedText();
    }
    
    /** */
    public int getSelectionStart() {
        return component.getSelectionStart();
    }
    
    /** */
    public int getSelectionEnd() {
        return component.getSelectionEnd();
    }
    
    /** */
    public void replace(int start,int end,String str) {
        component.replace(start,end,str);
    }
    
    /** */
    EditorComponent getComponent() {
        return component;
    }
}