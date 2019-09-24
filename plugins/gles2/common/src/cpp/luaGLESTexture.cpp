/*
-- @module Texture
*/
#include "ae_defs.h"
#include "lua_common.h"
#include "luaTexture.h"
#include "GLESTexture.h"
#include "luaGLESTexture.h"

using namespace ae::lua;
using namespace ae::texture::lua;

namespace ae {
    
namespace gles2 {
    
namespace lua {
    
/*
-- @name :bind
-- @func
-- @brief Binds this texture to the texture target.
*/
static int gles2TextureBind(lua_State *L) {
    TextureType *data = checkTextureType(L);    
    
// cast to GLES texture
#ifndef AE_ANDROID
    GLESTexture *glesTexture = dynamic_cast<GLESTexture *>(data->texture);
    if (glesTexture == (GLESTexture *)0) {
        luaPushError(L,"Not a GLES texture");
        return 0;
    }        
#else
    GLESTexture *glesTexture = (GLESTexture *)data->texture;
#endif

// bind
    if (glesTexture->bind() == false) {
        luaPushError(L,data->texture->getError().c_str());
        return 0;
    }        
    
    return 0;
}

/** */
static const struct luaL_Reg gles2TextureFuncs[] = {
    {"bind",gles2TextureBind},
    {0,0}
};

/** */
void loadGLESTextureLib(lua_State *L) {
    addMethodsToUserType(L,getLuaTextureMetatableName(),gles2TextureFuncs);
}

} // namespace

} // namespace

} // namespace
