#ifndef AE_SDL2_IO_RW_OPS_INPUT_STREAM_PROVIDER_H
#define AE_SDL2_IO_RW_OPS_INPUT_STREAM_PROVIDER_H

#include <string>

#include "InputStream.h"
#include "InputStreamProvider.h"

namespace ae {
    
namespace io {
  
/**
 * \brief Provides SDL RWops files in given directory.
 */
class RWopsInputStreamProvider:public InputStreamProvider {
    /// The directory in which to look for the files.
    std::string dir;
    
public:
    /**
     * \brief Constructs a RWopsInputStreamProvider.
     * \param dir_ The directory in which to look for the assets.
     */
    RWopsInputStreamProvider(const std::string &dir_):InputStreamProvider(),
        dir(dir_) {
    }
    
    /** */
    virtual ~RWopsInputStreamProvider() {
    }
    
    /** */
    virtual InputStream *getInputStream(const std::string &filename);    
};
    
} // namespace
    
} // namespace

#endif // AE_SDL2_IO_RW_OPS_INPUT_STREAM_PROVIDER_H