#ifndef AE_DRAWER_INDEX_FACTORY_H
#define AE_DRAWER_INDEX_FACTORY_H

#include "Error.h"
#include "DrawerIndex.h"

namespace ae {
    
namespace gpu {

/**
 * \brief Represents a drawer index factory.
 */
class DrawerIndexFactory:public Error {
protected:
    /** */
    DrawerIndexFactory();
    
public:
    /** */
    virtual ~DrawerIndexFactory();
    
    /**
     * \brief Creates a drawer index.
     * \param capacity The total number of vertices supported by the index.
     * \return The index or sets error and returns null if the drawer
     *   cannot be created.     
     */
    virtual DrawerIndex *createDrawerIndex(int capacity) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_DRAWER_INDEX_FACTORY_H