package com.andcreations.ae.lua.hierarchy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.andcreations.ae.issue.Issue;
import com.andcreations.ae.lua.parser.LuaConst;
import com.andcreations.ae.lua.parser.LuaExpression;
import com.andcreations.ae.lua.parser.LuaFieldExpression;
import com.andcreations.ae.lua.parser.LuaFileInfo;
import com.andcreations.ae.lua.parser.LuaFuncArgs;
import com.andcreations.ae.lua.parser.LuaFuncCall;
import com.andcreations.ae.lua.parser.LuaLocalAssignment;
import com.andcreations.ae.lua.parser.LuaNameExpression;
import com.andcreations.ae.lua.parser.LuaPrimaryExpression;
import com.andcreations.ae.lua.parser.LuaType;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class LuaFileClassParser {
    /** */
    public static final String ISSUE_NO_SUBCLASS_ARGS = "no.subclass.args";
    
    /** */
    public static final String ISSUE_INVALID_SUBCLASS_ARG =
        "invalid.subclass.arg";
    
    /** */
    public static final String ISSUE_DUPLICATED_CALL = "duplicated.call";
    
    /** */
    private BundleResources res = new BundleResources(LuaFileClassParser.class);     
    
    /** */
    private LuaFileClassInfo info;
    
    private Map<String,String> requireMap;
    
    /** */
    private boolean matchFieldExp(LuaPrimaryExpression exp,String... names) {
        List<LuaPrimaryExpression> exps = new ArrayList<>();        
    // expressions
        while (true) {
            if (exp instanceof LuaFieldExpression) {
                exps.add(exp);
                
            // next expression
                LuaFieldExpression fieldExp = (LuaFieldExpression)exp;
                exp = fieldExp.getLeftHandExp();
                if (exp == null) {
                    return false;
                }                
                continue;
            }
            if (exp instanceof LuaNameExpression) {
                exps.add(exp);
                break;
            }
            
            return false;
        }
        
    // match the names order
        Collections.reverse(exps);
        
    // must be exactly the same size
        if (exps.size() != names.length) {
            return false;
        }
        
    // the 1st expression
        if ((exps.get(0) instanceof LuaNameExpression) == false) {            
            return false;
        }
        LuaNameExpression nameExp = (LuaNameExpression)exps.get(0);
        if (names[0].equals(nameExp.getName()) == false) {
            return false;
        }
        
    // the following expressions
        for (int index = 1; index < exps.size(); index++) {
            if ((exps.get(index) instanceof LuaFieldExpression) == false) {
                return false;
            }
            
            LuaFieldExpression fieldExp = (LuaFieldExpression)exps.get(index);
            if (names[index].equals(fieldExp.getName()) == false) {
                return false;
            }
        }
        
        return true;
    }
    
    /** */
    private String getSuperclass(LuaFuncCall call) {
        LuaFuncArgs args = call.getArgs();
        if (args == null) {
            info.addIssue(Issue.error(ISSUE_NO_SUBCLASS_ARGS,
                res.getStr("no.subclass.args"),call.getBeginLine()));
            return null;
        }
        List<LuaExpression> exps = args.getExps();
        if (exps == null || exps.size() != 1) {
            info.addIssue(Issue.error(ISSUE_NO_SUBCLASS_ARGS,
                res.getStr("no.subclass.args"),call.getBeginLine()));
            return null;
        }
        LuaExpression exp = exps.get(0);
        if ((exp instanceof LuaFieldExpression) == false &&
            (exp instanceof LuaNameExpression) == false) {
        //
            info.addIssue(Issue.error(ISSUE_INVALID_SUBCLASS_ARG,
                res.getStr("invalid.subclass.arg"),call.getBeginLine()));
            return null;
        }
        
        List<String> names = new ArrayList<>();
        while (true) {
            if (exp instanceof LuaFieldExpression) {
                LuaFieldExpression fieldExp = (LuaFieldExpression)exp;
                names.add(fieldExp.getName());
                
            // next expression
                exp = fieldExp.getLeftHandExp();
                if (exp == null) {
                    break;
                }                
                continue;
            }
            if (exp instanceof LuaNameExpression) {
                LuaNameExpression nameExp = (LuaNameExpression)exp;
                names.add(nameExp.getName());
                break;
            }
            
            info.addIssue(Issue.error(ISSUE_INVALID_SUBCLASS_ARG,
                res.getStr("invalid.subclass.arg"),call.getBeginLine()));
            return null;
        }
        
        Collections.reverse(names);
        StringBuilder superclass = new StringBuilder();
        for (String name:names) {
            if (superclass.length() > 0) {
                superclass.append(".");
            }
            superclass.append(name);
        }
        
        return superclass.toString();
    }
    
    /** */
    private boolean getRequire(String name,LuaExpression exp) {
        if ((exp instanceof LuaFuncCall) == false) {
            return false;
        }
        
        LuaFuncCall call = (LuaFuncCall)exp;
    // require?
        if (matchFieldExp(call.getLeftHandExp(),"require") == false) {
            return false;
        }
        
        if (call.getArgs() == null) {
            return false;
        }
        LuaFuncArgs args = call.getArgs();
        if (args.getExps() == null || args.getExps().isEmpty() == true) {
            return false;
        }
        LuaExpression argExp = args.getExps().get(0);
        if ((argExp instanceof LuaConst) == false) {
            return false;
        }
        LuaConst argConst = (LuaConst)argExp;
        if (argConst.getType() != LuaType.STRING) {
            return false;
        }
        requireMap.put(name,argConst.getValue());
        
        return false;
    }
    
    /** */
    private boolean getRequire(LuaLocalAssignment assignment) {
        if (assignment.getExps() == null ||
            assignment.getExps().isEmpty() == true ||
            assignment.getNames() == null ||
            assignment.getNames().isEmpty() == true) {
        //
            return false;
        }
        
        boolean hasRequire = false;
        int index = 0;
        while (true) {
            if (index >= assignment.getNames().size()) {
                break;
            }
            if (index >= assignment.getExps().size()) {
                break;
            }
            
            String name = assignment.getNames().get(index);
            LuaExpression exp = assignment.getExps().get(index);
            
            if (getRequire(name,exp) == true) {
                hasRequire = true;
            }
            index++;
        }
        
        return hasRequire;
    }
    
    /** */
    private boolean getInfo(LuaLocalAssignment assignment) {
        if (assignment.getExps() == null ||
            assignment.getExps().isEmpty() == true) {
        //
            return false;
        }
        
        LuaExpression exp = assignment.getExps().get(0);
        if ((exp instanceof LuaFuncCall) == false) {
            return false;
        }
        
        LuaFuncCall call = (LuaFuncCall)exp;
        boolean isClass = matchFieldExp(
            call.getLeftHandExp(),"ae","oo","class");
        if (isClass == true) {
            info.setClass();
        }
        
        boolean isSubclass = matchFieldExp(
            call.getLeftHandExp(),"ae","oo","subclass");
        if (isSubclass == true) {
            String superclass = getSuperclass(call);
            if (superclass == null) {
                return false;
            }

            if (requireMap.containsKey(superclass) == true) {
                superclass = requireMap.get(superclass);
            }
            
            info.setSuperclass(call.getBeginLine(),superclass);
        }
        
        if (isClass == false && isSubclass == false) {
            return false;
        }        
        return true;
    }
    
    /** */
    public synchronized LuaFileClassInfo getLuaClassInfo(
        LuaFileInfo luaFileInfo) {
    //
        if (luaFileInfo.getLocalAssignments() == null ||
            luaFileInfo.getLocalAssignments().isEmpty() == true) {
        //
            return null;
        }
        
        info = new LuaFileClassInfo();
        requireMap = new HashMap<String,String>();
        
        boolean encountered = false;
    // for each local assignment
        for (LuaLocalAssignment assignment:luaFileInfo.getLocalAssignments()) {
            if (getRequire(assignment) == true) {
                continue;
            }
            
            if (getInfo(assignment) == true) {
                if (encountered) {
                    info.addIssue(Issue.error(ISSUE_DUPLICATED_CALL,
                        res.getStr("duplicated.call"),
                        assignment.getBeginLine()));
                    return info;
                }
                encountered = true;
            }
        }
        
        return info;
    }
}