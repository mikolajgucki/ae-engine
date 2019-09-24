#ifndef AE_LUA_DEBUG_MUTEX_H
#define AE_LUA_DEBUG_MUTEX_H

namespace ae {
    
namespace lua {

/**
 * \brief A mutex used to synchronized breakpoints.
 */
class LuaDebugMutex {
public:
    /**
     * \brief Locks the mutex.
     * \return true on success, false on error.
     */
    virtual bool lock() = 0;
    
    /**
     * \brief Unlocks the mutex.
     * \return true on success, false on error.
     */
    virtual bool unlock() = 0;
};
    
} // namespace

} // namespace

#endif // AE_LUA_DEBUG_MUTEX_H