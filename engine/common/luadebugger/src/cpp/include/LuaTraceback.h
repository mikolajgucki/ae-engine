#ifndef AE_LUA_TRACEBACK_H
#define AE_LUA_TRACEBACK_H

#include <vector>
#include "LuaTracebackItem.h"

namespace ae {
    
namespace lua {
  
/**
 * \brief Represents a Lua traceback.
 */
class LuaTraceback {
    /// The items.
    std::vector<LuaTracebackItem *> items;
    
public:
    /** */
    LuaTraceback():items() {
    }
    
    /** */
    ~LuaTraceback();
    
    /**
     * \brief Adds an item.
     * \param item The item to add.
     */
    void addItem(LuaTracebackItem *item) {
        items.push_back(item);
    }
    
    /**
     * \brief Gets the number of the items in the traceback.
     * \return The item count.
     */
    int getItemCount() const {
        return items.size();
    }
    
    /**
     * \brief Gets a traceback item.
     * \param index The index of the traceback item.
     * \return The item at the index.
     */
    LuaTracebackItem *getItem(int index) const {
        return items[index];
    }
};
    
} // namespace

} // namespace

#endif // AE_LUA_TRACEBACK_H
