#ifndef AE_GL_DEFAULT_DRAWER_H
#define AE_GL_DEFAULT_DRAWER_H

#include "VertexBufferData.h"
#include "GLTexture.h" 
#include "GLSLProgram.h"
#include "GLDrawerIndex.h"
#include "DefaultDrawer.h"

namespace ae {

namespace gles {
    
/**
 * \brief The OpenGL ES implementation of the default drawer.
 */
class GLDefaultDrawer:public ::ae::gpu::DefaultDrawer {
    /** */
    enum {
        /// Indicates no VBO generated.
        NO_VBO = 0,
        
        /// Indiates no value is set.
        NO_VALUE = 65535
    };
    
    /// Checks GL errors.
    GLError glError;
    
    /// The GLSL program.
    GLSLProgram *glslProgram;
    
    /// The buffer data.
    VertexBufferData *vbd;
    
    /// The subdata which can hold single vertex data.
    VertexBufferData *subdata;
    
    /// The pointer to the vertex coordinates in the subdata.
    GLfloat *vertexCoordsSubdata;
    
    /// The pointer to the vertex color in the subdata.
    GLubyte *vertexColorSubdata;
    
    /// The pointer to the texture coordinates in the subdata.
    GLushort *textureCoordsSubdata;
    
    /// The vertex coordinates size.
    int vertexCoordsSize;
    
    /// The vertex color size.
    int vertexColorSize;
    
    /// The texture coordinates size.
    int textureCoordsSize;
    
    /// The vertex stride.
    int stride;
    
    /// The vertex color offset.
    int vertexColorOffset;
    
    /// The texture coordinates offset.
    int textureCoordsOffset;

    /// The transformaton matrix shader uniform.
    GLint u_Transformation;
    
    /// The vertex coordinates shader attribute.
    GLint a_Coords;
    
    /// The color shader uniform.
    GLint u_Color;
    
    /// The vertex color shader attribute.
    GLint a_Color;
    
    /// The texture coordinates shader attribute.
    GLint a_TexCoords;
    
    /// The texture name shader uniform.
    GLint u_Tex;
    
    /// The vertex buffer object.
    GLuint vbo;
    
    /** */
    bool chkGLError();
    
    /** */
    bool createVBD();
    
    /** */
    bool createSubdata();
    
    /** */
    void getGLSLVariables();
    
    /**
     * \brief Gets the offset from the beginning of the data.
     * \param position The vertex position.
     * \param offset The offset in bytes from the beginning of the vertex data.
     * \return The offset.
     */
    size_t getOffset(int position,int offset) {
        return position * stride + offset;
    }
    
public:
    /** */
    GLDefaultDrawer(::ae::gpu::DefaultDrawerFeatures features,int capacity,
        GLSLProgram *glslProgram_);
    
    /** */
    virtual ~GLDefaultDrawer();
    
    /** */
    virtual bool activate();
    
    /** */
    virtual bool setVertexCoords(int position,float x,float y);
    
    /** */
    virtual bool setVertexColor(int position,::ae::math::Color color);
    
    /** */
    virtual bool setTextureCoords(int position,float u,float v);
    
    /** */
    virtual bool setCoords(int position,float x,float y,float u,float v);
    
    /** */
    virtual bool moveToGPU();
    
    /** */
    virtual bool deleteCPUData();    
    
    /** */
    virtual bool enqueue(::ae::gpu::GPUQueue *queue,int first,int count,
        ::ae::gpu::DrawerIndex *drawerIndex,
        ::ae::texture::Texture *texture,
        const ::ae::math::Mat4 *transformation);
    
    /** */
    void draw(int first,int count,GLDrawerIndex *drawerIndex,
        GLTexture *texture,const ::ae::math::Mat4 *transformation);
};

} // namespace

} // namespace

#endif // AE_GL_DEFAULT_DRAWER_H