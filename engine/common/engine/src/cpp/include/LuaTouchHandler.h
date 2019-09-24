#ifndef AE_ENGINE_LUA_TOUCH_MODEL_H
#define AE_ENGINE_LUA_TOUCH_MODEL_H

#include "lua.hpp"

#include "LuaGetField.h"
#include "LuaPCall.h"
#include "EngineRequest.h"
#include "TouchListener.h"

namespace ae {
    
namespace engine {
    
/**
 * \brief Passes the touch events to Lua.
 */
class LuaTouchHandler:public EngineRequest,public TouchListener {
    /** */
    enum {
        /** */
        EVENT_NONE = 0,
        
        /** */
        EVENT_DOWN = 1,
        
        /** */
        EVENT_MOVE = 2,
        
        /** */
        EVENT_UP = 3,
        
        /** */
        BUFFER_CAPACITY = 128
    };
    
    /** */
    class TouchEvent {
    public:
        /** */
        int type;
        
        /** */
        int pointerId;
        
        /** */
        float x;
        
        /** */
        float y;
        
        /** */
        TouchEvent():type(-1),pointerId(-1),x(-1),y(-1) {
        }
    };
    
    /// The Lua state.
    lua_State *L;
    
    /// The buffer for the events.
    TouchEvent** buffer;
    
    /// The current buffer index.
    int bufferIndex;
    
    /// Calls Lua in protected mode.
    ae::lua::LuaPCall luaPCall;
    
    /// Gets the Lua touch down function.
    ae::lua::LuaGetField luaGetDownFunc;
    
    /// Gets the Lua touch move function.
    ae::lua::LuaGetField luaGetMoveFunc;
    
    /// Gets the Lua touch up function.
    ae::lua::LuaGetField luaGetUpFunc;
    
    /**
     * \brief Creates the event buffer.
     */
    void createBuffer();
    
    /**
     * \brief Pushes the next event into the buffer.
     * \param type The event type.
     * \param pointer The pointer identifier.
     * \param x The X-coordinate.
     * \param y The Y-coordinate.
     */
    void pushEvent(int type,int pointer,float x,float y);
    
    /**
     * \brief Calls the Lua function (already on stack) which handles an event.
     * \param pointer The pointer identifier.
     * \param x The X-coordinate.
     * \param y The Y-coordinate.
     * \return <code>true</code> if the call is successful, <code>false</code>
     *     otherwise.     
     */
    bool callLuaFunc(int pointer,float x,float y);
    
public:
    /**
     * \brief Constructs a LuaTouchHandler.
     * \param L_ The Lua state.
     */
    LuaTouchHandler(lua_State *L_):EngineRequest(false),TouchListener(),
        L(L_),buffer((TouchEvent **)0),bufferIndex(0),luaPCall(),
        luaGetDownFunc(L_,"ae","touch","down"),
        luaGetMoveFunc(L_,"ae","touch","move"),
        luaGetUpFunc(L_,"ae","touch","up") {
    //
        createBuffer();
    }
    
    /** */
    ~LuaTouchHandler();
    
    /**
     * \brief Sets the Lua state.
     * \param L_ The Lua state.
     */
    void setLuaState(lua_State *L_) {
        L = L_;
        luaGetDownFunc.setLuaState(L_);
        luaGetMoveFunc.setLuaState(L_);
        luaGetUpFunc.setLuaState(L_);
    }
    
    /**
     * \brief Invoked on touch down event.
     * \param pointerId The identifier of the pointer.
     * \param x The X-coordinate relative to the display.
     * \param y The Y-coordinate relative to the display.
     */
    virtual void touchDown(int pointerId,float x,float y);
    
    /**
     * \brief Invoked on touch move event.
     * \param pointerId The identifier of the pointer.
     * \param x The X-coordinate relative to the display.
     * \param y The Y-coordinate relative to the display.
     */
    virtual void touchMove(int pointerId,float x,float y);

    /**
     * \brief Invoked on touch down up.
     * \param pointerId The identifier of the pointer.
     * \param x The X-coordinate relative to the display.
     * \param y The Y-coordinate relative to the display.
     */
    virtual void touchUp(int pointerId,float x,float y);

    /**
     * \brief Passes the buffered events to Lua.
     */
    virtual bool run();
};
    
} // namespace
    
} // namespace

#endif // AE_ENGINE_LUA_TOUCH_MODEL_H