#include "RemoveLuaModelRequest.h"

namespace ae {
    
namespace engine {

/** */
bool RemoveLuaModelRequest::run() {
    luaEngine->removeLuaModel(luaModel);
    luaEngine->getEngine()->removeModel(luaModel);
        
    return true;
}
    
} // namespace
    
} // namespace