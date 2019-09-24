#include <sstream>
#include "DesktopLuaLibFlurry.h"

using namespace std;

namespace ae {

namespace flurry {

/// The log tag.
static const char *logTag = "Desktop/Flurry";

/** */
static const char *boolToStr(bool value) {
    return value ? "true" : "false";
}

/** */
void DesktopLuaLibFlurry::init() {
    log->trace(logTag,"DesktopLuaLibFlurry::init()");
}

/** */
void DesktopLuaLibFlurry::logEvent(const string &eventId,bool timed) {
    ostringstream msg;
    msg << "DesktopLuaLibFlurry::logEvent(" << eventId <<
        "," << boolToStr(timed) << ")";
    log->trace(logTag,msg.str());
}

/** */
void DesktopLuaLibFlurry::logEvent(const string &eventId,
    map<string,string> &parameters,bool timed) {
//
    ostringstream msg;
    msg << "DesktopLuaLibFlurry::logEvent(" << eventId <<
        "," << boolToStr(timed) << ") with parameters";

    map<string,string>::iterator itr;
    for (itr = parameters.begin(); itr != parameters.end(); ++itr) {
        msg << "\n  " << itr->first << " = " << itr->second;
    }
    
    log->trace(logTag,msg.str());
}

/** */
void DesktopLuaLibFlurry::endTimedEvent(const string &eventId) {
    ostringstream msg;
    msg << "DesktopLuaLibFlurry::endTimedEvent(" << eventId << ")";
    log->trace(logTag,msg.str());    
}

/** */
void DesktopLuaLibFlurry::endTimedEvent(const string &eventId,
    map<string,string> &parameters) {
//
    ostringstream msg;
    msg << "DesktopLuaLibFlurry::endTimedEvent(" << eventId <<
        ") with parameters";

    map<string,string>::iterator itr;
    for (itr = parameters.begin(); itr != parameters.end(); ++itr) {
        msg << "\n  " << itr->first << " = " << itr->second;
    }
    
    log->trace(logTag,msg.str());
}
    
} // namespace

} // namespace