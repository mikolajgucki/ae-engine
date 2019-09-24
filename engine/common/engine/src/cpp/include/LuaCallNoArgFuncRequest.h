#ifndef AE_LUA_CALL_NO_ARG_FUNC_REQUEST_H
#define AE_LUA_CALL_NO_ARG_FUNC_REQUEST_H

#include "lua.hpp"
#include "LuaEngine.h"
#include "EngineRequest.h"

namespace ae {
    
namespace engine {
    
/**
 * \brief Engine request which calls a no-argument Lua function.
 */
class LuaCallNoArgFuncRequest:public EngineRequest {
protected:
    /// The Lua state.
    lua_State *L;
    
    /// The Lua function name.
    const char *funcName;
    
    /// Indicates if to fail if there is no such Lua function.
    bool failOnNoFunc;
    
public:
    /**
     * \brief Constructs a LuaCallNoArgFuncRequest.
     * \param L_ The Lua state.
     * \param funcName_ The (fully qualified) Lua function name.
     * \param failOnNoFunc_ Indicates if to fail if there is no
     *   such Lua function.
     */
    LuaCallNoArgFuncRequest(lua_State *L_,const char *funcName_,
        bool failOnNoFunc_):EngineRequest(),L(L_),funcName(funcName_),
        failOnNoFunc(failOnNoFunc_) {
    }
    
    /** */
    virtual ~LuaCallNoArgFuncRequest() {
    }
    
    /** */
    virtual bool run();
    
    /**
     * \brief Utility method which adds the call request to the engine.
     * \param luaEngine The Lua engine.
     * \param funcName The (fully qualified) Lua function name.
     * \param failOnNoFunc Indicates if to fail if there is no
     *   such Lua function.
     * \return `true` on success, `false` otherwise.     
     */
    static bool call(LuaEngine *luaEngine,const char *funcName,
        bool failOnNoFunc);
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_CALL_NO_ARG_FUNC_REQUEST_H