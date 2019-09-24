/*
-- @module GLSLProgram
-- @group GLES2
-- @brief Provides functions related to GLSL programs.
-- @full Provides functions related to GLSL programs. This module is
--   C libary which is loaded by default.
*/
#include <memory>

#include "lua_common.h" 
#include "GLSLProgram.h"
#include "luaGLSLShader.h"
#include "luaGLSLProgram.h"

using namespace std;
using namespace ae::lua;

namespace ae {
    
namespace gles2 {
    
namespace lua {
 
/**
 * \brief Wraps the GLSL program so that it can be used as Lua user type.
 */
struct GLSLProgramType {
    /** */
    GLSLProgram *program;    
};
typedef struct GLSLProgramType GLSLProgramType;
    
/// The name of the Lua user type.
static const char *glslProgramName = "GLSLProgram";

/// The name of the Lua metatable.
static const char *glslProgramMetatableName = "GLSLProgram.metatable";

/** */
static GLSLProgramType *checkGLSLProgramType(lua_State *L) {
    void *data = luaL_checkudata(L,1,glslProgramMetatableName);
    luaL_argcheck(L,data != (void *)0,1,"GLSLProgram expected");
    return (GLSLProgramType *)data;
}

/*
-- @name .new
-- @func
-- @brief Creates a new GLSL program object.
-- @return The new GLSL program object.
*/
static int glslProgramNew(lua_State *L) {
// create program
    GLSLProgram *program = new (nothrow) GLSLProgram();
    if (program == (GLSLProgram *)0) {
        luaPushNoMemoryError(L);
        return 0;
    }
    if (program->create() == false) {
        luaPushError(L,program->getError().c_str());
        delete program;
        return 0;
    }
    
// user data
    GLSLProgramType *data =
        (GLSLProgramType *)lua_newuserdata(L,sizeof(GLSLProgramType));
    data->program = program;
    
// metatable
    luaL_getmetatable(L,glslProgramMetatableName);
    lua_setmetatable(L,-2);
    
    return 1;
}

/*
-- @name :attach
-- @func
-- @brief Attaches a ()[GLSLShader].
-- @param shader The shader.
*/
static int glslProgramAttach(lua_State *L) {
    GLSLProgramType *programData = checkGLSLProgramType(L);
    GLSLShaderType *shaderData = checkGLSLShaderType(L,2);
    
    GLSLProgram *program = programData->program;
    GLSLShader *shader = shaderData->shader;
    
    if (program->attachShader(shader) == false) {
        luaPushError(L,program->getError().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :link
-- @func
-- @brief Links the program.
-- @full Links the program. The program must have a vertex and fragment
--   shaders attached. Use ()[GLSLProgram:attach(shader)] to attach the shaders.
*/
static int glslProgramLink(lua_State *L) {
    GLSLProgramType *data = checkGLSLProgramType(L);
    GLSLProgram *program = data->program;
    
    if (program->link() == false) {
        luaPushError(L,program->getError().c_str());
        return 0;
    }
    
    return true;
}

/*
-- @name :use
-- @func
-- @brief Uses the program in the current rendering.
*/
static int glslProgramUse(lua_State *L) {
    GLSLProgramType *data = checkGLSLProgramType(L);
    GLSLProgram *program = data->program;
    
    if (program->use() == false) {
        luaPushError(L,program->getError().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :uniform
-- @func
-- @brief Gets the location of a uniform variable
-- @param name The uniform variable name.
-- @return The ()[GLSLUniform] object. 
 */
static int glslProgramUniform(lua_State *L) {
    GLSLProgramType *data = checkGLSLProgramType(L);
    GLSLProgram *program = data->program;
    
// name
    const GLchar *name = (const GLchar *)lua_tostring(L,2);
    
// get the variable
    GLint glIndex;
    if (program->getUniformLocation(name,&glIndex) == false) {
        luaPushError(L,program->getError().c_str());
        return 0;
    }    
    if (glIndex == -1) {
        ostringstream err;
        err << "Uniform variable " << name << " does not correspond to" <<
            " a uniform variable in the program";
        luaPushError(L,err.str().c_str());
        return 0;
    }
    
    lua_pushnumber(L,glIndex);
    return 1;
}

/*
-- @name :attrib
-- @func
-- @brief Gets the location of a vertex attribute.
-- @param name The vertex attribute name.
-- @return The ()[GLSLAttrib] object.
*/
static int glslProgramAttrib(lua_State *L) {
    GLSLProgramType *data = checkGLSLProgramType(L);
    GLSLProgram *program = data->program;
    
// name
    const GLchar *name = (const GLchar *)lua_tostring(L,2);
    
// get the variable
    GLint glIndex;
    if (program->getAttribLocation(name,&glIndex) == false) {
        luaPushError(L,program->getError().c_str());
        return 0;
    }    
    if (glIndex == -1) {
        ostringstream err;
        err << "Attribute variable " << name << " does not correspond to" <<
            " an attribute variable in the program";
        luaPushError(L,err.str().c_str());
        return 0;
    }
    
    lua_pushnumber(L,glIndex);
    return 1;
}

/*
-- @name :__gc
-- @func
-- @brief Destorys the GLSL program.
-- @full Destorys the GLSL program. Never call this function directly!
*/
static int glslProgram__gc(lua_State *L) {
    GLSLProgramType *data = checkGLSLProgramType(L);
    delete data->program;
    
    return 0;
}

// TODO write __tostring metamethod.

/** The type functions. */
static const struct luaL_Reg glslProgramFuncs[] = {
    {"new",glslProgramNew},
    {0,0}
};

/** The type methods. */
static const struct luaL_Reg glslProgramMethods[] = {
    {"attach",glslProgramAttach},
    {"link",glslProgramLink},
    {"use",glslProgramUse},
    {"uniform",glslProgramUniform},
    {"attrib",glslProgramAttrib},
    {0,0}
};

/** The type metamethods. */
static const struct luaL_Reg glslProgramMetamethods[] = {
    {"__gc",glslProgram__gc},
    {0,0}
};

/** */
static int glslProgramRequireFunc(lua_State *L) {
    luaLoadUserType(L,glslProgramMetatableName,
        glslProgramFuncs,glslProgramMethods,glslProgramMetamethods);
    return 1;
}

/** */
void loadGLSLProgramLib(lua_State *L) {
    luaL_requiref(L,glslProgramName,glslProgramRequireFunc,1);
    lua_pop(L,1);
}

} // namespace

} // namespace

} // namespace