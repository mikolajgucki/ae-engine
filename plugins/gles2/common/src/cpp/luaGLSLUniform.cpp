/*
-- @module GLSLUniform
-- @group GLES2
-- @brief Provides functions related to GLSL uniform variables.
-- @full Provides functions related to GLSL uniform variables. This module is
--   C libary which is loaded by default.
*/  
#include "lua_common.h"
#include "ae_GLES2.h"
#include "luaMat4.h"
#include "luaGLSLUniform.h"

using namespace ae::lua;
using namespace ae::math::lua;

namespace ae {

namespace gles2 {
    
namespace lua {
 
/**
 * \brief The structure of the GLSL uniform user type.
 */ 
struct GLSLUniformType {
    /** The GL location of the uniform variable. */
    GLint glLocation;
};
typedef struct GLSLUniformType GLSLUniformType;

/// The name of the Lua user type.
static const char *glslUniformName = "GLSLUniform";

/// The name of the Lua metatable.
static const char *glslUniformMetatableName = "GLSLUniform.metatable";
 
/** */
static GLSLUniformType *checkGLSLUniformType(lua_State *L) {
    void *data = luaL_checkudata(L,1,glslUniformMetatableName);
    luaL_argcheck(L,data != (void *)0,1,"GLSLUniform expected");
    return (GLSLUniformType *)data;
}

/*
-- @name .new
-- @func
-- @brief Creates a uniform variable object.
-- @param location The location of a uniform variable.
-- @return The new GLSL uniform variable.
-- @full Creates a uniform variable object. The location can be obtained from
--   GLSL program (via function ()[GLSLProgram:uniform]).
*/
static int glslUniformNew(lua_State *L) {
// GL location
    GLint glLocation = (GLint)luaL_checknumber(L,1);
    
// user data
    GLSLUniformType *data =
        (GLSLUniformType *)lua_newuserdata(L,sizeof(GLSLUniformType));
    data->glLocation = glLocation;
    
// metatable
    luaL_getmetatable(L,glslUniformMetatableName);
    lua_setmetatable(L,-2);
    
    return 1;
}

/*
-- @name :set1i
-- @func
-- @brief Sets an integer value of the uniform variable.
-- @param v0 The value.
*/
static int glslUniformSet1i(lua_State *L) {
    GLSLUniformType *data = checkGLSLUniformType(L);
    GLint value0 = (GLint)luaL_checknumber(L,2);
    
    glUniform1i(data->glLocation,value0);
    return 0;
}

/*
-- @name :set2i
-- @func
-- @brief Sets 2 integer values of the uniform variable.
-- @param v0 The first value.
-- @param v1 The second value.
*/
static int glslUniformSet2i(lua_State *L) {
    GLSLUniformType *data = checkGLSLUniformType(L);
    GLint value0 = (GLint)luaL_checknumber(L,2);
    GLint value1 = (GLint)luaL_checknumber(L,3);
    
    glUniform2i(data->glLocation,value0,value1);
    return 0;
}

/*
-- @name :set3i
-- @func
-- @brief Sets 3 integer values of the uniform variable.
-- @param v0 The first value.
-- @param v1 The second value.
-- @param v2 The third value.
*/
static int glslUniformSet3i(lua_State *L) {
    GLSLUniformType *data = checkGLSLUniformType(L);
    GLint value0 = (GLint)luaL_checknumber(L,2);
    GLint value1 = (GLint)luaL_checknumber(L,3);
    GLint value2 = (GLint)luaL_checknumber(L,4);
    
    glUniform3i(data->glLocation,value0,value1,value2);
    return 0;
}

/*
-- @name :set4i
-- @func
-- @brief Sets 4 integer values of the uniform variable.
-- @param v0 The first value.
-- @param v1 The second value.
-- @param v2 The third value.
-- @param v3 The fourth value.
*/
static int glslUniformSet4i(lua_State *L) {
    GLSLUniformType *data = checkGLSLUniformType(L);
    GLint value0 = (GLint)luaL_checknumber(L,2);
    GLint value1 = (GLint)luaL_checknumber(L,3);
    GLint value2 = (GLint)luaL_checknumber(L,4);
    GLint value3 = (GLint)luaL_checknumber(L,5);
    
    glUniform4i(data->glLocation,value0,value1,value2,value3);
    return 0;
}

/*
-- @name :set1f
-- @func
-- @brief Sets a float value of the uniform variable.
-- @param v0 The value.
*/
static int glslUniformSet1f(lua_State *L) {
    GLSLUniformType *data = checkGLSLUniformType(L);
    GLfloat value0 = (GLfloat)luaL_checknumber(L,2);
    
    glUniform1f(data->glLocation,value0);    
    return 0;
}

/*
-- @name :set2f
-- @func
-- @brief Sets 2 float values of the uniform variable.
-- @param v0 The first value.
-- @param v1 The second value.
*/
static int glslUniformSet2f(lua_State *L) {
    GLSLUniformType *data = checkGLSLUniformType(L);
    GLfloat value0 = (GLfloat)luaL_checknumber(L,2);
    GLfloat value1 = (GLfloat)luaL_checknumber(L,3);
    
    glUniform2f(data->glLocation,value0,value1);    
    return 0;
}

/*
-- @name :set3f
-- @func
-- @brief Sets 3 float values of the uniform variable.
-- @param v0 The first value.
-- @param v1 The second value.
-- @param v2 The third value.
*/
static int glslUniformSet3f(lua_State *L) {
    GLSLUniformType *data = checkGLSLUniformType(L);
    GLfloat value0 = (GLfloat)luaL_checknumber(L,2);
    GLfloat value1 = (GLfloat)luaL_checknumber(L,3);
    GLfloat value2 = (GLfloat)luaL_checknumber(L,4);
    
    glUniform3f(data->glLocation,value0,value1,value2);    
    return 0;
}

/*
-- @name :set4f
-- @func
-- @brief Sets 4 float values of the uniform variable.
-- @param v0 The first value.
-- @param v1 The second value.
-- @param v2 The third value.
-- @param v3 The fourth value.
*/
static int glslUniformSet4f(lua_State *L) {
    GLSLUniformType *data = checkGLSLUniformType(L);
    GLfloat value0 = (GLfloat)luaL_checknumber(L,2);
    GLfloat value1 = (GLfloat)luaL_checknumber(L,3);
    GLfloat value2 = (GLfloat)luaL_checknumber(L,4);
    GLfloat value3 = (GLfloat)luaL_checknumber(L,5);
    
    glUniform4f(data->glLocation,value0,value1,value2,value3);    
    return 0;
}

/*
-- @name .mat4
-- @func
-- @brief Sets a matrix 4x4 value of the uniform variable.
-- @param matrix The matrix.
*/
static int glslUniformMat4(lua_State *L) {
    GLSLUniformType *data = checkGLSLUniformType(L);
    Mat4Type *mat4 = checkMat4Type(L,2);
    
    glUniformMatrix4fv(data->glLocation,1,GL_FALSE,mat4->matrix->asArray());
    return 0;
}

// TODO Write __tostring metamethod.

/** The type functions. */
static const struct luaL_Reg glslUniformFuncs[] = {
    {"new",glslUniformNew},
    {0,0}
};

/** The type methods. */
static const struct luaL_Reg glslUniformMethods[] = {
    {"set1i",glslUniformSet1i},
    {"set2i",glslUniformSet2i},
    {"set3i",glslUniformSet3i},
    {"set4i",glslUniformSet4i},
    
    {"set1f",glslUniformSet1f},
    {"set2f",glslUniformSet2f},
    {"set3f",glslUniformSet3f},
    {"set4f",glslUniformSet4f},
    
    {"mat4",glslUniformMat4},
    {0,0}
};

/** The type metamethods. */
static const struct luaL_Reg glslUniformMetamethods[] = {
    {0,0}
};

/** */
static int glslUniformRequireFunc(lua_State *L) {
    luaLoadUserType(L,glslUniformMetatableName,
        glslUniformFuncs,glslUniformMethods,glslUniformMetamethods);
    return 1;
}

/** */
void loadGLSLUniformLib(lua_State *L) {
    luaL_requiref(L,glslUniformName,glslUniformRequireFunc,1);
    lua_pop(L,1);
}    
    
} // namespace
    
} // namespace
    
} // namespace
