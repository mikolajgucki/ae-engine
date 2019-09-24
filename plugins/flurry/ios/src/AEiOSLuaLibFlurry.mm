#import "Flurry.h"
#include "Log.h"
#include "AEiOSLuaLibFlurry.h"

using namespace std;
using namespace ae;

/// The log tag.
static const char *logTag = "AEFlurry";

/** */
NSMutableDictionary *AEiOSLuaLibFlurry::parametersToDictionary(
    map<string,string> &parameters) {
//
    int capacity = (int)parameters.size();
// parameters
    NSMutableDictionary *nsParameters =
        [NSMutableDictionary dictionaryWithCapacity:capacity];
    map<string,string>::iterator itr;
    for (itr = parameters.begin(); itr != parameters.end(); ++itr) {
    // key
        const char *keyCStr = itr->first.c_str();
        NSString *nsKey = [NSString stringWithUTF8String:keyCStr];
        
    // value
        const char *valueCStr = itr->second.c_str();
        NSString *nsValue = [NSString stringWithUTF8String:valueCStr];
        
    // set
        [nsParameters setValue:nsValue forKey:nsKey];
    }
    
    return nsParameters;
}

/** */
void AEiOSLuaLibFlurry::init() {
    Log::trace(logTag,"AEiOSLuaLibFlurry::init()");

// API key
    const char *apiKeyCStr = apiKey.c_str();
    NSString *nsApiKey = [NSString stringWithUTF8String:apiKeyCStr];
    
// start session
    [Flurry startSession:nsApiKey];
}

/** */
void AEiOSLuaLibFlurry::logEvent(const string &eventId,bool timed) {
// log
    Log::trace(logTag,"AEiOSLuaLibFlurry::logEvent()");

// identifier
    const char *eventIdCStr = eventId.c_str();
    NSString *nsEventId = [NSString stringWithUTF8String:eventIdCStr];
    
// timed
    BOOL nsTimed = timed ? YES : NO;
    
// log
    [Flurry logEvent:nsEventId timed:nsTimed];
}

/** */
void AEiOSLuaLibFlurry::logEvent(const string &eventId,
    map<string,string> &parameters,bool timed) {
// log
    Log::trace(logTag,"AEiOSLuaLibFlurry::logEvent()");
    
// identifier
    const char *eventIdCStr = eventId.c_str();
    NSString *nsEventId = [NSString stringWithUTF8String:eventIdCStr];
    
// parameters
    NSMutableDictionary *nsParameters = parametersToDictionary(parameters);

// timed
    BOOL nsTimed = timed ? YES : NO;
    
// log
    [Flurry logEvent:nsEventId withParameters:nsParameters timed:nsTimed];
}

/** */  
void AEiOSLuaLibFlurry::endTimedEvent(const string &eventId) {
// identifier
    const char *eventIdCStr = eventId.c_str();
    NSString *nsEventId = [NSString stringWithUTF8String:eventIdCStr];

// end
    [Flurry endTimedEvent:nsEventId withParameters:nil];
}
    
/** */
void AEiOSLuaLibFlurry::endTimedEvent(const string &eventId,
    map<string,string> &parameters) {
// log
    Log::trace(logTag,"AEiOSLuaLibFlurry::endTimedEvent()");
    
// identifier
    const char *eventIdCStr = eventId.c_str();
    NSString *nsEventId = [NSString stringWithUTF8String:eventIdCStr];
    
// parameters
    NSMutableDictionary *nsParameters = parametersToDictionary(parameters);

// end
    [Flurry endTimedEvent:nsEventId withParameters:nsParameters];
}