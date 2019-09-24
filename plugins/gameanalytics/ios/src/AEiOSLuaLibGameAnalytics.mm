#import "GameAnalytics.h"
#include "Log.h"
#include "AEiOSLuaLibGameAnalytics.h"

using namespace std;
using namespace ae;

/// The log tag.
static const char *logTag = "AEGameAnalytics";

/** */
GAProgressionStatus AEiOSLuaLibGameAnalytics::progressionStatusToGAStatus(
    ProgressionStatus status) {
//
    if (status == PROGRESSION_STATUS_START) {
        return GAProgressionStatusStart;
    }
    else if (status == PROGRESSION_STATUS_COMPLETE) {
        return GAProgressionStatusComplete;
    }
    else if (status == PROGRESSION_STATUS_FAIL) {
        return GAProgressionStatusFail;
    }
    
// default
    Log::trace(logTag,"Unknown Game Analytics progression status");
    return GAProgressionStatusFail;
}

/** */
GAResourceFlowType AEiOSLuaLibGameAnalytics::flowTypeToGAFlowType(
    FlowType flowType) {
//
    if (flowType == FLOW_TYPE_SINK) {
        return GAResourceFlowTypeSink;
    }
    else if (flowType == FLOW_TYPE_SOURCE) {
        return GAResourceFlowTypeSource;
    }
    
// default
    Log::trace(logTag,"Unknown Game Analytics flow type");
    return GAResourceFlowTypeSink;
}

/** */
void AEiOSLuaLibGameAnalytics::enableLogging() {
    [GameAnalytics setEnabledInfoLog:YES];
    [GameAnalytics setEnabledVerboseLog:YES];
}

/** */
void AEiOSLuaLibGameAnalytics::configureAvailableResourceCurrencies(
    const std::vector<const std::string> currencies) {
//
    NSMutableArray *nsCurrencies = [[NSMutableArray alloc] init];
    for (unsigned int index = 0; index < currencies.size(); index++) {
        NSString *nsCurrency = [NSString stringWithUTF8String:currencies[index].c_str()];
        [nsCurrencies addObject:nsCurrency];
    }
    [GameAnalytics configureAvailableResourceCurrencies:nsCurrencies];
}

/** */
void AEiOSLuaLibGameAnalytics::configureAvailableResourceItemTypes(
    const std::vector<const std::string> itemTypes) {
//
    NSMutableArray *nsItemTypes = [[NSMutableArray alloc] init];
    for (unsigned int index = 0; index < itemTypes.size(); index++) {
        NSString *nsItemType = [NSString stringWithUTF8String:itemTypes[index].c_str()];
        [nsItemTypes addObject:nsItemType];
    }
    [GameAnalytics configureAvailableResourceItemTypes:nsItemTypes];
}

/** */
void AEiOSLuaLibGameAnalytics::initGameAnalytics() {
    Log::trace(logTag,"AEiOSLuaLibGameAnalytics::initGameAnalytics()");
    
// game key
    const char *gameKeyCStr = gameKey.c_str();
    NSString *nsGameKey = [NSString stringWithUTF8String:gameKeyCStr];
    
// secret key
    const char *secretKeyCStr = secretKey.c_str();
    NSString *nsSecretKey = [NSString stringWithUTF8String:secretKeyCStr];
    
// init
    [GameAnalytics configureBuild:@"alpha 0.1.0"];
    [GameAnalytics initializeWithGameKey:nsGameKey gameSecret:nsSecretKey];
}

/** */
void AEiOSLuaLibGameAnalytics::init() {
    Log::trace(logTag,"AEiOSLuaLibGameAnalytics::init()");
}

/** */
void AEiOSLuaLibGameAnalytics::addProgressionEvent(ProgressionStatus status,
    const string& progression01) {
// log
    Log::trace(logTag,"AEiOSLuaLibGameAnalytics::addProgressionEvent()");
    
// status
    GAProgressionStatus gaStatus = progressionStatusToGAStatus(status);
    
// progression
    NSString *nsProgression01 =
        [NSString stringWithUTF8String:progression01.c_str()];
            
// add event
    [GameAnalytics addProgressionEventWithProgressionStatus:gaStatus
      progression01:nsProgression01 progression02:nil progression03:nil];
}

/** */
void AEiOSLuaLibGameAnalytics::addProgressionEvent(ProgressionStatus status,
    const string& progression01,const string& progression02) {
// log
    Log::trace(logTag,"AEiOSLuaLibGameAnalytics::addProgressionEvent()");
    
// status
    GAProgressionStatus gaStatus = progressionStatusToGAStatus(status);
    
// progression
    NSString *nsProgression01 =
        [NSString stringWithUTF8String:progression01.c_str()];
    NSString *nsProgression02 =
        [NSString stringWithUTF8String:progression02.c_str()];
            
// add event
    [GameAnalytics addProgressionEventWithProgressionStatus:gaStatus
          progression01:nsProgression01 progression02:nsProgression02 progression03:nil];
}

/** */
void AEiOSLuaLibGameAnalytics::addProgressionEvent(ProgressionStatus status,
    const string& progression01,const string& progression02,
    const string& progression03) {
// log
    Log::trace(logTag,"AEiOSLuaLibGameAnalytics::addProgressionEvent()");
    
// status
    GAProgressionStatus gaStatus = progressionStatusToGAStatus(status);
    
// progression
    NSString *nsProgression01 =
        [NSString stringWithUTF8String:progression01.c_str()];
    NSString *nsProgression02 =
        [NSString stringWithUTF8String:progression02.c_str()];
    NSString *nsProgression03 =
        [NSString stringWithUTF8String:progression03.c_str()];
            
// add event
    [GameAnalytics addProgressionEventWithProgressionStatus:gaStatus
        progression01:nsProgression01 progression02:nsProgression02
        progression03:nsProgression03];        
}

/** */
void AEiOSLuaLibGameAnalytics::addDesignEvent(const string& eventId) {
//
    Log::trace(logTag,"AEiOSLuaLibGameAnalytics::addDesignEvent()");
    
// identifier
    NSString *nsEventId = [NSString stringWithUTF8String:eventId.c_str()];
    
// add event
    [GameAnalytics addDesignEventWithEventId:nsEventId];
}

/** */
void AEiOSLuaLibGameAnalytics::addDesignEvent(const string& eventId,
    double value) {
//
    Log::trace(logTag,"AEiOSLuaLibGameAnalytics::addDesignEvent()");
    
// identifier
    NSString *nsEventId = [NSString stringWithUTF8String:eventId.c_str()];
    
// add event
    [GameAnalytics addDesignEventWithEventId:nsEventId
       value:[NSNumber numberWithDouble:value]];
}

/** */
void AEiOSLuaLibGameAnalytics::addErrorEvent(ErrorSeverity severity,
    const string& msg) {
//
    Log::trace(logTag,"AEiOSLuaLibGameAnalytics::addErrorEvent()");
    
// severity
    GAErrorSeverity gaSeverity;
    if (severity == ERROR_SEVERITY_DEBUG) {
        gaSeverity = GAErrorSeverityDebug;
    }
    else if (severity == ERROR_SEVERITY_INFO) {
        gaSeverity = GAErrorSeverityInfo;
    }
    else if (severity == ERROR_SEVERITY_WARNING) {
        gaSeverity = GAErrorSeverityWarning;
    }
    else if (severity == ERROR_SEVERITY_ERROR) {
        gaSeverity = GAErrorSeverityError;
    }
    else if (severity == ERROR_SEVERITY_CRITICAL) {
        gaSeverity = GAErrorSeverityCritical;
    }
    else {
        Log::trace(logTag,"Invalid Game Analytics error severity");
        return;
    }
    
// message
    const char *msgCStr = msg.c_str();
    NSString *nsMsg = [NSString stringWithUTF8String:msgCStr];
    
// add event
    [GameAnalytics addErrorEventWithSeverity:gaSeverity message:nsMsg];
}

/** */
void AEiOSLuaLibGameAnalytics::addBusinessEvent(const string& currency,
    int amount,const string& itemType,const string& itemId,
    const string& cartType,const string *receipt,
    const std::string* store,const std::string *signature) {
//
    Log::trace(logTag,"AEiOSLuaLibGameAnalytics::addBusinessEvent()");
    
// parameters
    NSString *nsCurrency = [NSString stringWithUTF8String:currency.c_str()];
    NSString *nsItemType = [NSString stringWithUTF8String:itemType.c_str()];
    NSString *nsItemId = [NSString stringWithUTF8String:itemId.c_str()];
    NSString *nsCartType = [NSString stringWithUTF8String:cartType.c_str()];
    
// add event
    [GameAnalytics addBusinessEventWithCurrency:nsCurrency amount:amount
        itemType:nsItemType itemId:nsItemId cartType:nsCartType receipt:nil];
}

/** */
void AEiOSLuaLibGameAnalytics::addResourceEvent(FlowType flowType,
    const std::string &currency,float amount,const std::string& itemType,
    const std::string &itemId) {
//
    Log::trace(logTag,"AEiOSLuaLibGameAnalytics::addResourceEvent()");
    
// parameters
    GAResourceFlowType gaFlowType = flowTypeToGAFlowType(flowType);    
    NSString *nsCurrency = [NSString stringWithUTF8String:currency.c_str()];
    NSNumber *nsAmount = [NSNumber numberWithFloat:amount];
    NSString *nsItemType = [NSString stringWithUTF8String:itemType.c_str()];
    NSString *nsItemId = [NSString stringWithUTF8String:itemId.c_str()];
    
// add event
    [GameAnalytics addResourceEventWithFlowType:gaFlowType currency:nsCurrency
        amount:nsAmount itemType:nsItemType itemId:nsItemId];
}