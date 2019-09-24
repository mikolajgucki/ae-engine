#include <sstream>

#include "lua_common.h"
#include "Log.h"
#include "LuaModel.h"

using namespace std;
using namespace ae::lua;

namespace ae {
    
namespace engine {
    
/** */
void LuaModel::modelDraw() {
    if (getLuaDrawFunc == (Runnable *)0) {
        return;
    }
    
// push error handler to get traceback on error
    luaPCall.pushErrorHandler(L);    
    
    getLuaDrawFunc->run();
    if (getLuaDrawFunc->chkError()) {
        setError(getLuaDrawFunc->getError());
        return;
    }
    
    int callResult;
    if (luaPCall.tryCall(L,0,0,callResult) == false) {
        setError(luaPCall.getError());
        return;
    }
}

/** */
bool LuaModel::modelUpdate(long time) {
    if (getLuaUpdateFunc == (Runnable *)0) {
        return false;
    }
        
// push error handler to get traceback on error
    luaPCall.pushErrorHandler(L);    

    getLuaUpdateFunc->run();
    if (getLuaUpdateFunc->chkError()) {
        setError(getLuaUpdateFunc->getError());
        return false;
    }
    
    lua_pushnumber(L,time);
    int callResult;
    if (luaPCall.tryCall(L,1,1,callResult) == false) {
        setError(luaPCall.getError());        
        return false;
    }
    
    int updateResult = lua_toboolean(L,-1);
    lua_pop(L,1);
    
    return updateResult == AE_LUA_TRUE;
}
    
} // namespace
    
} // namespace