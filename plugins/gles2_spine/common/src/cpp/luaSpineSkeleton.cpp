/*
-- @module spine.Skeleton
-- @group Animation
-- @brief Animates a [Spine](http://esotericsoftware.com) skeleton.
*/
#include <cstring>
#include <memory>
#include <sstream>

#include "spine/spine.h"
#include "lua_common.h"
#include "LuaGetField.h"
#include "LuaPCall.h"
#include "ByteColor.h"
#include "luaTexture.h"
#include "lua_spine.h"
#include "Mat4.h"
#include "luaMat4.h"
#include "SpineUtil.h"
#include "SpineAttachmentRendererObject.h"
#include "luaSpineSkeleton.h"

using namespace std;
using namespace ae;
using namespace ae::lua;
using namespace ae::math;
using namespace ae::math::lua;
using namespace ae::texture::lua;
using namespace ae::spine::lua;

/// The log tag.
static const char *logTag = "AE/Spine";

/// The name of the Lua global with the create texture function name.
static const char *skeletonGlobalCreateTexture =
    "ae_SpineSkeletonCreateTexture";

/// The name of the Lua global with the animation listener function name.
static const char *skeletonGlobalAnimationFunc =
    "ae_SpineSkeletonAnimationFunc";

/// The name of the Lua global with the track listener function name.
static const char *skeletonGlobalTrackFunc =
    "ae_SpineSkeletonTrackFunc";

/// The log.
static DLog *dlog = (DLog *)0;
    
/**
 * \brief The renderer object passed to Spine functions.
 */
struct SpineSkeletonTypeObject {
    /** */
    SpineSkeletonType *data;
    
    /** */
    lua_State *L;
};
typedef struct SpineSkeletonTypeObject SpineSkeletonTypeObject;

// Spine functions
extern "C" {

/** */
static void callCreateTexture(SpineSkeletonTypeObject *object,spAtlasPage* self,
    const char* path) {
// the Lua state
    lua_State *L = object->L;

    LuaPCall luaPCall;    
// push error handler to get traceback on error
    luaPCall.pushErrorHandler(L); 
    
// get the function name
    lua_getglobal(L,skeletonGlobalCreateTexture);
    const string createTextureFuncName = string(lua_tostring(L,-1));
    lua_pop(L,1);
    
// get the function
    LuaGetField getFunc(L,createTextureFuncName);
    if (getFunc.run() == false) {
        ostringstream err;
        err << "Failed to create texture: function " <<
            createTextureFuncName << " not found";
        dlog->error(logTag,err.str());
        return;
    }
    
// arguments
    lua_pushstring(L,path);
    
// call the function
    int callResult;
    if (luaPCall.tryCall(L,1,1,callResult) == false) {
        dlog->error(logTag,luaPCall.getError());
        return;
    }   
    
// get the result and store it in the atlas page
    TextureType *textureType = checkTextureType(L,-1);
    self->rendererObject = textureType;
    self->width = textureType->texture->getWidth();
    self->height = textureType->texture->getHeight();
}
    
/** */
void _spAtlasPage_createTexture (spAtlasPage* self, const char* path) {
    SpineSkeletonTypeObject *object =
        (SpineSkeletonTypeObject *)self->atlas->rendererObject;
    callCreateTexture(object,self,path);
}

/** */
void _spAtlasPage_disposeTexture (spAtlasPage* self) {
    dlog->trace(logTag,"Called _spAtlasPage_disposeTexture");
}

/** We read the files ourselves. */
char* _spUtil_readFile (const char* path, int* length) {
    return (char *)0;
}

} // extern "C"

namespace ae {
    
namespace spine {
    
namespace lua {
    
/// The library name.
static const char *skeletonName = "Skeleton";    
    
/// The name of the Lua metatable.
static const char *skeletonMetatableName = "SpineSkeleton.metatable";

/** */
SpineSkeletonType *checkSpineSkeletonType(lua_State *L,int index) {
    void *data = luaL_checkudata(L,index,skeletonMetatableName);
    luaL_argcheck(L,data != (void *)0,1,"Skeleton expected");
    return (SpineSkeletonType *)data;
}

/*
-- @name .init
-- @func
-- @brief Initializes the Spine skeleton library.
-- @param createTextureFuncName The name of the create texture function.
-- @func
-- @brief Initializes the Spine skeleton library.
-- @param createTextureFuncName The name of the create texture function.
-- @param animationFuncName The name of the animation listener function.
-- @param trackFuncName The name of the track listener function.
*/
static int skeletonInit(lua_State *L) {
// get texture function name
    if (lua_isstring(L,1) == 0) {
        luaPushError(L,"Missing create texture function name");
        return 0;
    }
    lua_pushvalue(L,1);
    lua_setglobal(L,skeletonGlobalCreateTexture);
    
// get animation listener function name if passed to the function
    if (lua_isnoneornil(L,2) == 0) {
        if (lua_isstring(L,2) == 0) {
            luaPushError(L,"Animation listener function is not a string");
            return 0;
        }
        lua_pushvalue(L,2);
        lua_setglobal(L,skeletonGlobalAnimationFunc);
    }
    
// get track listener function name if passed to the function
    if (lua_isnoneornil(L,3) == 0) {
        if (lua_isstring(L,3) == 0) {
            luaPushError(L,"Track listener function is not a string");
            return 0;
        }
        lua_pushvalue(L,3);
        lua_setglobal(L,skeletonGlobalTrackFunc);
    }
    
    return 0;
}

/*
-- @name .setAnimationListener
-- @func
-- @brief Sets the track listener.
-- @param trackFuncName The name of the track listener function.
*/
static int skeletonSetAnimationListener(lua_State *L) {
    if (lua_isstring(L,1) == 0) {
        luaPushError(L,"Missing track listener function name");
        return 0;
    }
    lua_pushvalue(L,1);
    lua_setglobal(L,skeletonGlobalAnimationFunc);
    
    return 0;
}

/*
-- @name .setTrackListener
-- @func
-- @brief Sets the animation listener.
-- @param animationFuncName The name of the animation listener function.
*/
static int skeletonSetTrackListener(lua_State *L) {
    if (lua_isstring(L,1) == 0) {
        luaPushError(L,"Missing animation listener function name");
        return 0;
    }
    lua_pushvalue(L,1);
    lua_setglobal(L,skeletonGlobalTrackFunc);
    
    return 0;
}


/** */
static const char *spEventTypeToStr(spEventType type) {
    if (type == SP_ANIMATION_START) {
        return "SP_ANIMATION_START";
    }
    if (type == SP_ANIMATION_END) {
        return "SP_ANIMATION_END";
    }
    if (type == SP_ANIMATION_COMPLETE) {
        return "SP_ANIMATION_COMPLETE";
    }
    if (type == SP_ANIMATION_EVENT) {
        return "SP_ANIMATION_EVENT";
    }

    return "UNKNOWN";    
}

/** */
static void callListenerFunc(const char *globalWithFuncName,
    spAnimationState *state,int trackIndex,spEventType type,spEvent *event,
    int loopCount) {
//
    SpineSkeletonTypeObject *rendererObject =
        (SpineSkeletonTypeObject *)state->rendererObject;
        
// don't call the function if the skeleton doesn't have an identifier
    /*if (rendererObject->data->id == (const char *)0) {
        return;
    }*/

// the Lua state
    lua_State *L = rendererObject->L;

    LuaPCall luaPCall;
// push error handler to get traceback on error
    luaPCall.pushErrorHandler(L); 
    
// get the function name
    lua_getglobal(L,globalWithFuncName);
    if (lua_isnil(L,-1)) {
        // the function is not set
        return;
    }
    const string funcName = string(lua_tostring(L,-1));
    lua_pop(L,1);
    
// get the function
    LuaGetField getFunc(L,funcName);
    if (getFunc.run() == false) {
        ostringstream err;
        err << "Function " << funcName << " not found";
        dlog->error(logTag,err.str());
        return;
    }
    
// arguments
    lua_pushstring(L,rendererObject->data->id);
    lua_pushstring(L,spEventTypeToStr(type));
    if (event != (spEvent *)0) {
        lua_pushstring(L,event->data->name);
        lua_pushnumber(L,event->intValue);
        lua_pushnumber(L,event->floatValue);
        lua_pushstring(L,event->stringValue);
    }
    else {
        lua_pushnil(L); // name
        lua_pushnil(L); // intValue
        lua_pushnil(L); // floatValue
        lua_pushnil(L); // stringValue
    }
    lua_pushnumber(L,trackIndex);
    lua_pushnumber(L,loopCount);
    
// call the function
    int callResult;
    if (luaPCall.tryCall(L,8,0,callResult) == false) {
        dlog->error(logTag,luaPCall.getError());
        return;
    }  
}

/** */
static void animationListener(spAnimationState *state,int trackIndex,
    spEventType type,spEvent *event,int loopCount) {
//
    callListenerFunc(skeletonGlobalAnimationFunc,
        state,trackIndex,type,event,loopCount);
}

/** */
static void trackListener(spAnimationState *state,int trackIndex,
    spEventType type,spEvent *event,int loopCount) {
//
    callListenerFunc(skeletonGlobalTrackFunc,
        state,trackIndex,type,event,loopCount);
}

/** */
static bool createAttachmentRendererObjects(spSkeleton *skeleton) {
    vector<spAttachment *> attachments = SpineUtil::getAttachments(skeleton);
    for (size_t index = 0; index < attachments.size(); index++) {
        spAttachment *attachment = attachments[index];
        if (createAttachmentRendererObject(attachment) == false) {
            return false;
        }         
    }
    
    return true;
}

/** */
static void deleteAttachmentRendererObjects(spSkeleton *skeleton) {
    vector<spAttachment *> attachments = SpineUtil::getAttachments(skeleton);
    for (size_t index = 0; index < attachments.size(); index++) {
        spAttachment *attachment = attachments[index];
        
        SpineAttachmentRendererObject *object =
            getAttachmentRendererObject(attachment);
        if (object != (SpineAttachmentRendererObject *)0) {
            delete object;
        }
    }
}

/*
-- @name .load
-- @func
-- @brief Loads a skeleton from JSON and atlas data.
-- @param jsonData The JSON data.
-- @param atlasData The atlas data.
-- @param scale The scale.
-- @return The loaded skeleton.
*/
static int skeletonLoad(lua_State *L) {
    const char *jsonData = luaL_checkstring(L,1);
    const char *atlasData = luaL_checkstring(L,2);
    float scale = (float)luaL_checknumber(L,3);
    
// identifier
    const char *id = (const char *)0;
    if (lua_isstring(L,4) != 0) {
        id = luaL_checkstring(L,4);
    }
    
// rendered object
    SpineSkeletonTypeObject *rendererObject =
        new (nothrow) SpineSkeletonTypeObject();
    if (rendererObject == (SpineSkeletonTypeObject *)0) {
        luaPushNoMemoryError(L);
        return 0;
    }
    rendererObject->L = L;
    
// atlas
    spAtlas *atlas = spAtlas_create(
        atlasData,(int)strlen(atlasData),"",rendererObject);
    
// read from JSON
    spSkeletonJson *json = spSkeletonJson_create(atlas);
    json->scale = scale;
    spSkeletonData *skeletonData = spSkeletonJson_readSkeletonData(
        json,jsonData);
    spSkeletonJson_dispose(json);
    
// skeleton
    spSkeleton *skeleton = spSkeleton_create(skeletonData);
    
// animation state
    spAnimationStateData *stateData = spAnimationStateData_create(skeletonData);
    spAnimationState *state = spAnimationState_create(stateData);
    state->rendererObject = rendererObject;
    state->listener = animationListener;
    
// user data
    SpineSkeletonType *data = (SpineSkeletonType *)lua_newuserdata(
        L,sizeof(SpineSkeletonType));
    data->data = skeletonData;
    data->skeleton = skeleton;
    data->stateData = stateData;
    data->state = state;
    
// copy identifier
    data->id = new (nothrow) char[strlen(id) + 1];
    if (data->id == (char *)0) {
    // delete the skeleton
        spAnimationState_dispose(state);
        spAnimationStateData_dispose(stateData);
        spSkeleton_dispose(skeleton);
        spSkeletonData_dispose(skeletonData);
        
        luaPushNoMemoryError(L);
        return 0;        
    }
    strcpy(data->id,id);
    
// update rendered object
    rendererObject->data = data;
    if (createAttachmentRendererObjects(skeleton) == false) {
    // delete the skeleton
        spAnimationState_dispose(state);
        spAnimationStateData_dispose(stateData);
        spSkeleton_dispose(skeleton);
        spSkeletonData_dispose(skeletonData);
        
        luaPushNoMemoryError(L);
        return 0;          
    }
    
// metatable
    luaL_getmetatable(L,skeletonMetatableName);
    lua_setmetatable(L,-2);
    
    return 1;
}

/*
-- @name :getWidth
-- @func
-- @brief Gets the skeleton width.
-- @return The skeleton width.
*/
static int skeletonGetWidth(lua_State *L) {
    SpineSkeletonType *data = checkSpineSkeletonType(L);    
    lua_pushnumber(L,data->data->width);
    return 1;    
}

/*
-- @name :getHeight
-- @func
-- @brief Gets the skeleton height.
-- @return The skeleton height.
*/
static int skeletonGetHeight(lua_State *L) {
    SpineSkeletonType *data = checkSpineSkeletonType(L);    
    lua_pushnumber(L,data->data->height);
    return 1;    
}

/*
-- @name :setAttachmentColor
-- @func
-- @brief Set the attachment color to white.
-- @param name The attachment name.
-- @func
-- @brief Set the attachment color used to draw the attachment.
-- @param name The attachment name.
-- @param r The red component (0-255).
-- @param g The green component (0-255).
-- @param b The blue component (0-255).
-- @param a The alpha component (0-255).
*/
static int skeletonSetAttachmentColor(lua_State *L) {
    SpineSkeletonType *data = checkSpineSkeletonType(L);
    const char *name = luaL_checkstring(L,2);
    
// color
    ByteColor color;
    ByteColor::fromLua(L,3,4,5,6,color);
    
    bool set = false;
    vector<spAttachment *> attachments = SpineUtil::getAttachments(
        data->skeleton);
    for (size_t index = 0; index < attachments.size(); index++) {
        spAttachment *attachment = attachments[index];
        if (strcmp(attachment->name,name) == 0) {
            SpineAttachmentRendererObject *object =
                getAttachmentRendererObject(attachment);
            if (object != (SpineAttachmentRendererObject *)0) {
                object->color.set(color);
                set = true;
            }            
        }         
    }
    
    if (set == false) {
        ostringstream warn;
        warn << "Spine attachment of name " << name << " not found";
        dlog->warning(logTag,warn.str());
    }
    
    return 0;
}

/*
-- @name :setSkin
-- @func
-- @brief Sets the skin.
-- @param name The skin name.
*/
static int skeletonSetSkin(lua_State *L) {
    SpineSkeletonType *data = checkSpineSkeletonType(L);
    const char *name = luaL_checkstring(L,2);
    
    if (spSkeleton_setSkinByName(data->skeleton,name) == 0) {
        ostringstream err;
        err << "Skin not found " << name;
        luaPushError(L,err.str().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :setAnimation
-- @func
-- @brief Sets the current animation with track equals to 0.
-- @param name The animation name.
-- @func
-- @brief Sets the current animation.
-- @param name The animation name.
-- @param track The track index.
-- @param loop Indicates if to loop the animation.
*/
static int skeletonSetAnimation(lua_State *L) {
    SpineSkeletonType *data = checkSpineSkeletonType(L);    
    const char *name = luaL_checkstring(L,2);
    
// check if the animation exists
    if (spSkeletonData_findAnimation(data->data,name) == (spAnimation *)0) {
        ostringstream err;
        err << "Animation " << name << " not found";
        luaPushError(L,err.str().c_str());
        return 0;
    }
    
    int track = 0;
    if (lua_isnoneornil(L,3) == 0) {
        track = (int)luaL_checknumber(L,3);
    }
    
    int loop = 0;
    if (lua_isnoneornil(L,4) == 0) {
        loop = lua_toboolean(L,4);
    }
    
    spTrackEntry *entry = spAnimationState_setAnimationByName(
        data->state,track,name,loop);
    entry->listener = trackListener;
    
    return 0;
}

/*
-- @name :addAnimation
-- @func
-- @brief Adds an animation with track equals to 0.
-- @param name The animation name.
-- @func
-- @brief Adds an animation.
-- @param name The animation name.
-- @param track The track index.
-- @param loop Indicates if to loop the animation.
-- @param delay The delay in milliseconds after which to start animation.
*/
static int skeletonAddAnimation(lua_State *L) {
    SpineSkeletonType *data = checkSpineSkeletonType(L);    
    const char *name = luaL_checkstring(L,2);
    
// check if the animation exists
    if (spSkeletonData_findAnimation(data->data,name) == (spAnimation *)0) {
        ostringstream err;
        err << "Animation " << name << " not found";
        luaPushError(L,err.str().c_str());
        return 0;
    }    
    
    int track = 0;
    if (lua_isnoneornil(L,3) == 0) {
        track = (int)luaL_checknumber(L,3);
    }
    
    int loop = 0;
    if (lua_isnoneornil(L,4) == 0) {
        loop = (int)luaL_checknumber(L,4);
    }
    
    int delay = 0;
    if (lua_isnoneornil(L,5) == 0) {
        delay = (int)luaL_checknumber(L,5);
    }
    
    spTrackEntry *entry = spAnimationState_addAnimationByName(
        data->state,track,name,loop,(float)delay / 1000);
    entry->listener = trackListener;
    
    return 0;    
}

/*
-- @name :getBoneTransformation
-- @func
-- @brief Gets bone transformation and stores it in a 4x4 matrix.
-- @param name The bone name.
-- @param matrix The matrix.
*/
static int skeletonGetBoneTransformation(lua_State *L) {
    SpineSkeletonType *data = checkSpineSkeletonType(L);
    const char *name = luaL_checkstring(L,2);
    Mat4Type *matrix = checkMat4Type(L,3);
    
// get bone
    spBone *bone = spSkeleton_findBone(data->skeleton,name);
    if (bone == (spBone *)0) {
        lua_pushboolean(L,AE_LUA_FALSE);
        return 1;
    }
    
// fill matrix
    matrix->matrix->set(
        bone->m00,bone->m01,0,bone->worldX,
        bone->m10,bone->m11,0,bone->worldY,
        0,0,1,0,
        0,0,0,1);
    
    lua_pushboolean(L,AE_LUA_TRUE);
    return 1;
}

/*
-- @name :update
-- @func
-- @brief Updates the skeleton animation.
-- @param time The time in milliseconds elapsed since the last frame.
*/
static int skeletonUpdate(lua_State *L) {
    SpineSkeletonType *data = checkSpineSkeletonType(L);
    double milliseconds = (double)luaL_checknumber(L,2);
    double seconds = milliseconds / (double)1000;
    
    spAnimationState_update(data->state,seconds);
    spAnimationState_apply(data->state,data->skeleton);
    spSkeleton_updateWorldTransform(data->skeleton);
    
    return 0;
}

/*
-- @name :__gc
-- @func
-- @brief Destroys the skeleton.
-- @full Destroys the skeleton. Never call this function directly.
*/
static int skeleton__gc(lua_State *L) {
    SpineSkeletonType *data = checkSpineSkeletonType(L);
    deleteAttachmentRendererObjects(data->skeleton);
    
    spAnimationState_dispose(data->state);
    spAnimationStateData_dispose(data->stateData);
    spSkeleton_dispose(data->skeleton);
    spSkeletonData_dispose(data->data);
    delete[] data->id;
    
    return 0;
}

/*
-- @name :__tostring
-- @func
-- @brief Gets the string representation of the skeleton.
-- @return The string representation of the skeleton.
*/
static int skeleton__tostring(lua_State *L) {
    ostringstream str;  
    str << getSpineLibName() << ".Skeleton []";
    
    lua_pushstring(L,str.str().c_str());
    return 1;
}

/** The type functions. */
static const struct luaL_Reg skeletonFuncs[] = {
    {"init",skeletonInit},
    {"setAnimationListener",skeletonSetAnimationListener},
    {"setTrackListener",skeletonSetTrackListener},
    {"load",skeletonLoad},
    {0,0}
};

/** The type methods. */
static const struct luaL_Reg skeletonMethods[] = {
    {"getWidth",skeletonGetWidth},
    {"getHeight",skeletonGetHeight},
    {"setAttachmentColor",skeletonSetAttachmentColor},
    {"setSkin",skeletonSetSkin},
    {"setAnimation",skeletonSetAnimation},
    {"addAnimation",skeletonAddAnimation},
    {"getBoneTransformation",skeletonGetBoneTransformation},
    {"update",skeletonUpdate},
    {0,0}
};

/** The type metamethods. */
static const struct luaL_Reg skeletonMetamethods[] = {
    {"__gc",skeleton__gc},
    {"__tostring",skeleton__tostring},
    {0,0}
};

/** */
static int skeletonRequireFunc(lua_State *L) {
    luaLoadUserType(L,skeletonMetatableName,
        skeletonFuncs,skeletonMethods,skeletonMetamethods);
    return 1;    
}

/** */
void loadSpineSkeletonLib(lua_State *L,DLog *log_) {
    dlog = log_;
    
// get the Spine global table
    lua_getglobal(L,getSpineLibName());
    
// push the table key
    lua_pushstring(L,skeletonName);
    
// load the library (leaves it on the stack)
    luaL_requiref(L,skeletonName,skeletonRequireFunc,0);
    
// set the library in the Spine global table
    lua_settable(L,-3);
    
// remove the library from the stack
    lua_pop(L,1);    
}

} // namespace

} // namespace
    
} // namespace