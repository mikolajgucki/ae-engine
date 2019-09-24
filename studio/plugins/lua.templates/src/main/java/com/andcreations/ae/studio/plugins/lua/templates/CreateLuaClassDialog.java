package com.andcreations.ae.studio.plugins.lua.templates;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.io.FilenameUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.lua.classes.LuaClass;
import com.andcreations.ae.studio.plugins.lua.classes.LuaClasses;
import com.andcreations.ae.studio.plugins.lua.templates.args.ArgsTable;
import com.andcreations.ae.studio.plugins.lua.templates.args.ArgsTableRow;
import com.andcreations.ae.studio.plugins.lua.templates.resources.R;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.text.editor.EditorMediator;
import com.andcreations.ae.studio.plugins.ui.common.Dialog;
import com.andcreations.ae.studio.plugins.ui.common.DocumentChangedAdapter;
import com.andcreations.ae.studio.plugins.ui.common.JTableColumnsSizeSetter;
import com.andcreations.ae.studio.plugins.ui.common.UIFonts;
import com.andcreations.ae.studio.plugins.ui.common.layout.FormLayoutBuilder;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.resources.BundleResources;
import com.andcreations.resources.ResourceLoader;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class CreateLuaClassDialog extends Dialog {
    /** */
    private BundleResources res =
        new BundleResources(CreateLuaClassDialog.class);  
    
    /** */
    private EditorMediator editor;
        
    /** */
    private JTextField nameArgsField;
    
    /** */
    private LuaFuncNameArgsVerifier nameArgsVerifier;
    
    /** */
    private JTextField superclassField;
    
    /** */
    private JButton superclassButton;
    
    /** */
    private JLabel superclassStatus;
    
    /** */
    private JCheckBox generateCommentsCheck;
    
    /** */
    private JTextField moduleField;
    
    /** */
    private JTextField groupField;
    
    /** */
    private JLabel statusLabel;    
    
    /** */
    private ArgsTable argsTable;
    
    /** */
    private JScrollPane argsTableScroll;
    
    /** */
    private JButton createButton;
    
    /** */
    private JButton cancelButton;
        
    /** */
    CreateLuaClassDialog() {
        super(MainFrame.get(),"",true);
        create();        
    }
    
    /** */
    private void create() {
        setTitle(res.getStr("title"));
        makeEscapable();
        
    // name(arguments)
        nameArgsField = new JTextField();
        nameArgsField.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                if (nameArgsVerifier.verify() == false) {
                    return;
                }
                
                setVisible(false);
                createLuaClass();                
            }
        });
        nameArgsVerifier = new LuaFuncNameArgsVerifier(nameArgsField,
            res.getStr("invalid.name.args"));
        createDocumentListener();
        
    // superclass
        superclassField = new JTextField();
        superclassField.getDocument().addDocumentListener(
            new DocumentChangedAdapter() {
                /** */
                @Override
                public void documentChanged() {
                    superclassChanged();
                }
            });
        
    // select superclass button
        superclassButton = new JButton(res.getStr("select"));
        superclassButton.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent e) {
                selectSuperclass();
            }
        });
        
    // generate comments
        generateCommentsCheck = new JCheckBox(res.getStr("generate.comments"));
        generateCommentsCheck.setSelected(true);        
        generateCommentsCheck.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                boolean comments = generateCommentsCheck.isSelected();
                moduleField.setEnabled(comments);
                groupField.setEnabled(comments);
            }
        });
        
    // module
        moduleField = new JTextField();
        
    // group
        groupField = new JTextField();
        
    // arguments
        argsTable = new ArgsTable();
        argsTableScroll = new JScrollPane(argsTable);
        JTableColumnsSizeSetter.set(argsTable,new double[]{2,8});
        
    // create
        createButton = new JButton(res.getStr("create"));
        
    // cancel
        cancelButton = new JButton(res.getStr("cancel"));        
        
    // layout and create
        JPanel panel = layoutComponents();
        create(panel,createButton,cancelButton);
    }
    
    /** */
    private void createDocumentListener() {
        DocumentListener documentListener = new DocumentListener() {
            /** */
            @Override
            public void changedUpdate(DocumentEvent event) {
                nameArgsChanged();
            }
            
            /** */
            @Override
            public void removeUpdate(DocumentEvent event) {
                nameArgsChanged();
            }
            
            /** */
            @Override
            public void insertUpdate(DocumentEvent event) {
                nameArgsChanged();
            }            
        };
        nameArgsField.getDocument().addDocumentListener(documentListener);              
    } 
    
    /** */
    private JPanel layoutComponents() {
        FormLayoutBuilder builder;
        try {
            String src = ResourceLoader.loadAsString(
                R.class,"CreateLuaClassDialog.formlayout");
            builder = new FormLayoutBuilder(src);
        } catch (IOException exception) {
            Log.error("Failed to read form layout: " + exception.getMessage());
            return null;
        }
        
        builder.addLabel(res.getStr("name.args"),"nl");
        builder.add(nameArgsField,"nf");
        builder.addLabel(res.getStr("superclass"),"ul");
        builder.add(superclassField,"uf");
        builder.add(superclassButton,"ub");
        superclassStatus = builder.addLabel(" ","ufs");
        builder.add(generateCommentsCheck,"gc");
        builder.addLabel(res.getStr("module"),"ml");
        builder.add(moduleField,"mf");
        builder.addLabel(res.getStr("group"),"gl");
        builder.add(groupField,"gf");
        statusLabel = builder.addLabel(" ","sl");
        builder.addSeparator(res.getStr("arguments"),"as");
        builder.add(argsTableScroll,"at");
        
    // superclass status with small font
        superclassStatus.setFont(
            UIFonts.getSmallFont(superclassStatus.getFont()));
        
        return builder.getPanel();
    }    
    
    /** */
    private void updateArgsTable(String[] args) {
        argsTable.setArgs(args);
    }
    
    /** */
    private void verify() {
        boolean ok = true;
        
        statusLabel.setText(" ");
        statusLabel.setIcon(null);
        
        if (nameArgsVerifier.verify() == false) {
            statusLabel.setText(nameArgsVerifier.getErrorToolTip());
            statusLabel.setIcon(Icons.getIcon(DefaultIcons.ERROR));
            ok = false;
        }
        
        createButton.setEnabled(ok);
    }
    
    /** */
    private void nameArgsChanged() {
        verify();
        updateArgsTable(nameArgsVerifier.getArgs());
    }
    
    /** */
    private void superclassChanged() {
        superclassStatus.setText(" ");
        superclassStatus.setIcon(null);
        
        String superclass = superclassField.getText();
    // check if the superclass exists
        if (superclass != null && superclass.length() > 0 &&
            LuaClasses.get().hasLuaClass(superclass) == false) {
        //
            superclassStatus.setText(res.getStr("class.not.found"));
            superclassStatus.setIcon(Icons.getIcon(DefaultIcons.WARNING_SMALL));
        }
    }
    
    /** */
    private void selectSuperclass() {
        LuaClass luaClass = LuaClasses.get().selectLuaClass();
        if (luaClass != null) {
            superclassField.setText(luaClass.getLuaModule());
        }
    }
    
    /** */
    private String nullIfEmpty(String str) {
        return str.isEmpty() ? null : str;
    }
    
    private Map<String,String> getArgsDesc() {
        List<ArgsTableRow> rows = argsTable.getRows(nameArgsVerifier.getArgs());
        Map<String,String> argsDesc = new HashMap<String,String>();
        
    // for each row
        for (ArgsTableRow row:rows) {
            argsDesc.put(row.getName(),row.getDesc());
        }
        
        return argsDesc;
    }
    
    /** */
    private String createLuaClassSrc() {
    // load template
        String template;
        try {
            template = LuaTemplates.get().loadTemplate("lua_class.vm");
        } catch (IOException exception) {
            CommonDialogs.error(res.getStr("exception.dialog.title"),exception);
            return null;
        }
        
    // context
        VelocityContext context = new VelocityContext();
        context.put("name",nameArgsVerifier.getName());
        context.put("args",nameArgsVerifier.getArgs());        
        context.put("argsStr",nameArgsVerifier.getArgsStr());
        context.put("argsDesc",getArgsDesc());
        context.put("superclass",nullIfEmpty(superclassField.getText()));
        context.put("comments",generateCommentsCheck.isSelected());
        context.put("module",nullIfEmpty(moduleField.getText()));
        context.put("group",nullIfEmpty(groupField.getText()));
        
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context,writer,"src.gen",template);        
        
        return writer.toString();
    }
    
    /** */
    private void createLuaClass() {
        editor.setContent(createLuaClassSrc());  
        editor.goToLine(1);
    }
    
    /** */                                                             
    void showDialog(EditorMediator editor) {
        File file = editor.getFile();
        if (LuaFile.isLuaFile(file) == false) {
            return;
        }        
        this.editor = editor;
        
    // init UI
        nameArgsField.setText(String.format("%s()",
            FilenameUtils.getBaseName(file.getName())));
        superclassField.setText("");
        moduleField.setText(ProjectLuaFiles.get().getLuaModule(file));
        argsTable.clear();
        
    // show
        if (showDialog() == createButton) {
            argsTable.stopCellEditing();
            createLuaClass();
        }
    }
}