#ifndef AE_DESKTOP_TOUCH_HANDLER_H
#define AE_DESKTOP_TOUCH_HANDLER_H

#include <vector>
#include "Error.h"

namespace ae {
    
namespace engine {
    
namespace desktop {
  
/**
 * \brief Represents a desktop touch event.
 */
class DesktopTouchEvent {
    /** The event type. One of: DOWN, MOVE, UP */
    int type;
    
    /** The pointer identifier. */
    int pointerId;
    
    /** The X coordinate*/
    float x;
    
    /** The Y coordinate. */
    float y;    
    
public:
    /** */
    enum {
        /** */
        NONE,
        
        /** */
        DOWN,
        
        /** */
        MOVE, 
        
        /** */
        UP
    };
    
    /**
     * \brief Constructs a DesktopTouchEvent.
     * \param type_ The event type.
     * \param pointerId_ The pointer identifier.
     * \param x_ The X coordinate.
     * \param y_ The Y coordinate.
     */
    DesktopTouchEvent():type(NONE),pointerId(-1),x(-1),y(-1) {
    }
    
    /**
     * \brief Gets the event type.
     * \return The event type.
     */
    int getType() const {
        return type;        
    }
    
    /**
     * \brief Gets the pointer identifier.
     * \return The pointer identifier.
     */
    int getPointerId() const {
        return pointerId;
    }
    
    /**
     * \brief Gets the X coordinate.
     * \return The X coordinate.
     */
    float getX() const {
        return x;
    }
    
    /**
     * \brief Gets the Y coordinate.
     * \return The Y coordinate.
     */
    float getY() const {
        return y;
    }
    
    /**
     * \brief Sets the event data.
     * \param type_ The event type.
     * \param pointerId_ The pointer identifier.
     * \param x_ The X coordinate.
     * \param y_ The Y coordinate.
     */
    void set(int type_,int pointerId_,float x_,float y_) {
        type = type_;
        pointerId = pointerId_;
        setCoords(x_,y_);
    }
    
    /**
     * \brief Sets the coordinates.
     * \param x_ The X coordinate.
     * \param y_ The Y coordinate.
     */
    void setCoords(float x_,float y_) {
        x = x_;
        y = y_;
    }
};
    
/**
 * \brief The touch handler for the desktop engine.
 */
class DesktopTouchHandler:public Error {
    /** */
    enum {
        /** The maximum number the handler can hold. */
        BUFFER_CAPACITY = 128        
    };
    
    /// The event buffer.
    std::vector<DesktopTouchEvent *> buffer;
    
    /// The index of the next 
    int bufferIndex;
    
    /**
     * \brief Initializes the event buffer.
     */
    void initBuffer();
    
    /**
     * \brief Gets the next free event.
     * \return The next event.
     */
    DesktopTouchEvent *nextEvent();
    
public:
    /**
     * Constructs a DesktopTouchHandler.
     */
    DesktopTouchHandler():buffer() {
        initBuffer();
    }
    
    /**
     * \brief Handles the down event.
     * \param pointerId The pointer identifier.
     * \param x The X coordinate.
     * \param y The Y coordinate
     */
    void down(int pointerId,float x,float y);
    
    /**
     * \brief Handles the move event.
     * \param pointerId The pointer identifier.
     * \param x The X coordinate.
     * \param y The Y coordinate
     */
    void move(int pointerId,float x,float y);
    
    /**
     * \brief Handles the up event.
     * \param pointerId The pointer identifier.
     * \param x The X coordinate.
     * \param y The Y coordinate
     */
    void up(int pointerId,float x,float y);
};
    
} // namespace

} // namespace

} // namespace

#endif // AE_DESKTOP_TOUCH_HANDLER_H