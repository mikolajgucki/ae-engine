#ifndef AE_SDL_LUA_ENGINE_LISTENER_H
#define AE_SDL_LUA_ENGINE_LISTENER_H

#include "LuaEnginePauseListener.h"
#include "LuaEngineQuitListener.h"

namespace ae {
    
namespace engine {
  
/**
 * \brief The Lua engine listener.
 */
class SDLLuaEngineListener:public LuaEngineQuitListener,
    public LuaEnginePauseListener {
//
    /// Indicates if to pause.
    bool pauseRequest;
    
    /// Indicates if to resume.
    bool resumeRequest;
    
    /// Indicates if to quit. 
    bool quitRequest;
    
public:
    /**
     * \brief Constructs a SDLLuaEngineListener.
     */
    SDLLuaEngineListener():LuaEngineQuitListener(),pauseRequest(false),
        resumeRequest(false),quitRequest(false) {
    }
    
    /** */
    virtual ~SDLLuaEngineListener() {
    }
    
    /** */
    virtual void luaEnginePause(LuaEngine *luaEngine);
    
    /** */
    virtual void luaEngineResume(LuaEngine *luaEngine);       
    
    /** */
    virtual void luaEngineQuit(LuaEngine *luaEngine);
    
    /**
     * \brief Gets the pause request.
     * \return <code>true</code> if to pause the engine loop,
     *   <code>false</code> otherwise.
     */
    bool getPauseRequest() {
        bool request = pauseRequest;
        pauseRequest = false;
        return request;
    }
   
    /**
     * \brief Gets the resumt request.
     * \return <code>true</code> if to resumt the engine loop,
     *   <code>false</code> otherwise.
     */
    bool getResumeRequest() {
        bool request = resumeRequest;
        resumeRequest = false;
        return request;
    }    
        
    /**
     * \brief Gets the quit request.
     * \return The quit request.
     */
    bool getQuitRequest() const {
        return quitRequest;
    }    
};
    
} // namespace
    
} // namespace

#endif // AE_SDL_LUA_ENGINE_LISTENER_H