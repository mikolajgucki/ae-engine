#include <memory>
#include "SpineAttachmentRendererObject.h"

using namespace std;
using namespace ae::texture::lua;

namespace ae {
    
namespace spine {
    
/** */
static SpineAttachmentRendererObject *createAttachmentObject(
    spAtlasRegion *atlasRegion) {
//
    SpineAttachmentRendererObject *object =
        new (nothrow) SpineAttachmentRendererObject();
    if (object == (SpineAttachmentRendererObject *)0) {
        return (SpineAttachmentRendererObject *)0;
    }
    
    object->textureType = (TextureType *)(atlasRegion->page->rendererObject);    
    return object;
}    
    
/** */
bool createAttachmentRendererObject(spAttachment *attachment) {
    switch (attachment->type) {
        case SP_ATTACHMENT_REGION: {
            spRegionAttachment *region = (spRegionAttachment *)attachment;
            SpineAttachmentRendererObject *object = createAttachmentObject(
                (spAtlasRegion*)region->rendererObject);
            if (object == (SpineAttachmentRendererObject *)0) {
                return false;
            }
            region->rendererObject = object;  
            break;
        }   
        
        case SP_ATTACHMENT_MESH: {
            spMeshAttachment *mesh = (spMeshAttachment *)attachment;
            SpineAttachmentRendererObject *object = createAttachmentObject(
                (spAtlasRegion*)mesh->rendererObject);
            if (object == (SpineAttachmentRendererObject *)0) {
                return false;
            }
            mesh->rendererObject = object;
            break;
        }   
            
        default:
            break;                
    }
    
    return true;
}

/** */
SpineAttachmentRendererObject *getAttachmentRendererObject(
    spAttachment *attachment) {
//
    switch (attachment->type) {
        case SP_ATTACHMENT_REGION: {
            spRegionAttachment *region = (spRegionAttachment *)attachment;
            return (SpineAttachmentRendererObject *)region->rendererObject;
        }   
        
        case SP_ATTACHMENT_MESH: {
            spMeshAttachment *mesh = (spMeshAttachment *)attachment;
            return (SpineAttachmentRendererObject *)mesh->rendererObject;           
        }   
            
        default:
            break;
    }
    
    return (SpineAttachmentRendererObject *)0;
}
    
} // namespace
    
} // namespace