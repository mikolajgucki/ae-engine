#ifndef AE_DESKTOP_SLEEP_FUNC_H
#define AE_DESKTOP_SLEEP_FUNC_H

#include "System.h"
#include "SleepFunc.h"

namespace ae {
    
namespace engine {
  
namespace desktop {
    
/**
 * \brief Provides the sleep function.
 */
class DesktopSleepFunc:public SleepFunc {
    /// The system-related methods.
    ::ae::system::System sys;
    
public:
    /** */
    DesktopSleepFunc():SleepFunc(),sys() {
    }
    
    /** */
    virtual ~DesktopSleepFunc() {
    }
    
    /**
     * \brief Suspends the execution of the current thread.
     * \param milliseconds The number of milliseconds to sleep.
     */
    virtual void sleep(int milliseconds) {
        sys.sleep(milliseconds);
    }
};
    
} // namespace

} // namespace
    
} // namespace

#endif // AE_DESKTOP_SLEEP_FUNC_H