#ifndef AE_DEFAULT_DRAWER_FACTORY_H
#define AE_DEFAULT_DRAWER_FACTORY_H

#include "Error.h"
#include "DefaultDrawer.h"

namespace ae {

namespace gpu {
  
/**
 * \brief The abstract default drawer factory. Implementations are provided by
 *   other plugins. 
 */
class DefaultDrawerFactory:public Error {
public:
    /** */
    DefaultDrawerFactory():Error() {
    }
    
    /** */
    virtual ~DefaultDrawerFactory() {
    }
    
    /**
     * \brief Creates a default drawer.
     * \param features The features.
     * \param capacity The total number of vertices supported by the drawer.
     * \return The drawer or sets error and returns null if the drawer
     *   cannot be created.
     */
    virtual DefaultDrawer *createDefaultDrawer(DefaultDrawerFeatures features,
        int capacity) = 0;
};
    
} // namespace

} // namespace

#endif // AE_DEFAULT_DRAWER_FACTORY_H