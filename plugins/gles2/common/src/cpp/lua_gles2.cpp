/*
-- @module gl
-- @group GLES2
-- @brief OpenGL ES 2.0 module which wraps the basic functions.
-- @full OpenGL ES 2.0 module which wraps the basic functions.  This module\
--   is a C library which is loaded by default.
*/
#include "lua_common.h"
#include "ae_GLES2.h"
#include "lua_glerror.h"
#include "luaVertexBufferData.h"
#include "lua_gles2.h"

using namespace ae::lua;

namespace ae {
 
namespace gles2 {
    
namespace lua {
    
/// The library name.
static const char *gles2Name = "gl";
 
/// The log
static DLog *log = (DLog *)0;

/*
-- @name .init
-- @func
-- @brief Initializes the OpenGL ES 2.0 library. Must be called before any
--   other GL calls.
*/
static int gles2Initialize(lua_State *L) {
    glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
    return 0;
}

/*
-- @name .clearColor
-- @func
-- @brief Sets the clear color.
-- @param red The red component.
-- @param green The green component.
-- @param blue The blue component.
-- @param alpha The alpha component.
*/
static int gles2ClearColor(lua_State *L) {
    GLclampf r = (GLclampf)luaL_checknumber(L,1);
    GLclampf g = (GLclampf)luaL_checknumber(L,2);
    GLclampf b = (GLclampf)luaL_checknumber(L,3);
    GLclampf a = (GLclampf)luaL_checknumber(L,4);
    
    glClearColor(r,g,b,a);
    return 0;
}

/*
-- @name .clear
-- @func
-- @brief Clears the color buffer.
*/
static int gles2Clear(lua_State *L) {
    glClear(GL_COLOR_BUFFER_BIT);
    return 0;
}

/*
-- @name .enableBlend
-- @func
-- @brief Enables blending.
-- @full Enables blending by calling `glEnable(GL_BLEND)`.
*/
static int gles2EnableBlend(lua_State *L) {
    glEnable(GL_BLEND);
    return 0;
}

/*
-- @name .disableBlend
-- @func
-- @brief Disables blending.
-- @full Disables blending by calling `glDisable(GL_BLEND)`.
*/
static int gles2DisableBlend(lua_State *L) {
    glDisable(GL_BLEND);
    return 0;
}

/*
-- @name .setBlendEnabled
-- @func
-- @brief Enables or disables blending.
-- @param enabled `true` to enable, `false` to disable.
*/
static int gles2SetBlendEnabled(lua_State *L) {
    if (lua_toboolean(L,1)) {
        glEnable(GL_BLEND);
    }
    else {
        glDisable(GL_BLEND);
    }
    
    return 0;
}

/*
-- @name .setDepthTestEnabled
-- @func
-- @brief Enables or disables depth test.
-- @param enabled `true` to enable, `false` to disable.
*/
static int gles2SetDepthTestEnabled(lua_State *L) {
    if (lua_toboolean(L,1)) {
        glEnable(GL_DEPTH_TEST);
        glDepthMask(GL_TRUE);        
    }
    else {
        glDisable(GL_DEPTH_TEST);
        glDepthMask(GL_FALSE);        
    }
    
    return 0;    
}

/*
-- @name .isShader
-- @func
-- @brief Tests if a GL name corresponds to a shader.
-- @param name The GL name.
-- @return `true` if the name corresponds to a shader, `false` otherwise.
*/
static int gles2IsShader(lua_State *L) {
    GLuint name = (GLuint)luaL_checknumber(L,1);
    
    if (glIsShader(name) == GL_TRUE) {
        lua_pushboolean(L,AE_LUA_TRUE);
    }
    else {
        lua_pushboolean(L,AE_LUA_FALSE);
    }
    
    return 1;
}

/*
-- @name .viewport
-- @func
-- @brief Sets the viewport.
-- @param x The X coordinate (in display coordinates) of the lower left corner.
-- @param y The Y coordinate (in display coordinated) of the lower left corner.
-- @param width The viewport width in pixels.
-- @param height The viewport height in pixels.
*/
static int gles2Viewport(lua_State *L) {
    GLint x = (GLint)luaL_checknumber(L,1);
    GLint y = (GLint)luaL_checknumber(L,2);
    GLsizei width = (GLsizei)luaL_checknumber(L,3);
    GLsizei height = (GLsizei)luaL_checknumber(L,4);

    glViewport(x,y,width,height);
    return 0;
}

/*
-- @name .drawArrays
-- @func
-- @brief Renders primitives from array data.
-- @param mode What kind of primitives to render (`gl.GL_TRIANGLES` accepted
--   only at the moment).
-- @param count The number of indices to render.
-- @param first The starting index in the array.
-- @full Renders primitives from array data by calling `glDrawArrays()`.
*/
static int gles2DrawArrays(lua_State *L) {
    GLenum mode = (GLenum)luaL_checknumber(L,1);
    GLint count = (GLint)luaL_checknumber(L,2);    
    GLsizei first = (GLsizei)luaL_checknumber(L,3);
    
    glDrawArrays(mode,first,count);     
    if (luaChkGLError(L)) {
        return 0;
    }        
    
    return 0;    
}

/*
-- @name .drawElements
-- @func
-- @brief Renders primitives from vertex buffer object containing the indices.
-- @param mode What kind of primitives to render (`gl.GL_TRIANGLES` accepted
--   only at the moment).
-- @param count The number of elements to render.
-- @param type The type of the index values.
-- @full Renders primitives from array data by calling `glDrawElements()`.
-- @func
-- @brief Renders primitives from array data.
-- @param mode What kind of primitives to render (`gl.GL_TRIANGLES` accepted
--   only at the moment).
-- @param count The number of elements to render.
-- @param type The type of the index values.
-- @param pointer The vertex buffer data from which the indices are taken.
-- @full Renders primitives from array data by calling `glDrawElements()`.
*/
static int gles2DrawElements(lua_State *L) {
    GLenum mode = (GLenum)luaL_checknumber(L,1);
    GLsizei count = (GLsizei)luaL_checknumber(L,2);    
    GLenum type = (GLenum)luaL_checknumber(L,3);
    
// pointer
    void *pointer = (void *)0;
    if (lua_isnoneornil(L,4) == 0) {
        VertexBufferDataType *bufferData = checkVertexBufferDataType(L,4);
        pointer = bufferData->vbd->getPointer();
    }
    
// draw
    glDrawElements(mode,count,type,pointer);
    if (luaChkGLError(L)) {
        return 0;
    }
   
    return 0;
}

/** */
static const struct luaL_Reg gles2Funcs[] = {
    {"init",gles2Initialize},
    {"clearColor",gles2ClearColor},
    {"clear",gles2Clear},
    {"enableBlend",gles2EnableBlend},
    {"disableBlend",gles2DisableBlend},
    {"setBlendEnabled",gles2SetBlendEnabled},
    {"setDepthTestEnabled",gles2SetDepthTestEnabled},
    {"isShader",gles2IsShader},
    {"viewport",gles2Viewport},
    
    {"drawArrays",gles2DrawArrays},
    {"drawElements",gles2DrawElements},
    
    {0,0}
};

/** The number fields. */
static const struct LuaField<lua_Number> gles2NumberFields[] = {
/*
-- @name .GL_FLOAT
-- @var
-- @brief The value of the constant `GL_FLOAT`.
*/
    {"GL_FLOAT",GL_FLOAT},

/*
-- @name .GL_SHORT
-- @var
-- @brief The value of the constant `GL_SHORT`.
*/
    {"GL_SHORT",GL_SHORT},
    
/*
-- @name .GL_UNSIGNED_SHORT
-- @var
-- @brief The value of the constant `GL_UNSIGNED_SHORT`.
*/
    {"GL_UNSIGNED_SHORT",GL_UNSIGNED_SHORT},

/*
-- @name .GL_BYTE
-- @var
-- @brief The value of the constant `GL_BYTE`.
*/
    {"GL_BYTE",GL_BYTE},
    
/*
-- @name .GL_UNSIGNED_BYTE
-- @var
-- @brief The value of the constant `GL_UNSIGNED_BYTE`.
*/
    {"GL_UNSIGNED_BYTE",GL_UNSIGNED_BYTE},
/*
-- @name .GLfloatSize
-- @var
-- @brief The size of `GLfloat`.
*/
    {"GLfloatSize",sizeof(GLfloat)},
    
/*
-- @name .GLshortSize
-- @var
-- @brief The size of `GLshort`.
*/
    {"GLshortSize",sizeof(GLshort)},
    
/*
-- @name .GLushortSize
-- @var
-- @brief The size of `GLushort`.
*/
    {"GLushortSize",sizeof(GLushort)},
    
/*
-- @name .GLbyteSize
-- @var
-- @brief The size of `GLbyte`.
*/
    {"GLbyteSize",sizeof(GLbyte)},
    
/*
-- @name .GLubyteSize
-- @var
-- @brief The size of `GLubyte`.
*/
    {"GLubyteSize",sizeof(GLubyte)},
    
/*
-- @name .GL_ARRAY_BUFFER
-- @var
-- @brief The value of the constant `GL_ARRAY_BUFFER`.
*/
    {"GL_ARRAY_BUFFER",GL_ARRAY_BUFFER},
    
/*
-- @name .GL_ELEMENT_ARRAY_BUFFER
-- @var
-- @brief The value of the constant `GL_ELEMENT_ARRAY_BUFFER`.
*/
    {"GL_ELEMENT_ARRAY_BUFFER",GL_ELEMENT_ARRAY_BUFFER},
        
/*
-- @name .GL_STREAM_DRAW 
-- @var
-- @brief The value of the constant `GL_STREAM_DRAW`.
*/
    {"GL_STREAM_DRAW",GL_STREAM_DRAW},
    
/*
-- @name .GL_STATIC_DRAW
-- @var
-- @brief The value of the constant `GL_STATIC_DRAW`.
*/
    {"GL_STATIC_DRAW",GL_STATIC_DRAW},
    
/*
-- @name .GL_DYNAMIC_DRAW
-- @var
-- @brief The value of the constant `GL_DYNAMIC_DRAW`.
*/
    {"GL_DYNAMIC_DRAW",GL_DYNAMIC_DRAW},
        
/*
-- @name .GL_TRIANGLES
-- @var
-- @brief The value of the constant `GL_TRIANGLES`.
*/
    {"GL_TRIANGLES",GL_TRIANGLES},
    
    {0,0} 
};
 
/** */
static void gles2PushFields(lua_State *L) {
    luaSetFields(L,gles2NumberFields);
}

/** */
static int gles2RequireFunc(lua_State *L) {
    luaL_newlib(L,gles2Funcs);
    return 1;
}

/** */
void loadGLES2Lib(lua_State *L,DLog *log_) {
    log = log_;
    luaL_requiref(L,gles2Name,gles2RequireFunc,1);
    gles2PushFields(L);
    lua_pop(L,1);
}    
    
} // namespace

} // namespace

} // namespace