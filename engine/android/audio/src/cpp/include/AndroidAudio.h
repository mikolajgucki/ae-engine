#ifndef AE_ANDROID_AUDIO_H
#define AE_ANDROID_AUDIO_H

#include "AndroidMusic.h"
#include "Audio.h"

namespace ae {
    
namespace audio {

/**
 * \brief The wrapper for the SDL mixer.
 */
class AndroidAudio:public Audio {
    /** */
    AndroidMusic music;
    
public:
    /** */
    AndroidAudio():Audio() {
    }
    
    /** */
    virtual Sound *loadSound(const std::string& filename);
    
    /** */
    virtual void deleteSound(Sound *sound);
    
    /** */
    virtual Music *loadMusic(const std::string& filename);
    
    /** */
    virtual void deleteMusic(Music *music);
    
    /** */
    virtual bool setVolume(double volume);
    
    /** */
    virtual bool setVolumeFactor(double volumeFactor);    
};
    
} // namespace
    
} // namespace
    
#endif // AE_ANDROID_AUDIO_H