#include <sstream>
#include "LuaGetGlobal.h"

using namespace std;

namespace ae {
    
namespace lua {
    
/** */
bool LuaGetGlobal::run() {
// push the global onto the stack
    lua_getglobal(L,globalName.c_str());
    if (lua_isnil(L,-1))
    {
        stringstream err;
        err << "Lua global " << globalName << " is nil";
        setError(err.str());
        return false;
    }    
    
    return true;
}
    
} // namespace
    
} // namespace