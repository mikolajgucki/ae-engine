#ifndef AE_LUA_MUSIC_H
#define AE_LUA_MUSIC_H

#include "lua.hpp"
#include "Audio.h"

namespace ae {
    
namespace audio {
    
namespace lua {
    
/**
 * \brief Loads the music library.
 * \param L The Lua state.
 * \param audio The audio.
 */
void loadMusicLib(lua_State *L,Audio *audio);
    
} // namespace

} // namespace
    
} // namespace

#endif //  AE_LUA_AUDIO_H