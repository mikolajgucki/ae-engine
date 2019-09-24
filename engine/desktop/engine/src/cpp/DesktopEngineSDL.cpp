#include "ae_platform.h"
#ifdef AE_MACOSX
#include <OpenGL/gl.h>
#else
#include <GL/gl.h>
#endif
#include "DebugLog.h"
#include "DesktopEngineSDL.h"

using namespace std;

/// The log tag for the debug log.
static const char *const debugLogTag = "DesktopEngineSDL";

namespace ae {

namespace engine {
    
namespace desktop {
 
/** */
static const char *getSDLError() {
    const char *error = SDL_GetError();
    if (error == (const char *)0) {
        return (const char *)0;
    }
    if (strcmp(error,"") == 0) {
        return (const char *)0;
    }
    
    return error;
}    

/** */
void DesktopEngineSDL::destroy() {
    if (glContext != 0) {
        SDL_GL_DeleteContext(glContext);
    }
    if (window != (SDL_Window *)0) {
        SDL_DestroyWindow(window);
    }
}

/** */
bool DesktopEngineSDL::setSDLError() {
    const char *error = getSDLError();
    if (error == (const char *)0) {
        return false;
    }
    
    setError(error);
    return true;
}

/** */
bool DesktopEngineSDL::initGLAttributes() {
    SDL_GL_SetAttribute(SDL_GL_RED_SIZE,8);
    SDL_GL_SetAttribute(SDL_GL_GREEN_SIZE,8);
    SDL_GL_SetAttribute(SDL_GL_BLUE_SIZE,8);
    
    SDL_GL_SetAttribute(SDL_GL_ALPHA_SIZE,0);
    SDL_GL_SetAttribute(SDL_GL_DOUBLEBUFFER,1);
    SDL_GL_SetAttribute(SDL_GL_BUFFER_SIZE,0);
    SDL_GL_SetAttribute(SDL_GL_DEPTH_SIZE,0);
    SDL_GL_SetAttribute(SDL_GL_STENCIL_SIZE,0);
    SDL_GL_SetAttribute(SDL_GL_ACCUM_RED_SIZE,0);
    SDL_GL_SetAttribute(SDL_GL_ACCUM_GREEN_SIZE,0);
    SDL_GL_SetAttribute(SDL_GL_ACCUM_BLUE_SIZE,0);
    SDL_GL_SetAttribute(SDL_GL_ACCUM_ALPHA_SIZE,0);
    SDL_GL_SetAttribute(SDL_GL_STEREO,0);
    SDL_GL_SetAttribute(SDL_GL_MULTISAMPLEBUFFERS,0);
    SDL_GL_SetAttribute(SDL_GL_MULTISAMPLESAMPLES,0);

// OpenGL 2.0
    SDL_GL_SetAttribute(SDL_GL_CONTEXT_MAJOR_VERSION,2);
    SDL_GL_SetAttribute(SDL_GL_CONTEXT_MINOR_VERSION,1);
    
    glPixelStorei(GL_UNPACK_ALIGNMENT,1);
    glPixelStorei(GL_PACK_ALIGNMENT,1);
    
    if (setSDLError() == true) {
        return false;
    }
    return true;
}

/** */
bool DesktopEngineSDL::createGLContext() {
    DebugLog::trace(debugLogTag,"createGLContext()");
    
    if (glContext != 0) {
        return true;
    }
    SDL_ClearError();     
    glContext = SDL_GL_CreateContext(window);
    if (glContext == 0) {
        setSDLError();
        return false;
    }
    
    return true;
}

/** */
bool DesktopEngineSDL::init() {
    DebugLog::trace(debugLogTag,"init()");
    
// init
    if (SDL_Init(SDL_INIT_VIDEO) < 0) {
        setSDLError();
        return false;
    }    
    
// GL attributes
    if (initGLAttributes() == false) {
        return false;
    }
    
// clear any pending errors
    SDL_ClearError();     
    
    return true;
}

/** */
bool DesktopEngineSDL::createWindow() {
    DebugLog::trace(debugLogTag,"createWindow()");
    
// default size
    int width = 320;
    int height = 480;
    
// user size
    if (cfg->hasWinSize()) {
        width = cfg->getWinWidth();
        height = cfg->getWinHeight();
    }    
// create window
    window = SDL_CreateWindow(
        "AE Engine", // title
        SDL_WINDOWPOS_UNDEFINED,SDL_WINDOWPOS_UNDEFINED, // position
        width,height, // size
        SDL_WINDOW_OPENGL | SDL_WINDOW_SHOWN); // flags
    if (window == (SDL_Window *)0) {
        setSDLError();
        return false;
    }
    
// GL context
    if (createGLContext() == false) {
        return false;
    }
    
    return true;
}
    
/** */
void DesktopEngineSDL::getWindowSize(int &width,int &height) {
    SDL_GetWindowSize(window,&width,&height);
}

/** */
void DesktopEngineSDL::setWindowTitle(const string &title) {
    SDL_SetWindowTitle(window,title.c_str());
}

/** */
void DesktopEngineSDL::swapWindow() {
    SDL_GL_SwapWindow(window);
}

} // namespace

} // namespace

} // namespace