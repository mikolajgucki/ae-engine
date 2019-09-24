package com.andcreations.ae.studio.plugins.lua.templates;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.io.FilenameUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.lua.templates.resources.R;
import com.andcreations.ae.studio.plugins.text.editor.EditorMediator;
import com.andcreations.ae.studio.plugins.ui.common.Dialog;
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
class InsertFuncDialog extends Dialog {
    /** */
    private BundleResources res =
        new BundleResources(InsertFuncDialog.class);   
          
    /** */
    private EditorMediator editor;
        
    /** */
    private JTextField nameArgsField;        
    
    /** */
    private LuaFuncNameArgsVerifier nameArgsVerifier;
    
    /** */
    private JCheckBox localFuncCheck;    
    
    /** */
    private JCheckBox generateCommentsCheck;    
    
    /** */
    private JLabel statusLabel;    
    
    /** */
    private JButton insertButton;
    
    /** */
    private JButton cancelButton;    
    
    /** */
    InsertFuncDialog() {
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
                insertFunc();                
            }
        });
        nameArgsVerifier = new LuaFuncNameArgsVerifier(nameArgsField,
            res.getStr("invalid.name.args")); 
        createDocumentListener();
        
    // local function
        localFuncCheck = new JCheckBox(res.getStr("local.func"));   
        
    // generate comments
        generateCommentsCheck = new JCheckBox(res.getStr("generate.comments"));   
        generateCommentsCheck.setSelected(true);
        
    // insert
        insertButton = new JButton(res.getStr("insert"));
        
    // cancel
        cancelButton = new JButton(res.getStr("cancel"));          
        
    // layout and create
        JPanel panel = layoutComponents();
        create(panel,insertButton,cancelButton);        
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
                R.class,"InsertFuncDialog.formlayout");
            builder = new FormLayoutBuilder(src);
        } catch (IOException exception) {
            Log.error("Failed to read form layout: " + exception.getMessage());
            return null;
        }
        
        builder.addLabel(res.getStr("name.args"),"nl");
        builder.add(nameArgsField,"nf");
        builder.add(localFuncCheck,"lf");
        builder.add(generateCommentsCheck,"gc");
        statusLabel = builder.addLabel(" ","sl");
        
        return builder.getPanel();        
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
        
        insertButton.setEnabled(ok);
    }
    
    /** */
    private void nameArgsChanged() {
        verify();
    }    

    /** */
    private String createLocalFuncSrc() {
    // load template
        String template;
        try {
            template = LuaTemplates.get().loadTemplate(
                localFuncCheck.isSelected() ? "func_local.vm" : "func.vm");
        } catch (IOException exception) {
            CommonDialogs.error(res.getStr("exception.dialog.title"),exception);
            return null;
        }
        
    // context
        VelocityContext context = new VelocityContext();
        context.put("name",nameArgsVerifier.getName());
        context.put("args",nameArgsVerifier.getArgs());        
        context.put("argsStr",nameArgsVerifier.getArgsStr());
        context.put("comments",generateCommentsCheck.isSelected());
        
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context,writer,"src.gen",template);        
        
        return writer.toString();        
    }
    
    /** */
    private void insertFunc() {
        String src = createLocalFuncSrc();
        editor.insertAtCaret(src);
    }
    
    /** */
    public void showDialog(EditorMediator editor,boolean localFunc) {
        this.editor = editor;
        this.localFuncCheck.setSelected(localFunc);
        
    // name/args field
        nameArgsField.requestFocus();
        if (localFuncCheck.isSelected()) {
            nameArgsField.setText("");
        }
        else {
            String name = editor.getFile().getName();            
            nameArgsField.setText(FilenameUtils.removeExtension(name));
            nameArgsField.selectAll();
        }
        
        if (showDialog() == insertButton) {
            insertFunc();
        }
    }
}