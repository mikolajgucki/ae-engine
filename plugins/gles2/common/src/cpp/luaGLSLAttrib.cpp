/*
-- @module GLSLAttrib
-- @group GLES2
-- @brief Provides functions related to GLSL vertex attributes.
-- @full Provides functions related to GLSL vertex attributes. This module is
--   C libary which is loaded by default.
*/
#include "lua_common.h"
#include "ae_GLES2.h"
#include "lua_glerror.h"
#include "luaVertexBufferData.h"
#include "luaGLSLAttrib.h"

using namespace ae::lua;

namespace ae {

namespace gles2 {
    
namespace lua {
  
/**
 * \brief The structure of the GLSL attribute user data.
 */
struct GLSLAttribType {
    /** The GL location of the attribute variable. */
    GLint glLocation;
};
typedef struct GLSLAttribType GLSLAttribType;

/// The name of the Lua user type.
static const char *glslAttribName = "GLSLAttrib";
    
/// The name of the Lua metatable.
static const char *glslAttribMetatableName = "GLSLAttrib.metatable";

/** */
static GLSLAttribType *checkGLSLAttribType(lua_State *L) {
    void *data = luaL_checkudata(L,1,glslAttribMetatableName);
    luaL_argcheck(L,data != (void *)0,1,"GLSLAttrib expected");
    return (GLSLAttribType *)data;
}

/*
-- @name .new
-- @func
-- @brief Creates a vertex attribute object.
-- @param location The location of an attribute variable.
-- @return The new GLSL attribute object.
-- @full Creates a vertex attribute object. The location can be obtained from
--   GLSL program (via function ()[GLSLProgram:attrib]).
*/
static int glslAttribNew(lua_State *L) {
// GL location
    GLint glLocation = (GLint)luaL_checknumber(L,1);
    
// user data
    GLSLAttribType *data =
        (GLSLAttribType *)lua_newuserdata(L,sizeof(GLSLAttribType));
    data->glLocation = glLocation;
    
// metatable
    luaL_getmetatable(L,glslAttribMetatableName);
    lua_setmetatable(L,-2);
    
    return 1;
}

/*
-- @name :enable
-- @func
-- @brief Enables the vertex attribute.
*/
static int glslAttribEnable(lua_State *L) {
    GLSLAttribType *data = checkGLSLAttribType(L);
    glEnableVertexAttribArray(data->glLocation);
    
    return 0;
}

/*
-- @name :disable
-- @func
-- @brief Disables the vertex attribute.
*/
static int glslAttribDisable(lua_State *L) {
    GLSLAttribType *data = checkGLSLAttribType(L);
    glDisableVertexAttribArray(data->glLocation);
    
    return 0;
}

/*
-- @name :pointer
-- @func
-- @brief Specifies the array of the vertex attribute.
-- @param pointer The vertex buffer data from which to take the data or
--   stride if taking the data from a vertex buffer object.
-- @param size The number of components per vertex attribute.
-- @param type The type of each component in the array.
-- @param offset The offset from the pointer to the first element taken to
--   the array of the vertex attribute.
-- @param normalized `true` if to normalize the values stored as integers to
--   the range [-1,1].
*/
static int glslAttribPointer(lua_State *L) {
// GLSL attribute and vertex buffer data
    GLSLAttribType *attribData = checkGLSLAttribType(L);
    VertexBufferDataType *bufferData = (VertexBufferDataType *)0;
    
// arguments
    GLint size = (GLint)luaL_checknumber(L,3);
    GLenum type = (GLenum)luaL_checknumber(L,4);
    int offset = (int)luaL_checknumber(L,5);
    int normalized = lua_toboolean(L,6);
    
    int stride = 0;
    void *pointer = (void *)0;
// vertex buffer data
    if (lua_isnumber(L,2)) {
        stride = (int)lua_tointeger(L,2);
        pointer = (void *)(long)(offset);
    }
    else {
        bufferData = checkVertexBufferDataType(L,2);
        stride = bufferData->vbd->getStride();
        pointer = bufferData->vbd->getPointer(offset);
    }
    
// set the pointer
    glVertexAttribPointer(attribData->glLocation,size,type,
        normalized != 0 ? GL_TRUE : GL_FALSE,stride,pointer);
    if (luaChkGLError(L)) {
        return 0;
    }    
    
    return 0;
}

// TODO Write __tostring metamethod.

/** The type functions. */
static const struct luaL_Reg glslAttribFuncs[] = {
    {"new",glslAttribNew},
    {0,0}
};

/** The type methods. */
static const struct luaL_Reg glslAttribMethods[] = {
    {"enable",glslAttribEnable},
    {"disable",glslAttribDisable},
    {"pointer",glslAttribPointer},
    {0,0}
};

/** The type metamethods. */
static const struct luaL_Reg glslAttribMetamethods[] = {
    {0,0}
};

/** */
static int glslAttribRequireFunc(lua_State *L) {
    luaLoadUserType(L,glslAttribMetatableName,
        glslAttribFuncs,glslAttribMethods,glslAttribMetamethods);
    return 1;
}

/** */
void loadGLSLAttribLib(lua_State *L) {
    luaL_requiref(L,glslAttribName,glslAttribRequireFunc,1);
    lua_pop(L,1);
}

} // namespace
    
} // namespace
    
} // namespace