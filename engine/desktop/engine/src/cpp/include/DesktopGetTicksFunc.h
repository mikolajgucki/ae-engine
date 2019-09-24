#ifndef AE_DESKTOP_GET_TICKS_FUNC_H
#define AE_DESKTOP_GET_TICKS_FUNC_H

#include "System.h"
#include "GetTicksFunc.h"

namespace ae {
    
namespace engine {
  
namespace desktop {
    
/**
 * \brief Provides the get-ticks function.
 */
class DesktopGetTicksFunc:public GetTicksFunc {
    /// The system-related methods.
    ::ae::system::System sys;
    
public:
    /** */
    DesktopGetTicksFunc():GetTicksFunc(),sys() {
    }
    
    /** */
    virtual ~DesktopGetTicksFunc() {
    }
    
    /**
     * \brief Gets the milliseconds since a point in time (an epoch or engine
     *   start moment).
     * \return The milliseconds since a point in time.
     */
    virtual long getTicks() {
        return sys.getSystemTime();
    }
};
    
} // namespace

} // namespace
    
} // namespace

#endif // AE_DESKTOP_GET_TICKS_FUNC_H