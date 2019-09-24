#ifndef AE_LUA_VALUE_LIST_H
#define AE_LUA_VALUE_LIST_H

#include <vector>
#include "LuaValue.h"

namespace ae {

namespace lua {
    
/**
 * \brief List of Lua values.
 */
class LuaValueList {
    /// The values.
    std::vector<LuaValue *> values;
        
public:
    /** */
    LuaValueList():values() {
    }
    
    /** */
    ~LuaValueList();
    
    /**
     * \brief Adds a value to the list.
     * \param value The value.     
     */
    void addValue(LuaValue *value) {
        values.push_back(value);
    }
    
    /**
     * \brief Gets the number of the values in the list.
     * \return The value count.
     */
    int getValueCount() {
        return values.size();
    }
    
    /**
     * \brief Gets a value.
     * \param index The value index.
     * \return The value at the index.
     */
    LuaValue *getValue(int index) {
        return values[index];
    }
};
    
} // namespace

} // namespace

#endif // AE_LUA_VALUE_LIST_H