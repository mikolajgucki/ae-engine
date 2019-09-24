/*
-- @module Texture
-- @group GLES2
-- @brief Provides texture related functions.
-- @full Provides texture related functions. This module is a C library loaded
--   by default. <br/>It's more convenient to use ()[TextureLoader] to
--   load textures.
*/
#include <string>
#include <memory>

#include "Log.h"
#include "lua_common.h"
#include "luaImage.h"
#include "Texture.h"
#include "ImageTextureLoader.h"
#include "luaTexture.h"

using namespace std;
using namespace ae;
using namespace ae::lua;
using namespace ae::image;
using namespace ae::image::lua;

namespace ae {
    
namespace texture {
    
namespace lua {
    
/// The name of the Lua library and user type.
static const char *textureName = "Texture";
    
/// The name of the Lua metatable.
static const char *textureMetatableName = "Texture.metatable";

/// The name of the Lua global holding the texture factory.
static const char *textureGlobalTextureFactory = "ae_TextureFactory";

/// The name of the Lua global holding the texture loader.
static const char *textureGlobalTextureLoader = "ae_TextureLoader";

/// The log tag.
const char *const logTag = "lua.texture";

/** */
const char *getLuaTextureMetatableName() {
    return textureMetatableName;
}

/** */
TextureType *checkTextureType(lua_State *L,int index) {
    void *data = luaL_checkudata(L,index,textureMetatableName);
    luaL_argcheck(L,data != (void *)0,1,"Texture expected");
    return (TextureType *)data;
}

/**
 * \brief Gets the texture factory from the Lua state.
 * \param L The Lua state.
 * \return The texture loader.
 */
static TextureFactory *getTextureFactory(lua_State *L) {
    lua_getglobal(L,textureGlobalTextureFactory);
    TextureFactory *factory = (TextureFactory *)lua_touserdata(L,-1);
    lua_pop(L,1);
    return factory;    
}

/**
 * \brief Gets the texture loader from the Lua state.
 * \param L The Lua state.
 * \return The texture loader.
 */
static TextureLoader *getTextureLoader(lua_State *L) {
    lua_getglobal(L,textureGlobalTextureLoader);
    TextureLoader *loader = (TextureLoader *)lua_touserdata(L,-1);
    lua_pop(L,1);
    return loader;    
}

/**
 * \brief Creates a texture user type from a texture and push it onto the Lua
 *   stack.
 * \param L The Lua state.
 * \param texture The texture.
 */
static void createTextureUserType(lua_State *L,Texture *texture) {
// user type
    TextureType *data = (TextureType *)lua_newuserdata(L,sizeof(TextureType));
    data->texture = texture;
    
// metatable
    luaL_getmetatable(L,textureMetatableName);
    lua_setmetatable(L,-2);    
}

/*
-- @name .textureCreateFromImage
-- @func
-- @brief Creates a texture from an image.
-- @param id The texture identifier.
-- @param image The image from which to create the texture.
-- @return The texture.
*/
static int textureCreateFromImage(lua_State *L) {
    TextureFactory *factory = getTextureFactory(L);
    const char *id = luaL_checkstring(L,1);
    ImageType *image = checkImageType(L,2);
    
// create the texture
    Texture *texture = factory->createFromImage(string(id),image->image);
    if (texture == (Texture *)0) {        
        luaPushError(L,factory->getError().c_str());
        return 0;
    }
    
// user type
    createTextureUserType(L,texture);    
    
    return 1;
}

/*
-- @name .load
-- @func
-- @brief Loads a texture.
-- @full Loads a texture. <br/>It's more convenient to use ()[TextureLoader] to
--   load textures.
-- @param name The name of the file with the texture.
-- @return The loaded texture.
*/
static int textureLoad(lua_State *L) {
    TextureLoader *textureLoader = getTextureLoader(L);
    const char *filename = lua_tostring(L,1);
    
// load the texture
    Texture *texture = textureLoader->loadTexture(string(filename));
    if (texture == (Texture *)0) {
        luaPushError(L,textureLoader->getError().c_str());
        return 0;
    }    
    
// user type
    createTextureUserType(L,texture);
    
    return 1;
}

/*
-- @name :getWidth
-- @func
-- @brief Gets the texture width.
-- @return The texture width.
*/
static int textureGetWidth(lua_State *L) {
    TextureType *data = checkTextureType(L);
    lua_pushnumber(L,data->texture->getWidth());
    return 1;
}

/*
-- @name :getHeight
-- @func
-- @brief Gets the texture width.
-- @return The texture width.
*/
static int textureGetHeight(lua_State *L) {
    TextureType *data = checkTextureType(L);
    lua_pushnumber(L,data->texture->getHeight());
    return 1;
}

/*
-- @name :delete
-- @func
-- @brief Deletes the texture.
-- @full Deletes the texture. The texture cannot be used after it has been
--   deleted.
*/
static int textureDelete(lua_State *L) {
    TextureFactory *factory = getTextureFactory(L);
    TextureType *data = checkTextureType(L);
    
// check if already deleted
    if (data->texture == (Texture *)0) {
        Log::error(logTag,"Attempt to delete an already deleted texture");
        return 0;
    }
    
// delete
    if (factory->deleteTexture(data->texture) == false) {    
        luaPushError(L,factory->getError().c_str());
        return 0;
    }        
    
    data->texture = (Texture *)0;
    return 0;
}

/*
-- @name :__gc
-- @func
-- @brief Destroys the texture.
-- @full Destroys the texture. Never call this function directly!
*/
static int texture__gc(lua_State *L) {   
    TextureFactory *factory = getTextureFactory(L);
    TextureType *data = checkTextureType(L);
    
// check if already deleted
    if (data->texture == (Texture *)0) {
        return 0;
    }
    
// log
    ostringstream msg;
    msg << "Deleting texture " << data->texture->getId();
    Log::trace(logTag,msg.str());
    
// delete
    if (factory->deleteTexture(data->texture) == false) {
        ostringstream err;
        err << "Failed to delete texture " << data->texture->getId() << 
            ": " << factory->getError();
        Log::error(logTag,err.str());
        return 0;
    }        
    
    return 0;
}

// TODO Write __tostring metamethod.

/** The type functions. */
static const struct luaL_Reg textureFuncs[] = {
    {"createFromImage",textureCreateFromImage},
    {"load",textureLoad},
    {0,0}
};
    
/** The type methods. */
static const struct luaL_Reg textureMethods[] = {
    {"getWidth",textureGetWidth},
    {"getHeight",textureGetHeight},
    {"delete",textureDelete},
    {0,0}
};
    
/** The type meta-methods. */
static const struct luaL_Reg textureMetamethods[] = {
    {"__gc",texture__gc},
    {0,0}
};

/** */
static int textureRequireFunc(lua_State *L) {
    luaLoadUserType(L,textureMetatableName,textureFuncs,textureMethods,
        textureMetamethods);
    return 1;
}

/** */
static TextureLoader* createTextureLoader(ImageLoader *imageLoader,
    TextureFactory *textureFactory,Error *error) {
//
    TextureLoader *textureLoader = new (nothrow) ImageTextureLoader(
        imageLoader,textureFactory);
    if (textureLoader == (TextureLoader *)0) {
        error->setNoMemoryError();
        return (TextureLoader *)0;
    }
    
    return textureLoader;
}

/** */
bool loadTextureLib(lua_State *L,ImageLoader *imageLoader,
    TextureFactory *textureFactory,Error *error) {
// create texture loader
    TextureLoader *textureLoader = createTextureLoader(
        imageLoader,textureFactory,error);
    if (textureLoader == (TextureLoader *)0) {
        return false;
    }

// global with the texture factory
    lua_pushlightuserdata(L,textureFactory);
    lua_setglobal(L,textureGlobalTextureFactory);
    
// global with the texture loader
    lua_pushlightuserdata(L,textureLoader);
    lua_setglobal(L,textureGlobalTextureLoader);
    
// load the library
    luaL_requiref(L,textureName,textureRequireFunc,1);
    lua_pop(L,1);
    
    return true;
}

/** */
void unloadTextureLib(lua_State *L) {
// delete texture loader
    TextureLoader *textureLoader = getTextureLoader(L);
    if (textureLoader != (TextureLoader *)0) {
        delete textureLoader;
    }
}

} // namespace

} // namespace

} // namespace
