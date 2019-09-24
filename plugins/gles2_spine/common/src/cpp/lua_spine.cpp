#include "LuaPCall.h"
#include "lua_spine.h"

using namespace ae::lua;

namespace ae {
    
namespace spine {
    
namespace lua {
    
/// The library name.
static const char *spineName = "spine";    
    
/** */
void loadSpineLib(lua_State *L,Error *error) {
// set a empty global table under the library name 
    lua_newtable(L);
    lua_setglobal(L,spineName);
    
// load the Lua source
    LuaPCall luaPCall;
    if (luaPCall.require(L,"ae.spine") == false) {
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