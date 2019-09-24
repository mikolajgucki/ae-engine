#include "LuaTableUtil.h"

using namespace std;

namespace ae {
    
namespace lua {

/** */    
bool LuaTableUtil::newGlobalTable(lua_State *L,const std::string& name) {
    lua_newtable(L);
    lua_setglobal(L,name.c_str());
    return true;
}

    
/** */
bool LuaTableUtil::tableToMap(lua_State *L,int tableIndex,
    map<string,string> &result) {
// the first key
    lua_pushnil(L);
    
// while there are elements in the table
    while (lua_next(L,tableIndex) != 0) {
    // the key and value must be strings
        if (lua_isstring(L,-1) == 0) {
            setError("Encountered non-string table value");
            lua_pop(L,2); // pop key and value
            return false;
        }
        if (lua_isstring(L,-2) == 0) {
            setError("Encountered non-string table key");
            lua_pop(L,2); // pop key and value
            return false;
        }
        
    // insert
        const char *value = lua_tostring(L,-1); 
        const char *key = lua_tostring(L,-2);
        result[string(key)] = string(value);
    
    // pop the value and leave the key on the stack
        lua_pop(L,1);
    }
    
    return true;
}
    
/** */
bool LuaTableUtil::valuesToVector(lua_State *L,int tableIndex,
    vector<string> &result) {
// the first key
    lua_pushnil(L);
    
// while there are elements in the table
    while (lua_next(L,tableIndex) != 0) {
    // the value must be a string
        if (lua_isstring(L,-1) == 0) {
            setError("Encountered non-string table value");
            lua_pop(L,2); // pop key and value            
            return false;
        }
        
    // push
        const char *value = lua_tostring(L,-1);
        result.push_back(string(value));
    
    // pop the value and leave the key on the stack
        lua_pop(L,1);
    }
    
    return true;
}

/** */
void LuaTableUtil::arrayToTable(lua_State *L,float values[],int count) {
    lua_newtable(L);
    for (int index = 0; index < count; index++) {
        lua_pushinteger(L,index + 1); // key
        lua_pushnumber(L,values[index]); // value
        lua_settable(L,-3);
    }
}
    
} // namespace
    
} // namespace