#ifndef AE_LUA_CALL_STR_ARG_H
#define AE_LUA_CALL_STR_ARG_H

#include <string>
#include "lua.hpp"
#include "LuaCallArg.h"

namespace ae {
    
namespace lua {
  
/**
 * \brief Represents a Lua call argument.
 */
class LuaCallStrArg:public LuaCallArg {
    /// The argument value.
    const std::string value;
    
public:
    /** */    
    LuaCallStrArg(const std::string &value_):LuaCallArg(),value(value_) {
    }
    
    /** */
    virtual ~LuaCallStrArg() {
    }
    
    /**
     * \brief Pushes the argument onto the Lua stack.
     * \param L The Lua state.
     */
    virtual void pushArg(lua_State *L) {
        lua_pushstring(L,value.c_str());
    }
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_CALL_STR_ARG_H