#include <memory>

#include "luaVertexBufferData.h"
#include "luaVertexBufferObject.h"
#include "lua_gles2.h"
#include "luaGLSLShader.h"
#include "luaGLSLProgram.h"
#include "luaGLSLUniform.h"
#include "luaGLSLAttrib.h"
#include "luaGLESTexture.h"
#include "GLES2LuaEnginePlugin.h"

using namespace std;
using namespace ae::engine;
using namespace ae::gles2::lua;

namespace ae {
    
namespace gles2 {
    
/// The log tag.
static const char *logTag = "gles2";
    
/** */
bool GLES2LuaEnginePlugin::configureLuaEngine(LuaEngineCfg *cfg) {
// texture factory
    textureFactory = new (nothrow) GLESTextureFactory();
    if (textureFactory == (GLESTextureFactory *)0) {
        setNoMemoryError();
        return false;
    }
    cfg->setTextureFactory(textureFactory);

// display listener
    displayListener = new (nothrow) GLES2LuaEngineDisplayListener();
    if (displayListener == (GLES2LuaEngineDisplayListener *)0) {
        setNoMemoryError();
        return false;
    }
    cfg->setLuaEngineDisplayListener(displayListener);
    
    return true;
}

/** */
bool GLES2LuaEnginePlugin::start() {
    getLog()->trace(logTag,"Starting OpenGL ES 2.0 plugin");
    lua_State *L = getLuaState();
    
    
// Lua modules
    getLog()->info(logTag,"  - gl");
    loadGLES2Lib(L,getLog());
    
    getLog()->info(logTag,"  - GLSLShader");
    loadGLSLShaderLib(L);
    
    getLog()->info(logTag,"  - GLSLProgram");
    loadGLSLProgramLib(L);
    
    getLog()->info(logTag,"  - GLSLUniform");
    loadGLSLUniformLib(L);
    
    getLog()->info(logTag,"  - GLSLAttrib");
    loadGLSLAttribLib(L);
    
    getLog()->info(logTag,"  - VertexBufferData");    
    loadVertexBufferDataLib(L);
    
    getLog()->info(logTag,"  - VertexBufferObject");
    loadVertexBufferObjectLib(L);    
    
    getLog()->info(logTag,"  - Texture (GLES)");
    loadGLESTextureLib(L);
    
    return true;
}

/** */
bool GLES2LuaEnginePlugin::stop() {
    getLog()->trace(logTag,"Stopping OpenGL ES 2.0 plugin");
    
// delete display listener
    if (displayListener != (GLES2LuaEngineDisplayListener *)0) {
        delete displayListener;
    }
    
// delete texture factory
    if (textureFactory != (GLESTextureFactory *)0) {
        delete textureFactory;
    }
    
    return true;
}

} // namespace
    
} // namespace