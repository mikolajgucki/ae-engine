#include <algorithm>
#include <cstring>
#include <sstream>
#include "SDL_mixer.h"

#include "Log.h"
#include "SDLAudio.h"

using namespace std;
using namespace ae;
using namespace ae::audio;

namespace ae {

namespace audio {
 
/// The log tag.
const char *SDLAudio::logTag = "sdl.audio";
    
/** */
void SDLAudio::quit() {
    while (Mix_Init(0)) {
        Mix_Quit();
    }    
}
    
/** */
bool SDLAudio::init() {
    if (initialized == true) {
        return true;
    }
    
// log
    Log::trace(logTag,"SDLAudio::init()");
    int flags = MIX_INIT_OGG;
    
// init
    int initted = Mix_Init(flags);
    if ((initted & flags) != flags) {
        setError(Mix_GetError());
        return false;
    }
    
// open audio
    if (Mix_OpenAudio(FREQUENCY,MIX_DEFAULT_FORMAT,STEREO,CHUNK_SIZE) == -1) {
        quit();
        setError(Mix_GetError());
        return false;
    }
    
// 24 for sounds, 1 for music
    int allocatedChannels = Mix_AllocateChannels(25);
    
// log    
    ostringstream channelsMsg;
    channelsMsg << "Allocated channels " << allocatedChannels;
    Log::trace(logTag,channelsMsg.str());
    
// query
    int specFrequency;
    Uint16 specFormat;
    int specChannels;
    if (Mix_QuerySpec(&specFrequency,&specFormat,&specChannels) != 0) {
        ostringstream specMsg;
        specMsg << "Audio spec: frequency " << specFrequency;
        specMsg << ", format " << specFormat;
        specMsg << ", channels " << specChannels;
        Log::trace(logTag,specMsg.str());
    }
    else {
        Log::trace(logTag,"Audio query failed");
    }
    
    initialized = true;
    return true;
}

/** */
SDLAudio::~SDLAudio() {
    if (initialized) {
        deleteSounds();
        deleteMusics();
        quit();
        Mix_CloseAudio();
    }    
}

/** */
void SDLAudio::deleteSounds() {
    while (sounds.empty() == false) {
        deleteSound(sounds.front());
    }
}

/** */
void SDLAudio::deleteMusics() {
    while (musics.empty() == false) {
        deleteMusic(musics.front());
    }
}
 
/** */
Sound *SDLAudio::loadSound(const string &filename) {
    if (init() == false) {
        return (Sound *)0;
    }
    
// combine directory and filename
    ostringstream fullFilename;
    fullFilename << soundDir << "/" << filename;
    
// log
    ostringstream msg;
    msg << "Loading sound " << fullFilename.str();
    Log::trace(logTag,msg.str());
    
// SDL load
    Mix_Chunk *mixChunk = Mix_LoadWAV(fullFilename.str().c_str());
    if (mixChunk == (Mix_Chunk *)0) {
        setError(Mix_GetError());
        return (Sound *)0;
    }
    
// create the object
    SDLSound *sdlSound = new (nothrow) SDLSound(mixChunk,fullFilename.str());
    if (sdlSound == (SDLSound *)0) {
        Mix_FreeChunk(mixChunk);
        setNoMemoryError();
        return (Sound *)0;
    }        
    sounds.push_back(sdlSound);
    
    return sdlSound;
}

/** */
void SDLAudio::deleteSound(Sound *sound) {
    SDLSound *sdlSound = (SDLSound *)sound;
    
// log
    ostringstream msg;
    msg << "Deleting sound " << sdlSound->getFileName();
    Log::trace(logTag,msg.str());
    
// check
    if (sdlSound == (SDLSound *)0) {
        Log::warning(logTag,"Attempt to delete a null sound"); 
        return;
    }
    vector<SDLSound *>::iterator itr =
        find(sounds.begin(),sounds.end(),sdlSound);
    if (itr == sounds.end()) {
        Log::warning(logTag,"Attempt to delete an unknown sound");
        return;
    }
    
// SDL free
    Mix_Chunk *mixChunk = sdlSound->getMixChunk();
    if (mixChunk != (Mix_Chunk *)0) {
        Mix_FreeChunk(mixChunk);
    }
    
// delete
    delete sdlSound;
    sounds.erase(itr);
}

/** */
Music *SDLAudio::loadMusic(const string& filename) {
    if (init() == false) {
        return (Music *)0;
    }
        
// combine directory and filename
    ostringstream fullFilename;
    fullFilename << musicDir << "/" << filename;

// log
    ostringstream msg;
    msg << "Loading music " << fullFilename.str();
    Log::trace(logTag,msg.str());
    
// SDL load
    Mix_Music *mixMusic = Mix_LoadMUS(fullFilename.str().c_str());
    if (mixMusic == (Mix_Music *)0) {
        setError(Mix_GetError());
        return (Music *)0;
    }
    
// create the object
    SDLMusic *sdlMusic = new (nothrow) SDLMusic(
        mixMusic,fullFilename.str(),this,volume);
    if (sdlMusic == (SDLMusic *)0) {
        Mix_FreeMusic(mixMusic);
        setNoMemoryError();
        return (Music *)0;
    }
    musics.push_back(sdlMusic);
    
    return sdlMusic;
}

void SDLAudio::deleteMusic(Music *music) {
    SDLMusic *sdlMusic = (SDLMusic *)music;
    
// log
    ostringstream msg;
    msg << "Deleting music " << sdlMusic->getFileName();
    Log::trace(logTag,msg.str());
    
// check
    if (sdlMusic == (SDLMusic *)0) {
        Log::warning(logTag,"Attempt to delete a null music object");
        return;
    }
    vector<SDLMusic *>::iterator itr =
        find(musics.begin(),musics.end(),sdlMusic);
    if (itr == musics.end()) {
        Log::warning(logTag,"Attempt to delete an unknown music object");
        return;
    }
    
// SDL free
    Mix_Music *mixMusic = sdlMusic->getMixMusic();
    if (mixMusic != (Mix_Music *)0) {
        Mix_FreeMusic(mixMusic);
    }
    
// delete
    delete sdlMusic;
    musics.erase(itr);
}

/** */
bool SDLAudio::setVolume(double volume_) {
    volume = volume_;    
    Mix_Volume(-1,getMixVolume());
    Mix_VolumeMusic(getMixVolume());
    
    return true;
}

/** */
bool SDLAudio::setVolumeFactor(double volumeFactor_) {
    volumeFactor = volumeFactor_;
    return setVolume(volume);
}

/** */
int SDLAudio::getMixVolume(double volume_) {
    return (int)(volume_ * volumeFactor * MIX_MAX_VOLUME);
}

/** */
int SDLAudio::getMixVolume() {
    return getMixVolume(volume);
}

} // namespace
    
} // namespace