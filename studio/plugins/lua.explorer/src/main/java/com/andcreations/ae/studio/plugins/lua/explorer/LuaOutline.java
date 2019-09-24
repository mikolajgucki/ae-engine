package com.andcreations.ae.studio.plugins.lua.explorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.andcreations.ae.lua.parser.LuaAssignment;
import com.andcreations.ae.lua.parser.LuaElement;
import com.andcreations.ae.lua.parser.LuaExpression;
import com.andcreations.ae.lua.parser.LuaFileInfo;
import com.andcreations.ae.lua.parser.LuaFunc;
import com.andcreations.ae.lua.parser.LuaLocalAssignment;
import com.andcreations.ae.lua.parser.LuaParseException;
import com.andcreations.ae.lua.parser.LuaTableConstructor;
import com.andcreations.ae.lua.parser.LuaTableField;
import com.andcreations.ae.lua.parser.LuaVariableExpression;
import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.lua.LuaIcons;
import com.andcreations.ae.studio.plugins.lua.parser.LuaParser;
import com.andcreations.ae.studio.plugins.lua.parser.renderer.LuaElementRenderer;
import com.andcreations.ae.studio.plugins.outline.DefaultOutlineSource;
import com.andcreations.ae.studio.plugins.outline.Outline;
import com.andcreations.ae.studio.plugins.outline.OutlineItem;
import com.andcreations.ae.studio.plugins.outline.OutlineItemListener;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.text.editor.EditorMediator;
import com.andcreations.ae.studio.plugins.text.editor.TextEditor;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class LuaOutline {
    /** */
    private class LuaOutlineSource extends DefaultOutlineSource {
        /** */
        public OutlineItem getOutlineRootItem(File file) {
            return LuaOutline.this.getOutlineRootItem(file);
        }
    }
    
    /** */
    private static LuaOutline instance;
    
    /** */
    private final BundleResources res = new BundleResources(LuaOutline.class);    
    
    /** */
    private LuaOutline() {
    }
    
    /** */
    void init() {
        Outline.get().addOutlineSource(new LuaOutlineSource());
    }
    
    /** */
    private OutlineItem createErrorItem(String message) {
        return new OutlineItem(
            Icons.getIcon(DefaultIcons.ERROR),message,message);
    }
    
    /** */
    private void edit(File file,int line,int beginLine,int endLine) {
        EditorMediator editor = TextEditor.get().getEditor(file);
        if (editor != null) {
            editor.goToLineRange(line,endLine);
            editor.goToLine(beginLine);                
            editor.focus();
        }
        else {
            Log.error(String.format("No editor for file %s",
                file.getAbsolutePath()));
        }
    }
    
    /** */
    private OutlineItemListener createListener(final File file,
        final LuaElement element) {
    //
        return new OutlineItemListener() {
            /** */
            public void itemSelected(OutlineItem item) {
                int marginLine = element.getBeginLine() - 8;
                if (marginLine < 1) {
                    marginLine = 1;
                }                
                edit(file,marginLine,element.getBeginLine(),
                    element.getEndLine());
            }
        };
    }
    
    /** */
    private LuaOutlineItem createTableFieldItem(LuaTableField field) {
        String name = null;
    // name
        if (field.getIndex() != null) {
            name = field.getIndex().toString();
        }
        if (field.getName() != null) {
            name = field.getName();
        }
        
        String details = res.getStr("table.field");
    // item
        LuaOutlineItem item = new LuaOutlineItem(
            Icons.getIcon(LuaIcons.LUA_TABLE_FIELD),
            LuaElementRenderer.elementToPlain(name,details),
            LuaElementRenderer.elementToHTML(name,details),field);
        item.addChildItems(createExpressionItems(field.getRightHandExp()));
        
        return item;        
    }
    
    /** */
    private List<LuaOutlineItem> createExpressionItems(LuaExpression exp) {
        List<LuaOutlineItem> items = new ArrayList<>();
        
    // table
        if (exp instanceof LuaTableConstructor) {
            LuaTableConstructor table = (LuaTableConstructor)exp;
            for (LuaTableField field:table.getFields()) {
                items.add(createTableFieldItem(field));
            }
        }
        
        return items;
    }
    
    /** */
    private LuaOutlineItem createAssignmentItem(File file,
        LuaVariableExpression var,LuaExpression exp,String details) {
    //
        LuaOutlineItem item = new LuaOutlineItem(
            Icons.getIcon(LuaIcons.LUA_ASSIGNMENT),
            LuaElementRenderer.expressionToPlain(var,details),
            LuaElementRenderer.expressionToHTML(var,details),var);
        item.addOutlineItemListener(createListener(file,var));
        item.addChildItems(createExpressionItems(exp));
        
        return item;
    }
    
    /** */
    private List<LuaOutlineItem> createAssignmentItems(File file,
        LuaAssignment assignment) {
    //
        List<LuaOutlineItem> items = new ArrayList<>();
        
    // for each variable
        for (int index = 0; index < assignment.getVars().size(); index++) {
            LuaVariableExpression var = assignment.getVars().get(index);
            LuaExpression exp = null;
            if (index < assignment.getExps().size()) {
                exp = assignment.getExps().get(index);
            }
            items.add(createAssignmentItem(file,var,exp,
                res.getStr("assignment")));
        }
        
        return items;
    }
    
    /** */
    private LuaOutlineItem createLocalAssigmentItem(File file,
        String name,LuaElement element,String details) {
    //
        LuaOutlineItem item = new LuaOutlineItem(
            Icons.getIcon(LuaIcons.LUA_ASSIGNMENT_LOCAL),
            LuaElementRenderer.elementToPlain(name,details),
            LuaElementRenderer.elementToHTML(name,details),element);
        item.addOutlineItemListener(createListener(file,element));
        
        return item;
    }    
    
    /** */
    private List<LuaOutlineItem> createLocalAssignmentItems(File file,
        LuaLocalAssignment assignment) {
    //
        List<LuaOutlineItem> items = new ArrayList<>();
        
    // for each variable
        for (int index = 0; index < assignment.getNames().size(); index++) {
            String name = assignment.getNames().get(index);
            items.add(createLocalAssigmentItem(file,name,assignment,
                res.getStr("local.assignment")));
        }
        
        return items;
    }    
    
    /** */
    private OutlineItem createRootItem(final File file,
        LuaFileInfo luaFileInfo) {
    //
        String path = ProjectLuaFiles.get().getPath(file);
        OutlineItem rootItem = new OutlineItem(
            ProjectLuaFiles.get().getLuaFileIcon(file),path,path);
        rootItem.addOutlineItemListener(new OutlineItemListener() {
            /** */
            public void itemSelected(OutlineItem item) {
                edit(file,0,0,0);
            }
        });
        List<LuaOutlineItem> items = new ArrayList<>();

    // assignments
        for (LuaAssignment assignment:luaFileInfo.getAssignments()) {
            items.addAll(createAssignmentItems(file,assignment));            
        }
        
    // local assignments
        for (LuaLocalAssignment assignment:luaFileInfo.getLocalAssignments()) {
            items.addAll(createLocalAssignmentItems(file,assignment));
        }        
        
    // functions
        for (LuaFunc func:luaFileInfo.getFuncs()) {
            LuaOutlineItem item = new LuaOutlineItem(
                Icons.getIcon(LuaIcons.LUA_FUNC),
                LuaElementRenderer.funcToPlain(func),
                LuaElementRenderer.funcToHTML(func),func);
            item.addOutlineItemListener(createListener(file,func));
            items.add(item);
        }
        
    // local functions
        for (LuaFunc func:luaFileInfo.getLocalFuncs()) {
            LuaOutlineItem item = new LuaOutlineItem(
                Icons.getIcon(LuaIcons.LUA_FUNC_LOCAL),
                LuaElementRenderer.localFuncToPlain(func),
                LuaElementRenderer.localFuncToHTML(func),func);
            item.addOutlineItemListener(createListener(file,func));
            items.add(item);
        }        
        
    // sort
        Collections.sort(items);
        
    // add
        for (OutlineItem item:items) {
            rootItem.addChildItem(item);
        }
        
        return rootItem;
    }
    
    /** */
    private OutlineItem handleLuaParseException(EditorMediator editor,
        Exception exception,File file) {
    // add
        final String description = res.getStr("failed.to.parse",
            exception.getMessage());
        
        return createErrorItem(description);
    }
    
    /** */
    private OutlineItem getOutlineRootItem(File file) {
        if (LuaFile.isLuaFile(file) == false) {
            return null;
        }
        
    // get editor
        EditorMediator editor = TextEditor.get().getEditor(file);
        if (editor == null) {
            Log.error(String.format("No editor for file %s",
                file.getAbsolutePath()));
            return null;
        }
        
    // parse
        LuaFileInfo luaFileInfo;
        try {
            luaFileInfo = LuaParser.get().parseLua(editor.getContent());
        } catch (LuaParseException exception) {
            return handleLuaParseException(editor,exception,file);
        }
        
        return createRootItem(file,luaFileInfo);
    }
    
    /** */
    static LuaOutline get() {
        if (instance == null) {
            instance = new LuaOutline();
        }
        
        return instance;
    }
}