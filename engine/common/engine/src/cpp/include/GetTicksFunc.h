#ifndef AE_GET_TICKS_FUNC_H
#define AE_GET_TICKS_FUNC_H

namespace ae {
    
namespace engine {
  
/**
 * \brief Provides the get-ticks function.
 */
class GetTicksFunc {
public:
    /**
     * \brief Gets the milliseconds since a point in time (an epoch or engine
     *   start moment).
     * \return The milliseconds since a point in time.
     */
    virtual long getTicks() = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_GET_TICKS_FUNC_H