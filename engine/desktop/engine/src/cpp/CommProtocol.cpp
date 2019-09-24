#include "lua.hpp"
#include "CommProtocol.h"

using namespace std;
using namespace ae::util;
using namespace ae::lua;

namespace ae {
    
namespace engine {
    
namespace desktop {
    
/** */
const char* const CommProtocol::MSG_ID_KEY = "msg.id";
    
/** */
const char* const CommProtocol::REQUEST_ID_KEY = "request.id";

/** */
const char* const CommProtocol::VOLUME_KEY = "volume";

/** */
const char* const CommProtocol::NOTIFY_KEY = "notify";

/** */
const char* const CommProtocol::SOURCE_KEY = "source";

/** */
const char* const CommProtocol::LINE_KEY = "line";

/** */
const char* const CommProtocol::INDEX_KEY = "index";

/** */
const char* const CommProtocol::COUNT_KEY = "count";

/** */
const char* const CommProtocol::NAME_KEY = "name";

/** */
const char* const CommProtocol::VALUE_KEY = "value";

/** */
const char* const CommProtocol::TYPE_KEY = "type";

/** */
const char* const CommProtocol::WHAT_KEY = "what";
 
/** */
const char* const CommProtocol::SCOPE_KEY = "scope";
 
/** */
const char* const CommProtocol::POINTER_KEY = "pointer";

/** */
const char* const CommProtocol::ERROR_MSG_KEY = "error.msg";

/** */
void CommProtocol::addMsgId(MessagePackMap &map,int id) {
    map.putInt(MSG_ID_KEY,id);
}

/** */
bool CommProtocol::strToBool(MessagePackMap &map,const string &key) {
    return map.getInt(key,(int)CommProtocol::FALSE) == CommProtocol::TRUE;
}
    
/** */
const char* CommProtocol::whatToStr(LuaTracebackItem::What what) {
    if (what == LuaTracebackItem::LUA_FUNCTION) {
        return "lua_func";
    }
    if (what == LuaTracebackItem::C_FUNCTION) {
        return "c_func";
    }
    if (what == LuaTracebackItem::MAIN_CHUNK) {
        return "main_chunk";
    }
    if (what == LuaTracebackItem::TAIL_FUNCTION) {
        return "tail_func";
    }
    if (what == LuaTracebackItem::UNKNOWN) {
        return "unknown";
    }
    
    return (const char *)0;
}

/** */
const char* CommProtocol::valueScopeToStr(LuaValue::Scope scope) {
    if (scope == LuaValue::UNDEFINED) {
        return "undefined";
    }
    if (scope == LuaValue::LOCAL) {
        return "local";
    }
    if (scope == LuaValue::UPVALUE) {
        return "upvalue";
    }
    if (scope == LuaValue::GLOBAL) {
        return "global";
    }
    
    return (const char *)0;
}

/** */
const char *CommProtocol::valueTypeToStr(int type) {
    if (type == LUA_TNIL) {
        return "nil";
    }
    if (type == LUA_TNUMBER) {
        return "number";
    }
    if (type == LUA_TBOOLEAN) {
        return "boolean";
    }
    if (type == LUA_TSTRING) {
        return "string";
    }
    if (type == LUA_TTABLE) {
        return "table";
    }
    if (type == LUA_TFUNCTION) {
        return "func";
    }
    if (type == LUA_TUSERDATA) {
        return "userdata";
    }
    if (type == LUA_TTHREAD) {
        return "thread";
    }
    if (type == LUA_TLIGHTUSERDATA) {
        return "lightuserdata";
    }
    
    return (const char *)0;
}


} // namespace
    
} // namespace
    
} // namespace