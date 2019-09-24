#ifndef AE_SPINE_UTIL_H
#define AE_SPINE_UTIL_H

#include <vector>
#include "spine/spine.h"
#include "Error.h"

namespace ae {
    
namespace spine {
  
/**
 * \brief Provides Spine utility methods.
 */
class SpineUtil:public Error {
    /** */
    spAttachment *findAttachment(spSkeleton *skeleton,
        const char *attachmentName);
    
public:
    /**
     * \brief Gets all the attachments of a skeleton. Traverses all the skins
     *   and all the slots in the skins.
     * \param skeleton The skeleton.
     * \return All the attachments.
     */
    std::vector<spAttachment *> getAttachments(spSkeleton *skeleton);
    
    /**
     * \brief Gets world XY coordinates of a region.
     * \param skeleton The skeleton.
     * \param attachmentName The attachment name containing the region.
     * \param xy The result 4 coordinates.
     * \return true on success, false on error.
     */
    bool getRegionWorldXYCoords(spSkeleton *skeleton,const char *attachmentName,
        float *xy);    
};
    
} // namespace
    
} // namespace

#endif //  AE_SPINE_UTIL_H