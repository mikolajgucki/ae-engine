#ifndef AE_DESKTOP_LUA_ENGINE_LISTENER_H
#define AE_DESKTOP_LUA_ENGINE_LISTENER_H

#include <vector>

#include "System.h"
#include "LuaTouchHandler.h"
#include "LuaKeyHandler.h"
#include "LuaEngineRestartListener.h"
#include "LuaEnginePauseListener.h"
#include "LuaEngineQuitListener.h"
#include "CachedPNGImageLoader.h"
#include "DesktopEngineListener.h"

namespace ae {
    
namespace engine {
    
namespace desktop {
  
/**
 * \brief The Lua engine listener.
 */
class DesktopLuaEngineListener:public LuaEngineRestartListener,
    public LuaEngineQuitListener,public LuaEnginePauseListener {
//
    /// The image loaders.
    std::vector<ae::image::CachedPNGImageLoader *> imageLoaders;
    
    /// The Lua touch handler.
    ae::engine::LuaTouchHandler *luaTouchHandler;
    
    /// The Lua key handler.
    ae::engine::LuaKeyHandler *luaKeyHandler;
    
    /// The desktop engine listener.
    DesktopEngineListener *desktopEngineListener;
    
    /// The system-related methods.
    ae::system::System sys;
    
    /// The time at which the restart started.
    unsigned long restartStartTime;
    
    /// Indicates if to pause.
    bool pauseRequest;
    
    /// Indicates if to resume.
    bool resumeRequest;
    
    /// Indicates if to quit.
    bool quitRequest;
    
public:
    /** */
    DesktopLuaEngineListener(
        std::vector<ae::image::CachedPNGImageLoader *> imageLoaders_,
        ae::engine::LuaTouchHandler *luaTouchHandler_,
        ae::engine::LuaKeyHandler *luaKeyHandler_,
        DesktopEngineListener *desktopEngineListener_):
        imageLoaders(imageLoaders_),luaTouchHandler(luaTouchHandler_),
        luaKeyHandler(luaKeyHandler_),
        desktopEngineListener(desktopEngineListener_),sys(),
        pauseRequest(false),resumeRequest(false),quitRequest(false) {
    }
    
    /** */
    virtual ~DesktopLuaEngineListener() {
    }
    
    /** */
    virtual void luaEngineRestarting(LuaEngine *luaEngine);
    
    /** */
    virtual void luaEngineRestarted(LuaEngine *luaEngine);
    
    /** */
    virtual void luaEngineRestartFailed(LuaEngine *luaEngine);
    
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
     * \brief Gets the resume request.
     * \return <code>true</code> if to resume the engine loop,
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

} // namespace  

#endif // AE_DESKTOP_LUA_ENGINE_LISTENER_H