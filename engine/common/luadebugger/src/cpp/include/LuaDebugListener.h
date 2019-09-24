#ifndef AE_LUA_DEBUG_LISTENER_H
#define AE_LUA_DEBUG_LISTENER_H

#include "LuaDebugBreakpoint.h"

namespace ae {
    
namespace lua {
  
/**
 * \brief The abstract super-class for Lua debug listeners.
 */
class LuaDebugListener {
public:
    /**
     * \brief Called when the execution hooked to a line in source code.
     * \param source The name of the file in which the execution has hooked.
     * \param line The line in the source file in which the execution has
     *   hooked.
     */
    virtual void hooked(const std::string &source,int line) = 0;
    
    /**
     * \brief Called when the execution has suspended.
     * \param source The name of the file in which the execution has suspended.
     * \param line The line in the source file in which the execution has
     *   suspended.
     */
    virtual void suspended(const std::string &source,int line) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_DEBUG_LISTENER_H