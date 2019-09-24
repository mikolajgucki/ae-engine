#ifndef AE_DESKTOP_ENGINE_SDL_H
#define AE_DESKTOP_ENGINE_SDL_H

#include <string>

#include "SDL.h"
#include "Error.h" 
#include "DesktopEngineCfg.h"

namespace ae {

namespace engine {
    
namespace desktop {
  
/**
 * \brief Contains the SDL stuff related to the desktop engine.
 */
class DesktopEngineSDL:public Error {
    /// The desktop engine configuration.
    DesktopEngineCfg *cfg;    
    
    /// The GL context.
    SDL_GLContext glContext;
    
    /// The window.
    SDL_Window *window;    
    
    /** */
    bool setSDLError();
    
    /** */
    bool initGLAttributes();
    
    /** */
    bool createGLContext();
    
    /** */
    void destroy();
    
public:
    /** */
    DesktopEngineSDL(DesktopEngineCfg *cfg_):Error(),cfg(cfg_),glContext(0),
        window((SDL_Window *)0) {
    }
    
    /** */
    ~DesktopEngineSDL() {
        destroy();
    }
    
    /**
     * \brief Initializes SDL.
     * \return <code>true</code> on success, <code>false</code> otherwise.
     */
    bool init();
    
    /**
     * \brief Creates a window.
     * \return <code>true</code> on success, <code>false</code> otherwise.
     */
    bool createWindow();
    
    /**
     * \brief Gets the window size.
     * \param width The result width.
     * \param height The result height.
     */
    void getWindowSize(int &width,int &height);
    
    /**
     * \brief Sets the window title.
     * \param title The window title.
     */
    void setWindowTitle(const std::string &title);
    
    /**
     * \brief Swaps the window buffer.
     */
    void swapWindow();
};
    
} // namespace

} // namespace

} // namespace

#endif // AE_DESKTOP_ENGINE_SDL_H