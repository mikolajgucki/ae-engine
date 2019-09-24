#ifndef AE_AUDIO_DUMMY_SOUND_H
#define AE_AUDIO_DUMMY_SOUND_H

#include "Sound.h"

namespace ae {
    
namespace audio {
 
/**
 * \brief Does nothing.
 */
class DummySound:public Sound {
public:
    /** */
    DummySound():Sound("dummy") {
    }
    
    /** */
    virtual ~DummySound() {
    }
    
    /** */
    virtual bool play() {
        return true;
    }
};
    
} // namespace
    
} // namespace

#endif // AE_AUDIO_DUMMY_SOUND_H