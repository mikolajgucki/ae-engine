#ifndef AE_ENGINE_KEY_LISTENER_H
#define AE_ENGINE_KEY_LISTENER_H

#include "Key.h"

namespace ae {

namespace engine {
    
/**
 * \brief Defines the interface to the key event listeners.
 */
class KeyListener {
public:
    /**
     * \brief Invoked when a key has been pressed.
     * \param key The key.
     */
    virtual void keyDown(const Key &key) = 0;
    
    /**
     * \brief Invoked when a key has been released.
     * \param key The key.
     */
    virtual void keyUp(const Key &key) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_ENGINE_KEY_LISTENER_H