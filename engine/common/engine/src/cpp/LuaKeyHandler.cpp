#include <memory>
#include "LuaKeyHandler.h"

using namespace std;

namespace ae {

namespace engine {

/** */
void LuaKeyHandler::createBuffer() {
// create array
    buffer = new (nothrow) KeyEvent*[BUFFER_CAPACITY];
    if (buffer == (KeyEvent **)0) {
        setNoMemoryError();
        return;
    }
    
// initialize with empty values
    for (int index = 0; index < BUFFER_CAPACITY; index++) {
        buffer[index] = (KeyEvent *)0;
    }
    
// allocate events
    for (int index = 0; index < BUFFER_CAPACITY; index++) {
        buffer[index] = new (nothrow) KeyEvent();
        if (buffer[index] == (KeyEvent *)0) {
            setNoMemoryError();
            return;
        }            
    }
}
    
/** */
LuaKeyHandler::~LuaKeyHandler() {
    if (buffer != (KeyEvent **)0) {
        for (int index = 0; index < BUFFER_CAPACITY; index++) {
            if (buffer[index] != (KeyEvent *)0) {
                delete buffer[index];
            }
        }
        
        delete[] buffer;
    }
}   

/** */
void LuaKeyHandler::pushEvent(int type,const Key &key) {
    KeyEvent *event = buffer[bufferIndex++];
    event->type = type;
    event->key = &key;
}

/** */
void LuaKeyHandler::keyDown(const Key &key) {
    pushEvent(EVENT_DOWN,key);
}

/** */
void LuaKeyHandler::keyUp(const Key &key) {
    pushEvent(EVENT_UP,key);
}

/** */
bool LuaKeyHandler::callLuaFunc(const Key *key) {
// push arguments
    lua_pushstring(L,key->getName());
    lua_pushinteger(L,key->getCode());
    
// call the function
    int callResult;
    if (luaPCall.tryCall(L,2,0,callResult) == false) {
        setError(luaPCall.getError());
        return false;
    }
    
    return true;
}

/** */
bool LuaKeyHandler::run() {
    for (int index = 0; index < bufferIndex; index++) {
        KeyEvent *event = buffer[index];
        
    // down
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
            
        // call the funtion
            if (callLuaFunc(event->key) == false) {
                bufferIndex = 0;
                return false;
            }
            continue;
        }
        
    // up
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
            if (callLuaFunc(event->key) == false) {
                bufferIndex = 0;
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