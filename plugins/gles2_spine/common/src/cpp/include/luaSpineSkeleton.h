#ifndef AE_SPINE_LUA_SKELETON_H
#define AE_SPINE_LUA_SKELETON_H

#include <string>
#include "lua.hpp"
#include "spine/spine.h"
#include "DLog.h"

namespace ae {
    
namespace spine {
    
namespace lua {
    
/**
 * \brief Wraps the Spine objects so that it can be used as Lua user type.
 */
struct SpineSkeletonType {
    /** The skeleton identifier passed to the listener Lua functions. */
    char *id;
    
    /** The skeleton data. */
    spSkeletonData *data;
    
    /** */
    spSkeleton *skeleton;

    /** */    
    spAnimationStateData *stateData;
    
    /** */
    spAnimationState *state;
};
typedef struct SpineSkeletonType SpineSkeletonType;    
    
/**
 * \brief Checks wheter the object at given stack index is a user data of
 *     the Spine skeleton type. Raises error if the object is not of the type.
 * \param L The Lua state.
 * \param index The Lua stack index.
 * \return The user data of the Spine skeleton type. 
 */
SpineSkeletonType *checkSpineSkeletonType(lua_State *L,int index = 1);

/**
 * \brief Loads the Spine skeleton library.
 * \param L The Lua state.
 * \param log_ The log.
 */
void loadSpineSkeletonLib(lua_State *L,DLog *log_);
    
} // namespace
    
} // namespace
    
} // namespace

#endif // AE_SPINE_LUA_SKELETON_H