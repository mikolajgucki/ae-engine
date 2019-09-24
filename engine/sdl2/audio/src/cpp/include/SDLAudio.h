#ifndef AE_SDL_SDL_AUDIO_H
#define AE_SDL_SDL_AUDIO_H

#include <string>
#include <vector>

#include "Error.h"
#include "Audio.h"
#include "SDLMixVolume.h"
#include "SDLSound.h"
#include "SDLMusic.h"

namespace ae {
    
namespace audio {
  
/**
 * \brief The wrapper for the SDL mixer.
 */
class SDLAudio:public Audio,public SDLMixVolume {
    /** */
    enum {
        /** */
        STEREO = 2,
                
        /** */
        CHUNK_SIZE = 2048,
        
        /** */
        FREQUENCY = 22050
    };
    
    /// The log tag.
    static const char *logTag;
    
    /// The base directory for the files with sound samples.
    const std::string soundDir;
    
    /// The base directory for the file with music.
    const std::string musicDir;
    
    /// Indicates if the 
    bool initialized;

    /// The loaded sounds.
    std::vector<SDLSound *> sounds;
    
    /// The loaded music.
    std::vector<SDLMusic *> musics;
    
    /// The current volume.
    double volume;
    
    /// The volume factor.
    double volumeFactor;
    
    /**
     * \brief Quits the mix.
     */
    void quit();
    
    /**
     * \brief Deletes all the sounds.
     */
    void deleteSounds();    
    
    /**
     * \brief Deletes all the musics.
     */
    void deleteMusics();    
    
    /** */
    int getMixVolume();
    
public:
    /**
     * \brief Constructs an SDLAudio.
     * \param soundDir_ The base directory for the files with sound samples.
     * \param musicDir_ The base directory for the files with music.
     */
    SDLAudio(const std::string& soundDir_,const std::string &musicDir_):Audio(),
        soundDir(soundDir_),musicDir(musicDir_),initialized(false),sounds(),
        musics(),volume(1),volumeFactor(1) {
    }
    
    /** */
    virtual ~SDLAudio();
    
    /**
     * \brief Initializes the audio.
     */
    bool init();
    
    /** */
    virtual Sound *loadSound(const std::string &filename);
    
    /** */
    virtual void deleteSound(Sound *sound);
    
    /** */
    virtual Music *loadMusic(const std::string& filename);
    
    /** */
    virtual void deleteMusic(Music *music);

    /** */
    virtual bool setVolume(double volume_);
    
    /** */
    virtual bool setVolumeFactor(double volumeFactor_);
    
    /** */
    virtual int getMixVolume(double volume_);
};
    
} // namespace
    
} // namespace

#endif // AE_SDL_SDL_AUDIO_H