package com.andcreations.ae.lua.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.luaj.vm2.ast.Chunk;
import org.luaj.vm2.ast.Exp;
import org.luaj.vm2.ast.Exp.AnonFuncDef;
import org.luaj.vm2.ast.Exp.BinopExp;
import org.luaj.vm2.ast.Exp.Constant;
import org.luaj.vm2.ast.Exp.FieldExp;
import org.luaj.vm2.ast.Exp.FuncCall;
import org.luaj.vm2.ast.Exp.IndexExp;
import org.luaj.vm2.ast.Exp.NameExp;
import org.luaj.vm2.ast.Exp.PrimaryExp;
import org.luaj.vm2.ast.Exp.UnopExp;
import org.luaj.vm2.ast.Exp.VarExp;
import org.luaj.vm2.ast.FuncArgs;
import org.luaj.vm2.ast.FuncName;
import org.luaj.vm2.ast.Name;
import org.luaj.vm2.ast.ParList;
import org.luaj.vm2.ast.Stat.Assign;
import org.luaj.vm2.ast.Stat.FuncDef;
import org.luaj.vm2.ast.Stat.LocalAssign;
import org.luaj.vm2.ast.Stat.LocalFuncDef;
import org.luaj.vm2.ast.Stat.Return;
import org.luaj.vm2.ast.TableConstructor;
import org.luaj.vm2.ast.TableField;
import org.luaj.vm2.ast.Visitor;
import org.luaj.vm2.parser.LuaParser;
import org.luaj.vm2.parser.ParseException;
import org.luaj.vm2.parser.TokenMgrError;

/**
 * Parses a Lua file.
 *
 * @author Mikolaj Gucki
 */
public class LuaFileParser {
    /** */
    private String getFullName(FuncName funcName) {
        String name = funcName.name.name;       
        if (funcName.dots != null) {
            for (Object dot:funcName.dots) {
                name = name + "." + dot.toString();
            }
        }
        if (funcName.method != null) {
            name = name + ":" + funcName.method;
        }
        
        return name;
    }
    
    /** */
    private LuaFuncParams createFuncParams(ParList parlist) {
        List<String> names = new ArrayList<>();
        
        if (parlist.names != null) {
            for (Object nameObj:parlist.names) {
                Name name = (Name)nameObj;
                names.add(name.name);
            }
        }
        
        return new LuaFuncParams(names,parlist.isvararg);
    }
    
    /** */
    private LuaVariableExpression createVariableExpression(VarExp exp) {
        if (exp instanceof NameExp) {
            NameExp nameExp = (NameExp)exp;
            return new LuaNameExpression(nameExp.name.name,exp.beginLine,
                exp.endLine);
        }
        if (exp instanceof FieldExp) {
            FieldExp fieldExp = (FieldExp)exp;
            return new LuaFieldExpression(
                createPrimaryExpression(fieldExp.lhs),
                fieldExp.name.name,exp.beginLine,exp.endLine);        
        }
        if (exp instanceof IndexExp) {
            IndexExp indexExp = (IndexExp)exp;
            return new LuaIndexExpression(
                createPrimaryExpression(indexExp.lhs),
                createExpression(indexExp.exp),exp.beginLine,exp.endLine);
        }
        
        throw new IllegalArgumentException(String.format(
            "Unsupported expression type %s",exp.getClass()));
    }
    
    /** */
    private LuaFuncArgs createFuncArgs(FuncArgs funcArgs) {
        List<LuaExpression> exps = new ArrayList<>();
        if (funcArgs.exps != null) {
            for (Object expObj:funcArgs.exps) {
                exps.add(createExpression((Exp)expObj));
            }
        }
        
        return new LuaFuncArgs(exps,funcArgs.beginLine,funcArgs.endLine);
    }
    
    /** */
    private LuaPrimaryExpression createPrimaryExpression(PrimaryExp exp) {
        if (exp instanceof VarExp) {
            return createVariableExpression((VarExp)exp);
        }
        if (exp instanceof FuncCall) {
            FuncCall funcCall = (FuncCall)exp;
            return new LuaFuncCall(createPrimaryExpression(funcCall.lhs),
                createFuncArgs(funcCall.args),exp.beginLine,exp.endLine);
        }
        
        throw new IllegalArgumentException(String.format(
            "Unsupported expression type %s",exp.getClass()));
    }
    
    /** */
    private LuaConst createConst(Constant constant) {
        LuaType type = null;
        if (constant.value.isstring()) {
            type = LuaType.STRING;
        }
        
        return new LuaConst(type,constant.value.toString(),
            constant.beginLine,constant.endLine);
    }
    
    /** */
    private LuaTableConstructor createTable(TableConstructor tableExp) {
        List<LuaTableField> fields = new ArrayList<>();
        if (tableExp.fields != null) {
        // for each field
            for (Object fieldObj:tableExp.fields) {
                TableField field = (TableField)fieldObj;
                
                LuaExpression index = null;
                if (field.index != null) {
                    index = createExpression(field.index);
                }
                LuaExpression rightHandExp = createExpression(field.rhs);
            // create field
                fields.add(new LuaTableField(index,field.name,rightHandExp,
                    field.beginLine,field.endLine));
            }
        }
        
        return new LuaTableConstructor(
            fields,tableExp.beginLine,tableExp.endLine);
    }
    
    /** */
    private LuaExpression createExpression(Exp exp) {
        if (exp instanceof PrimaryExp) {
            return createPrimaryExpression((PrimaryExp)exp);
        }
        if (exp instanceof Constant) {
            Constant constant = (Constant)exp;
            return createConst(constant);
        }
        if (exp instanceof TableConstructor) {
            TableConstructor tableExp = (TableConstructor)exp;
            return createTable(tableExp);
        }
        if (exp instanceof UnopExp) {
            UnopExp unopExp = (UnopExp)exp;
            return new LuaUnopExpression(unopExp.op,
                createExpression(unopExp.rhs),
                unopExp.beginLine,unopExp.endLine);
        }
        if (exp instanceof BinopExp) {
            BinopExp binopExp = (BinopExp)exp;
            return new LuaBinopExpression(
                createExpression(binopExp.lhs),binopExp.op,
                createExpression(binopExp.rhs),
                binopExp.beginLine,binopExp.endLine);
        }
        if (exp instanceof AnonFuncDef) {
            AnonFuncDef anonFuncDef = (AnonFuncDef)exp;
            return new LuaAnonFunc(anonFuncDef.beginLine,anonFuncDef.endLine,
                createFuncParams(anonFuncDef.body.parlist));            
        }
        
        throw new IllegalArgumentException(String.format(
            "Unsupported expression type %s",exp.getClass()));
    }
    
    /** */
    private LuaAssignment createAssignment(Assign assign) {
    // left-hand variables
        List<LuaVariableExpression> vars = new ArrayList<>();                
        for (Object varExpObj:assign.vars) {
            vars.add(createVariableExpression((VarExp)varExpObj));
        }
        
    // expressions
        List<LuaExpression> exps = new ArrayList<>();
        for (Object expObj:assign.exps) {
            exps.add(createExpression((Exp)expObj));
        }

        return new LuaAssignment(vars,exps,assign.beginLine,assign.endLine);          
    }
    
    /** */
    private LuaLocalAssignment createLocalAssignment(LocalAssign assign) {
    // variable names
        List<String> names = new ArrayList<>();                
        for (Object nameObj:assign.names) {
            names.add(((Name)nameObj).name);            
        }
        
    // expressions
        List<LuaExpression> exps = new ArrayList<>();
        if (assign.values != null) {
            for (Object expObj:assign.values) {
                exps.add(createExpression((Exp)expObj));
            }
        }

        return new LuaLocalAssignment(
            names,exps,assign.beginLine,assign.endLine);         
    }
    
    /** */
    private LuaReturn createReturn(Return rtrn) {
    // expressions
        List<LuaExpression> exps = new ArrayList<>();
        if (rtrn != null && rtrn.values != null) {
            for (Object expObj:rtrn.values) {
                exps.add(createExpression((Exp)expObj));
            }
        }
        
        return new LuaReturn(exps,rtrn.beginLine,rtrn.endLine);
    }
    
    /** */
    private void parseSingleLineComments(String src,LuaFileInfo info)
        throws IOException,LuaParseException {
    //
        StringReader srcReader = new StringReader(src);
        BufferedReader reader = new BufferedReader(srcReader);
        
        int lineNo = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            lineNo++;
            
            int indexOf = line.indexOf("--");
            if (indexOf != -1) {
                String comment = line.substring(indexOf,line.length());
                info.addSingleLineComment(new LuaSingleLineComment(
                    comment,lineNo));
            }
        }
    }
    
    /** */
    public LuaFileInfo parse(String src) throws LuaParseException {
        StringReader srcReader = new StringReader(src);
    // parse
        LuaParser parser = new LuaParser(srcReader);
        Chunk chunk;
        try {
            chunk = parser.Chunk();
        } catch (ParseException exception) {
            throw new LuaParseException(exception.getMessage());
        } catch (TokenMgrError exception) {
            throw new LuaParseException(exception.getMessage());
        }
        
        final LuaFileInfo info = new LuaFileInfo();
    // build info
        chunk.accept(new Visitor() {
            /** */
            @Override
            public void visit(FuncDef func) {
                info.addFunc(new LuaFunc(getFullName(func.name),func.beginLine,
                    func.endLine,createFuncParams(func.body.parlist)));
            }
            
            /** */
            @Override
            public void visit(LocalFuncDef func) {
                info.addLocalFunc(new LuaLocalFunc(
                    func.name.name,func.beginLine,func.endLine,
                    createFuncParams(func.body.parlist)));
            }
            
            /** */
            @Override
            public void visit(Assign assign) {
                info.addAssignment(createAssignment(assign));
            }
            
            /** */
            @Override
            public void visit(LocalAssign assign) {
                if (assign != null) {
                    info.addLocalAssignment(createLocalAssignment(assign));
                }
            }
            
            /** */
            @Override
            public void visit(Return rtrn) {
                info.addReturn(createReturn(rtrn));
            }
        });
        
        try {
            parseSingleLineComments(src,info);
        } catch (IOException exception) {
            throw new LuaParseException(exception.getMessage());
        }
        
        return info;
    }
}