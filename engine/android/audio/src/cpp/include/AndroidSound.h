#ifndef AE_ANDROID_SOUND_H
#define AE_ANDROID_SOUND_H

#include "Sound.h"

namespace ae {
    
namespace audio {
    
/** */
class AndroidSound:public Sound {
    /** */
    int soundId;
    
public:
    /** */
    AndroidSound(int soundId_):Sound("android_sound"),soundId(soundId_) {
    }
    
    /** */
    int getSoundId() const {
        return soundId;
    }
    
    /** */
    virtual bool play();
};
    
} // namespace

} // namespace

#endif // AE_ANDROID_SOUND_H