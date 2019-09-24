#include <string>
#include "LuaEngineCfgOS.h"

using namespace std;

namespace ae {
    
namespace engine {
    
/** */
const LuaEngineCfgOS LuaEngineCfgOS::OS_ANDROID("android");

/** */
const LuaEngineCfgOS LuaEngineCfgOS::OS_IOS("ios");
    
/** */
const LuaEngineCfgOS *LuaEngineCfgOS::getOSByName(const char *name) {
    if (string(name) == OS_ANDROID.getName()) {
        return &OS_ANDROID;
    }
    if (string(name) == OS_IOS.getName()) {
        return &OS_IOS;
    }
    
    return (LuaEngineCfgOS *)0;
}

} // namespace
    
} // namespace