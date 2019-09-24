#ifndef AE_GLES2_VERTEX_BUFFER_OBJECT_H
#define AE_GLES2_VERTEX_BUFFER_OBJECT_H

#include "ae_GLES2.h"
#include "GLESError.h"

namespace ae {
    
namespace gles2 {
  
/**
 * \brief Represents a vertex buffer object.
 */
class VertexBufferObject:public GLESError {
    /// The GL buffer object name.
    GLuint glBuffer;
    
public:
    /**
     * \brief Constructs a VertexBufferObject.
     */
    VertexBufferObject():GLESError(),glBuffer(0) {
    }

    /** */
    ~VertexBufferObject() {
        deleteBuffer();
    }
    
    /**
     * \brief Generates the GL buffer object name.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> otherwise.
     */
    bool generate() {
        glGenBuffers(1,&glBuffer);
        return !chkGLError();
    }
    
    /**
     * \brief Binds the buffer to the given target.
     * \param target The GL target.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> otherwise.
     */
    bool bind(GLenum target) {
        glBindBuffer(target,glBuffer);
        return !chkGLError();
    }
    
    /**
     * \brief Deletes the buffer (if has been generated).
     */
    void deleteBuffer() {
        if (glBuffer != 0) {
            glDeleteBuffers(1,&glBuffer);
            glBuffer = 0;
        }
    }
};
    
} // namespace
    
} // namespace

#endif // AE_GLES2_VERTEX_BUFFER_OBJECT_H