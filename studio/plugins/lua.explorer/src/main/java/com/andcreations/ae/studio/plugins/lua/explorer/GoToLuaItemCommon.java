package com.andcreations.ae.studio.plugins.lua.explorer;

import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.lua.parser.LuaAssignment;
import com.andcreations.ae.lua.parser.LuaFileInfo;
import com.andcreations.ae.lua.parser.LuaFunc;
import com.andcreations.ae.lua.parser.LuaLocalAssignment;
import com.andcreations.ae.lua.parser.LuaVariableExpression;
import com.andcreations.ae.studio.plugins.lua.LuaIcons;
import com.andcreations.ae.studio.plugins.lua.parser.renderer.LuaElementRenderer;
import com.andcreations.ae.studio.plugins.ui.common.quickopen.QuickOpenItem;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class GoToLuaItemCommon {
    /** */  
    static class GoToLine {
        /** */
        private int beginLine;
        
        /** */
        private int endLine;

        /** */
        private GoToLine(int beginLine,int endLine) {
            this.beginLine = beginLine;
            this.endLine = endLine;
        }
        
        /** */
        int getBeginLine() {
            return beginLine;
        }
        
        /** */
        int getEndLine() {
            return endLine;
        }
    }    
    
    /** */
    private final BundleResources res =
        new BundleResources(GoToLuaItemCommon.class);    
    
    /** */
    private List<QuickOpenItem> createAssignmentItems(
        LuaAssignment assignment) {
    //
        String details = res.getStr("assignment");
        List<QuickOpenItem> items = new ArrayList<>();
        
    // for each variable
        for (LuaVariableExpression var:assignment.getVars()) {
            GoToLine goToLine = new GoToLine(
                assignment.getBeginLine(),assignment.getEndLine());
            items.add(new QuickOpenItem(
                Icons.getIcon(LuaIcons.LUA_ASSIGNMENT),
                LuaElementRenderer.expressionToSearch(var),
                LuaElementRenderer.expressionToPlain(var,details),
                LuaElementRenderer.expressionToHTML(var,details),goToLine));
        }
        
        return items;
    }
    
    /** */
    private List<QuickOpenItem> createLocalAssignmentItems(
        LuaLocalAssignment assignment) {
    //
        String details = res.getStr("local.assignment");
        List<QuickOpenItem> items = new ArrayList<>();
        
    // for each variable
        for (String name:assignment.getNames()) {
            GoToLine goToLine = new GoToLine(
                assignment.getBeginLine(),assignment.getEndLine());
            items.add(new QuickOpenItem(
                Icons.getIcon(LuaIcons.LUA_ASSIGNMENT_LOCAL),
                LuaElementRenderer.elementToSearch(name),
                LuaElementRenderer.elementToPlain(name,details),
                LuaElementRenderer.elementToHTML(name,details),goToLine));
        }
        
        return items;
    }
    
    /** */
    List<QuickOpenItem> createItems(LuaFileInfo luaFileInfo) {
        List<QuickOpenItem> items = new ArrayList<>();
        
    // assignments
        for (LuaAssignment assignment:luaFileInfo.getAssignments()) {
            items.addAll(createAssignmentItems(assignment));
        }
        
    // local assignments
        for (LuaLocalAssignment assignment:luaFileInfo.getLocalAssignments()) {
            items.addAll(createLocalAssignmentItems(assignment));
        }        
        
    // functions
        for (LuaFunc func:luaFileInfo.getFuncs()) {
            items.add(new QuickOpenItem(
                Icons.getIcon(LuaIcons.LUA_FUNC),
                LuaElementRenderer.funcToSearch(func),
                LuaElementRenderer.funcToPlain(func),
                LuaElementRenderer.funcToHTML(func),
                new GoToLine(func.getBeginLine(),func.getEndLine())));
        }
        
    // local functions
        for (LuaFunc func:luaFileInfo.getLocalFuncs()) {
            items.add(new QuickOpenItem(
                Icons.getIcon(LuaIcons.LUA_FUNC_LOCAL),
                LuaElementRenderer.funcToSearch(func),
                LuaElementRenderer.localFuncToPlain(func),
                LuaElementRenderer.localFuncToHTML(func),                
                new GoToLine(func.getBeginLine(),func.getEndLine())));
        }
        
        return items;
    }    
}