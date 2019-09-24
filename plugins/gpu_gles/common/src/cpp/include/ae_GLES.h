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

/// Converts a float to a normalize GL unsighed short.
#define GL_TO_NORMALIZED_USHORT(value) (GLushort)(value * 65535)