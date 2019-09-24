#ifndef AE_UTIL_FPS_MAX_H
#define AE_UTIL_FPS_MAX_H

namespace ae {
    
namespace util {
  
/**
 * \brief Provides period to delay between frames to get maximum FPS.
 */
class FPSMax {
    /// The maximum FPS.
    int maxFps;
        
public:
    /**
     * \brief Constructs a FPSMax.
     * \param maxFps_ The initial maximum FPS.
     */
    FPSMax(int maxFps_):maxFps(maxFps_) {
    }
    
    /**
     * \brief Gets the delay period to get maximum FPS.
     * \param frameRenderPeriod The time the last frame was rendered.
     * \return The period in milliseconds to delay.
     */
    int getDelayPeriod(int frameRenderPeriod) {
        int maxFpsPeriod = 1000 / maxFps;
        int delay = maxFpsPeriod - frameRenderPeriod;
        
        if (delay < 0) {
            delay = 0;
        }
        return delay;
    }
};
    
} // namespace
    
} // namespace

#endif // AE_UTIL_FPS_MAX_H