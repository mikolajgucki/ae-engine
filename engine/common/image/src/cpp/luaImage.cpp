/*
-- @module Image
-- @group Graphics
-- @brief Provides image related functions.
-- @full Provides image related functions. This module is a C library loaded
--   by default.
*/
#include <string>

#include "lua_common.h"
#include "luaImage.h"

using namespace std;
using namespace ae::lua;

namespace ae {
    
namespace image {
    
namespace lua {
 
/// The name of the Lua library and user type.
static const char *imageName = "Image";
    
/// The name of the Lua metatable.
static const char *imageMetatableName = "Image.metatable";

/// The name of the Lua global holding the image loader.
static const char *imageGlobalImageLoader = "ae_ImageLoader";    
 
/// The name of the Lua global holding the image utility.
static const char *imageGlobalImageUtil = "ae_ImageUtil";

/** */
ImageType *checkImageType(lua_State *L,int index) {
    void *data = luaL_checkudata(L,index,imageMetatableName);
    luaL_argcheck(L,data != (void *)0,1,"Image expected");
    return (ImageType *)data;    
}

/** */
static bool checkDeleted(lua_State *L,ImageType *data) {
    if (data->image == (Image *)0) {
        luaPushError(L,"Image already deleted");
        return true;
    }
    
    return false;
}

/**
 * \brief Gets the image loader from the Lua state.
 * \param L The Lua state.
 * \return The image loader.
 */
static ImageLoader *getImageLoader(lua_State *L) {
    lua_getglobal(L,imageGlobalImageLoader);
    ImageLoader *loader = (ImageLoader *)lua_touserdata(L,-1);
    lua_pop(L,1);
    return loader;    
}

/**
 * \brief Gets the image utility from the Lua state.
 * \param L The Lua state.
 * \return The image utility.
 */
static ImageUtil *getImageUtil(lua_State *L) {
    lua_getglobal(L,imageGlobalImageUtil);
    ImageUtil *util = (ImageUtil *)lua_touserdata(L,-1);
    lua_pop(L,1);
    return util;    
}

/**
 * \brief Creates an image user type from an image and push it onto the Lua
 *   stack.
 * \param L The Lua state.
 * \param image The image.
 */
static void createImageUserType(lua_State *L,Image *image) {
// user type
    ImageType *data = (ImageType *)lua_newuserdata(L,sizeof(ImageType));
    data->image = image;
    
// metatable
    luaL_getmetatable(L,imageMetatableName);
    lua_setmetatable(L,-2);    
}

/*
-- @name .new
-- @func
-- @brief Creates an image.
-- @param width The image width.
-- @param height The image heigh.
*/
static int imageNew(lua_State *L) {
// size
    int width = (int)luaL_checknumber(L,1);
    int height = (int)luaL_checknumber(L,2);
    
// check size
    if (width <= 0 || height <= 0) {
        luaPushError(L,"Invalid image size");
        return 0;
    }        
    
// data
    size_t size = (size_t)(width * height * Image::BYTES_PER_PIXEL);
    unsigned char *data = new (nothrow) unsigned char[size];
    if (data == (unsigned char *)0) {
        luaPushNoMemoryError(L);
        return 0;
    }
    
// image
    Image *image = new (nothrow) Image(width,height,data);
    if (image == (Image *)0) {
        delete[] data;
        luaPushNoMemoryError(L);
        return 0;
    }
    
// user type
    createImageUserType(L,image);
    
    return 1;
}

/*
-- @name .load
-- @func
-- @brief Loads an image.
-- @param name The name of the file with the image.
-- @return The loaded image.
*/
static int imageLoad(lua_State *L) {
    ImageLoader *imageLoader = getImageLoader(L);
    const char *filename = lua_tostring(L,1);
    
// load the image
    Image *image = imageLoader->loadImage(string(filename));
    if (image == (Image *)0) {
        luaPushError(L,imageLoader->getError().c_str());
        return 0;
    }
    
// user type
    createImageUserType(L,image);
    
    return 1;
}

/*
-- @name :getWidth
-- @func
-- @brief Gets the image width in pixels.
-- @return The image width in pixels.
*/
static int imageGetWidth(lua_State *L) {
    ImageType *data = checkImageType(L);
    if (checkDeleted(L,data)) {
        return 0;
    }
    
    lua_pushnumber(L,data->image->getWidth());
    return 1;
}

/*
-- @name :getHeight
-- @func
-- @brief Gets the image height in pixels.
-- @return The image height in pixels.
*/
static int imageGetHeight(lua_State *L) {
    ImageType *data = checkImageType(L);
    if (checkDeleted(L,data)) {
        return 0;
    }
    
    lua_pushnumber(L,data->image->getHeight());
    return 1;
}

/*
-- @name :getAspect
-- @func
-- @brief Gets the height-to-width aspect of the image.
-- @return The height-to-width aspect.
*/
static int imageGetAspect(lua_State *L) {
    ImageType *data = checkImageType(L);
    if (checkDeleted(L,data)) {
        return 0;
    }
    
    double aspect = (double)data->image->getHeight() /
        (double)data->image->getWidth();
    lua_pushnumber(L,aspect);
    return 1;
}

/*
-- @name :put
-- @func
-- @brief Puts an image.
-- @full Puts an image (does not consider the alpha channel).
-- @func
-- @brief Puts an image.
-- @full Puts an image (does not consider the alpha channel).
-- @param src The source image.
-- @func
-- @brief Puts an image.
-- @full Puts an image (does not consider the alpha channel).
-- @param src The source image.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @func
-- @brief Puts an image.
-- @full Puts an image (does not consider the alpha channel).
-- @param src The source image.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @param xsrc The source X coordinate.
-- @param ysrc The source Y coordinate.
-- @func
-- @brief Puts an image.
-- @full Puts an image (does not consider the alpha channel).
-- @param src The source image.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @param xsrc The source X coordinate.
-- @param ysrc The source Y coordinate.
-- @param width The width.
-- @param height The height.
*/
static int imagePut(lua_State *L) {
    ImageUtil *util = getImageUtil(L);
    ImageType *data = checkImageType(L,1);
    if (checkDeleted(L,data)) {
        return 0;
    }
    
// coordinates
    int x = 0;
    if (lua_isnoneornil(L,2) == 0) {
        x = (int)luaL_checknumber(L,2);
    }
    int y = 0;
    if (lua_isnoneornil(L,3) == 0) {
        y = (int)luaL_checknumber(L,3);
    }
    
// source
    ImageType *src = checkImageType(L,4);
    if (checkDeleted(L,src)) {
        return 0;
    }
    int xsrc = 0;
    if (lua_isnoneornil(L,5) == 0) {
        xsrc = (int)luaL_checknumber(L,5);
    }
    int ysrc = 0;
    if (lua_isnoneornil(L,6) == 0) {
        ysrc = (int)luaL_checknumber(L,6);
    }
    
// width
    int width = src->image->getWidth();
    if (lua_isnoneornil(L,7) == 0) {
        width = (int)luaL_checknumber(L,7);
    }
    
// height
    int height = src->image->getHeight();
    if (lua_isnoneornil(L,8) == 0) {
        height = (int)luaL_checknumber(L,8);
    }
    
// put
    util->put(data->image,x,y,src->image,xsrc,ysrc,width,height);
    
    return 0;
}

/*
-- @name :draw
-- @func
-- @brief Draws an image.
-- @full Draws an image (considers the alpha channel).
-- @param src The source image.
-- @func
-- @brief Draws an image.
-- @full Draws an image (considers the alpha channel).
-- @param src The source image.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @func
-- @brief Draws an image.
-- @full Draws an image (considers the alpha channel).
-- @param src The source image.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @param xsrc The source X coordinate.
-- @param ysrc The source Y coordinate.
-- @func
-- @brief Draws an image.
-- @full Draws an image (considers the alpha channel).
-- @param src The source image.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @param xsrc The source X coordinate. 
-- @param ysrc The source Y coordinate.
-- @param width The width.
-- @param height The height.
*/
static int imageDraw(lua_State *L) {
    ImageUtil *util = getImageUtil(L);
    ImageType *data = checkImageType(L,1);
    if (checkDeleted(L,data)) {
        return 0;
    }
    
// coordinates
    int x = 0;
    if (lua_isnoneornil(L,2) == 0) {
        x = (int)luaL_checknumber(L,2);
    }
    int y = 0;
    if (lua_isnoneornil(L,3) == 0) {
        y = (int)luaL_checknumber(L,3);
    }
    
// source
    ImageType *src = checkImageType(L,4);
    if (checkDeleted(L,src)) {
        return 0;
    }
    int xsrc = 0;
    if (lua_isnoneornil(L,5) == 0) {
        xsrc = (int)luaL_checknumber(L,5);
    }
    int ysrc = 0;
    if (lua_isnoneornil(L,6) == 0) {
        ysrc = (int)luaL_checknumber(L,6);
    }
    
// width
    int width = src->image->getWidth();
    if (lua_isnoneornil(L,7) == 0) {
        width = (int)luaL_checknumber(L,7);
    }
    
// height
    int height = src->image->getHeight();
    if (lua_isnoneornil(L,8) == 0) {
        height = (int)luaL_checknumber(L,8);
    }
    
// draw
    util->draw(data->image,x,y,src->image,xsrc,ysrc,width,height);
    
    return 0;
}

/*
-- @name :scale
-- @func
-- @brief Scales the image.
-- @param width The width of the scaled image.
-- @param height The height of the scaled image.
-- @return The scaled image.
*/
static int imageScale(lua_State *L) {
    ImageUtil *util = getImageUtil(L);
    ImageType *data = checkImageType(L);
    if (checkDeleted(L,data)) {
        return 0;
    }
    
// width, height
    int width = (int)luaL_checknumber(L,2);
    int height = (int)luaL_checknumber(L,3);
    
    Image *dst = util->scale(data->image,width,height);
    if (dst == (Image *)0) {
        luaPushError(L,util->getError().c_str());
        return 0;
    }
    
// user type
    createImageUserType(L,dst);
    
    return 1;
}

/*
-- @name :delete
-- @func
-- @brief Deletes the image data.
-- @full Deletes the image data. The image is unusable after this operation. 
*/
static int imageDelete(lua_State *L) {
    ImageType *data = checkImageType(L);
    if (checkDeleted(L,data)) {
        return 0;
    }
    delete data->image;
    data->image = (Image *)0;
    
    return 0;
}

/*
-- @name :__gc
-- @func
-- @brief Destorys the image.
-- @full Destorys the image. Never call this function directly!
*/
static int image__gc(lua_State *L) {   
    ImageType *data = checkImageType(L);
    if (data->image != (Image *)0) {
        delete data->image;
    }
    
    return 0;
}

// TODO Write __tostring metamethod.

/** The type functions. */
static const struct luaL_Reg imageFuncs[] = {
    {"new",imageNew},
    {"load",imageLoad},
    {0,0}
};
    
/** The type methods. */
static const struct luaL_Reg imageMethods[] = {
    {"getWidth",imageGetWidth},
    {"getHeight",imageGetHeight},
    {"getAspect",imageGetAspect},
    {"put",imagePut},
    {"draw",imageDraw},
    {"scale",imageScale},
    {"delete",imageDelete},
    {0,0}
};
    
/** The type meta-methods. */
static const struct luaL_Reg imageMetamethods[] = {
    {"__gc",image__gc},
    {0,0}
};

/** */
static int imageRequireFunc(lua_State *L) {
    luaLoadUserType(L,imageMetatableName,imageFuncs,imageMethods,
        imageMetamethods);
    return 1;
}

/** */
void loadImageLib(lua_State *L,ImageLoader *imageLoader,
    ImageUtil *imageUtil) {
// global with the image loader
    lua_pushlightuserdata(L,imageLoader);
    lua_setglobal(L,imageGlobalImageLoader);
    
// global with the image utility
    lua_pushlightuserdata(L,imageUtil);
    lua_setglobal(L,imageGlobalImageUtil);
    
// load the library
    luaL_requiref(L,imageName,imageRequireFunc,1);
    lua_pop(L,1);
}
    
} // namespace
    
} // namespace

} // namespace   