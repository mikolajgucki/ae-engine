/*
-- @module gpu.DefaultDrawer
-- @group GPU
-- @brief The default drawer.
*/
#include <sstream>

#include "lua_common.h"
#include "luaMat4.h"
#include "luaTexture.h"
#include "ae_gpu.h"
#include "luaDrawerIndex.h"
#include "luaGPUQueue.h"
#include "DefaultDrawerFeatures.h"
#include "luaDefaultDrawer.h"

using namespace std;
using namespace ae::lua;
using namespace ae::math;
using namespace ae::math::lua;
using namespace ae::engine;
using namespace ae::texture;
using namespace ae::texture::lua;

namespace ae {

namespace gpu {

namespace lua {
  
/**
 * \brief Wraps the default drawer so that it can be used as Lua user type.
 */
struct DefaultDrawerType {
    /** */
    DefaultDrawer *drawer;
};
typedef struct DefaultDrawerType DefaultDrawerType;
    
/// The library name.
static const char *defaultDrawerName = "DefaultDrawer";    
    
/// The name of the Lua metatable.
static const char *defaultDrawerMetatableName = "DefaultDrawer.metatable";

/// The name of the Lua global holding the default drawer factory.
static const char *defaultDrawerGlobalFactory = "gpu_DefaultDrawerFactory";

/**
 * \brief Gets the default drawer factory from the Lua state.
 * \param L The Lua state.
 * \return The default drawer factory.
 */
static DefaultDrawerFactory *getDefaultDrawerFactory(lua_State *L) {
    lua_getglobal(L,defaultDrawerGlobalFactory);
    DefaultDrawerFactory *factory =
        (DefaultDrawerFactory *)lua_touserdata(L,-1);
    lua_pop(L,1);
    return factory;
}

/** */
static DefaultDrawerType *checkDefaultDrawerType(lua_State *L) {
    void *data = luaL_checkudata(L,1,defaultDrawerMetatableName);
    luaL_argcheck(L,data != (void *)0,1,"DefaultDrawer expected");
    return (DefaultDrawerType *)data;
}  

/*
-- @name .new
-- @func
-- @brief Creates a default drawer.
-- @param features The comma-separated list of features.
-- @param capacity The number of vertices supported by the drawer.
-- @return The created default drawer. 
*/
static int defaultDrawerNew(lua_State *L) {
    DefaultDrawerFactory *factory = getDefaultDrawerFactory(L);
    
// features
    const char *featuresStr = luaL_checkstring(L,1);
    DefaultDrawerFeatures features;
    if (DefaultDrawerFeatures::fromString(featuresStr,&features) == false) {
        ostringstream err;
        err << "Invalid default drawer features: " << featuresStr;
        luaPushError(L,err.str().c_str());
        return 0;
    }
    
// capacity
    int capacity = (int)luaL_checkinteger(L,2);
    
// create
    DefaultDrawer *drawer = factory->createDefaultDrawer(features,capacity);
    if (drawer == (DefaultDrawer *)0) {
        luaPushError(L,factory->getError().c_str());
        return 0;
    }
    
// user data
    DefaultDrawerType *data = (DefaultDrawerType *)lua_newuserdata(
        L,sizeof(DefaultDrawerType));
    data->drawer = drawer;
    
// metatable
    luaL_getmetatable(L,defaultDrawerMetatableName);
    lua_setmetatable(L,-2);    
    
    return 1;
}

/*
-- @name :activate
-- @func
-- @brief Makes the drawer active so that all the following operations
--   changing the data are made on the drawer.
*/
static int defaultDrawerActivate(lua_State *L) {
    DefaultDrawerType *data = checkDefaultDrawerType(L);
    
// activate
    if (data->drawer->activate() == false) {
        luaPushError(L,data->drawer->getError().c_str());
        return 0;        
    }
    
    return 0;
}

/*
-- @name :setColor
-- @func
-- @brief Sets the color applied to all the vertices.
-- @param r The red component.
-- @param g The green component.
-- @param b The blue component.
-- @param a The alpha component.
*/
static int defaultDrawerSetColor(lua_State *L) {
    DefaultDrawerType *data = checkDefaultDrawerType(L);
    
// color
    Color color(Color::WHITE);
    Color::fromLua(L,2,3,4,5,color);
    
    data->drawer->setColor(color);
    return 0;
}

/*
-- @name :setVertexCoords
-- @func
-- @brief Sets vertex coordinates.
-- @param position The vertex position.
-- @param x The X coordinate.
-- @param y The Y coordinate.
*/
static int defaultDrawerSetVertexCoords(lua_State *L) {
    DefaultDrawerType *data = checkDefaultDrawerType(L);
    
// position
    int position = (int)luaL_checkinteger(L,2);

// coordinates
    float x = (float)luaL_checknumber(L,3);
    float y = (float)luaL_checknumber(L,4);
    
// set coordinates
    if (data->drawer->setVertexCoords(position,x,y) == false) {
        luaPushError(L,data->drawer->getError().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :setVertexColor
-- @func
-- @brief Sets a vertex color.
-- @param position The vertex position.
-- @param r The red component.
-- @param g The green component.
-- @param b The blue component.
-- @param a The alpha component.
*/
static int defaultDrawerSetVertexColor(lua_State *L) {
    DefaultDrawerType *data = checkDefaultDrawerType(L);
    
// position
    int position = (int)luaL_checkinteger(L,2);
   
// color
    Color color(Color::WHITE);
    Color::fromLua(L,3,4,5,6,color);
    
// set vertex color
    if (data->drawer->setVertexColor(position,color) == false) {
        luaPushError(L,data->drawer->getError().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :setTextureCoords
-- @func
-- @brief Sets texture coordinates.
-- @param position The vertex position.
-- @param u The U coordinate.
-- @param v The V coordinate.
*/
static int defaultDrawerSetTextureCoords(lua_State *L) {
    DefaultDrawerType *data = checkDefaultDrawerType(L);
    
// position
    int position = (int)luaL_checkinteger(L,2);

// coordinates
    float u = (float)luaL_checknumber(L,3);
    float v = (float)luaL_checknumber(L,4);
    
// set coordinates
    if (data->drawer->setTextureCoords(position,u,v) == false) {
        luaPushError(L,data->drawer->getError().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :setCoords
-- @func
-- @brief Sets vertex and texture coordinates.
-- @param position The vertex position.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @param u The U coordinate.
-- @param v The V coordinate.
*/
static int defaultDrawerSetCoords(lua_State *L) {
    DefaultDrawerType *data = checkDefaultDrawerType(L);
    
// position
    int position = (int)luaL_checkinteger(L,2);

// coordinates
    float x = (float)luaL_checknumber(L,3);
    float y = (float)luaL_checknumber(L,4);
    float u = (float)luaL_checknumber(L,5);
    float v = (float)luaL_checknumber(L,6);
    
// set coordinates
    if (data->drawer->setCoords(position,x,y,u,v) == false) {
        luaPushError(L,data->drawer->getError().c_str());
        return 0;
    }
    
    return 0;    
}

/*
-- @name :enqueue
-- @func
-- @brief Enqueues a command to draw triangles.
-- @param queue The queue.
-- @param first The position of the first vertex to draw.
-- @param count The count of the vertices to draw.
-- @func
-- @brief Enqueues a command to draw triangles.
-- @param queue The queue.
-- @param first The position of the first vertex to draw.
-- @param count The count of the vertices to draw.
-- @param drawerIndex The drawer index.
-- @func
-- @brief Enqueues a command to draw textured triangles.
-- @param queue The queue.
-- @param first The position of the first vertex to draw.
-- @param count The count of the vertices to draw.
-- @param texture The texture.
-- @func
-- @brief Enqueues a command to draw textured triangles.
-- @param queue The queue.
-- @param first The position of the first vertex to draw.
-- @param count The count of the vertices to draw.
-- @param drawerIndex The drawer index.
-- @param texture The texture.
-- @func
-- @brief Enqueues a command to draw textured triangles.
-- @param queue The queue.
-- @param first The position of the first vertex to draw.
-- @param count The count of the vertices to draw.
-- @param texture The texture.
-- @param transformation The transformation matrix.
-- @func
-- @brief Enqueues a command to draw textured triangles.
-- @param queue The queue.
-- @param first The position of the first vertex to draw.
-- @param count The count of the vertices to draw.
-- @param drawerIndex The drawer index.
-- @param texture The texture.
-- @param transformation The transformation matrix.
*/
static int defaultDrawerEnqueue(lua_State *L) {
    DefaultDrawerType *data = checkDefaultDrawerType(L);
    GPUQueueType *queueData = checkGPUQueueType(L,2);
    
// first, count
    int first = (int)luaL_checkinteger(L,3);
    int count = (int)luaL_checkinteger(L,4);
    
// drawer index
    DrawerIndex *drawerIndex = (DrawerIndex *)0;
    if (lua_isnoneornil(L,5) == 0) {
        DrawerIndexType *drawerIndexData = checkDrawerIndexType(L,5);
        drawerIndex = drawerIndexData->index;
    }
    
// texture
    Texture *texture = (Texture *)0;
    if (lua_isnoneornil(L,6) == 0) {
        TextureType *textureData = checkTextureType(L,6);
        texture = textureData->texture;
    }
    
// transformation
    Mat4 *transformation = (Mat4 *)0;
    if (lua_isnoneornil(L,7) == 0) {
        Mat4Type *mat4Data = checkMat4Type(L,7);
        transformation = mat4Data->matrix;
    }

// enqueue
    if (data->drawer->enqueue(queueData->queue,first,count,drawerIndex,
        texture,transformation) == false) {
    //
        luaPushError(L,data->drawer->getError().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :moveToGPU
-- @func
-- @brief Moves the buffer to GPU. All the changes to the buffer will be also
--   made on the GPU side. 
*/
static int defaultDrawerMoveToGPU(lua_State *L) {
    DefaultDrawerType *data = checkDefaultDrawerType(L);
    
// move
    if (data->drawer->moveToGPU() == false) {
        luaPushError(L,data->drawer->getError().c_str());
        return 0;        
    }
    
    return 0;
}

/*
-- @name :deleteCPUData
-- @func
-- @brief Deletes the data kept on the CPU side. From now on all the changes to
-    the buffer will be only made on the GPU side. 
*/
static int defaultDrawerDeleteCPUData(lua_State *L) {
    DefaultDrawerType *data = checkDefaultDrawerType(L);
    
// move
    if (data->drawer->deleteCPUData() == false) {
        luaPushError(L,data->drawer->getError().c_str());
        return 0;        
    }
    
    return 0;
}

/*
-- @name :__tostring
-- @func
-- @brief Gets the string representation of the default drawer.
-- @return The string representation of the default drawer.
*/
static int defaultDrawer__tostring(lua_State *L) {
    DefaultDrawerType *data = checkDefaultDrawerType(L);    
    
    ostringstream str; 
    str << getGPUName() << "." << defaultDrawerName << " [features=" <<
        data->drawer->getFeatures().toString() << ", capacity=" <<
        data->drawer->getCapacity() << "]";
    
    lua_pushstring(L,str.str().c_str());
    return 1;
}

/*
-- @name :__gc
-- @func
-- @brief Destroys the default drawer.
-- @full Destroys the default drawer. Never call this function directly.
*/
static int defaultDrawer__gc(lua_State *L) {
    DefaultDrawerType *data = checkDefaultDrawerType(L);
    
// check if already deleted
    if (data->drawer == (DefaultDrawer *)0) {
        return 0;
    }
    
// delete
    delete data->drawer;
    data->drawer = (DefaultDrawer *)0;
    
    return 0;
}

/** The type functions. */
static const struct luaL_Reg defaultDrawerFuncs[] = {
    {"new",defaultDrawerNew},
    {0,0}
};

/** The type methods. */
static const struct luaL_Reg defaultDrawerMethods[] = {
    {"activate",defaultDrawerActivate},
    {"setColor",defaultDrawerSetColor},
    {"setVertexCoords",defaultDrawerSetVertexCoords},
    {"setVertexColor",defaultDrawerSetVertexColor},
    {"setTextureCoords",defaultDrawerSetTextureCoords},
    {"setCoords",defaultDrawerSetCoords},
    {"moveToGPU",defaultDrawerMoveToGPU},
    {"deleteCPUData",defaultDrawerDeleteCPUData},
    {"enqueue",defaultDrawerEnqueue},
    {0,0}
};

/** The type metamethods. */
static const struct luaL_Reg defaultDrawerMetamethods[] = {
    {"__tostring",defaultDrawer__tostring},
    {"__gc",defaultDrawer__gc},
    {0,0}
};

/** */
static int defaultDrawerRequireFunc(lua_State *L) {
    luaLoadUserType(L,defaultDrawerMetatableName,
        defaultDrawerFuncs,defaultDrawerMethods,defaultDrawerMetamethods);
    return 1;     
}

/** */
void loadDefaultDrawerLib(LuaEngine *luaEngine,
    DefaultDrawerFactory *defaultDrawerFactory,Error *error) {
// Lua state
    lua_State *L = luaEngine->getLuaState();
    
// global with the default drawer factory
    lua_pushlightuserdata(L,defaultDrawerFactory);
    lua_setglobal(L,defaultDrawerGlobalFactory);    
    
// get the GPU global table
    lua_getglobal(L,getGPUName());
    
// push the table key
    lua_pushstring(L,defaultDrawerName);
    
// load the library (leaves it on the stack)
    luaL_requiref(L,defaultDrawerName,defaultDrawerRequireFunc,0);
    
// set the library in the GPU global table
    lua_settable(L,-3);
    
// remove the library from the stack
    lua_pop(L,1);      
}
    
} // namespace
    
} // namespace

} // namespace