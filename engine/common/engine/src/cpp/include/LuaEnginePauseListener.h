#ifndef AE_LUA_ENGINE_PAUSE_LISTENER_H
#define AE_LUA_ENGINE_PAUSE_LISTENER_H  

namespace ae {
    
namespace engine {
    
// Forward declaration.
class LuaEngine;
    
/**
 * \brief The superclass for a Lua engine pause/resume listener.
 */
class LuaEnginePauseListener {
public:
    /** */
    virtual ~LuaEnginePauseListener() {
    }
    
    /**
     * \brief Called on pause request coming from the Lua engine.
     * \param luaEngine The Lua engine from which the request comes.
     */
    virtual void luaEnginePause(LuaEngine *luaEngine) = 0;
    
    /**
     * \brief Called on resume request coming from the Lua engine.
     * \param luaEngine The Lua engine from which the request comes.
     */
    virtual void luaEngineResume(LuaEngine *luaEngine) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_ENGINE_PAUSE_LISTENER_H
