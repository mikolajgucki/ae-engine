#ifndef AE_IO_RW_OPS_ASSETS_H
#define AE_IO_RW_OPS_ASSETS_H

#include <string>
#include <vector>

#include "InputStream.h"
#include "RWopsInputStreamProvider.h"
#include "Assets.h"

namespace ae {

namespace io {
    
/**
 * \brief Assets access reading from files using the SDL RWops functions.
 */
class RWopsAssets:public Assets {
    /// The file input stream provider.
    RWopsInputStreamProvider *inputStreamProvider;    
    
    /**
     * \brief Creates the RW ops assets.
     */
    void create();
    
public:
    /**
     * \brief Constructs a RWopsAssets.
     */
    RWopsAssets():Assets(),inputStreamProvider((RWopsInputStreamProvider *)0) {
        create();
    }
    
    /** */
    virtual ~RWopsAssets();
        
    /** */
    virtual InputStream *getInputStream(const std::string &filename);    
};
    
} // namespace
    
} // namespace

#endif // AE_IO_RW_OPS_ASSETS_H