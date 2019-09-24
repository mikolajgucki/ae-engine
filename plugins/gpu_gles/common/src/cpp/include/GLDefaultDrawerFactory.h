#ifndef AE_GL_DEFAULT_DRAWER_FACTORY_H
#define AE_GL_DEFAULT_DRAWER_FACTORY_H

#include "DefaultDrawerFactory.h"
#include "GLDefaultDrawerShader.h"

namespace ae {

namespace gles {
    
/**
 * \brief The OpenGL ES implementation of the default drawer factory.
 */
class GLDefaultDrawerFactory:public ::ae::gpu::DefaultDrawerFactory {
    /// The default drawer shader.
    GLDefaultDrawerShader defaultDrawerShader;
    
public:
    /** */
    GLDefaultDrawerFactory():DefaultDrawerFactory(),defaultDrawerShader() {
    }
    
    /** */
    virtual ~GLDefaultDrawerFactory() {
    }
    
    /** */
    virtual ::ae::gpu::DefaultDrawer *createDefaultDrawer(
        ::ae::gpu::DefaultDrawerFeatures features,int capacity);
};

} // namespace

} // namespace

#endif // AE_GL_DEFAULT_DRAWER_FACTORY_H