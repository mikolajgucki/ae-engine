#ifndef AE_DESKTOP_LUA_LIB_FLURRY_H
#define AE_DESKTOP_LUA_LIB_FLURRY_H

#include "DLog.h"
#include "LuaLibFlurry.h"

namespace ae {

namespace flurry {
  
/**
 * \brief The desktop implementation of the Flurry Lua library.
 */
class DesktopLuaLibFlurry:public LuaLibFlurry {
    /// The log.
    DLog *log;
    
public:
    /** */
    DesktopLuaLibFlurry(DLog *log_):LuaLibFlurry(),log(log_) {
    }
    
    /** */
    virtual ~DesktopLuaLibFlurry() {
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
    
} // namespace

} // namespace

#endif // AE_DESKTOP_LUA_LIB_FLURRY_H