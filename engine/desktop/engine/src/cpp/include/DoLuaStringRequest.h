#ifndef AE_DO_LUA_STRING_REQUEST_H
#define AE_DO_LUA_STRING_REQUEST_H

#include "lua.hpp"
#include "EngineRequest.h"
#include "CommDebugger.h"

namespace ae {
    
namespace engine {
  
/**
 * \brief Runs a Lua code given as string.
 */
class DoLuaStringRequest:public EngineRequest {
    /// The Lua state.
    lua_State *L;
    
    /// The debugger.
    ::ae::engine::desktop::CommDebugger *debugger;
    
    /// The code to run.
    const std::string str;
    
public:
    /** */
    DoLuaStringRequest(lua_State *L_,
        ::ae::engine::desktop::CommDebugger *debugger_,
        const std::string &str_):EngineRequest(),
        L(L_),debugger(debugger_),str(str_) {
    }
    
    /** */
    virtual ~DoLuaStringRequest() {
    }
    
    /** */
    virtual bool run();
};
    
} // namespace
    
} // namespace

#endif // AE_DO_LUA_STRING_REQUEST_H