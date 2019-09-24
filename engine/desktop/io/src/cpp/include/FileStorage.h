#ifndef AE_IO_FILE_STORAGE_H
#define AE_IO_FILE_STORAGE_H

#include <string>

#include "InputStream.h"
#include "OutputStream.h"
#include "FileInputStreamProvider.h"
#include "FileOutputStreamProvider.h"
#include "Storage.h"

namespace ae {
    
namespace io {
  
/**
 * \brief A storage reading from and writing to files using functions from
 *   <code>cstdio</code>.
 */
class FileStorage:public Storage {
    /// The storage directory.
    const std::string dir;
    
    /// The file input stream provider.
    FileInputStreamProvider *inputStreamProvider;
    
    /// The file output stream provider .
    FileOutputStreamProvider *outputStreamProvider;
    
    /**
     * \brief Creates the file storage.
     * \param dir The directory from/to which read/write the files.
     */
    void create(const std::string &dir);
    
    /**
     * \brief Gets the path to a file in the storage.
     * \param filename The file name.
     * \return The path to the file.
     */
    const std::string getPath(const std::string &filename);
    
public:
    /**
     * \brief Constructs a FileStorage.
     * \param dir_ The directory from/to which read/write the files.
     */
    FileStorage(const std::string &dir_):Storage(),dir(dir_),
        inputStreamProvider((FileInputStreamProvider *)0),
        outputStreamProvider((FileOutputStreamProvider *)0) {
    //
        create(dir_);
    }
    
    /** */
    virtual ~FileStorage();
    
    /** */
    virtual bool createFile(const std::string &filename);
    
    /** */
    virtual InputStream *getInputStream(const std::string &filename);

    /** */
    virtual OutputStream *getOutputStream(const std::string &filename);     
};
    
} // namespace
    
} // namespace

#endif // AE_IO_FILE_STORAGE_H