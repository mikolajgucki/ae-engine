#ifndef AE_GLES2_GL_ERROR_H
#define AE_GLES2_GL_ERROR_H

#include "ae_GLES2.h"
#include "Error.h"

namespace ae {

namespace gles2 {
    
/**
 * \brief Handles GL errors.
 */
class GLESError:public Error {
public:
    /** */
    GLESError():Error() {
    }
    
    /** */
    virtual ~GLESError() {
    }
    
    /**
     * \brief Checks if GL error is set. Sets the GL error string representation
     *     as the error.
     * \return <code>true</code> if set, <code>false</code> otherwise.
     */
    bool chkGLError();
    
    /**
     * \brief Gets a string representing a GL error.
     * \param error The error returned by glGetError.
     * \return The string representation of the error.
     */
    static const char *getGLErrorStr(GLenum error);
};
    
} // namespace

} // namespace

#endif // AE_GLES2_GL_ERROR_H