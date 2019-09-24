package com.andcreations.ae.studio.plugins.lua.lib.parser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.luaj.vm2.ast.Chunk;
import org.luaj.vm2.ast.Exp;
import org.luaj.vm2.ast.Exp.FieldExp;
import org.luaj.vm2.ast.Exp.NameExp;
import org.luaj.vm2.ast.Exp.VarExp;
import org.luaj.vm2.ast.FuncName;
import org.luaj.vm2.ast.Name;
import org.luaj.vm2.ast.ParList;
import org.luaj.vm2.ast.Stat.Assign;
import org.luaj.vm2.ast.Stat.FuncDef;
import org.luaj.vm2.ast.Stat.LocalAssign;
import org.luaj.vm2.ast.Stat.LocalFuncDef;
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
    /** The file source. */
    private String src;
    
    /** */
    public LuaFileParser(String src) {
        this.src = src;
    }
    
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
    private FuncParams createFuncParams(ParList parlist) {
        List<String> names = new ArrayList<>();
        
        if (parlist.names != null) {
            for (Object nameObj:parlist.names) {
                Name name = (Name)nameObj;
                names.add(name.name);
            }
        }
        
        return new FuncParams(names,parlist.isvararg);
    }
    
    /** */
    private String getName(Exp exp) {
        if (exp instanceof NameExp) {
            NameExp nameExp = (NameExp)exp;
            return nameExp.name.name;
        }
        if (exp instanceof FieldExp) {
            FieldExp fieldExp = (FieldExp)exp;
            String name = fieldExp.name.name;
            
            String lhsName = getName(fieldExp.lhs);
            if (lhsName != null) {
                name = lhsName + "." + name;
            }
            
            return name;        
        }
        
        return "?";
    }
    
    /** */
    public LuaFileInfo parse() throws LuaParseException {
        StringReader reader = new StringReader(src);
    // parse
        LuaParser parser = new LuaParser(reader);
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
            public void visit(FuncDef stat) {
                info.addFunc(new Func(getFullName(stat.name),stat.beginLine,
                    stat.endLine,createFuncParams(stat.body.parlist)));
            }
            
            /** */
            @Override
            public void visit(LocalFuncDef stat) {
                info.addLocalFunc(new Func(stat.name.name,stat.beginLine,
                    stat.endLine,createFuncParams(stat.body.parlist)));
            }
            
            /** */
            @Override
            public void visit(Assign stat) {
                List<String> names = new ArrayList<>();
                for (Object varExpObj:stat.vars) {
                    VarExp varExp = (VarExp)varExpObj;
                    names.add(getName(varExp));
                }                
                info.addAssignment(new Assignment(
                    names,stat.beginLine,stat.endLine));  
            }
            
            /** */
            @Override
            public void visit(LocalAssign stat) {
                List<String> names = new ArrayList<>();
                for (Object nameObj:stat.names) {
                    names.add(((Name)nameObj).name);
                }
                info.addLocalAssignment(new Assignment(
                    names,stat.beginLine,stat.endLine));  
            }
        });
        
        return info;
    }
    
    /** */
    public static void main(String[] args) throws Exception {
        LuaFileParser parser = new LuaFileParser(
        "-- cannot use require here as searcher has not yet been set\n"+
        "ae.load('ae/init.lua')\n"+
        "local test = 'abc'\n" +
        "myvar = test\n"+
        "ae.xyz = 'xyz'\n"+
        "\n"+
        "-- @func sleep(seconds)\n"+
        "-- @brief Sleeps a number of seconds (for debugging/development only).\n"+
        "-- @param seconds The seconds to sleep.\n"+
        "function sleep(seconds)\n"+
        "    local start = os.clock()\n"+
        "    while os.clock() - start <= seconds do\n"+
        "    end\n"+
        "end\n"+
        "\n"+
        "-- @localfunc initKeyHandler()\n"+
        "-- @brief Initializes the key handler.\n"+
        "local function initKeyHandler()\n"+
        "    ae.key = {}\n"+
        "    ae.key.down = function(keyName,keyCode)\n"+
        "        if not bu.actions.enabled then\n"+
        "            return\n"+
        "        end\n"+
        "    \n"+
        "        if keyCode == ae.keys['back'] then\n"+
        "            bu.actions.runBackKeyAction()\n"+
        "        end\n"+
        "        if keyCode == ae.keys['menu'] then\n"+
        "            -- TODO Handle menu key event.\n"+
        "        end\n"+
        "    end\n"+
        "    \n"+
        "    ae.key.up = function(keyName,keyCode)\n"+
        "    end\n"+
        "end\n"+
        "\n"+
        "-- @localfunc initTouchHandler()\n"+
        "-- @brief Initializes the touch handler.\n"+
        "local function initTouchHandler()\n"+
        "    local singleTouchProxy = ae.SingleTouchProxy.new(bu.ui.model)\n"+
        "    local glCoordsTouchProxy = ae.GLCoordsTouchProxy.new(singleTouchProxy)\n"+
        "\n"+
        "    ae.touch = {}    \n"+
        "    ae.touch.down = function(pointerId,x,y)\n"+
        "        if not bu.actions.enabled then\n"+
        "            return\n"+
        "        end    \n"+
        "        glCoordsTouchProxy:touchDown(pointerId,x,y)\n"+
        "    end\n"+
        "    \n"+
        "    ae.touch.move = function(pointerId,x,y)\n"+
        "        if not bu.actions.enabled then\n"+
        "            return\n"+
        "        end    \n"+
        "        glCoordsTouchProxy:touchMove(pointerId,x,y)\n"+
        "    end\n"+
        "    \n"+
        "    ae.touch.up = function(pointerId,x,y)\n"+
        "        if not bu.actions.enabled then\n"+
        "            return\n"+
        "        end    \n"+
        "        glCoordsTouchProxy:touchUp(pointerId,x,y)\n"+
        "    end\n"+
        "end\n"+
        "\n"+
        "-- @localfunc initAds()\n"+
        "-- @brief Initializes the advert libraries.\n"+
        "local function initAds()\n"+
        "    applovin.initInterstitial()\n"+
        "end\n"+
        "\n"+
        "-- @localfunc loadAds()\n"+
        "-- @brief Loads adverts.\n"+
        "local function loadAds()\n"+
        "    applovin.loadInterstitialAd()\n"+
        "    chartboost.cacheInterstitial(chartboost.location.default)\n"+
        "end\n"+
        "\n"+
        "-- @localfunc initGameCenter()\n"+
        "-- @brief Initializes Game Center.\n"+
        "local function initGameCenter()   \n"+
        "    gamecenter.authenticate()\n"+
        "end\n"+
        "\n"+
        "-- Called when the engine has been initialized.\n"+
        "function ae.ready()\n"+
        "    ae.log.trace('ae.ready() called')\n"+
        "    \n"+
        "    ae.gl.setDepthTestEnabled(false)    \n"+
        "    require('bu.plugins')    \n"+
        "    bu = require('bu.bu')\n"+
        "    \n"+
        "    initKeyHandler()\n"+
        "    initTouchHandler()\n"+
        "    \n"+
        "    initAds()\n"+
        "    loadAds()\n"+
        "    \n"+
        "    if ae.os.ios then    \n"+
        "        initGameCenter()\n"+
        "    end\n"+
        "    \n"+
        "    bu.launch()\n"+
        "end\n"+
        "\n"+
        "-- Called when the engine is about to pause.\n"+
        "function ae.pausing()\n"+
        "    ae.log.trace('ae.pausing() called')\n"+
        "    bu.state.store()    \n"+
        "    bu.music.pause()\n"+
        "end\n"+
        "\n"+
        "-- Called when the engine has just resumed.\n"+
        "function ae.resuming()\n"+
        "    ae.log.trace('ae.resuming() called')\n"+
        "    bu.state.restore()\n"+
        "    bu.music.resume()\n"+
        "    loadAds()\n"+
        "    bu.bonus.load()\n"+
        "end\n"+
        "\n"+
        "-- Called when the app is about to terminate.\n"+
        "function ae.terminating()\n"+
        "    ae.log.trace('ae.terminating() called')\n"+
        "    bu.state.store()  \n"+
        "end\n"            
        );
        parser.parse();
    }
}