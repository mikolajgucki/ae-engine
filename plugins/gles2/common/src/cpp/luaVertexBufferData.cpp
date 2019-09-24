/*
-- @module VertexBufferData
-- @group GLES2
-- @brief Provides functions related to vertex buffer data (VBD).
-- @full Provides functions related to vertex buffer data (VBD).
--   This module is a C library is loaded by default.
*/
#include <memory>

#include "lua_common.h"
#include "luaVertexBufferData.h"

using namespace std;
using namespace ae::lua;

namespace ae {
    
namespace gles2 {
    
namespace lua {

/// The name of the Lua user type.
static const char *vertexBufferDataName = "VertexBufferData";
    
/// The name of the Lua metatable.
static const char *vertexBufferDataMetatableName = "VertexBufferData.metatable";

/** */
VertexBufferDataType *checkVertexBufferDataType(lua_State *L,int index) {
    void *data = luaL_checkudata(L,index,vertexBufferDataMetatableName);
    luaL_argcheck(L,data != (void *)0,index,"VertexBufferData expected");
    return (VertexBufferDataType *)data;
}

/*
-- @name .new
-- @func
-- @brief Creates and allocates memory for a vertex buffer data.
-- @param stride The offset between vertex attributes.
-- @param capacity The maximum number of vertices to be supported by this
--   vertex buffer data.
-- @return The new vertex buffer data.
*/
static int vertexBufferDataNew(lua_State *L) {
    int stride = (int)luaL_checknumber(L,1);
    int capacity = (int)luaL_checknumber(L,2);
    
// create the buffer data
    VertexBufferData *bufferData =
        new (nothrow) VertexBufferData(stride,capacity);
    if (bufferData == (VertexBufferData *)0) {
        luaPushNoMemoryError(L);
        return 0;
    }    
    if (bufferData->allocate() == false) {
        luaPushError(L,bufferData->getError().c_str());
        delete bufferData;
        return 0;
    }
    
// user data
    VertexBufferDataType *data =
        (VertexBufferDataType *)lua_newuserdata(L,sizeof(VertexBufferDataType));
    data->vbd = bufferData;
    
// metatable
    luaL_getmetatable(L,vertexBufferDataMetatableName);
    lua_setmetatable(L,-2);
    
    return 1;
}

/*
-- @name :getStride
-- @func
-- @brief Gets the stride.
-- @return The stride.
*/
static int vertexBufferDataGetStride(lua_State *L) {
    VertexBufferDataType *data = checkVertexBufferDataType(L);
    lua_pushnumber(L,data->vbd->getStride());
    
    return 1;
}

/*
-- @name :free
-- @func
-- @brief Frees the memory allocated for the vertex buffer data.
*/
static int vertexBufferDataFree(lua_State *L) {
    VertexBufferDataType *data = checkVertexBufferDataType(L);
    data->vbd->free();
    
    return 0;
}

/*
-- @name :set1f
-- @func
-- @brief Sets a float value of a vertex attribute.
-- @param index The index of vertex.
-- @param offset The offset within the vertex.
-- @param v0 The value.
*/
static int vertexBufferDataSet1f(lua_State *L) {
    VertexBufferDataType *data = checkVertexBufferDataType(L);
    int index = (int)luaL_checknumber(L,2);
    int offset = (int)luaL_checknumber(L,3);
    GLfloat v0 = (GLfloat)luaL_checknumber(L,4);
    
    data->vbd->set1f(index,offset,v0);
    return 0;
}

/*
-- @name :set2f
-- @func
-- @brief Sets 2 float values of a vertex attribute.
-- @param index The index of vertex.
-- @param offset The offset within the vertex.
-- @param v0 The first value.
-- @param v1 The second value.
*/
static int vertexBufferDataSet2f(lua_State *L) {
    VertexBufferDataType *data = checkVertexBufferDataType(L);
    int index = (int)luaL_checknumber(L,2);
    int offset = (int)luaL_checknumber(L,3);
    GLfloat v0 = (GLfloat)luaL_checknumber(L,4);
    GLfloat v1 = (GLfloat)luaL_checknumber(L,5);
    
    data->vbd->set2f(index,offset,v0,v1);
    return 0;
}

/*
-- @name :set3f
-- @func
-- @brief Sets 3 float values of a vertex attribute.
-- @param index The index of vertex.
-- @param offset The offset within the vertex.
-- @param v0 The first value.
-- @param v1 The second value.
-- @param v2 The third value.
*/
static int vertexBufferDataSet3f(lua_State *L) {
    VertexBufferDataType *data = checkVertexBufferDataType(L);
    int index = (int)luaL_checknumber(L,2);
    int offset = (int)luaL_checknumber(L,3);
    GLfloat v0 = (GLfloat)luaL_checknumber(L,4);
    GLfloat v1 = (GLfloat)luaL_checknumber(L,5);
    GLfloat v2 = (GLfloat)luaL_checknumber(L,6);
    
    data->vbd->set3f(index,offset,v0,v1,v2);
    return 0;
}

/*
-- @name :set4f
-- @func
-- @brief Sets 4 float values of a vertex attribute.
-- @param index The index of vertex.
-- @param offset The offset within the vertex.
-- @param v0 The first value.
-- @param v1 The second value.
-- @param v2 The third value.
-- @param v3 The fourth value.
*/
static int vertexBufferDataSet4f(lua_State *L) {
    VertexBufferDataType *data = checkVertexBufferDataType(L);
    int index = (int)luaL_checknumber(L,2);
    int offset = (int)luaL_checknumber(L,3);
    GLfloat v0 = (GLfloat)luaL_checknumber(L,4);
    GLfloat v1 = (GLfloat)luaL_checknumber(L,5);
    GLfloat v2 = (GLfloat)luaL_checknumber(L,6);
    GLfloat v3 = (GLfloat)luaL_checknumber(L,7);
    
    data->vbd->set4f(index,offset,v0,v1,v2,v3);
    return 0;
}

/*
-- @name :set1us
-- @func
-- @brief Sets an unsigned short value of a vertex attribute.
-- @param index The index of vertex.
-- @param offset The offset within the vertex.
-- @param v0 The value.
*/
static int vertexBufferDataSet1us(lua_State *L) {
    VertexBufferDataType *data = checkVertexBufferDataType(L);
    int index = (int)luaL_checknumber(L,2);
    int offset = (int)luaL_checknumber(L,3);
    GLushort v0 = (GLushort)luaL_checknumber(L,4);
    
    data->vbd->set1us(index,offset,v0);
    return 0;
}

/*
-- @name :set2us
-- @func
-- @brief Sets 2 unsigned short values of a vertex attribute.
-- @param index The index of vertex.
-- @param offset The offset within the vertex.
-- @param v0 The first value.
-- @param v1 The second value.
*/
static int vertexBufferDataSet2us(lua_State *L) {
    VertexBufferDataType *data = checkVertexBufferDataType(L);
    int index = (int)luaL_checknumber(L,2);
    int offset = (int)luaL_checknumber(L,3);
    GLushort v0 = (GLushort)luaL_checknumber(L,4);
    GLushort v1 = (GLushort)luaL_checknumber(L,5);
    
    data->vbd->set2us(index,offset,v0,v1);
    return 0;
}

/*
-- @name :set3us
-- @func
-- @brief Sets 3 unsigned short values of a vertex attribute.
-- @param index The index of vertex.
-- @param offset The offset within the vertex.
-- @param v0 The first value.
-- @param v1 The second value.
-- @param v2 The third value.
*/
static int vertexBufferDataSet3us(lua_State *L) {
    VertexBufferDataType *data = checkVertexBufferDataType(L);
    int index = (int)luaL_checknumber(L,2);
    int offset = (int)luaL_checknumber(L,3);
    GLushort v0 = (GLushort)luaL_checknumber(L,4);
    GLushort v1 = (GLushort)luaL_checknumber(L,5);
    GLushort v2 = (GLushort)luaL_checknumber(L,6);
    
    data->vbd->set3us(index,offset,v0,v1,v2);
    return 0;
}

/*
-- @name :set4us
-- @func
-- @brief Sets 4 unsigned short values of a vertex attribute.
-- @param index The index of vertex.
-- @param offset The offset within the vertex.
-- @param v0 The first value.
-- @param v1 The second value.
-- @param v2 The third value.
-- @param v3 The fourth value.
*/
static int vertexBufferDataSet4us(lua_State *L) {
    VertexBufferDataType *data = checkVertexBufferDataType(L);
    int index = (int)luaL_checknumber(L,2);
    int offset = (int)luaL_checknumber(L,3);
    GLushort v0 = (GLushort)luaL_checknumber(L,4);
    GLushort v1 = (GLushort)luaL_checknumber(L,5);
    GLushort v2 = (GLushort)luaL_checknumber(L,6);
    GLushort v3 = (GLushort)luaL_checknumber(L,7);
    
    data->vbd->set4us(index,offset,v0,v1,v2,v3);
    return 0;
}

/*
-- @name :set1ub
-- @func
-- @brief Sets an unsigned byte value of a vertex attribute.
-- @param index The index of vertex.
-- @param offset The offset within the vertex.
-- @param v0 The value.
*/
static int vertexBufferDataSet1ub(lua_State *L) {
    VertexBufferDataType *data = checkVertexBufferDataType(L);
    int index = (int)luaL_checknumber(L,2);
    int offset = (int)luaL_checknumber(L,3);
    GLubyte v0 = (GLushort)luaL_checknumber(L,4);
    
    data->vbd->set1ub(index,offset,v0);
    return 0;    
}

/*
-- @name :set2ub
-- @func
-- @brief Sets two unsigned byte values of a vertex attribute.
-- @param index The index of vertex.
-- @param offset The offset within the vertex.
-- @param v0 The first value.
-- @param v1 The second value.
*/
static int vertexBufferDataSet2ub(lua_State *L) {
    VertexBufferDataType *data = checkVertexBufferDataType(L);
    int index = (int)luaL_checknumber(L,2);
    int offset = (int)luaL_checknumber(L,3);
    GLubyte v0 = (GLushort)luaL_checknumber(L,4);
    GLubyte v1 = (GLushort)luaL_checknumber(L,5);
    
    data->vbd->set2ub(index,offset,v0,v1);
    return 0;    
}

/*
-- @name :set3ub
-- @func
-- @brief Sets three unsigned byte values of a vertex attribute.
-- @param index The index of vertex.
-- @param offset The offset within the vertex.
-- @param v0 The first value.
-- @param v1 The second value.
-- @param v2 The third value.
*/
static int vertexBufferDataSet3ub(lua_State *L) {
    VertexBufferDataType *data = checkVertexBufferDataType(L);
    int index = (int)luaL_checknumber(L,2);
    int offset = (int)luaL_checknumber(L,3);
    GLubyte v0 = (GLushort)luaL_checknumber(L,4);
    GLubyte v1 = (GLushort)luaL_checknumber(L,5);
    GLubyte v2 = (GLushort)luaL_checknumber(L,6);
    
    data->vbd->set3ub(index,offset,v0,v1,v2);
    return 0;    
}

/*
-- @name :set4ub
-- @func
-- @brief Sets four unsigned byte values of a vertex attribute.
-- @param index The index of vertex.
-- @param offset The offset within the vertex.
-- @param v0 The first value.
-- @param v1 The second value.
-- @param v2 The third value.
-- @param v3 The fourth value.
*/
static int vertexBufferDataSet4ub(lua_State *L) {
    VertexBufferDataType *data = checkVertexBufferDataType(L);
    int index = (int)luaL_checknumber(L,2);
    int offset = (int)luaL_checknumber(L,3);
    GLubyte v0 = (GLushort)luaL_checknumber(L,4);
    GLubyte v1 = (GLushort)luaL_checknumber(L,5);
    GLubyte v2 = (GLushort)luaL_checknumber(L,6);
    GLubyte v3 = (GLushort)luaL_checknumber(L,7);
    
    data->vbd->set4ub(index,offset,v0,v1,v2,v3);
    return 0;    
}

/*
-- @name :__gc
-- @func
-- @brief Destroys the vertex buffer data.
-- @full Destroys the vertex buffer data. Never call this function directly!
*/
static int vertexBufferData__gc(lua_State *L) {
    VertexBufferDataType *data = checkVertexBufferDataType(L);
    delete data->vbd;
    
    return 0;
}

// TODO Write __tostring metamethod.

/** The type functions. */
static const struct luaL_Reg vertexBufferDataFuncs[] = {
    {"new",vertexBufferDataNew},
    {0,0}
};

/** The type methods. */
static const struct luaL_Reg vertexBufferDataMethods[] = {
    {"getStride",vertexBufferDataGetStride},
    {"free",vertexBufferDataFree},
    
    {"set1f",vertexBufferDataSet1f},
    {"set2f",vertexBufferDataSet2f},
    {"set3f",vertexBufferDataSet3f},
    {"set4f",vertexBufferDataSet4f},
    
    {"set1us",vertexBufferDataSet1us},
    {"set2us",vertexBufferDataSet2us},
    {"set3us",vertexBufferDataSet3us},
    {"set4us",vertexBufferDataSet4us},
    
    {"set1ub",vertexBufferDataSet1ub},
    {"set2ub",vertexBufferDataSet2ub},
    {"set3ub",vertexBufferDataSet3ub},
    {"set4ub",vertexBufferDataSet4ub},
    
    {0,0}
};

/** The type metamethods. */
static const struct luaL_Reg vertexBufferDataMetamethods[] = {
    {"__gc",vertexBufferData__gc},
    {0,0}
};

/** */
static int vertexBufferDataRequireFunc(lua_State *L) {
    luaLoadUserType(L,vertexBufferDataMetatableName,
        vertexBufferDataFuncs,vertexBufferDataMethods,
        vertexBufferDataMetamethods);
    return 1;
}

/** */
void loadVertexBufferDataLib(lua_State *L) {
    luaL_requiref(L,vertexBufferDataName,vertexBufferDataRequireFunc,1);
    lua_pop(L,1);
}
    
} // namespace
    
} // namespace
    
} // namespace