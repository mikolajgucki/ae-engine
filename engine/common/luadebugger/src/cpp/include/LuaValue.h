#ifndef AE_LUA_VALUE_H
#define AE_LUA_VALUE_H

#include <string>

namespace ae {

namespace lua {
    
/**
 * \brief Represents a Lua value.
 */
class LuaValue {
public:
    /** */
    enum Scope {
        /** */
        UNDEFINED,
        
        /** */
        LOCAL,
        
        /** */
        UPVALUE,
        
        /** */
        GLOBAL        
    };
    
private:
    /// The value name.    
    std::string name;
    
    /// The scope of the value.
    Scope scope;
    
    /// The value type (as defined by Lua C API).
    int type;
    
    /// The value as string.
    std::string value;
    
    /// The pointer as hex.
    std::string pointer;

public:
    /** */
    LuaValue(int type_,const std::string value_):name(),scope(UNDEFINED),
        type(type_),value(value_),pointer() {
    }
    
    /** */
    const std::string getName() const {
        return name;
    }
    
    /** */
    void setName(const std::string name_) {
        name = name_;
    }
    
    /** */
    Scope getScope() const {
        return scope;
    }
    
    /** */
    void setScope(Scope scope_) {
        scope = scope_;
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
    void setPointer(const std::string& pointer_) {
        pointer = pointer_;
    }
    
    /** */
    std::string getPointer() const {
        return pointer;
    }
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_VALUE_H