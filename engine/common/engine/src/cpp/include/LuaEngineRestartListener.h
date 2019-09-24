#ifndef AE_ENGINE_LUE_ENGINE_RESTART_LISTENER_H
#define AE_ENGINE_LUE_ENGINE_RESTART_LISTENER_H

namespace ae {
    
namespace engine {
  
// Forward declaration.
class LuaEngine;
    
/**
 * \brief The superclass for a Lua engine restart listener.
 */
class LuaEngineRestartListener {
public:
    /** */
    virtual ~LuaEngineRestartListener() {
    }
    
    /**
     * \brief Invoked when the Lua engine is about to restart.
     * \param luaEngine The Lua engine being restarted.
     */
    virtual void luaEngineRestarting(LuaEngine *luaEngine) = 0;
    
    /**
     * \brief Invoked when the Lua engine has been restarted.
     * \param luaEngine The Lua engine which just restarted.
     */
    virtual void luaEngineRestarted(LuaEngine *luaEngine) = 0;
    
    /**
     * \brief Invoked when the Lua engine has failed to restart.
     * \param luaEngine The Lua engine which just restarted.
     */
    virtual void luaEngineRestartFailed(LuaEngine *luaEngine) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_ENGINE_LUE_ENGINE_RESTART_LISTENER_H