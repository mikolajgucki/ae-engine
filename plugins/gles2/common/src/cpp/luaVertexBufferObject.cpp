/*
-- @module VertexBufferObject
-- @group GLES2
-- @brief Provides functions related to vertex buffer object (VBO).
-- @full Provides functions related to vertex buffer object (VBO).
--    This module is a C library is loaded by default.
*/
#include <memory>

#include "lua_common.h"
#include "ae_GLES2.h"
#include "lua_glerror.h"
#include "VertexBufferObject.h"
#include "luaVertexBufferData.h"
#include "luaVertexBufferObject.h"

using namespace std;
using namespace ae::lua;

namespace ae {
    
namespace gles2 {
    
namespace lua {

/**
 * \brief Wraps the vertex buffer object so that it can be used as a user type.
 */
struct VertexBufferObjectType {
    /** */
    VertexBufferObject *vbo;
};
    
/// The name of the Lua user type.
static const char *vertexBufferObjectName = "VertexBufferObject";
    
/// The name of the Lua metatable.
static const char *vertexBufferObjectMetatableName =
    "VertexBufferObject.metatable";

/** */
static VertexBufferObjectType *checkVertexBufferObjectType(lua_State *L) {
    void *data = luaL_checkudata(L,1,vertexBufferObjectMetatableName);
    luaL_argcheck(L,data != (void *)0,1,"VertexBufferObject expected");
    return (VertexBufferObjectType *)data;
}
    
/*
-- @name .new
-- @func
-- @brief Creates a vertex buffer object.
-- @return The new VBO object.
*/
static int vertexBufferObjectNew(lua_State *L) {
// create VBO
    VertexBufferObject *vbo = new (nothrow) VertexBufferObject();
    if (vbo == (VertexBufferObject *)0) {
        luaPushNoMemoryError(L);
        return 0;
    }
    
// user data
    VertexBufferObjectType *data = (VertexBufferObjectType *)lua_newuserdata(
        L,sizeof(VertexBufferObjectType));
    data->vbo = vbo;
    
// metatable
    luaL_getmetatable(L,vertexBufferObjectMetatableName);
    lua_setmetatable(L,-2);
    
    return 1;
}

/*
-- @name .unbind
-- @func
-- @brief Unbinds the current vertex buffer object from the `GL_ARRAY_BUFFER`
--   target.
-- @func
-- @brief Unbinds the current vertex buffer object.
-- @param target The target to which the buffer is bound (either
--   ()[gl.GL_ARRAY_BUFFER] or ()[gl.GL_ELEMENT_ARRAY_BUFFER]).
*/
static int vertexBufferObjectUnbind(lua_State *L) {
    GLenum target = GL_ARRAY_BUFFER;
    if (lua_isnoneornil(L,1) == 0) {
        target = (GLenum)luaL_checknumber(L,1);
    }
    
    glBindBuffer(target,0);
    if (luaChkGLError(L)) {
        return 0;
    }    
    
    return 0;
}

/*
-- @name :generate
-- @func
-- @brief Generates the vertex buffer object.
*/
static int vertexBufferObjectGenerate(lua_State *L) {
    VertexBufferObjectType *data = checkVertexBufferObjectType(L);
    
    if (data->vbo->generate() == false) {
        luaPushError(L,data->vbo->getError().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :bind
-- @func
-- @brief Binds the buffer to the `GL_ARRAY_BUFFER` target.
-- @func
-- @brief Binds the buffer to a target.
-- @param target The target to which to bind the buffer (either
--   ()[gl.GL_ARRAY_BUFFER] or ()[gl.GL_ELEMENT_ARRAY_BUFFER]).
*/
static int vertexBufferObjectBind(lua_State *L) {
    VertexBufferObjectType *data = checkVertexBufferObjectType(L);
    
    GLenum target = GL_ARRAY_BUFFER;
    if (lua_isnoneornil(L,2) == 0) {
        target = (GLenum)luaL_checknumber(L,2);
    }
    
    if (data->vbo->bind(target) == false) {
        luaPushError(L,data->vbo->getError().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :data
-- @func
-- @brief Sets the vertex buffer object data.
-- @param vbd The vertex buffer data from which to take the data.
-- @param target The target buffer object (either
--   ()[gl.GL_ARRAY_BUFFER] or ()[gl.GL_ELEMENT_ARRAY_BUFFER]).
-- @param usage The expected usage (one of
--   ()[gl.GL_STREAM_DRAW], ()[gl.GL_STATIC_DRAW], ()[gl.GL_DYNAMIC_DRAW]).
*/
static int vertexBufferObjectData(lua_State *L) {
// arguments
    VertexBufferDataType *vbdData = checkVertexBufferDataType(L,2);
    GLenum target = (GLenum)luaL_checknumber(L,3);
    GLenum usage = (GLenum)luaL_checknumber(L,4);
    
// set the data
    glBufferData(target,vbdData->vbd->getSize(),
        vbdData->vbd->getPointer(),usage);
    if (luaChkGLError(L)) {
        return 0;
    }
    
    return 0;
}

/*
-- @name :__gc
-- @func
-- @brief Destorys the vertex buffer object.
-- @full Destorys the vertex buffer object. Never call this function directly!
*/
static int vertexBufferObject__gc(lua_State *L) {
    VertexBufferObjectType *data = checkVertexBufferObjectType(L);
    delete data->vbo;
    
    return 0;
}
    
// TODO Write __tostring metamethod.

/** The type functions. */
static const struct luaL_Reg vertexBufferObjectFuncs[] = {
    {"new",vertexBufferObjectNew},
    {"unbind",vertexBufferObjectUnbind},
    {0,0}
};

/** The type methods. */
static const struct luaL_Reg vertexBufferObjectMethods[] = {
    {"generate",vertexBufferObjectGenerate},
    {"bind",vertexBufferObjectBind},
    {"data",vertexBufferObjectData},
    {0,0}
};

/** The type metamethods. */
static const struct luaL_Reg vertexBufferObjectMetamethods[] = {
    {"__gc",vertexBufferObject__gc},
    {0,0}
};
    
/** */
static int vertexBufferObjectRequireFunc(lua_State *L) {
    luaLoadUserType(L,vertexBufferObjectMetatableName,
        vertexBufferObjectFuncs,vertexBufferObjectMethods,
        vertexBufferObjectMetamethods);
    return 1;
}

/** */
void loadVertexBufferObjectLib(lua_State *L) {
    luaL_requiref(L,vertexBufferObjectName,vertexBufferObjectRequireFunc,1);
    lua_pop(L,1);
}
    
} // namespace
    
} // namespace
    
} // namespace