#include "LuaPCall.h"
#include "lua_spine.h"

using namespace ae::lua;
using namespace ae::engine;

namespace ae {
    
namespace spine {
    
namespace lua {
    
/// The library name.
static const char *spineName = "spine";    
    
/** */
void loadSpineLib(LuaEngine *luaEngine,Error *error) {
// Lua state
    lua_State *L = luaEngine->getLuaState();
    
// set a empty global table under the library name 
    lua_newtable(L);
    lua_setglobal(L,spineName);
    
// load the Lua source
    LuaPCall luaPCall;
    if (luaPCall.require(L,"spine") == false) {
        error->setError(luaPCall.getError());
        return;
    }    
}

/** */
const char *getSpineLibName() {
    return spineName;
}

} // namespace

} // namespace
    
} // namespace