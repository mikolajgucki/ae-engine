#ifndef AE_ENGINE_TOUCH_LISTENER_H
#define AE_ENGINE_TOUCH_LISTENER_H

namespace ae {
    
namespace engine {
  
/**
 * \brief Defines the interface to the touch event listeners.
 */
class TouchListener {
public:
    /**
     * \brief Invoked on touch down event.
     * \param pointer The identifier of the pointer.
     * \param x The X-coordinate relative to the display.
     * \param y The Y-coordinate relative to the display.
     */
    virtual void touchDown(int pointer,float x,float y) = 0;
    
    /**
     * \brief Invoked on touch move event.
     * \param pointer The identifier of the pointer.
     * \param x The X-coordinate relative to the display.
     * \param y The Y-coordinate relative to the display.
     */
    virtual void touchMove(int pointer,float x,float y) = 0;

    /**
     * \brief Invoked on touch down up.
     * \param pointer The identifier of the pointer.
     * \param x The X-coordinate relative to the display.
     * \param y The Y-coordinate relative to the display.
     */
    virtual void touchUp(int pointer,float x,float y) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_ENGINE_TOUCH_LISTENER_H