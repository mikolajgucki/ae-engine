#ifndef AE_LUA_CALL_ARG_H
#define AE_LUA_CALL_ARG_H

#include "lua.hpp"

namespace ae {
    
namespace lua {
  
/**
 * \brief Represents a Lua call argument.
 */
class LuaCallArg {
public:
    /** */
    virtual ~LuaCallArg() {
    }
    
    /**
     * \brief Pushes the argument onto the Lua stack.
     * \param L The Lua state.
     */
    virtual void pushArg(lua_State *L) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_CALL_ARG_H