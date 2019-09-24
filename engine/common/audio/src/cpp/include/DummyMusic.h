#ifndef AE_AUDIO_DUMMY_MUSIC_H
#define AE_AUDIO_DUMMY_MUSIC_H

#include "Music.h"

namespace ae {

namespace audio {
  
/**
 * \brief Does nothing.
 */
class DummyMusic:public Music {
public:
    /** */
    DummyMusic():Music("dummy") {
    }
    
    /** */
    virtual bool play(int loops) {
        return true;
    }
    
    /** */
    virtual bool pause() {
        return true;
    }
    
    /** */
    virtual bool resume() {
        return true;
    }
    
    /** */
    virtual bool stop() {
        return true;
    }
    
    /** */
    virtual bool setVolume(double volume) {
        return true;
    }
};
    
} // namespace
    
} // namespace

#endif // AE_AUDIO_DUMMY_MUSIC_H