#ifndef AE_LUA_TABLE_UTIL_H
#define AE_LUA_TABLE_UTIL_H

#include <string>
#include <vector>
#include <map>

#include "lua.hpp"
#include "Error.h"

namespace ae {
    
namespace lua {
  
/**
 * \brief Provides Lua table-related utility methods.
 */
class LuaTableUtil:public Error {
public:
    /** */
    LuaTableUtil():Error() {
    }
    
    /**
     * \brief Creates a new global table.
     * \param L The Lua state.
     * \param name The table name.
     * \param <code>true</code> on success, sets error and returns
     *   <code>false</code> otherwise.
     */
    bool newGlobalTable(lua_State *L,const std::string& name);
    
    /**
     * \brief Converts Lua table into a string-string map. The keys and values
     *   must be strings.
     * \param L The Lua state.
     * \param tableIndex The index of the table in the Lua stack.
     * \param result The result map.
     * \param <code>true</code> on success, sets error and returns
     *   <code>false</code> otherwise.
     */
    bool tableToMap(lua_State *L,int tableIndex,
        std::map<std::string,std::string> &result);
    
    /**
     * \brief Converts Lua table values to a string vector. All the values must
     *   be strings. The values in the vector are not sorted by the table keys.
     * \param L The Lua state.
     * \param tableIndex The index of the table in the Lua stack.
     * \param result The result vector.
     * \param <code>true</code> on success, sets error and returns
     *   <code>false</code> otherwise.
     */
    bool valuesToVector(lua_State *L,int tableIndex,
        std::vector<std::string> &result);
    
    /**
     * \brief Converts an array of float values to a new Lua table.
     * \param L The Lua state.
     * \param values The values.
     * \param count The value count.
     */
    void arrayToTable(lua_State *L,float values[],int count);
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_TABLE_UTIL_H