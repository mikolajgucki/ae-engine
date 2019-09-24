#include "AppendLuaModelRequest.h"

namespace ae {
    
namespace engine {

/** */
bool AppendLuaModelRequest::run() {
    luaEngine->addLuaModel(luaModel);
    luaEngine->getEngine()->appendModel(luaModel);
        
    return true;
}
    
} // namespace
    
} // namespace