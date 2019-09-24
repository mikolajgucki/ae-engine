#ifndef AE_SPINE_ATTACHMENT_RENDERER_OBJECT_H
#define AE_SPINE_ATTACHMENT_RENDERER_OBJECT_H

#include "spine/spine.h"
#include "Color.h"
#include "luaTexture.h"

namespace ae {
    
namespace spine {
  
/**
 * \brief The renderer object set on all of the attachments.
 */
struct SpineAttachmentRendererObject {
    /** */
    ae::texture::lua::TextureType *textureType;
    
    /** */
    ae::math::Color color;    
    
    /** */
    SpineAttachmentRendererObject():textureType(),
        color(ae::math::Color::WHITE) {            
    }
};
typedef struct SpineAttachmentRendererObject SpineAttachmentRendererObject;
    
/**
 * \brief Creates an attachment renderer object.
 * \param attachment The attachment to which to assign the renderer object.
 * \return <code>true</code> on success, <code>false</code> otherwise.
 */
bool createAttachmentRendererObject(spAttachment *attachment);

/**
 * \brief Gets an attachment renderer object.
 * \param attachment The attachment.
 * \return The renderer object.
 */
SpineAttachmentRendererObject *getAttachmentRendererObject(
    spAttachment *attachment);

} // namespace
    
} // namespace

#endif //  AE_SPINE_ATTACHMENT_RENDERER_OBJECT_H