#ifndef AE_GL_DRAWER_INDEX_FACTORY_H
#define AE_GL_DRAWER_INDEX_FACTORY_H

#include "DrawerIndexFactory.h"

namespace ae {

namespace gles {
  
/**
 * \brief The OpenGL implementation of the drawer index factory.
 */
class GLDrawerIndexFactory:public ::ae::gpu::DrawerIndexFactory {
public:
    /** */
    GLDrawerIndexFactory():DrawerIndexFactory() {
    }
    
    /** */
    virtual ~GLDrawerIndexFactory() {
    }
    
    /** */
    virtual ::ae::gpu::DrawerIndex *createDrawerIndex(int capacity);
};
    
} // namespace

} // namespace

#endif // AE_GL_DRAWER_INDEX_FACTORY_H