#ifndef AE_ENGINE_REMOVE_LUA_MODEL_REQUEST_H
#define AE_ENGINE_REMOVE_LUA_MODEL_REQUEST_H

#include "EngineRequest.h"
#include "LuaEngine.h"
#include "LuaModel.h"

namespace ae {
    
namespace engine {
 
/**
 * \brief Removes a Lua model.
 */
class RemoveLuaModelRequest:public EngineRequest {
    /// The engine from which to remove.
    LuaEngine *luaEngine;
    
    /// The model from which to remove.
    LuaModel *luaModel;    
    
public:
    /**
     * \brief Constructs an RemoveLuaModelRequest.
     *
     * \param luaEngine_ The engine from which to remove.
     * \param luaModel_ The model from which to remove.
     */
    RemoveLuaModelRequest(LuaEngine *luaEngine_,LuaModel *luaModel_):
        EngineRequest(),luaEngine(luaEngine_),luaModel(luaModel_) {
    }
    
    /** */
    virtual bool run();
};
    
} // namespace
    
} // namespace

#endif // AE_ENGINE_REMOVE_LUA_MODEL_REQUEST_H