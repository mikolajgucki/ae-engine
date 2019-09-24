#ifndef AE_LUA_CALL_FUNC_REQUEST_H
#define AE_LUA_CALL_FUNC_REQUEST_H

#include <vector>
#include "lua.hpp"
#include "LuaCallArg.h"
#include "LuaEngine.h"
#include "LuaCallNoArgFuncRequest.h"

namespace ae {
    
namespace engine {
    
/**
 * \brief Engine request which calls a Lua function.
 */
class LuaCallFuncRequest:public LuaCallNoArgFuncRequest {
    /// The function arguments.
    const std::vector<ae::lua::LuaCallArg *> args;
    
public:
    /**
     * \brief Constructs a LuaCallFuncRequest.
     * \param L_ The Lua state.
     * \param funcName_ The (fully qualified) Lua function name.
     * \param args_ The function arguments.
     * \param failOnNoFunc_ Indicates if to fail if there is no
     *   such Lua function.
     */
    LuaCallFuncRequest(lua_State *L_,const char *funcName_,
        const std::vector<ae::lua::LuaCallArg *> args_,bool failOnNoFunc_):
        LuaCallNoArgFuncRequest(L_,funcName_,failOnNoFunc_),args(args_) {
    }
    
    /** */
    virtual ~LuaCallFuncRequest() {
    }
    
    /** */
    virtual bool run();
    
    /**
     * \brief Utility method which adds the call request to the engine.
     * \param luaEngine The Lua engine.
     * \param funcName The (fully qualified) Lua function name.
     * \param args The function arguments.     
     * \param failOnNoFunc Indicates if to fail if there is no
     *   such Lua function.
     * \return `true` on success, `false` otherwise.     
     */
    static bool call(LuaEngine *luaEngine,const char *funcName,
        const std::vector<ae::lua::LuaCallArg *> args,bool failOnNoFunc);
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_CALL_FUNC_REQUEST_H