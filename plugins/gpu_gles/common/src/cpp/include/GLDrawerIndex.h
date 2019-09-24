#ifndef AE_GL_DRAWER_INDEX_H
#define AE_GL_DRAWER_INDEX_H

#include "ae_GLES.h"
#include "DrawerIndex.h"

namespace ae {

namespace gles {
  
/**
 * \brief The OpenGL implementation of the drawer index.
 */
class GLDrawerIndex:public ::ae::gpu::DrawerIndex {
    /// The data.
    GLushort *data;
    
    /** */
    void create();
    
public:
    /** */
    GLDrawerIndex(int capacity):DrawerIndex(capacity),data((GLushort *)0) {
        create();
    }
    
    /** */
    virtual ~GLDrawerIndex() {
    }
    
    /** */
    virtual bool setValue(int position,int value);
    
    /** */
    void *getPointer(int position) {
        return (void *)(data + position);
    }
    
    /** */
    void use();
};
    
} // namespace

} // namespace

#endif // AE_GL_DRAWER_INDEX_H