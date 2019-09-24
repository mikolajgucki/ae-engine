#include <vector>
#include "Log.h"
#include "LuaCallArg.h"
#include "LuaCallStrArg.h"
#include "LuaCallStrFuncRequest.h"
#include "LuaCallFuncRequest.h"
#include "LuaUnityAdsCallback.h"

using namespace std;
using namespace ae::lua;
using namespace ae::engine;

namespace ae {

namespace unityads {
    
/** */
void LuaUnityAdsCallback::callStrFunc(const char *funcName,const string& arg) {
    LuaCallStrFuncRequest::call(luaEngine,funcName,arg,true);
}    

/** */
void LuaUnityAdsCallback::callStrFunc(const char *funcName,const string& arg0,
    const string &arg1) {
// arguments
    vector<LuaCallArg *> args;
    args.push_back(new LuaCallStrArg(arg0));
    args.push_back(new LuaCallStrArg(arg1));
    
// call
    LuaCallFuncRequest::call(luaEngine,funcName,args,true);
}

/** */
void LuaUnityAdsCallback::setReady(const string &placementId) {
    callStrFunc("unityads.ready",placementId);
}

/** */
void LuaUnityAdsCallback::started(const string &placementId) {
    callStrFunc("unityads.started",placementId);
}

/** */
void LuaUnityAdsCallback::failed(const string &error,const string &msg) {
    callStrFunc("unityads.failed",error,msg);
}

/** */
void LuaUnityAdsCallback::finished(const string &placementId,
    FinishState state) {
//
    const char *stateStr = finishStateToStr(state);
    callStrFunc("unityads.finished",placementId,string(stateStr));
}
    
} // namespace
    
} // namespace