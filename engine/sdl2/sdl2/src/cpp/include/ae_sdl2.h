#ifndef AE_SDL2_H
#define AE_SDL2_H

#include "SDL.h"
#include "LuaExtraLib.h"
#include "LuaEnginePlugin.h"

/**
 * \brief Initializes the engine in the debug mode.
 */
void aeInitDebug();

/**
 * \brief Indicates if the engine is running the debug mode.
 * \return true if running in debug mode, false otherwise.
 */
bool aeIsDebug();

/**
 * \brief Initializes the logger. Must be called after aeInitDebug() and before
 *   ae_sdl_main().
 */
void aeInitLogger();

/**
 * \brief Sets the storage directory.
 * \param storageDir_ The storage directory.
 */
void aeSetStorageDir(const char *storageDir_);

/**
 * \brief Gets the SDL window.
 * \return The SDL window.
 */
SDL_Window *aeGetSDLWindow();

/**
 * \brief Adds a Lua extra library loaded on Lua engine start.
 * \param lib The library.
 */
void aeAddLuaExtraLib(::ae::engine::LuaExtraLib *lib);

/**
 * \brief Adds a Lua engine plugin.
 * \param plugin The plugin.
 */
void aeAddLuaEnginePlugin(::ae::engine::LuaEnginePlugin *plugin);

/**
 * \brief The entry point to AE Engine.
 * \param argc The argument count.
 * \param argv The arguments.
 */
int ae_sdl_main(int argc,char *argv[]);

#endif // AE_SDL2_H