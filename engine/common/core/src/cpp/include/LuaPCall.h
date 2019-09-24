#ifndef AE_LUA_PCALL_H
#define AE_LUA_PCALL_H

#include <string>

#include "lua.hpp"
#include "Error.h"

namespace ae {
    
namespace lua {
    
/**
 * \brief Makes a protected Lua call.
 */
class LuaPCall:public Error {
    /// Indicates if the error handler function has been pushed.
    bool errorHandling;
    
    /**
     * \brief Makes a protected call.
     * \param L The Lua state.
     * \param nargs The number of arguments.
     * \param nresults The number of result values.
     * \param result The lua_pcall result.
     * \param errorFunc The stack index of the error handler (function).     
     */
    bool call(lua_State *L,int nargs,int nresults,int &result,
        int errorFunc);
    
    /**
     * \brief Removes the error handler from the stack.
     * \param L The Lua state.
     * \param nresults The number of result values.     
     */
    void removeErrorHandler(lua_State *L,int nresults);
    
public:
    /** */
    LuaPCall():Error(),errorHandling(false) {
    }  
    
    /**
     * \brief Pushes the error handler.
     * \param L The Lua state.
     */
    void pushErrorHandler(lua_State *L);
    
    /**
     * \brief Calls a function in protected mode using the function lua_pcall.
     * Sets the error on Lua error or exception thrown using the throw
     * function.
     *
     * \param L The Lua state.
     * \param nargs The number of arguments.
     * \param nresults The number of results.
     * \param result The placeholder for the Lua call result.
     * \return <code>true</code> if the call is successful, <code>false</code>
     *     otherwise.
     */
    bool tryCall(lua_State *L,int nargs,int nresults,int &result);
        
    /**
     * \brief Loads and runs a string.
     * \param L The Lua state.
     * \param str The string to run.
     * \return <code>true</code> on success, <code>false</code> otherwise.
     */
    bool doString(lua_State *L,const std::string &str);
    
    /**
     * \brief Loads and runs a file.
     * \param L The Lua state.
     * \param path The path to the file as in the Lua require function.
     * \return <code>true</code> on success, <code>false</code> otherwise.
     */
    bool require(lua_State *L,const std::string &path);
};
    
} // namespace

} // namespace

#endif // AE_LUA_PCALL_H
