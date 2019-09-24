#ifndef AE_LUA_MODEL_H
#define AE_LUA_MODEL_H

#include <vector>
#include "lua.hpp"

#include "Runnable.h"
#include "LuaPCall.h"
#include "Model.h"

namespace ae {
    
namespace engine {
  
/**
 * \brief Model which calls Lua functions on draw and update.
 */
class LuaModel:public Model {
    /// The Lua state.
    lua_State *L;
    
    /// To call the draw and update functions
    ae::lua::LuaPCall luaPCall;    
    
    /// The runnable which gets the Lua draw function
    Runnable *getLuaDrawFunc;
    
    /// The runnable which gets the Lua update function
    Runnable *getLuaUpdateFunc;
    
public:
    /**
     * \brief Constructs a LuaModel.
     * \param L_ The Lua state.
     * \param getLuaDrawFunc_ The runnable which gets the draw function
     *     (destoryed in the destructor).
     * \param getLuaUpdateFunc_ The runnable which gets the update function
     *     (destoryed in the destructor).
     */
    LuaModel(lua_State *L_,Runnable *getLuaDrawFunc_,
        Runnable *getLuaUpdateFunc_):L(L_),luaPCall(),
        getLuaDrawFunc(getLuaDrawFunc_),getLuaUpdateFunc(getLuaUpdateFunc_) {
    }
    
    /** */
    virtual ~LuaModel() {
        delete getLuaDrawFunc;
        delete getLuaUpdateFunc;
    }
    
    /** */
    virtual void modelDraw();
    
    /** */
    virtual bool modelUpdate(long time);
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_MODEL_H