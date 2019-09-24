/*
-- @module ae
-- @brief Provides the AE Engine core functions.
-- @full Provides the AE Engine core functions. This module is a C library
--   loaded by default.
*/
#include <iostream>
#include <string>

#include "LuaGetField.h"
#include "SleepFunc.h"
#include "LuaModel.h"
#include "Key.h"
#include "lua_ae.h"
#include "PrependLuaModelRequest.h"
#include "AppendLuaModelRequest.h"
#include "RemoveLuaModelRequest.h"

using namespace std;
using namespace ae::io;
using namespace ae::lua;

namespace ae {
    
namespace engine {
    
namespace lua {
    
/**
 * \brief The structure of the Lua model user data.
 */
struct LuaModelType {
    /** */
    LuaModel *luaModel;
};
typedef struct LuaModelType LuaModelType;
    
/// The name of the Lua metatable of the Lua model user type.
static const char *luaModelMetatableName = "LuaModel.metatable";

/// The library name.
static const char *aeName = "ae";
    
/// The name of the Lua global holding the Lua engine
static const char *aeGlobalLuaEngine = "ae_lua_engine";

/**
 * \brief Gets the Lua engine from the Lua state.
 * \param L The Lua state.
 * \return The Lua engine.
 */
static LuaEngine *getLuaEngine(lua_State *L) {
    lua_getglobal(L,aeGlobalLuaEngine);
    LuaEngine *engine = (LuaEngine *)lua_touserdata(L,-1);  
    lua_pop(L,1);
    return engine;    
}

/**
 * \brief Creates and pushes a Lua model user type.
 * \param L The Lua state.
 * \param luaModel The Lua model contained in the type.
 */
static void luaPushLuaModelType(lua_State *L,LuaModel *luaModel) {
// user data
    LuaModelType *data =
        (LuaModelType *)lua_newuserdata(L,sizeof(LuaModelType));
    data->luaModel = luaModel;
    
// metatable
    luaL_getmetatable(L,luaModelMetatableName);
    lua_setmetatable(L,-2);
}

/** */
static LuaModelType *checkLuaModelType(lua_State *L) {
    void *data = luaL_checkudata(L,1,luaModelMetatableName);
    luaL_argcheck(L,data != (void *)0,1,"LuaModel expected");
    return (LuaModelType *)data;
}

/*
-- @name .getOS
-- @func
-- @brief Gets the operating system under which the engine is running.
-- @return The OS which can be either `android` or `ios`. 
*/
static int aeGetOS(lua_State *L) {
    LuaEngine *luaEngine = getLuaEngine(L);
    lua_pushstring(L,luaEngine->getOSName());
    return 1;
}

/*
-- @name .getTicks
-- @func
-- @brief Gets the milliseconds elapsed since an epoch or engine start.
-- @return The ticks since an epoch or engine start.
*/
static int aeGetTicks(lua_State *L) {
    LuaEngine *luaEngine = getLuaEngine(L);
    
// get function
    GetTicksFunc *getTicksFunc =
        luaEngine->getEngine()->getCfg().getGetTicksFunc();
    if (getTicksFunc == (GetTicksFunc *)0) {
        luaPushError(L,"Get ticks function not set");
        return 0;
    }
    
// push ticks
    long ticks = getTicksFunc->getTicks();
    lua_pushnumber(L,ticks);
    
    return 1;
}

/*
-- @name .sleep
-- @func
-- @brief Suspends the execution. Use this function for debug purpuses only.
-- @param milliseconds The number milliseconds to sleep.
*/
static int aeSleep(lua_State *L) {
    LuaEngine *luaEngine = getLuaEngine(L);
    int milliseconds = (int)luaL_checknumber(L,1);
    
    SleepFunc *sleepFunc = luaEngine->getEngine()->getCfg().getSleepFunc();
    if (sleepFunc == (SleepFunc *)0) {
        luaPushError(L,"Sleep function not set");
        return 0;
    }
    sleepFunc->sleep(milliseconds);
    
    return 0;
}

/*
-- @name .load
-- @func
-- @brief Loads a source from a file.
-- @full Loads a source from a file. Supports only return of a single value.
-- @param file The name of the source file.
*/
static int aeLoad(lua_State *L) {
    LuaEngine *luaEngine = getLuaEngine(L);    
    const char *name = luaL_checkstring(L,1);
    
    int level = lua_gettop(L);
    if (luaEngine->loadSource(name) == false) {
        luaPushError(L,luaEngine->getError().c_str());       
        return 0;
    }
    
    int resultCount = lua_gettop(L) - level;
    return resultCount;
}

/*
-- @name .prependModel
-- @func
-- @brief Adds a model to the Lua engine at the beginning of the model list.
-- @param draw The name of the draw function called when the model is to be
--   drawn. Can be `nil` if the model is never drawn.
-- @param update The name of the update function called when the model is to
--   be updated. Can be `nil` if the model is never updated.
-- @return The model.
*/
static int aePrependModel(lua_State *L) {
    LuaEngine *luaEngine = getLuaEngine(L);   
    
// draw function
    LuaGetField *getLuaDrawFunc = (LuaGetField *)0;
    if (lua_isnil(L,1) == 0) {    
        string drawFuncName = string(luaL_checkstring(L,1));
        getLuaDrawFunc = new LuaGetField(L,drawFuncName);
    }
    
// update function
    LuaGetField *getLuaUpdateFunc = (LuaGetField *)0;
    if (lua_isnil(L,2) == 0) {
        string updateFuncName = string(luaL_checkstring(L,2));
        getLuaUpdateFunc = new LuaGetField(L,updateFuncName);
    }
    
    LuaModel *luaModel = new LuaModel(L,getLuaDrawFunc,getLuaUpdateFunc);
    luaEngine->getEngine()->addRequest(
        new PrependLuaModelRequest(luaEngine,luaModel));
    
    luaPushLuaModelType(L,luaModel);
    return 1;
}

/*
-- @name .appendModel
-- @func
-- @brief Adds a model to the Lua engine at the end of the model list.
-- @param draw The name of the draw function called when the model is to be
--   drawn. Can be `nil` if the model is never drawn.
-- @param update The name of the update function called when the model is to
--   be updated. Can be `nil` if the model is never updated.
-- @return The model
*/
static int aeAppendModel(lua_State *L) {
    LuaEngine *luaEngine = getLuaEngine(L); 
    
// draw function
    LuaGetField *getLuaDrawFunc = (LuaGetField *)0;
    if (lua_isnil(L,1) == 0) {    
        string drawFuncName = string(luaL_checkstring(L,1));
        getLuaDrawFunc = new LuaGetField(L,drawFuncName);
    }
    
// update function
    LuaGetField *getLuaUpdateFunc = (LuaGetField *)0;
    if (lua_isnil(L,2) == 0) {
        string updateFuncName = string(luaL_checkstring(L,2));
        getLuaUpdateFunc = new LuaGetField(L,updateFuncName);
    }
    
    LuaModel *luaModel = new LuaModel(L,getLuaDrawFunc,getLuaUpdateFunc);
    luaEngine->getEngine()->addRequest(
        new AppendLuaModelRequest(luaEngine,luaModel));
    
    luaPushLuaModelType(L,luaModel);
    return 1;
}

/*
-- @name .removeModel
-- @func
-- @brief Removes a model from the Lua engine.
-- @param model The model to remove.
*/
static int aeRemoveModel(lua_State *L) {
    LuaModelType *data = checkLuaModelType(L);    
    LuaEngine *luaEngine = getLuaEngine(L);
    
    luaEngine->getEngine()->addRequest(
        new RemoveLuaModelRequest(luaEngine,data->luaModel));    
    
    return 0;
}

/** */
static void aeSetKeyCode(lua_State *L,Key key) {
    luaSetTable(L,key.getName(),(lua_Integer)key.getCode());
}

/*
-- @name .getKeyCodeTable
-- @func
-- @brief Gets the table with the key codes in which the key name is the
--   table key and key code is the value.
-- @return The key code table.
*/
static int aeGetKeyCodeTable(lua_State *L) {
    lua_newtable(L);
    
// key: back
    aeSetKeyCode(L,Key::BACK);
    
// key: menu
    aeSetKeyCode(L,Key::MENU);
    
    return 1;
}

/*
-- @name .pause
-- @func
-- @brief Pauses the engine loop execution.
*/
static int aePause(lua_State *L) {
    LuaEngine *luaEngine = getLuaEngine(L);
        
    LuaEnginePauseListener *listener = luaEngine->getLuaEnginePauseListener();
    if (listener != (LuaEnginePauseListener *)0) {
        listener->luaEnginePause(luaEngine);
    }
    
    return 0;    
}


/*
-- @name .resume
-- @func
-- @brief Resumes the engine loop execution.
*/
static int aeResume(lua_State *L) {
    LuaEngine *luaEngine = getLuaEngine(L);
        
    LuaEnginePauseListener *listener = luaEngine->getLuaEnginePauseListener();
    if (listener != (LuaEnginePauseListener *)0) {
        listener->luaEngineResume(luaEngine);
    }
    
    return 0;    
}

/*
-- @name .quit
-- @func
-- @brief Shuts down and quits the game (application).
*/
static int aeQuit(lua_State *L) {
    LuaEngine *luaEngine = getLuaEngine(L);
    
    LuaEngineQuitListener *listener = luaEngine->getLuaEngineQuitListener();
    if (listener != (LuaEngineQuitListener *)0) {
        listener->luaEngineQuit(luaEngine);
    }
    
    return 0;
}

/** */
static const struct luaL_Reg aeFuncs[] = {
    {"getOS",aeGetOS},
    {"getTicks",aeGetTicks},
    {"sleep",aeSleep},
    {"load",aeLoad},
    {"prependModel",aePrependModel},
    {"appendModel",aeAppendModel},
    {"removeModel",aeRemoveModel},
    {"getKeyCodeTable",aeGetKeyCodeTable},
    {"pause",aePause},
    {"resume",aeResume},
    {"quit",aeQuit},
    {0,0}
};    

/** */
static int aeRequireFunc(lua_State *L) {
    luaL_newlib(L,aeFuncs);
    return 1;    
}

/** */    
void loadAELib(LuaEngine *luaEngine) {
    lua_State *L = luaEngine->getLuaState();
    
// create the Lua model metatable
    luaL_newmetatable(L,luaModelMetatableName);
    lua_pop(L,1); // pop the metatable
    
// global with the Lua engine
    lua_pushlightuserdata(L,luaEngine);
    lua_setglobal(L,aeGlobalLuaEngine);
    
// load the library
    luaL_requiref(L,aeName,aeRequireFunc,1);
    lua_pop(L,1);
}
    
} // namespace
    
} // namespace
    
} // namespace