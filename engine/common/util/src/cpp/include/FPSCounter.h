#ifndef AE_UTIL_FPS_COUNTER_H
#define AE_UTIL_FPS_COUNTER_H

namespace ae {
    
namespace util {
  
/**
 * \brief Counts frames per second.
 */
class FPSCounter {
    /** */
    enum {
        /// The default period in milliseconds for which to calculate FPS.
        PERIOD = 1000
    };
    
    /// The accumulated time.
    long accum;
    
    /// The number of frames since the last FPS calculation.
    long frames;
    
    /// The frames per second.
    double fps;
    
public:
    /**
     * \brief Constructs a FPSCounter.
     */
    FPSCounter():accum(0),frames(0),fps(0) {
        reset();        
    }
    
    /**
     * \brief Resets the counter.
     */
    void reset();   
    
    /**
     * \brief Invoked when a frame has been drawn (or updated).
     * \param time The time in milseconds since the last frame.
     * \return <code>true</code> if new FPS has been calculated,
     *     <code>false</code> otherwise.
     */
    bool tick(long time);
    
    /**
     * \brief Gets frames per second.
     * \return The frames per second.
     */
    double getFPS() const;
};
    
} // namespace
    
} // namespace

#endif // AE_UTIL_FPS_COUNTER_H