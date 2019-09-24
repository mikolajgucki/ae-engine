#include <string>
#include "SDLMusic.h"

using namespace std;

namespace ae {
    
namespace audio {
    
/** */
bool SDLMusic::load(const string& filename) {
    return true;
}
    
/** */
bool SDLMusic::play(int loops) {
    if (Mix_PlayMusic(mixMusic,loops) == -1) {
        setError(Mix_GetError());
        return false;
    }
    
// setting music volume before it plays doesn't seem to have effect
    setVolume(volume);
    
    return true;
}

/** */
bool SDLMusic::pause() {
    Mix_PauseMusic();
    return true;
}

/** */
bool SDLMusic::resume() {
    Mix_ResumeMusic();
    return true;
}

/** */
bool SDLMusic::stop() {
    Mix_HaltMusic();
    return true;
}

/** */
bool SDLMusic::setVolume(double volume) {
    Mix_VolumeMusic(mixVolume->getMixVolume(volume));
    return true;
}

} // namespace
    
} // namespace