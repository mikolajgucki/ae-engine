#ifndef AE_LUA_GET_FIELD_H
#define AE_LUA_GET_FIELD_H

#include <string>
#include <vector>
#include <iostream>
#include "lua.hpp"

#include "Runnable.h"

namespace ae {
    
namespace lua {

/**
 * \brief Gets the field of a Lua global and pushes it onto the stack.
 */
class LuaGetField:public Runnable {
    /// The Lua state.
    lua_State *L;
    
    /// The name of the global object.
    std::string global;
    
    /// The names of the fields.
    std::vector<std::string> fields;
    
    /// The dot-separated path.
    std::string path;

    /**
     * \brief Builds the path from the global name and field names. 
     */
    void buildPath();
    
    /**
     * \brief Gets the global and fields from a path.
     * \param path The dot-separated path.
     */
    void fromPath(const std::string& path);
    
public:
    /**
     * \brief Constructs a LuaGetField.
     * \param L_ The Lua state.
     * \param global_ The name of the global object.
     * \param field_ The name of the field of the global object.
     */
    LuaGetField(lua_State *L_,const std::string &global_,
        const std::string &field_):L(L_),global(global_),
        fields(),path() {
    //
        fields.push_back(field_);
        buildPath();
    }
    
    /**
     * \brief Constructs a LuaGetField.
     * \param L_ The Lua state.
     * \param global_ The name of the global object.
     * \param field0_ The name of the field of the global object.
     * \param field1_ The name of the field of the object under the first
     *     field.
     */
    LuaGetField(lua_State *L_,const std::string &global_,
        const std::string &field0_,const std::string &field1_):L(L_),
        global(global_),fields(),path() {
    //
        fields.push_back(field0_);
        fields.push_back(field1_);
        buildPath();
    }
    
    /**
     * \brief Constructs a LuaGetField.
     * \param L_ The Lua state.
     * \param path_ The dot-separeted path to the field of form:
     *     <code>global.field.field...</code>.
     */    
    LuaGetField(lua_State *L_,const std::string &path_):L(L_),
        global(),fields(),path(path_) {
    //
        fromPath(path);
    }
    
    /** */
    virtual ~LuaGetField() {
    }    
    
    /**
     * \brief Sets the Lua state.
     * \param L_ The Lua state.
     */
    void setLuaState(lua_State *L_) {
        L = L_;
    }
    
    /**
     * \brief Checks whether the field is nil.
     * \return <code>true</code> if nil, <code>false</code> otherwise.
     */
    bool isNil();
    
    /**
     * \brief Gets the field.
     * \return <code>false</code> if the field cannot be retrived (sets
     *   error), <code>true</code> otherwise.
     */
    virtual bool run();
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_GET_FIELD_H