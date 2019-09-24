#ifndef AE_LUA_IMAGE_H
#define AE_LUA_IMAGE_H

#include "lua.hpp"
#include "Image.h"
#include "ImageLoader.h"
#include "ImageUtil.h"

namespace ae {
    
namespace image {
    
namespace lua {
    
/**
 * \brief Wraps the image so that it can be used as Lua user type.
 */
struct ImageType {
    /** */    
    Image *image;
};
typedef struct ImageType ImageType;
    
/**
 * \brief Checks wheter the object at given stack index is a user data of
 *     the image type. Raises error if the object is not of the type.
 * \param index The Lua stack index.
 * \return The user data of the image type. 
 */
ImageType *checkImageType(lua_State *L,int index = 1);

/**
 * \brief Loads the image library.
 * \param L The Lua state.
 * \param imageLoader The image loader.
 * \param imageUtil The image utility.
 */
void loadImageLib(lua_State *L,ImageLoader *imageLoader,ImageUtil *imageUtil);
    
} // namespace
    
} // namespace

} // namespace    
    
#endif // AE_LUA_IMAGE_H