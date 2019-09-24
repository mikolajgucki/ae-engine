#ifndef AE_SDL2_IO_RW_OPS_STORAGE_H
#define AE_SDL2_IO_RW_OPS_STORAGE_H

#include <string>

#include "InputStream.h"
#include "OutputStream.h"
#include "RWopsInputStreamProvider.h"
#include "RWopsOutputStreamProvider.h"
#include "Storage.h"

namespace ae {

namespace io {
  
/**
 * \brief A storage reading and writing using SDL2 RWops.
 */
class RWopsStorage:public Storage {
    /// The storage directory.
    const std::string dir;
    
    /// The input stream provider.
    RWopsInputStreamProvider *inputStreamProvider;
    
    /// The output stream provider.
    RWopsOutputStreamProvider *outputStreamProvider;
    
    /**
     * \brief Creates the RWops storage.
     */
    void create();
    
    /**
     * \brief Gets the path to a file in the storage.
     * \param filename The file name.
     * \return The path to the file.
     */
    const std::string getPath(const std::string &filename);    
    
public:
    /**
     * \brief Creates a RWops storage.
     * \param dir_ The directory from/to which read/write the files.     
     */
    RWopsStorage(const std::string &dir_):Storage(),dir(dir_),
        inputStreamProvider((RWopsInputStreamProvider *)0),
        outputStreamProvider((RWopsOutputStreamProvider *)0) {
    //
        create();
    }
    
    /** */
    virtual ~RWopsStorage();
    
    /** */
    virtual bool createFile(const std::string &filename);
    
    /** */
    virtual InputStream *getInputStream(const std::string &filename);

    /** */
    virtual OutputStream *getOutputStream(const std::string &filename);     
};
    
} // namespace
    
} // namespace

#endif // AE_SDL2_IO_RW_OPS_STORAGE_H