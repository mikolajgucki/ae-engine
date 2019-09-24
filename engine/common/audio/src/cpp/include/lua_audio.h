#ifndef AE_LUA_AUDIO_H
#define AE_LUA_AUDIO_H

#include "lua.hpp"
#include "Audio.h"

namespace ae {
    
namespace audio {
    
namespace lua {
    
/**
 * \brief Loads the audio library.
 * \param L The Lua state.
 * \param audio The audio.
 */
void loadAudioLib(lua_State *L,Audio *audio);
    
} // namespace

} // namespace
    
} // namespace

#endif // AE_LUA_AUDIO_H