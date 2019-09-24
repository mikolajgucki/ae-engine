#ifndef AE_ENGINE_LUA_KEY_HANDLER_H
#define AE_ENGINE_LUA_KEY_HANDLER_H

#include "lua.hpp"

#include "LuaGetField.h"
#include "LuaPCall.h"
#include "EngineRequest.h"
#include "Key.h"
#include "KeyListener.h"

namespace ae {
    
namespace engine {
  
/**
 * \brief Passes the key events to Lua.
 */
class LuaKeyHandler:public EngineRequest,public KeyListener {
    /** */
    enum {
        /** */
        EVENT_DOWN = 0,
        
        /** */
        EVENT_UP = 1,
        
        /** */
        BUFFER_CAPACITY = 128        
    };
    
    /** */
    class KeyEvent {
    public:
        /** */
        int type;
        
        /** */
        const Key *key;
        
        /** */
        KeyEvent():type(-1),key((const Key *)0) {
        }
    };
    
    /// The log tag.
    static const char *logTag;
    
    /// The Lua state.
    lua_State *L;
    
    /// The buffer for the events.
    KeyEvent **buffer;
    
    /// The current buffer index.
    int bufferIndex;
    
    /// Calls Lua in protected mode.
    ae::lua::LuaPCall luaPCall;    
    
    /// Gets the Lua key pressed function.
    ae::lua::LuaGetField luaGetDownFunc;
    
    /// Gets the Lua key released function.
    ae::lua::LuaGetField luaGetUpFunc;
    
    /**
     * \brief Creates the event buffer.
     */
    void createBuffer();
    
    /**
     * \brief Pushes the next event into the buffer.
     * \param type The event type.
     * \param key The key.
     */
    void pushEvent(int type,const Key &key);    
    
    /**
     * \brief Calls the Lua function (already on stack) which handles an event.
     * \param key The key to pass to the function.
     */
    bool callLuaFunc(const Key *key);
    
public:
    /**
     * \brief Constructs a LuaKeyListener.
     * \param L_ The Lua state.
     */
    LuaKeyHandler(lua_State *L_):EngineRequest(false),KeyListener(),
        L(L_),buffer((KeyEvent **)0),bufferIndex(0),luaPCall(),
        luaGetDownFunc(L_,"ae","key","down"), 
        luaGetUpFunc(L_,"ae","key","up") {
    //
        createBuffer();
    }
    
    /** */
    ~LuaKeyHandler();
    
    /**
     * \brief Sets the Lua state.
     * \param L_ The Lua state.
     */
    void setLuaState(lua_State *L_) {
        L = L_;
        luaGetDownFunc.setLuaState(L_);
        luaGetUpFunc.setLuaState(L_);
    }    
    
    /** */
    virtual void keyDown(const Key &key);
    
    /** */
    virtual void keyUp(const Key &key);
    
    /**
     * \brief Invoked when a key has been pressed and released.
     * \param key The key.
     */
    virtual void keyDownAndUp(const Key &key) {
        keyDown(key);
        keyUp(key);
    }
    
    /**
     * \brief Passes the buffered events to Lua.
     */
    virtual bool run();    
};
    
} // namespace
    
} // namespace

#endif // AE_ENGINE_LUA_KEY_HANDLER_H