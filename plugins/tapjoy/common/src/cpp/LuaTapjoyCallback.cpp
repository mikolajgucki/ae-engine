#include "LuaCallNoArgFuncRequest.h"
#include "LuaCallStrFuncRequest.h"
#include "LuaCallArg.h"
#include "LuaCallStrArg.h"
#include "LuaCallFuncRequest.h"
#include "LuaTapjoyCallback.h"

using namespace std;
using namespace ae::lua;
using namespace ae::engine;

namespace ae {
    
namespace tapjoy {

/** */
void LuaTapjoyCallback::callNoArgFunc(const char *funcName) {
    LuaCallNoArgFuncRequest::call(luaEngine,funcName,false);
}  
    
/** */
void LuaTapjoyCallback::callStrFunc(const char *funcName,
    const string& arg) {
//
    LuaCallStrFuncRequest::call(luaEngine,funcName,arg,false);
}

/** */
void LuaTapjoyCallback::connectionSucceeded() {
    callNoArgFunc("tapjoy.connectionSucceeded");
}

/** */
void LuaTapjoyCallback::connectionFailed() {
    callNoArgFunc("tapjoy.connectionFailed");
}
    
/** */
void LuaTapjoyCallback::requestSucceeded(const string& placement) {
    callStrFunc("tapjoy.requestSucceeded",placement);    
}

/** */
void LuaTapjoyCallback::requestFailed(const string& placement,
    const std::string& error) {
// arguments
    vector<LuaCallArg *> args;
    args.push_back(new LuaCallStrArg(placement));
    args.push_back(new LuaCallStrArg(error));
    
// call
    LuaCallFuncRequest::call(luaEngine,"tapjoy.requestFailed",args,true);
}

/** */
void LuaTapjoyCallback::contentIsReady(const string& placement) {
    callStrFunc("tapjoy.contentIsReady",placement);    
}

/** */
void LuaTapjoyCallback::contentShown(const string& placement) {
    callStrFunc("tapjoy.contentShown",placement);    
}

/** */
void LuaTapjoyCallback::contentDismissed(const string& placement) {
    callStrFunc("tapjoy.contentDismissed",placement);    
}

} // namespace
    
} // namespace