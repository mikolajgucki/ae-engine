package com.andcreations.ae.lua.doc;

import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.lua.parser.LuaElement;
import com.andcreations.ae.lua.parser.LuaFunc;
import com.andcreations.ae.lua.parser.LuaLocalFunc;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class LuaDocFuncParser extends LuaDocParser {
    /** The string resources. */
    private BundleResources res = new BundleResources(LuaDocFuncParser.class); 
    
    /** */
    LuaDocFuncParser() {
    }
    
    /** */
    private boolean matchNext(List<LuaElement> elements,int index) {
        LuaElement next = getNextElement(elements,index);
        return (next instanceof LuaFunc) || (next instanceof LuaLocalFunc);
    }        
    
    /** */
    @Override
    boolean match(LuaDocCommentBlock block,List<LuaElement> elements,
        int index) {
    //
        if (matchByTag(block,LuaDocTag.FUNC) == true) {
            return true;
        }
        
        return matchNext(elements,index);
    }  
    
    /** */
    private boolean validateFuncTags(LuaDocParseContext context,
        LuaDocCommentBlock block) {
    //
        int nameIndex = block.getTagIndex(LuaDocTag.NAME);
        if (nameIndex != -1 && nameIndex != 0) {
            LuaDocIssue.addError(context,LuaDocFunc.ERROR_NAME_TAG_NOT_FIRST,
                LuaDocTag.NAME,res.getStr("name.tag.not.first"),
                block.getBeginLine());
            return false;
        }
        
        return true;
    }
    
    /** */
    private List<LuaDocCommentBlock> split(LuaDocCommentBlock block) {
        List<LuaDocCommentBlock> variants = new ArrayList<>();
        LuaDocCommentBlock variant = null;
        
    // for each tag
        for (LuaDocTag tag:block.getTags()) {
            if (tag.matchName(LuaDocTag.NAME) == true) {
                continue;
            }
            
            if (tag.matchName(LuaDocTag.FUNC) == true) {
                if (variant != null) {
                    variants.add(variant);
                    variant = null;
                }
            }
            
            if (variant == null) {
                variant = new LuaDocCommentBlock(tag.getLine());
            }
            variant.addTag(tag);
        }
        
        if (variant != null) {
            variants.add(variant);
        }
        
        return variants;
    }
    
    /** */
    private LuaDocFuncParam parseParam(LuaDocParseContext context,
        LuaDocTag tag) {
    //
        String tagValue = tag.getValue();
    // check if it has name
        int indexOf = tagValue.indexOf(' ');
        if (indexOf <= 0) {
            LuaDocIssue.addError(context,LuaDocFuncParam.ERROR_NO_PARAM_NAME,
                null,res.getStr("no.param.name"),tag.getLine());
            return null;
        }        
        
        LuaDocFuncParam param = new LuaDocFuncParam();
        param.setName(tagValue.substring(0,indexOf));
        param.setDesc(tagValue.substring(indexOf + 1));
        param.setLine(tag.getLine());
       
        return param;
    }
    
    /** */
    private LuaDocFuncVariant parseVariant(LuaDocCommentBlock block,
        LuaDocParseContext context,LuaDocFunc luaDocFunc) {
    // variant
        LuaDocFuncVariant variant = new LuaDocFuncVariant();
        
    // values
        variant.setBrief(block.getTagValue(LuaDocTag.BRIEF));
        variant.setFull(block.getTagValue(LuaDocTag.FULL));
        variant.setReturn(block.getTagValue(LuaDocTag.RETURN));
        variant.setLine(block.getBeginLine());
        
    // parameters
        for (LuaDocTag tag:block.getTags()) {
            if (tag.matchName(LuaDocTag.PARAM)) {                
                LuaDocFuncParam param = parseParam(context,tag);
                if (param == null) {
                    return null;
                }
                variant.addParam(param);
            }
        }
        
        return variant;
    }
    
    /** */
    private void validateFuncParams(LuaDocFuncVariant variant,LuaFunc luaFunc,
        LuaDocParseContext context) {
    //
        if (luaFunc == null) {
            return;
        }
    
        boolean hasVarArg = false;
    // unknown parameters
        for (LuaDocFuncParam luaDocParam:variant.getParams()) {
        // vararg
            if (LuaDocFuncParam.VAR_ARG.equals(luaDocParam.getName()) &&
                luaFunc.getParams().isVarArg() == true) {
            //
                 hasVarArg = true;
                 continue;               
            }
            
            
            if (luaFunc.getParams().contains(luaDocParam.getName()) == false) {
                LuaDocIssue.addWarning(context,
                    LuaDocFuncParam.WARNING_UNKNOWN_PARAM,luaDocParam.getName(),
                    res.getStr("unknown.param",luaDocParam.getName()),
                    luaDocParam.getLine());
            }
        }
        
    // missing vararg description
        if (hasVarArg == false && luaFunc.getParams().isVarArg() == true) {
            LuaDocIssue.addWarning(context,
                LuaDocFuncParam.WARNING_MISSING_PARAM,LuaDocFuncParam.VAR_ARG,
                res.getStr("missing.param",LuaDocFuncParam.VAR_ARG),
                variant.getLine());
        }
        
        String missing = null;
    // missing parameters
        for (String luaFuncParamName:luaFunc.getParams().getNames()) {
            if (variant.getParam(luaFuncParamName) == null) {
                if (missing == null) {
                    missing = "";
                }
                else {
                    missing += ",";
                }
                missing += luaFuncParamName;
            }
        }
        
        if (missing != null) {
            LuaDocIssue.addWarning(context,
                LuaDocFuncParam.WARNING_MISSING_PARAM,missing,
                res.getStr("missing.param",missing),variant.getLine());
        }
    }
    
    /** */
    private boolean isLocal(LuaFunc luaFunc) {
        if (luaFunc == null) {
            return false;
        }
        return (luaFunc instanceof LuaLocalFunc);
    }
    
    /** */
    private boolean isMethod(String funcName) {
        return funcName.indexOf(':') != -1;
    }
    
    /** */
    @Override
    void parse(LuaDocCommentBlock block,List<LuaElement> elements,int index,
        LuaDocParseContext context) {
    // validate
        if (validateTags(context,block,new String[]{
            LuaDocTag.FUNC + "*",
            LuaDocTag.NAME + "?",
            LuaDocTag.BRIEF + "*",
            LuaDocTag.FULL + "*",
            LuaDocTag.PARAM + "*",
            LuaDocTag.RETURN + "*"}) == false) {
        //
            return;
        }
        if (validateNoValues(context,block,LuaDocTag.FUNC) == false) {
            return;
        }
        if (validateFuncTags(context,block) == false) {
            return;
        }
        
    // if the name is not provided via a tag, then the comment block must be
    // followed by a function from which the name is taken
        if (block.hasTag(LuaDocTag.NAME) == false &&
            matchNext(elements,index) == false) {
        //
            LuaDocIssue.addError(context,LuaDocFunc.NOT_FOLLOWED_BY_FUNC,null,
                res.getStr("not.followed.by.func"),block.getBeginLine());  
            return;
        }
        
    // the next element (function)
        LuaFunc luaFunc = null;
        LuaElement nextLuaElement = getNextElement(elements,index);
        if (nextLuaElement instanceof LuaFunc) {
            luaFunc = (LuaFunc)nextLuaElement;
        }
        
    // function
        LuaDocFunc luaDocFunc = new LuaDocFunc();

    // function name
        String funcName = block.getTagValue(LuaDocTag.NAME);
        if (funcName != null) {
            if (funcName.charAt(0) == '.' || funcName.charAt(0) == ':') {
                funcName = getModule(context).getName() + funcName;
            }
        }
        if (funcName == null && luaFunc != null) {
            funcName = luaFunc.getName();
        }
        if (funcName == null) {
            LuaDocIssue.addError(context,LuaDocFunc.FUNC_WITHOUT_NAME,null,
                res.getStr("func.without.name"),block.getBeginLine());
            return;
        }
        
    // local name
        String localName = null;
        try {
            localName = getLocalName(funcName,context);
        } catch (LuaDocException exception) {
            // getLocalName() adds error and thus nothing happens here
            return;
        }
        
    // function values
        luaDocFunc.setName(funcName);
        luaDocFunc.setLocalName(localName);
        luaDocFunc.setLocal(isLocal(luaFunc));
        luaDocFunc.setMethod(isMethod(funcName));
        luaDocFunc.updateGlobalName(getModule(context).getName());
        
    // split into variants
        List<LuaDocCommentBlock> variantBlocks = split(block);
        for (LuaDocCommentBlock variantBlock:variantBlocks) {
        // validate variant
            if (validateTags(context,variantBlock,new String[]{
                LuaDocTag.FUNC + "?",
                LuaDocTag.BRIEF + "?",
                LuaDocTag.FULL + "?",
                LuaDocTag.PARAM + "*",
                LuaDocTag.RETURN + "?"}) == false) {
            //
                return;
            }            
            
        // parse variant
            LuaDocFuncVariant variant = parseVariant(
                variantBlock,context,luaDocFunc);
            if (variant == null) {
                return;
            } 
            luaDocFunc.addVariant(variant);
        }
        
    // validate parameters (if the function does not have variants)
        if (luaDocFunc.getVariants().size() == 1) {
            LuaDocFuncVariant variant = luaDocFunc.getVariants().get(0);
            validateFuncParams(variant,luaFunc,context);
        }
        
        context.getFileData().addFunc(luaDocFunc);
    }
}