package com.andcreations.ae.studio.plugins.lua.editor;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.lua.editor.resources.R;
import com.andcreations.ae.studio.plugins.lua.lib.runner.LuaRunnerFactory;
import com.andcreations.ae.studio.plugins.ui.common.Dialog;
import com.andcreations.ae.studio.plugins.ui.common.DocumentChangedAdapter;
import com.andcreations.ae.studio.plugins.ui.common.layout.FormLayoutBuilder;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.lua.parser.LuaStringParser;
import com.andcreations.lua.runner.LuaRunner;
import com.andcreations.resources.BundleResources;
import com.andcreations.resources.ResourceLoader;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class LuaUTF8Dialog extends Dialog {
    /** */
    private BundleResources res = new BundleResources(LuaUTF8Dialog.class);
    
    /** */
    private Font defaultFont;
    
    /** */
    private LuaStringParser luaStringParser;
    
    /** */
    private JTextField luaField;
    
    /** */
    private boolean luaFieldUpdating;
    
    /** */
    private JTextField utf8Field;
    
    /** */
    private boolean utf8FieldUpdating;
    
    /** */
    private JLabel stringStatusLabel;   
    
    /** */
    private JButton okButton;
    
    /** */
    private JButton cancelButton;
    
    /** */
    LuaUTF8Dialog() {
        super(MainFrame.get(),"",true);
        create();
    }
    
    /** */
    private void create() {
        setTitle(res.getStr("title"));
        makeEscapable();       
        
    // Lua string parser
        LuaRunner luaRunner = LuaRunnerFactory.get().createLuaRunner();
        luaStringParser = new LuaStringParser(luaRunner);
        
        ActionListener okActionListener = new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                close(okButton);
            }
        };
        
    // Lua field
        luaField = new JTextField();
        luaField.addActionListener(okActionListener); 
        luaField.getDocument().addDocumentListener(
            new DocumentChangedAdapter() {
                /** */
                @Override
                public void documentChanged() {
                    updateUTF8Field();
                }
            });
        
    // UTF-8 field
        utf8Field = new JTextField();
        utf8Field.addActionListener(okActionListener); 
        utf8Field.getDocument().addDocumentListener(
            new DocumentChangedAdapter() {
                /** */
                @Override
                public void documentChanged() {
                    updateLuaField();
                }
            });        
        
    // default font
        defaultFont = new Font(Font.MONOSPACED,Font.PLAIN, 
            utf8Field.getFont().getSize());
        utf8Field.setFont(defaultFont);
        
    // ok
        okButton = new JButton(res.getStr("ok"));
        
    // cancel
        cancelButton = new JButton(res.getStr("cancel"));
        
    // layout and create
        JPanel panel = layoutComponents();
        create(panel,okButton,cancelButton);
        
    // initialize
        setNoStringError();
    }
    
    /** */
    private JPanel layoutComponents() {
        FormLayoutBuilder builder;
        try {
            String src = ResourceLoader.loadAsString(
                R.class,"LuaUTF8Dialog.formlayout");
            builder = new FormLayoutBuilder(src);
        } catch (IOException exception) {
            Log.error("Failed to read form layout: " + exception.getMessage());
            return null;
        }
        
    // Lua
        builder.addLabel(res.getStr("lua.string"),"ll");
        builder.add(luaField,"lf");
        
    // UTF-8
        builder.addLabel(res.getStr("utf8.string"),"ul");
        builder.add(utf8Field,"uf");
        
    // string status
        stringStatusLabel = builder.addLabel(" ","ssl");
        
        return builder.getPanel();        
    }    
    
    /** */
    private void setStringError(String error) {
        stringStatusLabel.setText(error);
        stringStatusLabel.setIcon(Icons.getIcon(DefaultIcons.ERROR));
    }
    
    /** */
    private void setNoStringError() {
        stringStatusLabel.setText(" ");
        stringStatusLabel.setIcon(null);
    }
    
    /** */
    private String getUTF8StringFromLua() {
        setNoStringError();
        
    // empty
        String luaString = luaField.getText();
        if (luaString.length() == 0) {
            return "";
        }            
        
    // let Lua parse the string
        File tmpFile = Files.get().getTmpFile("utf8.lua");
        String utf8String = null;
        try {
            utf8String = luaStringParser.parse(luaString,tmpFile);
        } catch (IOException exception) {
            setStringError(res.getStr("error.parsing.lua.string"));
            return "";
        } finally {
            tmpFile.delete();
        }
        
        return utf8String;
    }
    
    /** */
    private void updateUTF8Field() {
        if (luaFieldUpdating == true) {
            return;
        }
        utf8FieldUpdating = true;
        utf8Field.setText(getUTF8StringFromLua());
        utf8FieldUpdating = false;
    }
    
    /** */
    private String getLuaStringFromUTF8() {
        String luaString = utf8Field.getText();
        StringBuilder utf8String = new StringBuilder();
        
        for (int index = 0; index < luaString.length(); index++) {
            char ch = luaString.charAt(index);
            if (ch >= 32 && ch <= 127) {
                utf8String.append(ch);
            }
            else {
                utf8String.append(String.format("\\u{%x}",(int)ch));
            }
        }
        
        return utf8String.toString();
    }
    
    /** */
    private void updateLuaField() {
        if (utf8FieldUpdating == true) {
            return;
        }
        luaFieldUpdating = true;
        luaField.setText(getLuaStringFromUTF8());
        luaFieldUpdating = false;
    }
    
    /** */
    String getLuaString() {
        return luaField.getText();
    }
    
    /** */
    public boolean showDialog(String luaString) {
        luaField.setText(luaString);
        updateUTF8Field();
        
        if (showDialog() == okButton) {
            return true;
        }
        return false;
    }
}