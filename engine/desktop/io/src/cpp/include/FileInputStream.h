#ifndef AE_IO_FILE_INPUT_STREAM_H
#define AE_IO_FILE_INPUT_STREAM_H

#include <cstdio>
#include <string>

#include "InputStream.h"

namespace ae {
    
namespace io {
  
/**
 * \brief Binary stream reading from a file.
 */
class FileInputStream:public InputStream {
    /// The name of the file
    const std::string filename;
    
    /// The file from which the data is read
    FILE *file;
    
public:
    /**
     * \brief Constructs a FileInputStream.
     * \param filename_ The name of the file.
     */
    FileInputStream(const std::string &filename_);
    
    /** */
    virtual bool open();
    
    /** */
    virtual bool read(int &value);
    
    /** */
    virtual bool close();
};
    
} // namespace
    
} // namespace

#endif // AE_IO_FILE_INPUT_STREAM_H