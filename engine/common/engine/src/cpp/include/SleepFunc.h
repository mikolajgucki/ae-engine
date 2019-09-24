#ifndef AE_SLEEP_FUNC_H
#define AE_SLEEP_FUNC_H

namespace ae {
    
namespace engine {
  
/**
 * \brief Provides the sleep function.
 */
class SleepFunc {
public:
    /**
     * \brief Suspends the execution of the current thread.
     * \param milliseconds The number of milliseconds to sleep.
     */
    virtual void sleep(int milliseconds) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_SLEEP_FUNC_H