#ifndef AE_AUDIO_LUA_SOUND_H
#define AE_AUDIO_LUA_SOUND_H

#include "lua.hpp"
#include "Audio.h"

namespace ae {

namespace audio {
    
namespace lua {
    
/**
 * \brief Loads the sound library.
 * \param L The Lua state.
 * \param audio The audio.
 */
void loadSoundLib(lua_State *L,Audio *audio);
    
} // namespace
    
} // namespace
    
} // namespace

#endif // AE_AUDIO_LUA_SOUND_H