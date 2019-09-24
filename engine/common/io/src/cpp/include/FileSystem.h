#ifndef AE_IO_FILE_SYSTEM_H
#define AE_IO_FILE_SYSTEM_H

#include <string>

#include "Storage.h"
#include "InputStream.h"
#include "OutputStream.h"

namespace ae {
    
namespace io {
    
/**
 * \brief The file system superclass.
 */
class FileSystem:public Storage {
public:
    /**
     * \brief Constructs a FileSystem.
     */
    FileSystem():Storage() {
    }
    
    /** */
    virtual ~FileSystem() {
    }    
};
    
} // namespace
    
} // namespace

#endif //  AE_IO_FILE_SYSTEM_H