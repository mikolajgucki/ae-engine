#ifndef AE_LUA_TRACEBACK_ITEM_H
#define AE_LUA_TRACEBACK_ITEM_H

#include <string>
#include "LuaValueList.h"

namespace ae {
    
namespace lua {
  
/**
 * \brief Represents an item of a Lua traceback.
 */
class LuaTracebackItem {
public:
    /** */
    enum What {
        /** */
        LUA_FUNCTION,
        
        /** */
        C_FUNCTION,
        
        /** */
        MAIN_CHUNK,
        
        /** */
        TAIL_FUNCTION,
        
        /** */
        UNKNOWN
    };

private:    
    /// The index.
    int index;
    
    /// The name of the source file.
    const std::string source;
    
    /// The current line.
    int line;
    
    /// Lua function, C function or main chunk.
    const What what;
    
    /// The function name.
    const std::string name;
    
    /// The values.
    LuaValueList *values;
    
public:    
    /** */
    LuaTracebackItem(int index_,const std::string source_,int line_,What what_,
        const std::string name_,LuaValueList *values_):index(index_),
        source(source_),line(line_),what(what_),name(name_),values(values_) {
    }
    
    /** */
    ~LuaTracebackItem() {
        if (values != (LuaValueList *)0) {
            delete values;
        }
    }
    
    /**
     * \brief Gets the index of the item in the call stack.
     * \return The index.
     */
    int getIndex() {
        return index;
    }
    
    /**
     * \brief Gets the name of the source file.
     * \return The name of the source file.
     */
    const std::string getSource() const {
        return source;
    }
    
    /**
     * \brief Gets the current line.
     * \return The current line.
     */
    int getLine() const {
        return line;
    }
    
    /**
     * \brief Gets the what.
     * \return The what.
     */
    What getWhat() const {
        return what;
    }
    
    /**
     * \brief Gets the function name.
     * \return The function name.
     */
    const std::string getName() const {
        return name;
    }
    
    /**
     * \brief Gets the values.
     * \return The values.
     */
    LuaValueList *getValues() const {
        return values;
    }
};
    
} // namespace

} // namespace

#endif // AE_LUA_TRACEBACK_ITEM_H
