#include "SpineUtil.h"

using namespace std;

namespace ae {
    
namespace spine {
 
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
    
} // namespace
    
} // namespace