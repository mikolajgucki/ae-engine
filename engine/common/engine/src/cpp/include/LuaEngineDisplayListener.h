#ifndef AE_LUA_ENGINE_DISPLAY_LISTENER_H
#define AE_LUA_ENGINE_DISPLAY_LISTENER_H

namespace ae {
    
namespace engine {
    
/**
 * \brief The superclass for a Lua engine display listener.
 */
class LuaEngineDisplayListener {
public:
    /** */
    virtual ~LuaEngineDisplayListener() {
    }
    
    /**
     * \brief Called when the display has been resized.
     * \param width The display width.
     * \param height The display height.
     */
    virtual void luaEngineDisplayResized(int width,int height) = 0;
};

} // namespace
    
} // namespace    

#endif // AE_LUA_ENGINE_DISPLAY_LISTENER_H