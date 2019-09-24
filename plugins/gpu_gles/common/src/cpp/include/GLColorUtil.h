#ifndef AE_GL_COLOR_UTIL_H
#define AE_GL_COLOR_UTIL_H

#include "ae_GLES.h"
#include "Color.h"

namespace ae {
    
namespace gles {
  
/**
 * \brief Provides utility color-related methods.
 */
class GLColorUtil {
public:
    /**
     * \brief Specifies the color of a uniform variable.
     * \param location The variable location.
     * \param color The color.
     */
    static void uniform(GLint location,const ae::math::Color &color) {
        glUniform4f(location,(GLfloat)color.getRed(),(GLfloat)color.getGreen(),
        (GLfloat)color.getBlue(),(GLfloat)color.getAlpha());
    }
};
    
} // namespace
    
} // namespace

#endif // AE_GL_COLOR_UTIL_H