#ifndef AE_LUA_COMMON_H
#define AE_LUA_COMMON_H

#include "lua.hpp"
#include <string>

namespace ae {
    
namespace lua {
 
/// The value for the Lua boolean value false
#define AE_LUA_FALSE 0

/// The value for the Lua boolean value true
#define AE_LUA_TRUE 1
    
/**
 * \brief Represents a key/value pair.
 * \param K The key type.
 * \param V The value type.
 */
template<typename K,typename V> struct LuaPair {
    /** The key. */
    K key;
    
    /** The value. */
    V value;
};
    
/**
 * \brief Represents a Lua field with name and its value.
 * \param V The value type.
 */
template<typename V> struct LuaField {
    /** The field name. */
    const char *name;
    
    /** The field value. */
    V value;
};
    
/**
 * \brief Converts a boolean into a Lua boolean.
 * \param value The boolean value.
 * \return The Lua boolean.
 */
int luaBoolean(bool value);

/**
 * \brief Pushes an error.
 * \param L The Lua state.
 * \param msg The error message.
 */ 
void luaPushError(lua_State *L,const char *msg);

/**
 * \brief Pushes the no memory error.
 * \param L The Lua state.
 */
void luaPushNoMemoryError(lua_State *L);

/**
 * \brief Sets a field of number type.
 * \param L The Lua state.
 * \param name The field name.
 * \param value The field value.
 */
void luaSetField(lua_State *L,const std::string &name,lua_Number value);
    
/**
 * \brief Sets a field of string type of the element at the stack top.
 * \param L The Lua state.
 * \param name The field name.
 * \param value The field value.
 */
void luaSetField(lua_State *L,const std::string &name,const char *value);

/**
 * \brief Sets fields of string type of the element at the stack top.
 * \param L The Lua state.
 * \param fields The fields.
 */
void luaSetFields(lua_State *L,const LuaField<const char *> *fields);

/**
 * \brief Sets fields of number type of the element at the stack top.
 * \param L The Lua state.
 * \param fields The fields.
 */
void luaSetFields(lua_State *L,const LuaField<lua_Number> *fields);

/**
 * \brief Sets a string/string pair of the table at the stack top.
 * \param L The Lua state.
 * \param key The key.
 * \param value The value.
 */
void luaSetTable(lua_State *L,const char *key,const char *value);

/**
 * \brief Sets a number/string pair of the table at the stack top.
 * \param L The Lua state.
 * \param key The key.
 * \param value The value.
 */
void luaSetTable(lua_State *L,lua_Number key,const char *value);

/**
 * \brief Sets a string/number pair of the table at the stack top.
 * \param L The Lua state.
 * \param key The key.
 * \param value The value.
 */
void luaSetTable(lua_State *L,const char *key,lua_Number value);

/**
 * \brief Sets a string/integer pair of the table at the stack top.
 * \param L The Lua state.
 * \param key The key.
 * \param value The value.
 */
void luaSetTable(lua_State *L,const char *key,lua_Integer value);

/**
 * \brief Sets key/value pairs of the table at the stack top. The function
 *   stops iteration over the pairs when the null key is encountered.
 * \param L The Lua state.
 * \param pairs The key/value pairs to set.
 * \param nullKey The value of the key which represents the null.  
 */
void luaSetTable(lua_State *L,const LuaPair<lua_Number,const char *> *pairs,
    lua_Number nullKey);

/**
 * \brief Loads a user type. Should be used within the open function of
 *     called by luaL_requiref.
 * \param L The Lua state.
 * \param metatableName The name of the metatable.
 * \param funcs The functions of the user type table.
 * \param methods The user type methods.
 * \param metamethods The user type meta-methods (start with __).
 */
void luaLoadUserType(lua_State *L,const std::string &metatableName,
    const struct luaL_Reg *funcs,const struct luaL_Reg *methods,
    const struct luaL_Reg *metamethods);

/**
 * \brief Adds methods to a user type.
 * \param L The Lua state.
 * \param metatableName The name of the metatable.
 * \param methods The user type methods to add.
 */
bool addMethodsToUserType(lua_State *L,const std::string& metatableName,
    const struct luaL_Reg *methods);

/**
 * \brief Logs the Lua stack (for debugging).
 * \param L The Lua state.
 */
void logLuaStack(lua_State *L);

} // namespace
    
} // namespace

#endif // AE_LUA_COMMON_H