#include <cstdio>
#include <cstring>
#include "SDL_log.h"

#include "SDLLogger.h"

namespace ae {
    
/** */
void SDLLogger::trace(const char *tag,const char *msg) {
    SDL_LogVerbose(SDL_LOG_CATEGORY_APPLICATION,"[%s] %s",tag,msg);
}
    
/** */
void SDLLogger::debug(const char *tag,const char *msg) {
    SDL_LogDebug(SDL_LOG_CATEGORY_APPLICATION,"[%s] %s",tag,msg);
}
    
/** */
void SDLLogger::info(const char *tag,const char *msg) {
    SDL_LogInfo(SDL_LOG_CATEGORY_APPLICATION,"[%s] %s",tag,msg);
}
    
/** */
void SDLLogger::warning(const char *tag,const char *msg) {
    SDL_LogWarn(SDL_LOG_CATEGORY_APPLICATION,"[%s] %s",tag,msg);
}
    
/** */
void SDLLogger::error(const char *tag,const char *msg) {
    SDL_LogError(SDL_LOG_CATEGORY_APPLICATION,"[%s] %s",tag,msg);
}
    
} // namespace