#include "FPSCounter.h"

namespace ae {
    
namespace util {
 
/** */
void FPSCounter::reset() {
    accum = 0;
    frames = 0;
    fps = 0;
}
    
/** */
bool FPSCounter::tick(long time) {
    accum += time;
    frames++;
    
    if (accum >= PERIOD) {
        fps = (double)frames;
        frames = 0;
        accum -= PERIOD;
        
        return true;
    }
    
    return false;
}

/** */
double FPSCounter::getFPS() const {   
    return fps;
}
    
} // namespace
    
} // namespace