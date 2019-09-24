#ifndef AE_LUA_GET_GLOBAL_H
#define AE_LUA_GET_GLOBAL_H

#include <string>
#include "lua.hpp"

#include "Runnable.h"

namespace ae {
    
namespace lua {

/**
 * \brief Gets a Lua global and pushes it onto the stack.
 */
class LuaGetGlobal:public Runnable {
    /// The Lua state
    lua_State *L;
    
    /// The name of the global
    const std::string globalName;
    
public:
    /**
     * \brief Constructs a LuaGetGlobal.
     * \param L_ The Lua state.
     * \param globalName_ The name of the global.
     */
    LuaGetGlobal(lua_State* L_,const std::string globalName_):L(L_),
        globalName(globalName_) {
    }
    
    /** */
    virtual ~LuaGetGlobal() {
    }    
    
    /**
     * \brief Sets the Lua state.
     * \param L_ The Lua state.
     */
    void setLuaState(lua_State *L_) {
        L = L_;
    }    
    
    /**
     * \brief Gets the global.
     * \return <code>false</code> if the global cannot be retrived (sets
     *   error), <code>true</code> otherwise.
     */
    virtual bool run();
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_GET_GLOBAL_H