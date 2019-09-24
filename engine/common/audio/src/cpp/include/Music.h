#ifndef AE_MIXER_MUSIC_H
#define AE_MIXER_MUSIC_H

#include <string>
#include "Error.h"

namespace ae {
    
namespace audio {
  
/**
 * \brief Represents a music player.
 */
class Music:public Error {
    /// The music identifier.
    const std::string id;
    
public:
    /** */
    Music(const std::string &id_):Error(),id(id_) {
    }
    
    /** */
    virtual ~Music() {
    }
    
    /**
     * \brief Gets the music identifier.
     * \return The music identifier.
     */
    const std::string &getId() const {
        return id;
    }    
    
    /**
     * \brief Plays the loaded music.
     * \param loops The number of times to play the music, -1 to play forever.
     * \return <code>true</code> on success, <code>false</code> otherwise.     
     */
    virtual bool play(int loops) = 0;
    
    /**
     * \brief Pauses the music playback.    
     * \return <code>true</code> on success, <code>false</code> otherwise.          
     */
    virtual bool pause() = 0;
    
    /**
     * \brief Resumes the music playback.
     * \return <code>true</code> on success, <code>false</code> otherwise.  
     */
    virtual bool resume() = 0;
    
    /**
     * \brief Stops playing the music.
     * \return <code>true</code> on success, <code>false</code> otherwise.     
     */
    virtual bool stop() = 0;
    
    /**
     * Sets the music volume.
     *
     * \param volume The volume from the range 0..1.
     * \return <code>true</code> on success, <code>false</code> otherwise.     
     */
    virtual bool setVolume(double volume) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_MIXER_MUSIC_H