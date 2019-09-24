#ifndef AE_SPINE_SKELETON_DRAWER_H
#define AE_SPINE_SKELETON_DRAWER_H

#include "spine/spine.h"
#include "GLESError.h"
#include "Color.h"
#include "Mat4.h"
#include "GLSLProgram.h"
#include "Texture.h"
#include "VertexBufferData.h"
#include "SpineAttachmentRendererObject.h"

namespace ae {
    
namespace spine {
    
/**
 * \brief Draws a Spine skeleton.
 */
class SpineSkeletonDrawer:public ae::gles2::GLESError {
    /** */
    enum {
        /// The maximum number of supported coordinates.
        MAX_COORDS = 256
    };
    
    /// The GLSL program.
    ae::gles2::GLSLProgram program;
    
    /// The color uniform.
    GLint u_Color;
    
    /// The texture uniform.
    GLint u_Tex;
    
    /// The transformation matrix uniform.
    GLint u_Matrix;
    
    /// The coordinates attribute.
    GLint a_Coords;
    
    /// The color attribute;
    GLint a_Color;
    
    /// The texture coordinates attribute.
    GLint a_TexCoords;
    
    /// The stride of the coordinates VBD.
    int coordsStride;
    
    /// The vertex buffer data with coordinates, color and texture coordinates.
    ae::gles2::VertexBufferData *coordsVbd;
    
    /// The vertex buffer data with vertex indices.
    ae::gles2::VertexBufferData *indexVbd;
    
    /// The vertex coordinates.
    float coords[MAX_COORDS];
    
    /**
     * \brief Creates the drawer.
     */
    void create();
    
    /**
     * \brief Sets the blending function.
     * \param slot The skeleton slot.
     */
    void setBlending(spSlot *slot);
    
    /** */
    bool draw(SpineAttachmentRendererObject *rendererObject,float *coords,
        float *uvs,const int *indices,int vertexCount,int indexCount);
    
    /**
     * \brief Draws a region.
     * \param slot The slot containing the region.
     * \param region The region.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.     
     */
    bool drawRegion(spSlot *slot,spRegionAttachment *region);
    
    /**
     * \brief Draws a mesh.
     * \param slot The slot containing the mesh.
     * \param mesh The mesh.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.     
     */
    bool drawMesh(spSlot *slot,spMeshAttachment *mesh);
    
    /**
     * \brief Draws a skinned mesh.
     * \param slot The slot containing the skinned mesh.
     * \param skinnedMesh The skinned mesh.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.     
     */
    bool drawSkinnedMesh(spSlot *slot,spSkinnedMeshAttachment *skinnedMesh);
    
public:
    /**
     * \brief Constructs a SpineSkeletonDrawer.
     */
    SpineSkeletonDrawer():GLESError(),program(),u_Color(0),u_Tex(0),u_Matrix(0),
        a_Coords(0),a_TexCoords(0),coordsVbd((ae::gles2::VertexBufferData *)0),
        indexVbd((ae::gles2::VertexBufferData *)0),coords() {
    //
        create();
    }
    
    /** */
    ~SpineSkeletonDrawer();
    
    /**
     * \brief Starts drawing.
     * \param color The skeleton color.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.     
     */
    bool drawStart(const ae::math::Color &color);
    
    /**
     * \brief Starts drawing.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.     
     */
    bool drawStart() {
        return drawStart(ae::math::Color::WHITE);
    }
    
    /**
     * \brief Draws a skeleton.
     * \param skeleton The skeleton to draw.
     * \param transformation The transformation matrix.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.
     */
    bool draw(spSkeleton *skeleton,const ae::math::Mat4 &transformation);
    
    /**
     * \brief Ends drawing.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.
     */
    bool drawEnd();
};
    
} // namespace
    
} // namespace

#endif // AE_SPINE_SKELETON_DRAWER_H