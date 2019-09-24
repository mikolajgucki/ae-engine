#ifndef AE_GLES2_LUA_GLSL_SHADER_H
#define AE_GLES2_LUA_GLSL_SHADER_H

#include "lua.hpp"
#include "GLSLShader.h"

namespace ae {
    
namespace gles2 {
    
namespace lua {
    
/**
 * \brief Wraps the GLSL shader so that it can be used as Lua user type.
 */
struct GLSLShaderType {
    /** */
    GLSLShader *shader;
};
typedef struct GLSLShaderType GLSLShaderType;    
    
/**
 * \brief Checks wheter the object at given stack index is a user data of
 *     the GLSL shader type. Raises error if the object is not of the type.
 * \param index The Lua stack index.
 * \return The user data of the GLSL shader type. 
 */
GLSLShaderType *checkGLSLShaderType(lua_State *L,int index = 1);

/**
 * \brief Loads the GLSL shader library.
 * \param L The Lua state.
 */
void loadGLSLShaderLib(lua_State *L);
    
} // namespace
    
} // namespace

} // namespace

#endif // AE_GLES2_LUA_GLSL_SHADER_H