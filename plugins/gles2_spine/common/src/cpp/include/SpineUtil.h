#ifndef AE_SPINE_UTIL_H
#define AE_SPINE_UTIL_H

#include <vector>
#include "spine/spine.h"

namespace ae {
    
namespace spine {
  
/**
 * \brief Provides Spine utility methods.
 */
class SpineUtil {
public:
    /**
     * \brief Gets all the attachments of a skeleton. Traverses all the skins
     *   and all the slots in the skins.
     * \param skeleton The skeleton.
     * \return All the attachments.
     */
    static std::vector<spAttachment *> getAttachments(spSkeleton *skeleton);
};
    
} // namespace
    
} // namespace

#endif //  AE_SPINE_UTIL_H