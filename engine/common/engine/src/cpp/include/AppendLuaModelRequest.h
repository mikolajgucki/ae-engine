#ifndef AE_ENGINE_APPEND_LUA_MODEL_REQUEST_H
#define AE_ENGINE_APPEND_LUA_MODEL_REQUEST_H

#include "EngineRequest.h"
#include "LuaEngine.h"
#include "LuaModel.h"

namespace ae {   
    
namespace engine {
  
/**
 * \brief Prepends a Lua model.
 */
class AppendLuaModelRequest:public EngineRequest {
    /// The engine to which to add.
    LuaEngine *luaEngine;
    
    /// The model to which to add.
    LuaModel *luaModel;

public:
    /**
     * \brief Constructs an AppendLuaModelRequest.
     *
     * \param luaEngine_ The engine to which to add.
     * \param luaModel_ The model to which to add.
     */
    AppendLuaModelRequest(LuaEngine *luaEngine_,LuaModel *luaModel_):
        EngineRequest(),luaEngine(luaEngine_),luaModel(luaModel_) {
    }
    
    /** */
    virtual bool run();
};
    
} // namespace
    
} // namespace

#endif // AE_ENGINE_APPEND_LUA_MODEL_REQUEST_H