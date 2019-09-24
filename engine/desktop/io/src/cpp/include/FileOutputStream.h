#ifndef AE_DESKTOP_FILE_OUTPUT_STREAM_H
#define AE_DESKTOP_FILE_OUTPUT_STREAM_H

#include <cstdio>
#include <string>

#include "OutputStream.h"

namespace ae {
    
namespace io {

/**
 * \brief Binary stream writing to a file.
 */
class FileOutputStream:public OutputStream {
    /// The name of the file
    const std::string filename;
    
    /// The file from which the data is read
    FILE *file;
    
public:
    /**
     * \brief Constructs a FileOutputStream.
     * \param filename_ The name of the file.
     */
    FileOutputStream(const std::string &filename_);
    
    /** */
    virtual bool open();
    
    /** */
    virtual bool write(int value);
    
    /** */
    virtual bool close();
};
    
} // namespace
    
} // namespace
    
#endif // AE_DESKTOP_FILE_OUTPUT_STREAM_H