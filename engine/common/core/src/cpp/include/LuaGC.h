#ifndef AE_LUA_GC_H
#define AE_LUA_GC_H

#include "lua.hpp"

#include "Error.h"
#include "LuaPCall.h"

namespace ae {
    
namespace lua {
    
/**
 * \brief Provides methods related to Lua garbage collector.
 */ 
class LuaGC:public Error {
    /// The Lua state
    lua_State *L;
    
    // The Lua protected call.
    LuaPCall luaPCall;    
    
public:
    /**
     * \brief Constructs a LuaGC.
     * \param L_ The Lua state.
     */    
    LuaGC(lua_State* L_):Error(),L(L_),luaPCall() {
    }    
    
    /**
     * \brief Constructs a LuaGC.
     */    
    LuaGC():Error(),L((lua_State* )0),luaPCall() {
    }    
    
    /** */
    virtual ~LuaGC() {
    }     
    
    /**
     * \brief Sets the Lua state.
     * \param L_ The Lua state.
     */
    void setLuaState(lua_State* L_) {
        L = L_;
    }
    
    /**
     * \brief Gets the memory used by Lua (in kilobytes).
     * \param usedMemory The argument in which to store the used memory.
     */
    void getUsedMemory(double &usedMemory);
};
    
} // namespace

} // namespace

#endif // AE_LUA_GC_H