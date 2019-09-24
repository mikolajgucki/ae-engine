/*
-- @module GLSLShader
-- @group GLES2
-- @brief Provides functions related to GLSL shader.
-- @full Provides functions related to GLSL shader. This module is a
--   C library is loaded by default.
*/
#include <memory>

#include "lua_common.h"
#include "GLSLShader.h"
#include "luaGLSLShader.h"

using namespace std;
using namespace ae::lua;
using namespace ae::gles2;

namespace ae {
    
namespace gles2 {
    
namespace lua {

/// The name of the Lua user type.
static const char *glslShaderName = "GLSLShader";

/// The name of the Lua metatable.
static const char *glslShaderMetatableName = "GLSLShader.metatable";

/// The value of the field identyfing a vertex shader
static const lua_Number glslShaderVertex = 1;

/// The value of the field identyfing a fragment shader
static const lua_Number glslShaderFragment = 2;

/** */
GLSLShaderType *checkGLSLShaderType(lua_State *L,int index) {
    void *data = luaL_checkudata(L,index,glslShaderMetatableName);
    luaL_argcheck(L,data != (void *)0,1,"GLSLShader exepected");
    return (GLSLShaderType *)data;
}

/*
-- @name .compile
-- @func
-- @brief Compiles a GLSL shader.
-- @param type The shader type (one of ()[GLSLShader.vertex],
--   ()[GLSLShader.fragment]).
-- @param src The shader source.
-- @return The compiled shader object.
*/
static int glslShaderCompile(lua_State *L) {
// shader type argument
    lua_Number luaShaderType = (int)lua_tointeger(L,1);
    if (luaShaderType != glslShaderVertex &&
        luaShaderType != glslShaderFragment) {
    //
        luaPushError(L,"Invalid shader type");
        return 0;
    }
    
// shader source
    const char *src = luaL_checkstring(L,2);
    
// shader type
    GLenum shaderType;
    if (luaShaderType == glslShaderVertex) {
        shaderType = GL_VERTEX_SHADER;
    }        
    else if (luaShaderType == glslShaderFragment) {
        shaderType = GL_FRAGMENT_SHADER;
    }
    else {
        luaPushError(L,"Unknown shader type");
        return 0;
    }
    
// create shader
    GLSLShader *shader = new (nothrow) GLSLShader(shaderType);
    if (shader == (GLSLShader *)0) {
        luaPushNoMemoryError(L);
        return 0;
    }        
    if (shader->create() == false) {
        luaPushError(L,shader->getError().c_str());
        delete shader;
        return 0;
    }
    
// compile
    if (shader->compile((const GLchar *)src) == false) {
        luaPushError(L,shader->getError().c_str());
        return 0;
    }
    
// user data
    GLSLShaderType *data =
        (GLSLShaderType *)lua_newuserdata(L,sizeof(GLSLShaderType));
    data->shader = shader;
    
// metatable
    luaL_getmetatable(L,glslShaderMetatableName);
    lua_setmetatable(L,-2);
    
    return 1;
}

/*
-- @name :__gc
-- @func
-- @brief Destroys the GLSL shader.
-- @full Destroys the GLSL shader. Never call this function directly!
*/
static int glslShader__gc(lua_State *L) {
    GLSLShaderType *data = checkGLSLShaderType(L);
    delete data->shader;    
    
    return 0;
}

// TODO Write __tostring metamethod.

/** The type functions. */
static const struct luaL_Reg glslShaderFuncs[] = {
    {"compile",glslShaderCompile},
    {0,0}
};

/** The type methods. */
static const struct luaL_Reg glslShaderMethods[] = {
    {0,0}
};

/** The type metamethods. */
static const struct luaL_Reg glslShaderMetamethods[] = {
    {"__gc",glslShader__gc},
    {0,0}
};

/** The number fields. */
static const struct LuaField<lua_Number> glslShaderNumberFields[] = {
/*
-- @name GLSLShader.vertex
-- @var
-- @brief The vertex shader type.
*/
    {"vertex",glslShaderVertex},
    
/*
-- @name GLSLShader.fragment
-- @var
-- @brief The fragment shader type.
*/
    {"fragment",glslShaderFragment},
    
    {0,0} 
};

/** */
static void glslShaderPushFields(lua_State *L) {
    luaSetFields(L,glslShaderNumberFields);
}

/** */
static int glslShaderRequireFunc(lua_State *L) {
    luaLoadUserType(L,glslShaderMetatableName,
        glslShaderFuncs,glslShaderMethods,glslShaderMetamethods);
    return 1;
}

/** */
void loadGLSLShaderLib(lua_State *L) {
    luaL_requiref(L,glslShaderName,glslShaderRequireFunc,1);    
    glslShaderPushFields(L);
    lua_pop(L,1);
}

} // namespace

} // namespace

} // namespace