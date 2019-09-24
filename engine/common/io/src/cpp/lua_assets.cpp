/*
-- @module assets
-- @group IO
-- @brief Provides read-only access to the assets.
*/

#include "lua_common.h"
#include "luaInputStream.h"
#include "Assets.h"
#include "lua_assets.h"

using namespace ae::lua;

namespace ae {
    
namespace io {

namespace lua {
    
/// The library name.
static const char *assetsName = "assets";
    
/// The name of the Lua global with the assets.
static const char *assetsGlobalAssets = "ae_assets";
 
/**
 * \brief Gets the assets from the Lua state.
 * \param L The Lua state.
 * \return The assets.
 */
static Assets *getAssets(lua_State *L) {
    lua_getglobal(L,assetsGlobalAssets);
    Assets *assets = (Assets *)lua_touserdata(L,-1);
    lua_pop(L,1);
    return assets;    
}

/*
-- @name .getInputStream
-- @func
-- @brief Gets the input stream reading from an asset.
-- @param filename The file name.
-- @return The input stream object (see ()[InputStream]).
*/
static int assetsGetInputStream(lua_State *L) {
    Assets *assets = getAssets(L);
    const char *filename = luaL_checkstring(L,1);
    
    InputStream *input = assets->getInputStream(filename);
    if (input == (InputStream *)0) {
        luaPushError(L,assets->getError().c_str());
        return 0;        
    }
    
    luaPushInputStreamType(L,input);    
    return 1;
}

/** */
static const struct luaL_Reg assetsFuncs[] = {
    {"getInputStream",assetsGetInputStream},
    {0,0}
};

/** */
static int assetsRequireFunc(lua_State *L) {
    luaL_newlib(L,assetsFuncs);
    return 1;
}

/** */
void loadAssetsLib(lua_State *L,Assets *assets) {
// global with the assets
    lua_pushlightuserdata(L,assets);
    lua_setglobal(L,assetsGlobalAssets);
    
// load the library
    luaL_requiref(L,assetsName,assetsRequireFunc,1);
    lua_pop(L,1);
}
    
} // namespace
    
} // namespace
    
} // namespace