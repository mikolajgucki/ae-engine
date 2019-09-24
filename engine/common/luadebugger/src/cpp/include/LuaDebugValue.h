#ifndef AE_LUA_DEBUG_VALUE_H
#define AE_LUA_DEBUG_VALUE_H

#include <string>
#include <vector>

#include "lua.hpp"

namespace ae {
    
namespace lua {
  
/**
 * \brief Represents a Lua value.
 */
class LuaDebugValue {
public:
    /** */
    enum Scope {
        /** */
        LOCAL,
        
        /** */
        UPVALUE
    };
    
private:
    /// The value name.
    const std::string name;
    
    /// The scope of the value.
    Scope scope;
    
    /// The value type (as defined by Lua C API).
    int type;
    
    /// The value as string.
    std::string value;
    
    /// The pointer to the Lua value.
    const void *pointer;
    
    /// The parent table.
    LuaDebugValue *parent;
    
    /// The child values (of a table).
    std::vector<LuaDebugValue *> childValues;
    
    /// Indicates if this value is just a reference to another table without
    /// child values as it would create an infinite loop.
    bool loopRef;    
    
public:
    /** */
    LuaDebugValue(const std::string name_,Scope scope_,int type_,
        const std::string value_,LuaDebugValue *parent_):name(name_),
        scope(scope_),type(type_),value(value_),pointer((const char *)0),
        parent(parent_),childValues(),loopRef(false) {
    }
    
    /** */
    LuaDebugValue(const std::string name_,Scope scope_,
        const void *pointer_,LuaDebugValue *parent_,bool loopRef_ = false):
        name(name_),scope(scope_),type(LUA_TTABLE),value(),pointer(pointer_),
        parent(parent_),childValues(),loopRef(loopRef_) {
    }
    
    /** */
    const std::string getName() const {
        return name;
    }
    
    /** */
    Scope getScope() const {
        return scope;
    }
    
    /** */
    int getType() const {
        return type;
    }
    
    /** */
    const std::string getValue() const {
        return value;
    }
    
    /** */
    const void *getPointer() const {
        return pointer;
    }
    
    /** */
    bool isLoopRef() const {
        return loopRef;
    }
    
    /** */
    void addChildValue(LuaDebugValue *value) {
        childValues.push_back(value);
    }
    
    /** */
    int getChildValueCount() const {
        return childValues.size();
    }
    
    /** */
    LuaDebugValue *getChildValue(int index) const {
        return childValues[index];
    }
    
    /** */
    bool checkLoop(const void *pointer) {
        LuaDebugValue *value = this;
        
        while (value != (LuaDebugValue *)0) {
            if (value->pointer == pointer) {
                return true;
            }
            value = value->parent;
        }
        
        return false;
    }
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_DEBUG_VALUE_H