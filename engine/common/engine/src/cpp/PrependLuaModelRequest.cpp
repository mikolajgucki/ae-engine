#include "PrependLuaModelRequest.h"

namespace ae {
    
namespace engine {

/** */
bool PrependLuaModelRequest::run() {
    luaEngine->addLuaModel(luaModel);
    luaEngine->getEngine()->prependModel(luaModel);
        
    return true;
}
    
} // namespace
    
} // namespace