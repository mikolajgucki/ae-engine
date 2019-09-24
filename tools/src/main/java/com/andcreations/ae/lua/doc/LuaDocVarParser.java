package com.andcreations.ae.lua.doc;

import java.util.List;

import com.andcreations.ae.lua.parser.LuaAssignment;
import com.andcreations.ae.lua.parser.LuaElement;
import com.andcreations.ae.lua.parser.LuaFieldExpression;
import com.andcreations.ae.lua.parser.LuaLocalAssignment;
import com.andcreations.ae.lua.parser.LuaNameExpression;
import com.andcreations.ae.lua.parser.LuaPrimaryExpression;
import com.andcreations.ae.lua.parser.LuaVariableExpression;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class LuaDocVarParser extends LuaDocParser {
    /** The string resources. */
    private BundleResources res = new BundleResources(LuaDocVarParser.class);     
    
    /** */
    private boolean matchNext(List<LuaElement> elements,int index) {
        LuaElement next = getNextElement(elements,index);
        return (next instanceof LuaAssignment) ||
            (next instanceof LuaLocalAssignment);
    }    
    
    /** */
    @Override
    boolean match(LuaDocCommentBlock block,List<LuaElement> elements,
        int index) {
    //
        if (matchByTag(block,LuaDocTag.VAR) == true) {
            return true;
        }
        
        return matchNext(elements,index);    
    }
    
    /** */
    private String getFieldName(LuaPrimaryExpression exp) {
    // name expression
        if (exp instanceof LuaNameExpression) {
            LuaNameExpression nameExp = (LuaNameExpression)exp;
            return nameExp.getName();
        }
        
    // field expression
        if (exp instanceof LuaFieldExpression) {
            LuaFieldExpression fieldExp = (LuaFieldExpression)exp;
            String parentName = getFieldName(fieldExp.getLeftHandExp());
            if (parentName == null) {
                return null;
            }
            
            return parentName + "." + fieldExp.getName();
        }
        
        return null;
    }
    
    /** */
    @Override
    void parse(LuaDocCommentBlock block,List<LuaElement> elements,int index,
        LuaDocParseContext context) {
    // validate
        if (validateTags(context,block,new String[] {
            LuaDocTag.VAR + "?",
            LuaDocTag.NAME + "?",
            LuaDocTag.BRIEF + "?",
            LuaDocTag.FULL + "?",}) == false) {
        //
            return;
        }
        if (validateNoValues(context,block,LuaDocTag.VAR) == false) {
            return;
        }        
        
    // if the name is not provided via a tag, then the comment block must be
    // followed by a variable from which the name is taken
        if (block.hasTag(LuaDocTag.NAME) == false &&
            matchNext(elements,index) == false) {
        //
            LuaDocIssue.addError(context,LuaDocVar.ERROR_NOT_FOLLOWED_BY_VAR,
                null,res.getStr("not.followed.by.var"),block.getBeginLine());
            return;  
        }
            
    // variable
        LuaDocVar var = new LuaDocVar();
        String varName = null;
        
    // the next element (function)
        LuaElement nextElement = getNextElement(elements,index);
        
    // assignment
        if (nextElement instanceof LuaAssignment) {
            LuaAssignment luaAssignment = (LuaAssignment)nextElement;      
            LuaVariableExpression varExp = luaAssignment.getVars().get(0);
            
        //  name
            if (varExp instanceof LuaNameExpression) {
                LuaNameExpression nameExp = (LuaNameExpression)varExp;
                varName = nameExp.getName();
            }
            else if (varExp instanceof LuaFieldExpression) {
                LuaFieldExpression fieldExp = (LuaFieldExpression)varExp;
                varName = getFieldName(fieldExp);
            }
            if (varName == null) {
                LuaDocIssue.addError(context,LuaDocVar.ERROR_INVALID_EXPRESSION,
                    null,res.getStr("invalid.exp"),block.getBeginLine());
                return;            
            }
        }
        
    // local assignment
        if (nextElement instanceof LuaLocalAssignment) {
            LuaLocalAssignment luaAssignment =(LuaLocalAssignment)nextElement;
            varName = luaAssignment.getNames().get(0);
            var.setLocal(true);
        }
        
    // variable name from tag
        if (block.hasTag(LuaDocTag.NAME) == true) {
            varName = block.getTagValue(LuaDocTag.NAME);
            if (varName.charAt(0) == '.') {
                varName = getModule(context).getName() + varName;
            }            
        }
        
    // local name
        String localName = null;
        try {
            localName = getLocalName(varName,context);
        } catch (LuaDocException exception) {
            // getLocalName() adds error and thus nothing happens here
            return;
        }        
        
    // values
        var.setName(varName);
        var.setLocalName(localName);
        var.setBrief(block.getTagValue(LuaDocTag.BRIEF));
        var.setFull(block.getTagValue(LuaDocTag.FULL));
        var.setLine(block.getBeginLine());
        var.updateGlobalName(getModule(context).getName());
        
    // add
        context.getFileData().addVar(var);
    }
}