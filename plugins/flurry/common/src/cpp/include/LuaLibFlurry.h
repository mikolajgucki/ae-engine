#ifndef AE_LUA_LIB_FLURRY_H
#define AE_LUA_LIB_FLURRY_H

#include <string>
#include <map>
#include "Error.h"

namespace ae {
    
namespace flurry {
    
/**
 * \brief The superclass for platform-specific implementations of the Flurry
 *   Lua library.
 */
class LuaLibFlurry:public Error {
protected:
    /** */
    LuaLibFlurry():Error() {
    }
    
public:
    /** */
    virtual ~LuaLibFlurry() {
    }
    
    /**
     * \brief Initializes the library. Called right after having loaded the Lua
     *   library.
     */
    virtual void init() = 0;  
    
    /**
     * \brief Logs an event.
     * \param eventId The event identifier.
     * \param timed Indicates if the event is timed.
     */
    virtual void logEvent(const std::string &eventId,bool timed) = 0;
    
    /**
     * \brief Logs an event.
     * \param eventId The event identifier.
     * \param parameters The event parameters.
     * \param timed Indicates if the event is timed.
     */
    virtual void logEvent(const std::string &eventId,
        std::map<std::string,std::string> &parameters,bool timed) = 0; 
    
    /**
     * \brief Ends a timed event.
     * \param eventId The event identifier.     
     */
    virtual void endTimedEvent(const std::string &eventId) = 0;
    
    /**
     * \brief Ends a timed event.
     * \param eventId The event identifier.     
     * \param parameters The event parameters.
     */
    virtual void endTimedEvent(const std::string &eventId,
        std::map<std::string,std::string> &parameters) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_LIB_FLURRY_H