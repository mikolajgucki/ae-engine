#ifndef AE_LUA_ANDROID_H
#define AE_LUA_ANDROID_H

#include "DLog.h"
#include "Error.h"
#include "LuaEngine.h"

namespace ae {

namespace android {
    
namespace lua {

/**
 * \brief Loads the Android library.
 * \param log The log.
 * \param luaEngine The Lua engine.
 * \param error The error to set if loading fails. 
 */
void loadAndroidLib(DLog *log,::ae::engine::LuaEngine *luaEngine,Error *error);

/**
 * \brief Unloads the Android library.
 * \param log The log.
 */
void unloadAndroidLib(DLog *log);    
    
} // namespace

} // namespace

} // namespace

#endif // AE_LUA_ANDROID_H