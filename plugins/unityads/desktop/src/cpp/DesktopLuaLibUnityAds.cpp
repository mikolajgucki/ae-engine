#include <sstream>
#include "DesktopLuaLibUnityAds.h"

using namespace std;

namespace ae {
    
namespace unityads {
    
/// The log tag.
static const char *logTag = "AE/UnityAds";
    
/** */
bool DesktopLuaLibUnityAds::isReady() {
    dlog->trace(logTag,"DesktopLuaLibUnityAds::isReady()");
    return false;
}
    
/** */
bool DesktopLuaLibUnityAds::isReady(const std::string &placementId) {
    ostringstream msg;
    msg << "DesktopLuaLibUnityAds::isReady(" << placementId << ")";
    dlog->trace(logTag,msg.str());
    
    return false;
}

/** */
void DesktopLuaLibUnityAds::show() {
    dlog->trace(logTag,"DesktopLuaLibUnityAds::show()");
}


/** */
void DesktopLuaLibUnityAds::show(const std::string &placementId) {
    ostringstream msg;
    msg << "DesktopLuaLibUnityAds::show(" << placementId << ")";
    dlog->trace(logTag,msg.str());
}
    
} // namespace
    
} // namespace