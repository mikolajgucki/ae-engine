#include <sstream>

#include "Log.h"
#include "DesktopLuaLibGameAnalytics.h"

using namespace std;

namespace ae {
    
namespace gameanalytics {
    
/// The log cat.
static const char *logTag = "Desktop/GameAnalytics";    
    
/** */
const char *DesktopLuaLibGameAnalytics::progressionStatusToStr(
    ProgressionStatus status) {
//
    if (status == PROGRESSION_STATUS_START) {
        return "start";
    }
    else if (status == PROGRESSION_STATUS_COMPLETE) {
        return "complete";
    }
    else if (status == PROGRESSION_STATUS_FAIL) {
        return "fail";
    }
    
    return "";
}

/** */
void DesktopLuaLibGameAnalytics::init() {
    log->trace(logTag,"DesktopLuaLibGameAnalytics::init()");
}

/** */
void DesktopLuaLibGameAnalytics::addProgressionEvent(ProgressionStatus status,
    const string& progression01) {
//
    ostringstream msg;
    msg << "DesktopLuaLibGameAnalytics::addProgressionEvent(" <<
        progressionStatusToStr(status) << "," << progression01 << ")";
    log->trace(logTag,msg.str());
}

/** */
void DesktopLuaLibGameAnalytics::addProgressionEvent(ProgressionStatus status,
    const string& progression01,const string& progression02) {
//
    ostringstream msg;
    msg << "DesktopLuaLibGameAnalytics::addProgressionEvent(" <<
        progressionStatusToStr(status) << "," <<  progression01 << "," <<
        progression02 << ")";
    log->trace(logTag,msg.str());
}

/** */
void DesktopLuaLibGameAnalytics::addProgressionEvent(ProgressionStatus status,
    const string& progression01,const string& progression02,
    const string& progression03) {
//
    ostringstream msg;
    msg << "DesktopLuaLibGameAnalytics::addProgressionEvent(" <<
        progressionStatusToStr(status) << "," << progression01 << "," <<
        progression02 << "," << progression03 << ")";
    log->trace(logTag,msg.str());
}

/** */
void DesktopLuaLibGameAnalytics::addDesignEvent(const string& eventId) {
    ostringstream msg;
    msg << "DesktopLuaLibGameAnalytics::addDesignEvent(" << eventId << ")";
    log->trace(logTag,msg.str());    
}

/** */
void DesktopLuaLibGameAnalytics::addDesignEvent(const string& eventId,
    double value) {
//
    ostringstream msg;
    msg << "DesktopLuaLibGameAnalytics::addDesignEvent(" << eventId <<
        "," << value << ")";
    log->trace(logTag,msg.str());    
}

/** */
void DesktopLuaLibGameAnalytics::addErrorEvent(ErrorSeverity severity,
    const string& errorMsg) {
//
    const char *severityStr = "";
    if (severity == ERROR_SEVERITY_DEBUG) {
        severityStr = "debug";
    }
    else if (severity == ERROR_SEVERITY_INFO) {
        severityStr = "info";
    }
    else if (severity == ERROR_SEVERITY_WARNING) {
        severityStr = "warning";
    }
    else if (severity == ERROR_SEVERITY_ERROR) {
        severityStr = "error";
    }
    else if (severity == ERROR_SEVERITY_CRITICAL) {
        severityStr = "critical";
    }
 
    ostringstream msg;
    msg << "DesktopLuaLibGameAnalytics::addErrorEvent(" << severityStr <<
        "," << errorMsg << ")";
    log->trace(logTag,msg.str());    
}

/** */
void DesktopLuaLibGameAnalytics::addBusinessEvent(const string& currency,
    int amount,const string& itemType,const string& itemId,
    const string& cartType,const string *receipt,
    const string *store,const string *signature) {
// log message
    ostringstream msg;
    msg << "DesktopLuaLibGameAnalytics::addBusinessEvent(" << currency <<
        "," << amount << "," << itemType << "," << itemId << "," << cartType;
        
    if (receipt != (const string *)0) {
        msg << "," << receipt;
    }
    else {
        msg << ",null";
    }
    if (store != (const string *)0) {
        msg << "," << store;
    }
    else {
        msg << ",null";
    }
    if (signature != (const string *)0) {
        msg << "," << signature;
    }
    else {
        msg << ",null";
    }
    msg << ")";
        
// log
    log->trace(logTag,msg.str());
}

/** */
void DesktopLuaLibGameAnalytics::addResourceEvent(FlowType flowType,
    const string &currency,float amount,const string& itemType,
    const string &itemId) {
// flow type
    const char *flowTypeStr;
    if (flowType == FLOW_TYPE_SINK) { 
        flowTypeStr = "sink";
    }
    else if (flowType == FLOW_TYPE_SOURCE) { 
        flowTypeStr = "source";
    }
    else {
        flowTypeStr = "unknown";
    }

// log
    ostringstream msg;
    msg << "DesktopLuaLibGameAnalytics::addResourceEvent(" << flowTypeStr <<
        "," << currency << "," << amount << "," << itemType << "," << itemId <<
        ")";
    log->trace(logTag,msg.str());
}

} // namespace

} // namespace