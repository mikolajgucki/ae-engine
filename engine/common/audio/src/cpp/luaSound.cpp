/*
-- @module Sound
-- @group Audio
-- @brief Provides sound related functions.
-- @full Provides sound related functions. This module is a C library loaded
--   by default.
*/
#include <memory>
#include <string>

#include "Log.h"
#include "lua_common.h"
#include "Sound.h"
#include "luaSound.h"

using namespace std;
using namespace ae;
using namespace ae::lua;
using namespace ae::audio;

namespace ae {
    
namespace audio {
    
namespace lua {
    
/**
 * \brief Wraps the sound so that it can be used as Lua user type.
 */
struct SoundType {
    /** */
    Sound *sound;
};
typedef struct SoundType SoundType;

/// The name of the Lua library and user type.
static const char *soundName = "Sound";

/// The name of the Lua metatable.
static const char *soundMetatableName = "Sound.metatable";
    
/// The name of the Lua global holding the audio.
static const char *soundGlobalAudio = "ae_sound_audio";

/// The log tag.
const char *const logTag = "lua.audio";

/** */
static SoundType *checkSoundType(lua_State *L) {
    void *data = luaL_checkudata(L,1,soundMetatableName);
    luaL_argcheck(L,data != (void *)0,1,"Sound expected");
    return (SoundType *)data;
}

/**
 * \brief Gets the audio from the Lua state.
 * \param L The Lua state.
 * \return The audio.
 */
static Audio *getAudio(lua_State* L) {
    lua_getglobal(L,soundGlobalAudio);
    return (Audio *)lua_touserdata(L,-1);
}

/*
-- @name .load
-- @func
-- @brief Loads a sound.
-- @param name The name of the file with the sound.
-- @return The sound object.
*/
static int soundLoad(lua_State *L) {
    Audio *audio = getAudio(L);
    const char *filename = lua_tostring(L,1);
    
// load the sound
    Sound *sound = audio->loadSound(string(filename));
    if (sound == (Sound *)0) {
        luaPushError(L,audio->getError().c_str());
        return 0;
    }
    
// user type
    SoundType *data = (SoundType *)lua_newuserdata(L,sizeof(SoundType));
    data->sound = sound;
    
// metatable
    luaL_getmetatable(L,soundMetatableName);
    lua_setmetatable(L,-2);
    
    return 1;
}

/*
-- @name :play
-- @func
-- @brief Plays the sound.
*/
static int soundPlay(lua_State *L) {
    SoundType *data = checkSoundType(L);
    if (data->sound->play() == false) {
        luaPushError(L,data->sound->getError().c_str());
        return 0;
    }
    
    return 0;
} 

/*
-- @name :delete
-- @func
-- @brief Deletes the sound.
*/
static int soundDelete(lua_State *L) {
    SoundType *data = checkSoundType(L);
    Audio *audio = getAudio(L);
    
// check if already deleted
    if (data->sound == (Sound *)0) {
        Log::error(logTag,"Attempt to delete an already deleted sound");        
        return 0;
    }
    
// log
    ostringstream msg;
    msg << "Deleting sound " << data->sound->getId();
    Log::trace(logTag,msg.str());       
    
// delete
    audio->deleteSound(data->sound);
    data->sound = (Sound *)0;
    
    return 0;
}

/*
-- @name :__gc
-- @func
-- @brief Destroys the sound.
-- @full Destroys the sound. Never call this function directly!
*/
static int sound__gc(lua_State *L) {
    SoundType *data = checkSoundType(L);
    Audio *audio = getAudio(L);
    
// check if already deleted
    if (data->sound == (Sound *)0) {
        return 0;
    }
    
// log
    ostringstream msg;
    msg << "Deleting sound " << data->sound->getId();
    Log::trace(logTag,msg.str());    
    
// delete
    audio->deleteSound(data->sound);
    
    return 0;
}

// TODO Write __tostring metamethod.

/** The type functions. */
static const struct luaL_Reg soundFuncs[] = {
    {"load",soundLoad},
    {0,0}
};
    
/** The type methods. */
static const struct luaL_Reg soundMethods[] = {
    {"play",soundPlay},
    {"delete",soundDelete},    
    {0,0}
};
    
/** The type meta-methods. */
static const struct luaL_Reg soundMetamethods[] = {
    {"__gc",sound__gc},
    {0,0}
};

/** */
static int soundRequireFunc(lua_State *L) {
    luaLoadUserType(L,soundMetatableName,soundFuncs,soundMethods,
        soundMetamethods);
    return 1;
}

/** */
void loadSoundLib(lua_State *L,Audio *audio) {
// global with the audio
    lua_pushlightuserdata(L,audio);
    lua_setglobal(L,soundGlobalAudio);
    
// load the library
    luaL_requiref(L,soundName,soundRequireFunc,1);
    lua_pop(L,1);
}
    
} // namespace
    
} // namespace
    
} // namespace