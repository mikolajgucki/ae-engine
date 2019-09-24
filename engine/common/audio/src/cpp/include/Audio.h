#ifndef AE_AUDIO_AUDIO_H
#define AE_AUDIO_AUDIO_H

#include <string>

#include "Error.h"
#include "Sound.h"
#include "Music.h"

namespace ae {
    
namespace audio {
  
/**
 * \brief Responsible for playing sounds and music.
 */
class Audio:public Error {
protected:
    /**
     * \brief Constructs a Audio.
     */
    Audio():Error() {
    }     
public:
    /** */
    virtual ~Audio() {
    }
    
    /**
     * \brief Loads a sound sample.
     * \param filename The name of the file with the sound sample.
     * \return The loaded sample or null on error.
     */
    virtual Sound *loadSound(const std::string& filename) = 0;
    
    /**
     * \brief Deletes a sound sample.
     * \param sound The sound sample to delete.
     */
    virtual void deleteSound(Sound *sound) = 0;
    
    /**
     * \brief Loads music.
     * \param filename The name of the file with the music.
     * \return The music object or null on error.
     */
    virtual Music *loadMusic(const std::string& filename) = 0;
    
    /**
     * \brief Deletes a music object.
     * \param music The music object to delete.
     */
    virtual void deleteMusic(Music *music) = 0;
    
    /**
     * \brief Sets the global audio volume.
     * \param volume The volume from 0 to 1.
     * \return <code>true</code> on success, <code>false</code> otherwise. 
     */
    virtual bool setVolume(double volume) = 0;
    
    /**
     * \brief Sets the volume factor.
     * \param volumeFactor The volume factor from 0 to 1.
     * \return <code>true</code> on success, <code>false</code> otherwise.      
     */
    virtual bool setVolumeFactor(double volumeFactor) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_AUDIO_AUDIO_H