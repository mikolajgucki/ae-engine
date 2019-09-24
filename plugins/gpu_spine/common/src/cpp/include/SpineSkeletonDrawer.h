#ifndef AE_SPINE_SKELETON_DRAWER_H
#define AE_SPINE_SKELETON_DRAWER_H

#include "spine/spine.h"
#include "Error.h"
#include "Color.h"
#include "Mat4.h"
#include "Texture.h"
#include "GPUQueue.h"
#include "DefaultDrawer.h"
#include "DefaultDrawerFactory.h"
#include "SpineAttachmentRendererObject.h"

namespace ae {
    
namespace spine {
    
/**
 * \brief Draws a Spine skeleton.
 */
class SpineSkeletonDrawer:public ::ae::Error {
    /** */
    enum {
        /// The maximum number of supported XY coordinates.
        DEFAULT_XY_COORDS_COUNT = 256
    };    
    
    /// The skeleton drawn by this drawer.
    spSkeleton *skeleton;    
    
    /// The vertex XY coordinates.
    float *xy;

    /// The transforamation.
    ::ae::math::Mat4 transformation;
    
    /// The drawer.
    ::ae::gpu::DefaultDrawer *drawer;
    
    /// The drawer vertex position.
    int vposition;
    
    /// The texture.
    ::ae::texture::Texture *texture;
    
    /**
     * \brief Creates the drawer.
     * \param drawerFactory The drawer factory.
     */
    void create(::ae::gpu::DefaultDrawerFactory *drawerFactory);
    
    /**
     * \brief Sets the blending function.
     * \param slot The skeleton slot.
     */
    void setBlending(spSlot *slot);    
    
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
    
public:
    /**
     * \brief Constructs a SpineSkeletonDrawer.
     */
    SpineSkeletonDrawer(::ae::gpu::DefaultDrawerFactory *drawerFactory_,
        spSkeleton *skeleton_):Error(),skeleton(skeleton_),
        xy((float *)0),transformation(),drawer((::ae::gpu::DefaultDrawer *)0),
        vposition(0),texture((::ae::texture::Texture *)0) {
    //
        create(drawerFactory_);
    }
    
    /**
     * \brief Enqeues a command to draw the skeleton.
     * \param queue The queue.     
     * \param transformation_ The transformation matrix.
     * \param color The skeleton color.     
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.
     */
    bool enqueue(::ae::gpu::GPUQueue *queue,
        const ae::math::Mat4 &transformation_,const ae::math::Color &color);    
    
    /** */
    ~SpineSkeletonDrawer();
};
    
} // namespace
    
} // namespace

#endif // AE_SPINE_SKELETON_DRAWER_H