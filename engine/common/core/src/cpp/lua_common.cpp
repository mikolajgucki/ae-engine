#include <cstring>
#include <string>
#include <sstream>

#include "Log.h"
#include "lua_common.h"

using namespace std;

namespace ae {

namespace lua {
    
/// The log tag.
static const char *logTag = "lua";
 
/** */
int luaBoolean(bool value) {
    return value ? AE_LUA_TRUE : AE_LUA_FALSE;
}

/** */
void luaPushError(lua_State *L,const char *msg) {
    luaL_traceback(L,L,msg,1);
    const char *traceback = lua_tostring(L,-1);    
    lua_pushstring(L,traceback);
    lua_pop(L,1);    
    lua_error(L);
}

/** */
void luaPushNoMemoryError(lua_State *L) {
    luaPushError(L,"Out of memory");
}
    
/** */
void luaSetField(lua_State *L,const std::string &name,lua_Number value) {
    lua_pushnumber(L,value);
    lua_setfield(L,-2,name.c_str());
}

/** */
void luaSetField(lua_State *L,const std::string &name,const char *value) {
    lua_pushstring(L,value);
    lua_setfield(L,-2,name.c_str());
}
   
/** */
void luaSetFields(lua_State *L,const LuaField<lua_Number> *fields) {
    int index = 0;
    while (fields[index].name != (const char *)0) {
        luaSetField(L,fields[index].name,fields[index].value);
        index++;
    }
}

/** */
void luaSetFields(lua_State *L,const LuaField<const char *> *fields) {
    int index = 0;
    while (fields[index].name != (const char *)0) {
        luaSetField(L,fields[index].name,fields[index].value);
        index++;
    }
}

/** */
void luaSetTable(lua_State *L,const char *key,const char *value) {
    lua_pushstring(L,key);
    lua_pushstring(L,value);
    lua_settable(L,-3);
}

/** */
void luaSetTable(lua_State *L,lua_Number key,const char *value) {
    lua_pushnumber(L,key);
    lua_pushstring(L,value);
    lua_settable(L,-3);
}

/** */
void luaSetTable(lua_State *L,const char *key,lua_Number value) {
    lua_pushstring(L,key);
    lua_pushnumber(L,value);
    lua_settable(L,-3);
}

/** */
void luaSetTable(lua_State *L,const char *key,lua_Integer value) {
    lua_pushstring(L,key);
    lua_pushinteger(L,value);
    lua_settable(L,-3);
}

/** */
void luaSetTable(lua_State *L,const LuaPair<lua_Number,const char *> *pairs,
    lua_Number nullKey) {
//
    int index = 0;
    while (pairs[index].key != nullKey) {
        luaSetTable(L,pairs[index].key,pairs[index].value);
        index++;
    }
}

/**
 * \brief Equivalent to the <code>luaL_newlib</code> except that this function
 *     takes pointer to luaL_Reg instead of an array.
 * \param L The Lua state.
 * \param funcs The functions which built the libarary.
 */
static void newlib(lua_State *L,const struct luaL_Reg *funcs) {
// count the number of the functions
    int nrec = 0;
    for (nrec = 0; funcs[nrec].name != (const char *)0; nrec++) {
    }    
    
    lua_createtable(L,0,nrec);    
    luaL_setfuncs(L,funcs,0);
}
    
/** */
void luaLoadUserType(lua_State *L,const std::string &metatableName,
    const struct luaL_Reg *funcs,const struct luaL_Reg *methods,
    const struct luaL_Reg *metamethods) {

// create library
    newlib(L,funcs); // stack: table
    
// create metatable
    luaL_newmetatable(L,metatableName.c_str()); // stack: table, metatable    
    newlib(L,methods); // stack: table, metatable, methods
    
// metatable[__index] = methods    
    lua_setfield(L,-2,"__index"); // stack: table, metatable
    
// set meta-methods
    for (int index = 0; metamethods[index].name != 0; index++) {
        lua_pushstring(L,metamethods[index].name);
        // stack: table, metatable, "name"
        
        lua_pushcfunction(L,metamethods[index].func);
        // stack: table, metatable, "name", function
        
    // metatable["name"] = function
        lua_rawset(L,-3);
        // statck: table, metatable
    }
    
    lua_pop(L,1);
    // stack: table
}

/** */
bool addMethodsToUserType(lua_State *L,const string& metatableName,
    const struct luaL_Reg *methods) {
// push the metatable onto the stack
    luaL_getmetatable(L,metatableName.c_str());
    // stack: metatable
    
// nil pushed if there is no such metatable
    if (lua_isnil(L,-1)) {
        lua_pop(L,1);
        return false;
    }
    
// get __index
    lua_getfield(L,-1,"__index");
    // stack, metatable, __index
    
// for each method
    for (int index = 0; methods[index].name != (const char *)0; index++) {
    // push the name
        lua_pushstring(L,methods[index].name);
        // stack: metatable, __index, "name"
        
    // push the function
        lua_pushcfunction(L,methods[index].func);
        // stack: metatable, __index, "name", function
        
    // __index["name"] = function
        lua_rawset(L,-3);
        // stack: metatable, __index
    }
    
// pop metatable, __index
    lua_pop(L,2);
    
    return true;
}

/** */
void logLuaStack(lua_State *L) {
    ostringstream str;
    str << "Lua stack top-to-bottom:\n";
    
    for (signed int index = lua_gettop(L); index > 0; index--) {
        str << "  " << index << ": ";
        if (lua_isnumber(L,index)) {
            str << lua_tonumber(L,index) << " (number)";
        }
        else if (lua_isboolean(L,index)) {
            str << lua_toboolean(L,index) << " (boolean)";
        }
        else if (lua_isstring(L,index)) {
            str << lua_tostring(L,index) << " (string)";
        }
        else if (lua_isfunction(L,index)) {
            str << " (function)";
        }
        else if (lua_iscfunction(L,index)) {
            str << " (cfunction)";
        }
        else {
            str << "(?)";
        }
            
        str << "\n";
    }
    
    Log::trace(logTag,str.str());
}

} // namespace
    
} // namespace
