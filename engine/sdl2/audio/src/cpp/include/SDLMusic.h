#ifndef AE_SDL_SDL_MUSIC_H
#define AE_SDL_SDL_MUSIC_H

#include <string>
#include "SDL_mixer.h"

#include "Music.h"
#include "SDLMixVolume.h"

namespace ae {
    
namespace audio {
 
/**
 * \brief The SDL music.
 */
class SDLMusic:public Music {
    /// The name of the file with the music.
    const std::string filename;
    
    /// The SDL music.
    Mix_Music *mixMusic;
    
    /// The Mix volume.
    SDLMixVolume *mixVolume;
    
    /// The volume.
    double volume;
    
public:
    /**
     * \brief Constructs an SDLMusic.
     * \param mixMusic_ The music.
     */
    SDLMusic(Mix_Music *mixMusic_,const std::string &filename_,
        SDLMixVolume *mixVolume_,double volume_):Music(filename_),
        filename(filename_),mixMusic(mixMusic_),mixVolume(mixVolume_),
        volume(volume_) {
    }
    
    /** */
    virtual ~SDLMusic() {
    }
    
    /**
     * \brief Gets the mix music.
     * \return The mix music.
     */
    Mix_Music* getMixMusic() const {
        return mixMusic;
    }
    
    /**
     * \brief Gets the name of the file with the music.
     * \return The file name.
     */
    const std::string &getFileName() {
        return filename;
    }
    
    /** */
    virtual bool load(const std::string& filename);
    
    /** */
    virtual bool play(int loops);
    
    /** */
    virtual bool pause();
    
    /** */
    virtual bool resume();
    
    /** */
    virtual bool stop();
    
    /** */
    virtual bool setVolume(double volume);
};
    
} // namespace
    
} // namespace

#endif // AE_SDL_SDL_MUSIC_H