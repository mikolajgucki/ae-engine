#ifndef AE_IO_FILE_OUTPUT_STREAM_PROVIDER_H
#define AE_IO_FILE_OUTPUT_STREAM_PROVIDER_H

#include <string>
#include "OutputStreamProvider.h"

namespace ae {
    
namespace io {
  
/**
 * \brief Provides output streams of given file names. Stores the files in
 *   the given directory.
 */
class FileOutputStreamProvider:public OutputStreamProvider {
    /// The directory in which to write the files.
    const std::string dir;
    
public:
    /**
     * \brief Constructs a FileOutputStreamProvider.
     * \param dir_ The directory in which to write the files.
     */
    FileOutputStreamProvider(const std::string &dir_):OutputStreamProvider(),
        dir(dir_) {
    }
    
    /** */
    virtual ~FileOutputStreamProvider() {
    }
    
    /** */
    virtual OutputStream *getOutputStream(const std::string &filename);
};
    
} // namespace
    
} // namespace

#endif // AE_IO_FILE_OUTPUT_STREAM_PROVIDER_H