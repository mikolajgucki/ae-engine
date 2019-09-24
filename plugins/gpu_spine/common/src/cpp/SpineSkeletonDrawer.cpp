/**
 * More info on the Spine runtimes:
 * http://esotericsoftware.com/spine-using-runtimes
 */
#include <memory>

#include "ae_defs.h"
#include "Texture.h"
#include "DefaultDrawerFeatures.h"
#include "SpineSkeletonDrawer.h"

using namespace std;
using namespace ae::math;
using namespace ae::texture;
using namespace ae::gpu;

namespace ae {
    
namespace spine {
    
/** */
void SpineSkeletonDrawer::create(DefaultDrawerFactory *drawerFactory) {
// TODO Calculate the number of coordinates for this skeleton.
    const int xyCount = DEFAULT_XY_COORDS_COUNT;
    const int vertexCount = xyCount / 2;
    
// XY coordinates
    xy = new (nothrow) float[xyCount];
    if (xy == (float *)0) {
        setNoMemoryError();
        return;
    }
    
// drawer features
    DefaultDrawerFeatures features;
    features.setColor(true);
    features.setTexture(true);
    
// drawer
    drawer = drawerFactory->createDefaultDrawer(features,vertexCount);
    if (drawer == (DefaultDrawer *)0) {
    // delete coordinates
        delete[] xy;
        xy = (float *)0;
        
    // set error
        setError(drawerFactory->getError());
        return;
    }
}
    
/** */
SpineSkeletonDrawer::~SpineSkeletonDrawer() {
    if (drawer != (DefaultDrawer *)0) {
        delete drawer;
    }
    if (xy != (float *)0) {
        delete[] xy;
    }
}

/** */
void SpineSkeletonDrawer::setBlending(spSlot *slot) {
    switch (slot->data->blendMode) {
        case SP_BLEND_MODE_ADDITIVE:
            // TODO
            break;
            
        case SP_BLEND_MODE_MULTIPLY:
            // TODO
            break;
            
        case SP_BLEND_MODE_NORMAL:
            // TODO
            break;
            
        case SP_BLEND_MODE_SCREEN:
            // TODO
            break;
    }
}

/** */
bool SpineSkeletonDrawer::drawRegion(spSlot *slot,spRegionAttachment *region) {
    SpineAttachmentRendererObject *object =
        (SpineAttachmentRendererObject *)region->rendererObject;
    spRegionAttachment_computeWorldVertices(region,slot->bone,xy);
    
// texture
    if (texture == (Texture *)0) {
        texture = object->textureType->texture;
    }       
    
    float *uvs = region->uvs;
// vertex 1
    drawer->setVertexCoords(vposition,xy[SP_VERTEX_X1],xy[SP_VERTEX_Y1]);
    drawer->setTextureCoords(vposition,uvs[SP_VERTEX_X1],uvs[SP_VERTEX_Y1]);
    vposition++;

// vertex 2    
    drawer->setVertexCoords(vposition,xy[SP_VERTEX_X2],xy[SP_VERTEX_Y2]);
    drawer->setTextureCoords(vposition,uvs[SP_VERTEX_X2],uvs[SP_VERTEX_Y2]);
    vposition++;
    
// vertex 3    
    drawer->setVertexCoords(vposition,xy[SP_VERTEX_X3],xy[SP_VERTEX_Y3]);
    drawer->setTextureCoords(vposition,uvs[SP_VERTEX_X3],uvs[SP_VERTEX_Y3]);
    vposition++;
    
// vertex 4
    drawer->setVertexCoords(vposition,xy[SP_VERTEX_X1],xy[SP_VERTEX_Y1]);
    drawer->setTextureCoords(vposition,uvs[SP_VERTEX_X1],uvs[SP_VERTEX_Y1]);
    vposition++;
    
// vertex 5
    drawer->setVertexCoords(vposition,xy[SP_VERTEX_X3],xy[SP_VERTEX_Y3]);
    drawer->setTextureCoords(vposition,uvs[SP_VERTEX_X3],uvs[SP_VERTEX_Y3]);
    vposition++;
    
// vertex 6
    drawer->setVertexCoords(vposition,xy[SP_VERTEX_X4],xy[SP_VERTEX_Y4]);
    drawer->setTextureCoords(vposition,uvs[SP_VERTEX_X4],uvs[SP_VERTEX_Y4]);
    vposition++;
    
    return true;
}

/** */
bool SpineSkeletonDrawer::drawMesh(spSlot *slot,spMeshAttachment *mesh) {
    SpineAttachmentRendererObject *object =
        (SpineAttachmentRendererObject *)mesh->rendererObject;
    spMeshAttachment_computeWorldVertices(mesh,slot,xy);
    
// texture
    if (texture == (Texture *)0) {
        texture = object->textureType->texture;
    }        
    
    float *uvs = mesh->uvs;
// for each vertex
    for (int i = 0; i < mesh->trianglesCount; i++) {
        int vindex = mesh->triangles[i] << 1;
        
        drawer->setVertexCoords(vposition,xy[vindex],xy[vindex + 1]);
        drawer->setTextureCoords(vposition,uvs[vindex],uvs[vindex + 1]);
        vposition++;
    }
    
    return true;
}

/** */
bool SpineSkeletonDrawer::enqueue(GPUQueue *queue,const Mat4 &transformation_,
    const Color &color) {
// reset
    vposition = 0;
    texture = (Texture *)0;

// for each slot
    for (int slotIndex = 0; slotIndex < skeleton->slotsCount; slotIndex++) {
        spSlot* slot = skeleton->drawOrder[slotIndex];
        if (!slot->attachment) {
            continue;
        }        
        setBlending(slot);
        
        switch (slot->attachment->type) {
            case SP_ATTACHMENT_REGION:
                if (drawRegion(slot,
                    (spRegionAttachment *)slot->attachment) == false) {
                //
                    return false;
                }
                break;
                
            case SP_ATTACHMENT_MESH:
                if (drawMesh(slot,
                    (spMeshAttachment *)slot->attachment) == false) {
                //
                    return false;
                }
                break;
                
            default:
                break;
        }
    }
    
// texture must be set
    if (texture == (Texture *)0) {
        setError("No texture in skeleton");
        return false;
    }
    
// color, transformation
    drawer->setColor(color);
    transformation.assign(&transformation_);
    
// enqueue
    if (drawer->enqueue(queue,0,vposition,(DrawerIndex *)0,texture,
        &transformation) == false) {
    //
        setError(drawer->getError());
        return false;
    }
    
    return true;
}
    
} // namespace
    
} // namespace