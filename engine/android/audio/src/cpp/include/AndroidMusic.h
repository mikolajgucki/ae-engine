#ifndef AE_ANDROID_MUSIC_H
#define AE_ANDROID_MUSIC_H

#include <string>
#include "Music.h"

namespace ae {
    
namespace audio {
 
/** */
class AndroidMusic:public Music {
    /** */
    bool callJNINoArgMethod(const std::string &methodName);
    
public:
    /** */
    AndroidMusic():Music("android_music") {
    }
    
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

#endif // AE_ANDROID_MUSIC_H