#ifndef AE_SDL_MIX_VOLUME_H
#define AE_SDL_MIX_VOLUME_H

namespace ae {
    
namespace audio {
    
/**
 * \brief Provides Mix volume.
 */
class SDLMixVolume {
public:
    /**
     * \brief Gets the Mix for normalize volume 0..1.
     * \param volume The normalized volume.
     * \return The Mix volume.
     */
    virtual int getMixVolume(double volume) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_SDL_MIX_VOLUME_H