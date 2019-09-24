#ifndef AE_SDL2_IO_RW_OPS_OUTPUT_STREAM_PROVIDER_H
#define AE_SDL2_IO_RW_OPS_OUTPUT_STREAM_PROVIDER_H

#include <string>

#include "OutputStream.h"
#include "OutputStreamProvider.h"

namespace ae {
    
namespace io {

/**
 * \brief Provides SDL RWops files in given directory.
 */
class RWopsOutputStreamProvider:public OutputStreamProvider {
    /// The directory in which to write the files.
    std::string dir;
    
public:
    /**
     * \brief Constructs a RWopsOutputStreamProvider.
     * \param dir_ The directory in which to write the files.
     */
    RWopsOutputStreamProvider(const std::string &dir_):OutputStreamProvider(),
        dir(dir_) {
    }
    
    /** */
    virtual ~RWopsOutputStreamProvider() {
    }    
    
    /** */
    virtual OutputStream *getOutputStream(const std::string &filename);
};
    
} // namespace
    
} // namespace
    
#endif // AE_SDL2_IO_RW_OPS_OUTPUT_STREAM_PROVIDER_H