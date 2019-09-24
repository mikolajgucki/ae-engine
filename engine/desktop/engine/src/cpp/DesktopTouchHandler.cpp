#include <memory>
#include "DesktopTouchHandler.h"

using namespace std;

namespace ae {
    
namespace engine {
    
namespace desktop {

/** */
void DesktopTouchHandler::initBuffer() {
    for (int index = 0; index < BUFFER_CAPACITY; index++) {
        buffer[index] = new (nothrow) DesktopTouchEvent();
        if (buffer[index] == (DesktopTouchEvent *)0) {
            setNoMemoryError();
            return;
        }
    }
}

/** */
DesktopTouchEvent *DesktopTouchHandler::nextEvent() {
    return buffer[bufferIndex++];
}
    
/** */
void DesktopTouchHandler::down(int pointerId,float x,float y) {
    DesktopTouchEvent *event = nextEvent();
    event->set(DesktopTouchEvent::DOWN,pointerId,x,y);
}

/** */
void DesktopTouchHandler::move(int pointerId,float x,float y) {
    for (int index = bufferIndex - 1; index >= 0; index--) {
        DesktopTouchEvent *event = buffer[index];
        if (event->getType() == DesktopTouchEvent::MOVE &&
            event->getPointerId() == pointerId) {
        //
            event->setCoords(x,y);
            return;
        }
    }
    
    DesktopTouchEvent *event = nextEvent();
    event->set(DesktopTouchEvent::MOVE,pointerId,x,y);
}

/** */
void DesktopTouchHandler::up(int pointerId,float x,float y) {
    DesktopTouchEvent *event = nextEvent();
    event->set(DesktopTouchEvent::UP,pointerId,x,y);
}
    
} // namespace

} // namespace

} // namespace