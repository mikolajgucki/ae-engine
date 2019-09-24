/*
-- @module gameanalytics
-- @group Analytics
*/
#include <cstring>
#include <sstream>
#include <memory>

#include "LuaTableUtil.h"
#include "lua_gameanalytics.h"

using namespace std;
using namespace ae::lua;
using namespace ae::engine;

namespace ae {
    
namespace gameanalytics {
    
namespace lua {
    
/// The name of the Game Analytics library.
static const char *gameAnalyticsName = "gameanalytics";

/// The name of the Lua global holding the Game Analytics Lua library
/// implementation.
static const char *gameAnalyticsNameGlobalLuaLibGA = "ae_gameanalytics";       

/**
 * \brief Gets the Game Analytics implementation from the Lua state.
 * \param L The Lua state.
 * \return The Game Analytics implementation.
 */
static LuaLibGameAnalytics *getLuaLibGameAnalytics(lua_State *L) {
    lua_getglobal(L,gameAnalyticsNameGlobalLuaLibGA);
    LuaLibGameAnalytics *lib = (LuaLibGameAnalytics *)lua_touserdata(L,-1);    
    lua_pop(L,1);
    return lib;
}

/*
-- @name .addProgressionEvent
-- @func
-- @brief Logs a progression event.
-- @param status The progression status (must be `start`, `complete`, `fail`
--   or `undefined`).
-- @param progression01 The progression location.
-- @func
-- @brief Logs a progression event.
-- @param status The progression status (must be `start`, `complete`, `fail`
--   or `undefined`).
-- @param progression01 The progression location.
-- @param progression02 The progression location.
-- @func
-- @brief Logs a progression event.
-- @param status The progression status (must be `start`, `complete`, `fail`
--   or `undefined`).
-- @param progression01 The progression location.
-- @param progression02 The progression location.
-- @param progression03 The progression location.
*/
static int gameAnalyticsAddProgressionEvent(lua_State *L) {
    LuaLibGameAnalytics *luaLibGameAnalytics = getLuaLibGameAnalytics(L);

// status
    LuaLibGameAnalytics::ProgressionStatus status;
    const char *statusStr = luaL_checkstring(L,1);
    if (strcmp(statusStr,"start") == 0) {
        status = LuaLibGameAnalytics::PROGRESSION_STATUS_START;
    }
    else if (strcmp(statusStr,"complete") == 0) {
        status = LuaLibGameAnalytics::PROGRESSION_STATUS_COMPLETE;
    }
    else if (strcmp(statusStr,"fail") == 0) {
        status = LuaLibGameAnalytics::PROGRESSION_STATUS_FAIL;
    }
    else {
        ostringstream err;
        err << "Unknown progression status " << statusStr;
        luaPushError(L,err.str().c_str());
        return 0;
    }
    
// progression
    const char *progression01 = luaL_checkstring(L,2);
    if (lua_isnoneornil(L,3) == 0) {
        const char *progression02 = luaL_checkstring(L,3);
        if (lua_isnoneornil(L,4) == 0) {
            const char *progression03 = luaL_checkstring(L,4);
            luaLibGameAnalytics->addProgressionEvent(status,
                string(progression01),string(progression02),
                string(progression03));
        }
        else {
            luaLibGameAnalytics->addProgressionEvent(status,
                string(progression01),string(progression02));
        }
    }
    else {
        luaLibGameAnalytics->addProgressionEvent(status,string(progression01));
    }
    
    return 0;
}

/*
-- @name .addDesignEvent
-- @func
-- @brief Adds a design event.
-- @param eventId The event identifier.
-- @func
-- @brief Adds a design event.
-- @param eventId The event identifier.
-- @param value The event value (number).
*/
static int gameAnalyticsAddDesignEvent(lua_State *L) {
    LuaLibGameAnalytics *luaLibGameAnalytics = getLuaLibGameAnalytics(L);
    
// identifier
    const char *eventId = luaL_checkstring(L,1);
    
// value
    if (lua_isnoneornil(L,2) == 0) {
        double value = luaL_checknumber(L,2);
        luaLibGameAnalytics->addDesignEvent(string(eventId),value);
    }
    else {
        luaLibGameAnalytics->addDesignEvent(string(eventId));
    }
    
    return 0;
}

/*
-- @name .addErrorEvent
-- @func
-- @brief Adds an error event.
-- @param severity The severity (one of `debug`, `info`, `warning`,\
--   `error`, `critical`).
-- @param msg The error message.
*/
static int gameAnalyticsAddErrorEvent(lua_State *L) {
    LuaLibGameAnalytics *luaLibGameAnalytics = getLuaLibGameAnalytics(L);
    
// severity
    LuaLibGameAnalytics::ErrorSeverity severity;
    const char *severityStr = luaL_checkstring(L,1);
    if (strcmp(severityStr,"debug") == 0) {
        severity = LuaLibGameAnalytics::ERROR_SEVERITY_DEBUG;
    } else if (strcmp(severityStr,"info") == 0) {
        severity = LuaLibGameAnalytics::ERROR_SEVERITY_INFO;
    } else if (strcmp(severityStr,"warning") == 0) {
        severity = LuaLibGameAnalytics::ERROR_SEVERITY_WARNING;
    } else if (strcmp(severityStr,"error") == 0) {
        severity = LuaLibGameAnalytics::ERROR_SEVERITY_ERROR;
    } else if (strcmp(severityStr,"critical") == 0) {
        severity = LuaLibGameAnalytics::ERROR_SEVERITY_CRITICAL;
    } else {
        ostringstream err;
        err << "Unknown error severity " << severityStr;
        luaPushError(L,err.str().c_str());
        return 0;        
    }
    
// message
    const char *msg = luaL_checkstring(L,2);
    
// add event
    luaLibGameAnalytics->addErrorEvent(severity,msg);
    return 0;
}

/*
-- @name .addBusinessEvent
-- @func
-- @brief Adds a business event.
-- @param currency The currency in ISO 4217 format.
-- @param amount The amount in cents.
-- @param itemType The item type.
-- @param itemId The item identifier.
-- @param cartType The cart type.
-- @param receipt The transaction receipt.
-- @func
-- @brief Adds a business event (Google Play).
-- @param currency The currency in ISO 4217 format.
-- @param amount The amount in cents.
-- @param itemType The item type.
-- @param itemId The item identifier.
-- @param cartType The cart type.
-- @param receipt The transaction receipt.
-- @param store The app store (must be `google_play`).
-- @param signature The transaction receipt signature.
*/
static int gameAnalyticsAddBusinessEvent(lua_State *L) {
    LuaLibGameAnalytics *luaLibGameAnalytics = getLuaLibGameAnalytics(L);
    
    const char *currency = luaL_checkstring(L,1);
    lua_Number amount = luaL_checknumber(L,2);
    const char *itemType = luaL_checkstring(L,3);
    const char *itemId = luaL_checkstring(L,4);
    const char *cartType = luaL_checkstring(L,5);
    
// receipt
    string *receipt = (string *)0;
    string receiptStr;
    if (lua_isnoneornil(L,6) == 0) {
        receiptStr.assign(luaL_checkstring(L,6));
        receipt = &receiptStr;
    }
    
// store (Google Play only)
    string *store = (string *)0;
    string storeStr;
    if (lua_isnoneornil(L,7) == 0) {
        storeStr.assign(luaL_checkstring(L,7));
        store = &storeStr;
    }
    
// signature (Google Play only)
    string *signature = (string *)0;
    string signatureStr;    
    if (lua_isnoneornil(L,8) == 0) {
        signatureStr.assign(luaL_checkstring(L,8));
        signature = &signatureStr;
    }
    
// add event
    luaLibGameAnalytics->addBusinessEvent(string(currency),(int)amount,
        string(itemType),string(itemId),string(cartType),receipt,store,
        signature);
    return 0;
}

/*
-- @name .addResourceEvent
-- @func
-- @brief Adds a resource event.
-- @param flowType The flow type (one of `sink`, `source`).
-- @param currency The currency.
-- @param amount The amount.
-- @param itemType The item type.
-- @param itemId The item identifier.
*/
static int gameAnalyticsAddResourceEvent(lua_State *L) {
    LuaLibGameAnalytics *luaLibGameAnalytics = getLuaLibGameAnalytics(L);
    
// severity
    LuaLibGameAnalytics::FlowType flowType;
    const char *flowTypeStr = luaL_checkstring(L,1);
    if (strcmp(flowTypeStr,"sink") == 0) {
        flowType = LuaLibGameAnalytics::FLOW_TYPE_SINK;
    } else if (strcmp(flowTypeStr,"source") == 0) {
        flowType = LuaLibGameAnalytics::FLOW_TYPE_SOURCE;
    } else {
        ostringstream err;
        err << "Unknown flow type " << flowTypeStr;
        luaPushError(L,err.str().c_str());
        return 0;        
    }    
    
// other parameters
    const char *currency = luaL_checkstring(L,2);
    lua_Number amount = luaL_checknumber(L,3);
    const char *itemType = luaL_checkstring(L,4);
    const char *itemId = luaL_checkstring(L,5);
    
// add event
    luaLibGameAnalytics->addResourceEvent(flowType,string(currency),
        (float)amount,string(itemType),string(itemId));
    return 0;
}

/** */
static const struct luaL_Reg gameAnalyticsFuncs[] = {
    {"addProgressionEvent",gameAnalyticsAddProgressionEvent},
    {"addDesignEvent",gameAnalyticsAddDesignEvent},
    {"addErrorEvent",gameAnalyticsAddErrorEvent},
    {"addBusinessEvent",gameAnalyticsAddBusinessEvent},
    {"addResourceEvent",gameAnalyticsAddResourceEvent},
    {0,0}
};

/** */
static int gameAnalyticsRequireFunc(lua_State *L) {
    luaL_newlib(L,gameAnalyticsFuncs);
    return 1;
}

/** */
void loadGameAnalyticsLib(LuaEngine *luaEngine,
    LuaLibGameAnalytics *luaLibGameAnalytics,Error *error) {
//
    lua_State *L = luaEngine->getLuaState();
    
// Lua global with Game Analytics
    lua_pushlightuserdata(L,luaLibGameAnalytics);
    lua_setglobal(L,gameAnalyticsNameGlobalLuaLibGA);
    
// load the library
    luaL_requiref(L,gameAnalyticsName,gameAnalyticsRequireFunc,1);
    lua_pop(L,1);
    
// initialize the library
    luaLibGameAnalytics->init();
}

} // namespace
    
} // namespace
    
} // namespace