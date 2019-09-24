#ifndef AE_LUA_DEBUG_BREAKPOINT_H
#define AE_LUA_DEBUG_BREAKPOINT_H

#include <string>

namespace ae {
    
namespace lua {
  
/**
 * \brief A breakpoint.
 */
class LuaDebugBreakpoint {
    /// The name of the source file.
    std::string source;
    
    /// The line at which the breakpoint is set.
    int line;
    
public:
    /**
     * \brief Constructs a breakpoint.
     * \param source_ The name of the source file.
     * \param line_ The line at which the breakpoint is set.
     */
    LuaDebugBreakpoint(const std::string &source_,int line_):
        source(source_),line(line_) {
    }
    
    /**
     * \brief Gets the name of the source file.
     * \return The name of the source file.
     */
    const std::string &getSource() const {
        return source;
    }
    
    /**
     * \brief Gets the line at which the breakpoint is set.
     * \return The line.
     */
    int getLine() const {
        return line;
    }
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_DEBUG_BREAKPOINT_H