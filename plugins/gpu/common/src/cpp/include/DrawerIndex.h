#ifndef AE_DRAWER_INDEX_H
#define AE_DRAWER_INDEX_H

#include "Error.h"

namespace ae {
    
namespace gpu {

/**
 * \brief Represents a drawer index.
 */
class DrawerIndex:public Error {
    /// The capacity.
    int capacity;
    
protected:
    /** */
    DrawerIndex(int capacity_);
    
    /** */
    bool checkPosition(int position) {
        if (position >= capacity) {
            setError("Invalid position");
            return false;
        }
        
        return true;
    }
    
public:
    /** */
    virtual ~DrawerIndex();
    
    /**
     * \brief Gets the index capacity.
     * \return The index capacity.
     */
    int getCapacity() const {
        return capacity;
    }
    
    /**
     * \brief Sets a value in the drawer index.
     * \param position The position at which to set.
     * \param value The value to set.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.     
     */
    virtual bool setValue(int position,int value) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_DRAWER_INDEX_H