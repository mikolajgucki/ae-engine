/*
-- @module Music
-- @group Audio
-- @brief Provides music related functions.
-- @full Provides music related functions. This module is a C library loaded
--   by default.
*/
#include <memory>
#include <string>

#include "Log.h"
#include "lua_common.h"
#include "Audio.h"
#include "Music.h"
#include "luaMusic.h"

using namespace std;
using namespace ae;
using namespace ae::lua;
using namespace ae::audio;

namespace ae {
    
namespace audio {
    
namespace lua {

/**
 * \brief Wraps the music so that it can be used as Lua user type.
 */
struct MusicType {
    /** */
    Music *music;
};
typedef struct MusicType MusicType;
    
/// The name of the Lua library and user type.
static const char *musicName = "Music";

/// The name of the Lua metatable.
static const char *musicMetatableName = "Music.metatable";

/// The name of the Lua global holding the audio.
static const char *musicGlobalAudio = "ae_music_audio";
 
/// The log tag.
const char *const logTag = "lua.audio";

/** */
static MusicType *checkMusicType(lua_State *L) {
    void *data = luaL_checkudata(L,1,musicMetatableName);
    luaL_argcheck(L,data != (void *)0,1,"Music expected");
    return (MusicType *)data;
}

/**
 * \brief Gets the audio from the Lua state.
 * \param L The Lua state.
 * \return The audio.
 */
static Audio *getAudio(lua_State* L) {
    lua_getglobal(L,musicGlobalAudio);
    return (Audio *)lua_touserdata(L,-1);
}

/*
-- @name .load
-- @func
-- @brief Loads music and does not yet play it.
-- @param filename The name of the file with the music.
-- @return The music object.
*/
static int musicLoad(lua_State *L) {
    Audio *audio = getAudio(L);
    const char *filename = lua_tostring(L,1);

// load the music
    Music *music = audio->loadMusic(string(filename));
    if (music == (Music *)0) {
        luaPushError(L,audio->getError().c_str());
        return 0;
    }
    
// user type
    MusicType *data = (MusicType *)lua_newuserdata(L,sizeof(MusicType));
    data->music = music;
    
// metatable
    luaL_getmetatable(L,musicMetatableName);
    lua_setmetatable(L,-2);
    
    return 1;
}

/*
-- @name :play
-- @func
-- @brief Plays the music forever.
-- @func
-- @brief Plays the music.
-- @param loops The number of times to play the music.
*/
static int musicPlay(lua_State *L) {
    MusicType *data = checkMusicType(L);    
    
    int loops = -1;
    if (lua_isnoneornil(L,2) == 0) {
        loops = (int)luaL_checknumber(L,2);
    }
    
    if (data->music->play(loops) == false) {
        luaPushError(L,data->music->getError().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :stop
-- @func
-- @brief Stops the playback.
*/
static int musicStop(lua_State *L) {
    MusicType *data = checkMusicType(L);
    
    if (data->music->stop() == false) {
        luaPushError(L,data->music->getError().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :pause
-- @func
-- @brief Pauses the music playback.
*/
static int musicPause(lua_State *L) {
    MusicType *data = checkMusicType(L);    
    if (data->music->pause() == false) {
        luaPushError(L,data->music->getError().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :resume
-- @func
-- @brief Resumes the music playback.
*/
static int musicResume(lua_State *L) {
    MusicType *data = checkMusicType(L);    
    if (data->music->resume() == false) {
        luaPushError(L,data->music->getError().c_str());
        return 0;
    }
    
    return 0;    
}

/*
-- @name :setVolume
-- @func
-- @brief Sets the music volume.
-- @param volume The volume between 0 and 1.
*/
static int musicSetVolume(lua_State *L) {
    MusicType *data = checkMusicType(L);    
    double volume = (double)luaL_checknumber(L,2);
    data->music->setVolume(volume);
    
    return 0;
}

/*
-- @name :delete
-- @func
-- @brief Deletes the music.
*/
static int musicDelete(lua_State *L) {
    MusicType *data = checkMusicType(L);    
    Audio *audio = getAudio(L);
    
// check if already deleted    
    if (data->music == (Music *)0) {
        Log::error(logTag,"Attempt to delete an already deleted music");        
        return 0;        
    }
    
// log
    ostringstream msg;
    msg << "Deleting music " << data->music->getId();
    Log::trace(logTag,msg.str());      
    
// delete
    audio->deleteMusic(data->music);
    data->music = (Music *)0;
    
    return 0;
}

/*
-- @name :__gc
-- @func
-- @brief Destroys the music.
-- @full Destroys the music. Never call this function directly!
*/
static int music__gc(lua_State *L) {
    MusicType *data = checkMusicType(L);
    Audio *audio = getAudio(L);
    
// check if already deleted    
    if (data->music == (Music *)0) {
        return 0;
    }
    
// log
    ostringstream msg;
    msg << "Deleting music " << data->music->getId();
    Log::trace(logTag,msg.str());      
    
// delete
    audio->deleteMusic(data->music);
    
    return 0;
}

// TODO Write __tostring metamethod.

/** The type functions. */
static const struct luaL_Reg musicFuncs[] = {
    {"load",musicLoad},
    {0,0}
};
    
/** The type methods. */
static const struct luaL_Reg musicMethods[] = {
    {"play",musicPlay},
    {"stop",musicStop},
    {"pause",musicPause},
    {"resume",musicResume},
    {"setVolume",musicSetVolume},
    {"delete",musicDelete},
    {0,0}
};
    
/** The type meta-methods. */
static const struct luaL_Reg musicMetamethods[] = {
    {"__gc",music__gc},
    {0,0}
};

/** */
static int musicRequireFunc(lua_State *L) {
    luaLoadUserType(L,musicMetatableName,musicFuncs,musicMethods,
        musicMetamethods);
    return 1;
}

/** */
void loadMusicLib(lua_State *L,Audio *audio) {
// global with the mixer
    lua_pushlightuserdata(L,audio);
    lua_setglobal(L,musicGlobalAudio);
    
// load the library
    luaL_requiref(L,musicName,musicRequireFunc,1);
    lua_pop(L,1);
}

} // namespace

} // namespace

} // namespace