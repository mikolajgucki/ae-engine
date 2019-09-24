#ifndef AE_LUA_CALL_UTIL_H
#define AE_LUA_CALL_UTIL_H

#include <vector>
#include "lua.hpp"
#include "Error.h"
#include "LuaPCall.h"
#include "LuaCallArg.h"

namespace ae {
    
namespace lua {
  
/**
 * \brief Provides utility Lua call methods.
 */
class LuaCallUtil:public Error {
    /// The Lua protected call.    
    LuaPCall luaPCall;
    
    /**
     * \brief Gets the function and pushes it onto the Lua stack. Does nothing
     *   if there is no such function.
     * \param L The Lua state.
     * \param funcName The function name.
     * \param hasFunc Indicates if there is such a function.    
     * \return <code>true</code> on success, <code>false</code> otherwise.     
     */
    bool getFunc(lua_State *L,const char *funcName,bool &hasFunc);
    
    /**
     * \brief Reports function not founc error.
     * \param funcName The function name.
     */
    void noFuncError(const char *funcName);
    
public:
    /** */
    LuaCallUtil():Error(),luaPCall() {
    }
    
    /**
     * \brief Calls a no-argument function.
     * \param L The Lua state.
     * \param funcName The function name (can be fully qualified).
     * \param failOnNoFunc Indicates if to fail the there is no such function.
     * \return <code>true</code> on succes, <code>false</code> otherwise.
     */
    bool callNoArgFunc(lua_State *L,const char *funcName,
        bool failOnNoFunc);
    
    /**
     * \brief Calls a function with a boolean argument.
     * \param L The Lua state.
     * \param funcName The function name (can be fully qualified).
     * \param arg The function argument.
     * \param failOnNoFunc Indicates if to fail the there is no such function.
     * \return <code>true</code> on succes, <code>false</code> otherwise.
     */    
    bool callBooleanFunc(lua_State *L,const char *funcName,
        bool arg,bool failOnNoFunc);
    
    /**
     * \brief Calls a function with a string argument.
     * \param L The Lua state.
     * \param funcName The function name (can be fully qualified).
     * \param arg The function argument.
     * \param failOnNoFunc Indicates if to fail the there is no such function.
     * \return <code>true</code> on succes, <code>false</code> otherwise.
     */
    bool callStrFunc(lua_State *L,const char *funcName,
        const char *arg,bool failOnNoFunc);
    
    /**
     * \brief Calls a function with a number of arguments.
     * \param L The Lua state.
     * \param funcName The function name (can be fully qualified).
     * \param args The function arguments.
     * \param failOnNoFunc Indicates if to fail the there is no such function.
     * \param nresults The number of results.
     * \return <code>true</code> on succes, <code>false</code> otherwise.
     */
    bool callFunc(lua_State *L,const char *funcName,
        std::vector<LuaCallArg *> args,bool failOnNoFunc,int nresults = 0);
    
    /**
     * \brief Deletes arguments.
     * \param args The arguments.
     */
    void deleteArgs(std::vector<LuaCallArg *> args);
};
    
} // namespace

} // namespace

#endif // AE_LUA_CALL_UTIL_H