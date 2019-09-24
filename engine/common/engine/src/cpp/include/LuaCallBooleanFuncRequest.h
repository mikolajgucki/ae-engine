#ifndef AE_LUA_CALL_BOOLEAN_FUNC_REQUEST_H
#define AE_LUA_CALL_BOOLEAN_FUNC_REQUEST_H

#include <string>
#include "lua.hpp"
#include "LuaEngine.h"
#include "LuaCallNoArgFuncRequest.h"

namespace ae {
    
namespace engine {
    
/**
 * \brief Engine request which calls a boolean-argument Lua function.
 */
class LuaCallBooleanFuncRequest:public LuaCallNoArgFuncRequest {
    /// The function argument.
    bool arg;
    
public:
    /**
     * \brief Constructs a LuaCallBooleanFuncRequest.
     * \param L_ The Lua state.
     * \param funcName_ The (fully qualified) Lua function name.
     * \param arg_ The function argument.
     * \param failOnNoFunc_ Indicates if to fail if there is no
     *   such Lua function.
     */
    LuaCallBooleanFuncRequest(lua_State *L_,const char *funcName_,
        bool arg_,bool failOnNoFunc_):
        LuaCallNoArgFuncRequest(L_,funcName_,failOnNoFunc_),arg(arg_) {
    }
    
    /** */
    virtual ~LuaCallBooleanFuncRequest() {
    }
    
    /** */
    virtual bool run();
    
    /**
     * \brief Utility method which adds the call request to the engine.
     * \param luaEngine The Lua engine.
     * \param funcName The (fully qualified) Lua function name.
     * \param arg The function argument.     
     * \param failOnNoFunc Indicates if to fail if there is no
     *   such Lua function.
     * \return `true` on success, `false` otherwise.     
     */
    static bool call(LuaEngine *luaEngine,const char *funcName,
        bool arg,bool failOnNoFunc);
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_CALL_BOOLEAN_FUNC_REQUEST_H