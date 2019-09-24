#include <cstring>
#include "SpineUtil.h"

using namespace std;

namespace ae {
    
namespace spine {
 
/** */
spAttachment *SpineUtil::findAttachment(spSkeleton *skeleton,
    const char *attachmentName) {
//
    return (spAttachment *)0;
}

/** */
vector<spAttachment *> SpineUtil::getAttachments(spSkeleton *skeleton) {
    vector<spAttachment *> attachments;    
    spSkeletonData *data = skeleton->data;
    
// for each skin
    for (int skinIndex = 0; skinIndex < data->skinsCount; skinIndex++) {
        spSkin *skin = data->skins[skinIndex];
        
    // for each slot
        for (int slotIndex = 0; slotIndex < data->slotsCount; slotIndex++) {
            int attachmentIndex = 0;
            while (true) {                
                const char *attachmentName = spSkin_getAttachmentName(
                    skin,slotIndex,attachmentIndex);
                if (attachmentName == (const char *)0) {
                    break;
                }
                
                spAttachment *attachment = spSkin_getAttachment(
                    skin,slotIndex,attachmentName);
                attachments.push_back(attachment);
                
                attachmentIndex++;
            }
        }
    }  
    
    return attachments;
}

/** */
bool SpineUtil::getRegionWorldXYCoords(spSkeleton *skeleton,
    const char *attachmentName,float *xy) {
// for each slot
    for (int slotIndex = 0; slotIndex < skeleton->slotsCount; slotIndex++) {
        spSlot* slot = skeleton->drawOrder[slotIndex];
        if (!slot->attachment) {
            continue;
        }
        
        if (slot->attachment != (spAttachment *)0 &&
            slot->data != (spSlotData *)0 &&
            slot->data->attachmentName != (const char *)0) {  
        //
            if (strcmp(slot->data->attachmentName,attachmentName) == 0) {
                if (slot->attachment->type != SP_ATTACHMENT_REGION) {
                    setError("Not a region");
                    return false;
                }
                
                spRegionAttachment_computeWorldVertices(
                    (spRegionAttachment *)slot->attachment,slot->bone,xy);
                return true;
            }
        }
    }

    ostringstream err;
    err << "Unknow region with attachment name " << attachmentName;
    setError(err.str());
    
    return false;
}    

} // namespace
    
} // namespace