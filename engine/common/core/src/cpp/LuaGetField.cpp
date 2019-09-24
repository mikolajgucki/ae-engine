#include <sstream>

#include "string_util.h"
#include "LuaGetField.h"

using namespace std;
using namespace ae::util;

namespace ae {
    
namespace lua {
    
/** */
void LuaGetField::buildPath() {
    ostringstream stream;
    
// global
    stream << global;
    
// fields
    vector<string>::iterator itr;
    for (itr = fields.begin(); itr != fields.end(); ++itr) {
        stream << '.' << (*itr);
    }    
    
    path = stream.str();
}
 
/** */
void LuaGetField::fromPath(const std::string& path) {
    vector<string> values = split(path,'.');
    global = values[0];
    
    for (unsigned int index = 1; index < values.size(); index++) {
        fields.push_back(values[index]);
    }
}

/** */
bool LuaGetField::isNil() {
// push the global onto the stack
    lua_getglobal(L,global.c_str());
    if (lua_isnil(L,-1)) {
        lua_remove(L,1); // remove the global
        return true;
    }
    
// push onto the stack field by field
    vector<string>::iterator itr;
    for (itr = fields.begin(); itr != fields.end(); ++itr) {
        lua_getfield(L,-1,(*itr).c_str()); // push the field on the stack
        if (lua_isnil(L,-1))
        {
            lua_remove(L,-1); // remove the object with the field
            lua_remove(L,-1); // remove the field
            return true;
        }
        
        lua_remove(L,-2); // remove the object with the field
    }
    
    lua_remove(L,-1); // remove the field
    return false;
}

/** */
bool LuaGetField::run() {
// push the global onto the stack
    lua_getglobal(L,global.c_str());
    if (lua_isnil(L,-1))
    {
        lua_remove(L,-1); // remove the global
        stringstream err;
        err << "Lua global " << global << " is nil" <<
            " (while getting field " << path << ")";
        setError(err.str());
        return false;
    }
    
// push onto the stack field by field
    vector<string>::iterator itr;
    for (itr = fields.begin(); itr != fields.end(); ++itr) {
        lua_getfield(L,-1,(*itr).c_str()); // push the field on the stack
        if (lua_isnil(L,-1))
        {
            lua_remove(L,-1); // remove the object with the field
            lua_remove(L,-1); // remove the field
            
            stringstream err;
            err << "Lua field " << (*itr) << " is nil" <<
                " (while getting field " << path << ")";
            setError(err.str());
            return false;
        }
        
        lua_remove(L,-2); // remove the object with the field
    }
    
    return true;
}
    
} // namespace

} // namespace