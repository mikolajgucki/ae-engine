/*
-- @module iap
-- @group IAP
*/
#include <memory>

#include "lua_common.h"
#include "LuaPCall.h"
#include "lua_iap.h"
#include "LuaIAPCallback.h"

using namespace std;
using namespace ae;
using namespace ae::lua;
using namespace ae::engine;

namespace ae {
    
namespace iap {
    
namespace lua {

/// The log tag.
static const char *logTag = "lua.iap";    
    
/// The name of the IAP library.
static const char *iapName = "iap";

/// The name of the Lua global holding the IAP Lua library implementation.
static const char *iapGlobalLuaLibIap = "ae_iap";

/**
 * \brief Gets the IAP implementation from the Lua state.
 * \param L The Lua state.
 * \return The IAP implementation.
 */
static LuaLibIAP *getLuaLibIAP(lua_State *L) {
    lua_getglobal(L,iapGlobalLuaLibIap);
    LuaLibIAP *lib = (LuaLibIAP *)lua_touserdata(L,-1);
    lua_pop(L,1);
    return lib;    
}

/*
-- @name .isSupported
-- @func
-- @brief Checks if IAP is supported.
-- @return `true` if supported, `false` otherwise.
*/
static int iapIsSupported(lua_State *L) {
    LuaLibIAP *luaLibIap = getLuaLibIAP(L);    
    lua_pushboolean(L,luaBoolean(luaLibIap->isSupported()));
    
    return 1;
}

/*
-- @name .purchase
-- @func
-- @brief Purchases a product.
-- @param productId The product identifier.
*/
static int iapPurchase(lua_State *L) {
    LuaLibIAP *luaLibIap = getLuaLibIAP(L);   
    
    const char *productId = lua_tostring(L,1);
    luaLibIap->purchase(string(productId));
    
    return 0;
}

/** */
static void deleteProducts(vector<IAPProduct *> products) {
    vector<IAPProduct *>::iterator itr;
    for (itr = products.begin(); itr != products.end(); ++itr) {
        IAPProduct *product = *itr;
        delete product;
    }
}

/*
-- @name .getProducts
-- @func
-- @brief Gets the products.
-- @return The table with the products.
*/
static int iapGetProducts(lua_State *L) {
    LuaLibIAP *luaLibIap = getLuaLibIAP(L);
    
// get the products
    vector<IAPProduct *> products;
    if (luaLibIap->getProducts(products) == false) {
        deleteProducts(products);
        luaPushError(L,luaLibIap->getError().c_str());
        return 0;
    }
    
// table with the products
    lua_newtable(L);
    
    int index = 1;
    vector<IAPProduct *>::iterator itr;
// for each product
    for (itr = products.begin(); itr != products.end(); ++itr) {
        IAPProduct *product = *itr;
        
    // index in the products table
        lua_pushnumber(L,index);
        index++;
        
    // table with the product
        lua_newtable(L);
    
    // identifier, title, price, price in cents
        luaSetTable(L,"id",product->getId().c_str());
        luaSetTable(L,"title",product->getTitle().c_str());
        luaSetTable(L,"price",product->getPrice().c_str());
        luaSetTable(L,"priceInCents",(lua_Number)product->getPriceInCents());
        luaSetTable(L,"currencyCode",product->getCurrencyCode().c_str());
    
    // set it in the products table
        lua_settable(L,-3);
    }
        
    deleteProducts(products);
    return 1;
}

/*
-- @name .restorePurchases
-- @func
-- @brief Restores the purchases.
*/
static int iapRestorePurchases(lua_State *L) {
    LuaLibIAP *luaLibIap = getLuaLibIAP(L);
    luaLibIap->restorePurchases();
    return 0;
}

/** */
static const struct luaL_Reg iapFuncs[] = {
    {"isSupported",iapIsSupported},
    {"purchase",iapPurchase},
    {"getProducts",iapGetProducts},
    {"restorePurchases",iapRestorePurchases},
    {0,0}
};

/** */
static int iapRequireFunc(lua_State *L) {
    luaL_newlib(L,iapFuncs);
    return 1;
}

/** */
void loadIAPLib(DLog *log,LuaEngine *luaEngine,LuaLibIAP *luaLibIap,
    Error *error) {
//
    log->trace(logTag,"loadIAPLib()");
    lua_State *L = luaEngine->getLuaState();
    
// Lua global with IAP
    lua_pushlightuserdata(L,luaLibIap);
    lua_setglobal(L,iapGlobalLuaLibIap);
    
// load the library
    luaL_requiref(L,iapName,iapRequireFunc,1);
    lua_pop(L,1);
    
// log
    luaLibIap->setDLog(log);
    
// callback
    LuaIAPCallback *callback = new (nothrow) LuaIAPCallback(log,luaEngine);
    if (callback == (LuaIAPCallback *)0) {
        log->error(logTag,"Failed to create Lua IAP callback. No memory.");
        error->setNoMemoryError();
        return;
    }
    luaLibIap->setCallback(callback);
    
// load the Lua source (the global 'iap' already exists at this point so
// the Lua sources must be kept in the directory 'ae')
    LuaPCall luaPCall;
    if (luaPCall.require(L,"ae.iap") == false) {
        error->setError(luaPCall.getError());
        return;
    }
    
// initialize
    luaLibIap->loaded();
    luaLibIap->init();
}

/** */
void unloadIAPLib(DLog *log,LuaLibIAP *luaLibIap) {
    log->trace(logTag,"unloadIAPLib()");
    luaLibIap->setCallback((IAPCallback *)0);
}
    
} // namespace
    
} // namespace
    
} // namespace