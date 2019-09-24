#ifndef AE_SDL_SDL_SOUND_H
#define AE_SDL_SDL_SOUND_H

#include <string>
#include "SDL_mixer.h"

#include "Sound.h"

namespace ae {
    
namespace audio {
  
/**
 * \brief An SDL sound sample.
 */
class SDLSound:public Sound {
    /// The name of the file from which the sample was loaded.
    const std::string filename;
    
    /// The chunk.
    Mix_Chunk *mixChunk;
    
public:
    /**
     * \brief Constructs an SDLSound.
     * \param chunk_ The chunk.
     * \param filename_ The name of the file from which the sample was loaded.
     */
    SDLSound(Mix_Chunk *mixChunk_,const std::string &filename_):
        Sound(filename_),filename(filename_),mixChunk(mixChunk_) {
    }
    
    /** */
    virtual ~SDLSound() {
    }
    
    /**
     * \brief Gets the mix chunk.
     * \return The mix chunk.
     */
    Mix_Chunk *getMixChunk() const {
        return mixChunk;
    }
    
    /**
     * \brief Gets the name of the file from which the sample was loaded.
     * \return The file name.
     */
    const std::string &getFileName() {
        return filename;
    }
    
    /** */
    virtual bool play();
};
    
} // namespace
    
} // namespace

#endif // AE_SDL_SDL_SOUND_H