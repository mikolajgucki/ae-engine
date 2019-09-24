#include "ae_platform.h"

#if defined(AE_ANDROID)
#include <GLES2/gl2.h>
#elif defined(AE_MACOSX)
#include <OpenGL/gl.h>
#elif defined(AE_IOS)
#include <OpenGLES/ES2/gl.h>
#elif defined(AE_WIN32)
#include <GL/glew.h>
#endif