#include <sstream>

#include "Log.h"
#include "screenshot.h"
#include "lua_common.h"
#include "lua_screenshot.h"

using namespace std;
using namespace ae::lua;

namespace ae {
    
namespace engine {
    
namespace desktop {
    
/** The log tag. */
static const char *logTag = "screenshot";

/** */
static Screenshot screenshot;

/** The engine. */
static Engine *engine = (Engine *)0;
    
/** */
static int screenshotLuaFunc(lua_State *L) {
    const char *filename = luaL_checkstring(L,1);
    
// log
    ostringstream log;
    log << "Taking screenshot " << filename;
    Log::trace(logTag,log.str());
    
// take screenshot
    screenshot.setSize(engine->getWidth(),engine->getHeight());
    screenshot.take(filename);
    if (screenshot.chkError() == true) {
        luaPushError(L,screenshot.getError().c_str());
        return 0;
    }
    
    return 0;
}
    
/** */
void loadLuaScreenshotFunc(lua_State *L,Engine *engine_) {
    engine = engine_;
    lua_register(L,"screenshot",screenshotLuaFunc);
}
    
} // namespace

} // namespace
    
} // namespace