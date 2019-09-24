#ifndef AE_SPINE_ATTACHMENT_LOADER_H
#define AE_SPINE_ATTACHMENT_LOADER_H

#include "spine/AttachmentLoader.h"
#include "spine/Atlas.h"

#ifdef __cplusplus
extern "C" {
#endif

/** */
struct SpineAttachmentLoader {
    /** */
    spAttachmentLoader super;
    
    /** */
    spAtlasAttachmentLoader *atlasAttachmentLoader;
};
typedef struct SpineAttachmentLoader;

SpineAttachmentLoader 

#ifdef __cplusplus
}
#endif

#endif // AE_SPINE_ATTACHMENT_LOADER_H