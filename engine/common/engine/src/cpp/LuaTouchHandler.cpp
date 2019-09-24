/*
-- @module ae
*/

#include <sstream>
#include <memory>

#include "LuaTouchHandler.h"

using namespace std;

namespace ae {
    
namespace engine {
   
/** */
void LuaTouchHandler::createBuffer() {
// create array
    buffer = new (nothrow) TouchEvent*[BUFFER_CAPACITY];
    if (buffer == (TouchEvent **)0) {
        setNoMemoryError();
        return;
    }
    
// initialize with empty values
    for (int index = 0; index < BUFFER_CAPACITY; index++) {
        buffer[index] = (TouchEvent *)0;
    }
    
// allocate events
    for (int index = 0; index < BUFFER_CAPACITY; index++) {
        buffer[index] = new (nothrow) TouchEvent();
        if (buffer[index] == (TouchEvent *)0) {
            setNoMemoryError();
            return;
        }
    }
}

LuaTouchHandler::~LuaTouchHandler() {
    if (buffer != (TouchEvent **)0) {
        for (int index = 0; index < BUFFER_CAPACITY; index++) {
            if (buffer[index] != (TouchEvent *)0) {
                delete buffer[index];
            }
        }
        
        delete[] buffer;
    }
}

/** */
void LuaTouchHandler::pushEvent(int type,int pointerId,float x,float y) {
    TouchEvent *event = buffer[bufferIndex++];
    event->type = type;
    event->pointerId = pointerId;
    event->x = x;
    event->y = y;
}

/** */
void LuaTouchHandler::touchDown(int pointerId,float x,float y) {
    pushEvent(EVENT_DOWN,pointerId,x,y);
}
    
/** */
void LuaTouchHandler::touchMove(int pointerId,float x,float y) {
// look for a previous move of the pointer...
    for (signed int index = bufferIndex - 1; index >= 0; index--) {
        TouchEvent *event = buffer[index];
        
    // ...and update the event with new coordinates not to pass every move
    // event of the same pointer to Lua which are generated between frames 
        if (event->type == EVENT_MOVE && event->pointerId == pointerId) {
            event->x = x;
            event->y = y;
        }
    }
    
    pushEvent(EVENT_MOVE,pointerId,x,y);
}

/** */
void LuaTouchHandler::touchUp(int pointerId,float x,float y) {
    pushEvent(EVENT_UP,pointerId,x,y);
}

/** */
bool LuaTouchHandler::callLuaFunc(int pointerId,float x,float y) {
// push arguments
    lua_pushnumber(L,pointerId);
    lua_pushnumber(L,x);
    lua_pushnumber(L,y);
    
// call the function
    int callResult;
    if (luaPCall.tryCall(L,3,0,callResult) == false) {
        setError(luaPCall.getError());
        return false;
    }
    
    return true;
}

/** */
bool LuaTouchHandler::run() {
    for (int index = 0; index < bufferIndex; index++) {
        TouchEvent *event = buffer[index];
        
/*
-- @name .touch.down
-- @func
-- @brief Invoked when on touch down event.
-- @full  Invoked when on touch down event. The pointer identifier identifies
--   subsequent move and up events related to this pointer. 
-- @param pointerId The pointer identifier.
-- @param x The X coordinate of the event (given in display coordinates).
-- @param y The Y coordinate of the event (given in display coordinates).
*/
        if (event->type == EVENT_DOWN) {                
        // do nothing if there is no such function
            if (luaGetDownFunc.isNil() == true) {
                continue;
            }
            
        // push error handler to get traceback on error
            luaPCall.pushErrorHandler(L);  
            
        // get the function
            if (luaGetDownFunc.run() == false) {
                setError(luaGetDownFunc.getError());
                bufferIndex = 0;
                return false;
            }
            
        // call the function
            if (callLuaFunc(event->pointerId,event->x,event->y) == false) {
                bufferIndex = 0;
                return false;
            }
            continue;
        }
        
/*
-- @name .touch.move
-- @func
-- @brief Invoked when on touch move event.
-- @param pointerId The pointer identifier.
-- @param x The X coordinate of the event (given in display coordinates).
-- @param y The Y coordinate of the event (given in display coordinates).
*/
        if (event->type == EVENT_MOVE) {
        // do nothing if there is no such function
            if (luaGetMoveFunc.isNil() == true) {
                continue;
            }
            
        // push error handler to get traceback on error
            luaPCall.pushErrorHandler(L);  
            
        // get the function
            if (luaGetMoveFunc.run() == false) {
                setError(luaGetMoveFunc.getError());
                bufferIndex = 0;
                return false;
            }
            
        // call the function
            if (callLuaFunc(event->pointerId,event->x,event->y) == false) {
                bufferIndex = 0;
                return false;
            }           
            continue;
        }
        
/*
-- @name .touch.up
-- @func
-- @brief Invoked when on touch up event.
-- @param pointerId The pointer identifier.
-- @param x The X coordinate of the event (given in display coordinates).
-- @param y The Y coordinate of the event (given in display coordinates).
*/
        if (event->type == EVENT_UP) {
        // do nothing if there is no such function
            if (luaGetUpFunc.isNil() == true) {
                continue;
            }            
            
        // push error handler to get traceback on error
            luaPCall.pushErrorHandler(L);  
            
        // get the function
            if (luaGetUpFunc.run() == false) {
                setError(luaGetUpFunc.getError());
                bufferIndex = 0;
                return false;
            }
            
        // call the function
            if (callLuaFunc(event->pointerId,event->x,event->y) == false) {
                return false;
            }            
            continue;
        }
    }
    
    bufferIndex = 0;    
    return true;
}

} // namespace
    
} // namespace
