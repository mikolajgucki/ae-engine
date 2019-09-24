#ifndef AE_ENGINE_LUA_ENGINE_QUIT_LISTENER_H
#define AE_ENGINE_LUA_ENGINE_QUIT_LISTENER_H

namespace ae {
    
namespace engine {
  
// Forward declaration.
class LuaEngine;
    
/**
 * \brief The superclass for a Lua engine quit listener.
 */
class LuaEngineQuitListener {
public:
    /** */
    virtual ~LuaEngineQuitListener() {
    }
    
    /**
     * \brief Called on quit request coming from the Lua engine.
     * \param luaEngine The Lua engine from which the request comes.
     */
    virtual void luaEngineQuit(LuaEngine *luaEngine) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_ENGINE_LUA_ENGINE_QUIT_LISTENER_H