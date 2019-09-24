#ifndef AE_AUDIO_DUMMY_AUDIO_H
#define AE_AUDIO_DUMMY_AUDIO_H

#include <string>

#include "DummySound.h"
#include "DummyMusic.h"
#include "Audio.h"

using namespace std;

namespace ae {
    
namespace audio {

/**
 * \brief Does nothing.
 */
class DummyAudio:public Audio {
public:
    /** */
    DummyAudio():Audio() {
    }
    
    /** */
    virtual ~DummyAudio() {
    }
    
    /** */
    virtual Sound *loadSound(const string& filename) {
        return new DummySound();
    }
    
    /** */
    virtual void deleteSound(Sound *sound) {
        delete sound;
    }
    
    /** */
    virtual Music *loadMusic(const string& filename) {
        return new DummyMusic();
    }
    
    /** */
    virtual void deleteMusic(Music *music) {
        delete music;
    }
    
    /** */
    virtual bool setVolume(double volume) {
        return true;
    }
    
    /** */
    virtual bool setVolumeFactor(double volumeFactor) {
        return true;
    }
};
    
} // namespace
    
} // namespace

#endif // AE_AUDIO_DUMMY_AUDIO_H