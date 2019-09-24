#ifndef AE_DESKTOP_ENGINE_LISTENER_H
#define AE_DESKTOP_ENGINE_LISTENER_H

#include "lua.hpp"
#include "Engine.h"

namespace ae {
    
namespace engine {
    
namespace desktop {
    
/**
 * \brief The superclass for a desktop engine listener.
 */
class DesktopEngineListener {
public:
    /**
     * \brief Called when the Lua state has been created (after start and
     *   restart).
     * \param L The Lua state.
     */
    virtual void luaStateCreated(lua_State *L) = 0;
    
    /**
     * \brief Called when the engine has been created.
     * \param engine The engine.
     */
    virtual void engineCreated(Engine *engine) = 0;
    
    /**
     * \brief Called when the engine is about to enter the loop.
     */
    virtual void willEnterLoop() = 0;
    
    /**
     * \brief Called when a frame has been rendered
     */
    virtual void frameRendered() = 0;
    
    /**
     * \brief Called when the engine is stopping.
     */
    virtual void stopping() = 0;
    
    /**
     * \brief Called when the engine has been paused.
     */
    virtual void paused() = 0;
    
    /**
     * \brief Called when the engine has been resumed.
     */
    virtual void resumed() = 0;
    
    /**
     * \brief Called when the engine is restarting.
     */
    virtual void restarting() = 0;
    
    /**
     * \brief Called when the audio volume factor has been set.
     * \param volumeFactor The new volume factor.
     */
    virtual void volumeFactorSet(double volumeFactor) = 0;
};
    
} // namespace
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_ENGINE_LISTENER_H