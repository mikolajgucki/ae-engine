#ifndef AE_LUA_LIB_GAME_ANALYTICS_H
#define AE_LUA_LIB_GAME_ANALYTICS_H

#include <string>
#include <vector>
#include "Error.h"

namespace ae {
    
namespace gameanalytics {
    
/**
 * \brief The superclass for platform-specific implementations of the
 *   Game Analytics Lua library. 
 */
class LuaLibGameAnalytics:public Error {
public:
    enum ProgressionStatus {
        /** */
        PROGRESSION_STATUS_START,
        
        /** */
        PROGRESSION_STATUS_COMPLETE,
        
        /** */
        PROGRESSION_STATUS_FAIL
    };
    
    /** */
    enum ErrorSeverity {
        /** */
        ERROR_SEVERITY_DEBUG,
        
        /** */
        ERROR_SEVERITY_INFO,
        
        /** */
        ERROR_SEVERITY_WARNING,
        
        /** */
        ERROR_SEVERITY_ERROR,
        
        /** */
        ERROR_SEVERITY_CRITICAL
    };
    
    /** */
    enum FlowType {
        /** */
        FLOW_TYPE_SINK,
        
        /** */
        FLOW_TYPE_SOURCE
    };
protected:
    /** */
    LuaLibGameAnalytics():Error() {
    }
    
public:
    /** */
    virtual ~LuaLibGameAnalytics() {
    }
    
    /** 
     * \brief Initializes the library.
     */
    virtual void init() = 0;
    
    /**
     * \brief Adds a progression event.
     * \param status The status.
     * \param progression01 The progression location.
     */
    virtual void addProgressionEvent(ProgressionStatus status,
        const std::string& progression01) = 0;
    
    /**
     * \brief Adds a progression event.
     * \param status The status.
     * \param progression01 The progression location.
     * \param progression02 The progression location.
     */
    virtual void addProgressionEvent(ProgressionStatus status,
        const std::string& progression01,const std::string& progression02) = 0;
    
    /**
     * \brief Adds a progression event.
     * \param status The status.
     * \param progression01 The progression location.
     * \param progression02 The progression location.
     * \param progression03 The progression location.
     */
    virtual void addProgressionEvent(ProgressionStatus status,
        const std::string& progression01,const std::string& progression02,
        const std::string& progression03) = 0;
    
    /**
     * \brief Adds a design event.
     * \param eventId The event identifier.
     */
    virtual void addDesignEvent(const std::string& eventId) = 0;
    
    /**
     * \brief Adds a design event.
     * \param eventId The event identifier.
     * \param value The event value.
     */
    virtual void addDesignEvent(const std::string& eventId,double value) = 0;
    
    /**
     * \brief Adds an error event.
     * \param severity The severity.
     * \param msg The message.
     */
    virtual void addErrorEvent(ErrorSeverity severity,
        const std::string& msg) = 0;
    
    /**
     * \brief Adds a business event.
     * \param currency The currency in ISO 4217 format.
     * \param amount The amount in cents.
     * \param itemType The item type.
     * \param itemId The item identifier.
     * \param cartType The cart type.
     * \param receipt The receipt.
     * \param store The store.
     * \param signature The signature.
     */
    virtual void addBusinessEvent(const std::string& currency,int amount,
        const std::string& itemType,const std::string& itemId,
        const std::string& cartType,const std::string *receipt,
        const std::string* store,const std::string *signature) = 0;
    
    /**
     * \brief Adds a resource event.
     * \param flowType The flow type.
     * \param currency The currency.
     * \param amount The amount.
     * \param itemType The item type.
     * \param itemId The item identifier.
     */
    virtual void addResourceEvent(FlowType flowType,const std::string &currency,
        float amount,const std::string& itemType,const std::string &itemId) = 0;
};

    
} // namespace
    
} // namespace

#endif // AE_LUA_LIB_GAME_ANALYTICS_H