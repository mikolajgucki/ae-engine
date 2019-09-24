#ifndef AE_TEXTURE_LUA_TEXTURE_H
#define AE_TEXTURE_LUA_TEXTURE_H

#include "lua.hpp"
#include "Error.h"
#include "ImageLoader.h"
#include "TextureFactory.h"
#include "TextureLoader.h"

namespace ae {
    
namespace texture {
    
namespace lua {

/**
 * \brief Wraps the texture so that it can be used as Lua user type.
 */
struct TextureType {
    /** */    
    Texture *texture;
};
typedef struct TextureType TextureType;
    
/**
 * \brief Gets the name of the texture metatable.
 * \return The metatable name.
 */
const char *getLuaTextureMetatableName();

/**
 * \brief Checks wheter the object at given stack index is a user data of
 *     the texture type. Raises error if the object is not of the type.
 * \param index The Lua stack index.
 * \return The user data of the texture type. 
 */
TextureType *checkTextureType(lua_State *L,int index = 1);
    
/**
 * \brief Loads the texture library.
 * \param L The Lua state.
 * \param imageLoader The imager loader.
 * \param textureFactory The texture factory.
 * \param error The error set when loading fails.
 * \return <code>true</code> on success, <code>false</code> otherwise.
 */
bool loadTextureLib(lua_State *L,::ae::image::ImageLoader *imageLoader,
    TextureFactory *textureFactory,Error *error);
    
/**
 * \brief Unloads the texture library.
 * \param L The Lua state.
 */
void unloadTextureLib(lua_State *L);

} // namespace

} // namespace

} // namespace

#endif // AE_TEXTURE_LUA_TEXTURE_H