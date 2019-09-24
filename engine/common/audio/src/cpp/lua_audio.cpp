/*
-- @module audio
-- @group Audio
-- @brief Provides general audio functions.
*/
#include "lua_common.h"
#include "lua_audio.h"

using namespace ae::lua;

namespace ae {
    
namespace audio {
    
namespace lua {

/// The library name.
static const char *audioName = "audio";    
    
/// The name of the Lua global with the audio.
static const char *audioGlobalAudio = "ae_audio";

/**
 * \brief Gets the audio from the Lua state.
 * \param L The Lua state.
 * \return The audio.
 */
static Audio *getAudio(lua_State* L) {
    lua_getglobal(L,audioGlobalAudio);
    return (Audio *)lua_touserdata(L,-1);
}

/*
-- @name .setVolume
-- @func
-- @brief Sets the general audio volume.
-- @param volume The volume between 0 and 1.
*/
static int audioSetVolume(lua_State *L) {
    Audio *audio = getAudio(L);
    
    double volume = (double)luaL_checknumber(L,1);
    if (audio->setVolume(volume) == false) {
        luaPushError(L,audio->getError().c_str());
        return 0;
    }
    
    return 0;
}

/** */
static const struct luaL_Reg audioFuncs[] = {
    {"setVolume",audioSetVolume},
    {0,0}
};

/** */
static int audioRequireFunc(lua_State *L) {
    luaL_newlib(L,audioFuncs);
    return 1;
}

/** */
void loadAudioLib(lua_State *L,Audio *audio) {
// global with the audio
    lua_pushlightuserdata(L,audio);
    lua_setglobal(L,audioGlobalAudio);
    
// load the library
    luaL_requiref(L,audioName,audioRequireFunc,1);
    lua_pop(L,1);    
}
    
} // namespace

} // namespace
    
} // namespace