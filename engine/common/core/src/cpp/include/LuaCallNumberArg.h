#ifndef AE_LUA_CALL_NUMBER_ARG_H
#define AE_LUA_CALL_NUMBER_ARG_H

#include "lua.hpp"
#include "LuaCallArg.h"

namespace ae {
    
namespace lua {
  
/**
 * \brief Represents a Lua call number argument.
 */
class LuaCallNumberArg:public LuaCallArg {
    /// The argument value.
    const lua_Number value;
    
public:
    /** */    
    LuaCallNumberArg(const lua_Number &value_):LuaCallArg(),value(value_) {
    }
    
    /** */
    virtual ~LuaCallNumberArg() {
    }
    
    /**
     * \brief Pushes the argument onto the Lua stack.
     * \param L The Lua state.
     */
    virtual void pushArg(lua_State *L) {
        lua_pushnumber(L,value);
    }
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_CALL_NUMBER_ARG_H