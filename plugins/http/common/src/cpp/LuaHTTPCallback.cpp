#include <vector>
#include "LuaCallArg.h"
#include "LuaCallNumberArg.h"
#include "LuaCallStrArg.h"
#include "LuaCallFuncRequest.h"
#include "LuaHTTPCallback.h"

using namespace std;
using namespace ae::lua;
using namespace ae::engine;

namespace ae {
    
namespace http {
    
/** */
void LuaHTTPCallback::receivedResponse(const string& id,int code,
    const string &body) {
//
    vector<LuaCallArg *> args;
    args.push_back(new LuaCallStrArg(id));
    args.push_back(new LuaCallNumberArg(code));
    args.push_back(new LuaCallStrArg(body));
    
    LuaCallFuncRequest::call(luaEngine,"http.receivedResponse",args,true);
}
    
/** */
void LuaHTTPCallback::failedToReceiveResponse(const string& id,
    const string& error) {
//
    vector<LuaCallArg *> args;
    args.push_back(new LuaCallStrArg(id));
    args.push_back(new LuaCallStrArg(error));
    
    LuaCallFuncRequest::call(luaEngine,
        "http.failedToReceiveResponse",args,true);
}

} // namespace
    
} // namespace