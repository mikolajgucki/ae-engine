#ifndef AE_IOS_LUA_LIB_FLURRY_H
#define AE_IOS_LUA_LIB_FLURRY_H

#import <Foundation/Foundation.h>
#include "LuaLibFlurry.h"

/**
 * \brief The iOS implementation of the Flurry Lua library.
 */
class AEiOSLuaLibFlurry:public ::ae::flurry::LuaLibFlurry {
    /// The API key.
    const std::string apiKey;
    
    /**
     * \brief Converts parameters to a dictionary.
     * \param parameters The parameters.
     * \return The dictionary.
     */
    NSMutableDictionary *parametersToDictionary(
        std::map<std::string,std::string> &parameters);
    
public:
    /** */
    AEiOSLuaLibFlurry(const std::string apiKey_):LuaLibFlurry(),
        apiKey(apiKey_) {
    }
    
    /** */
    virtual ~AEiOSLuaLibFlurry() {
    }
    
    /** */
    virtual void init();  
    
    /** */
    virtual void logEvent(const std::string &eventId,bool timed);
    
    /** */
    virtual void logEvent(const std::string &eventId,
        std::map<std::string,std::string> &parameters,bool timed);
    
    /** */
    virtual void endTimedEvent(const std::string &eventId);
    
    /** */
    virtual void endTimedEvent(const std::string &eventId,
        std::map<std::string,std::string> &parameters);     
};

#endif // AE_IOS_LUA_LIB_FLURRY_H