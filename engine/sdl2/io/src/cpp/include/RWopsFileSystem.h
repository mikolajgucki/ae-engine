#ifndef AE_SDL2_IO_RW_OPS_FILE_SYSTEM_H
#define AE_SDL2_IO_RW_OPS_FILE_SYSTEM_H

#include <string>
#include "RWopsStorage.h"
#include "FileSystem.h"

namespace ae {
    
namespace io {
  
/**
 * \brief A file system read from and writing to files using SDL2 RWops.
 */
class RWopsFileSystem:public FileSystem {
    /// The wrapped storage.
    ae::io::RWopsStorage *storage;
    
    /**
     * \brief Creates the file system.
     * \param dir The directory from/to which read/write the files. 
     */
    void create(const std::string &dir);
    
    /** */
    void destroy();    
    
public:
    /**
     * \brief Creates the RWops file system.
     * \param dir_ The directory from/to which read/write the files.         
     */
    RWopsFileSystem(const std::string &dir_):FileSystem(),
        storage((ae::io::RWopsStorage *)0) {
    //
        create(dir_);
    }
    
    /** */
    virtual ~RWopsFileSystem() {
        destroy();
    }    
    
    /**
     * \brief Creates a file if it does not exist. Does nothing if the file
     *   already exists.
     * \param filename The name of the file.
     * \return <code>true</code> on success, <code>false</code> on failure
     *   (error is set).
     */
    virtual bool createFile(const std::string &filename) {
        return storage->createFile(filename);
    }
    
    /**
     * \brief Gets an input stream of a given file name.
     * \param filename The file name.
     * \return The input stream or null if there is no such input stream
     *   or error occurs.
     */
    virtual InputStream *getInputStream(const std::string &filename) {
        return storage->getInputStream(filename);
    }

    /**
     * \brief Gets an output stream of a given file name.
     * \param filename The file name.
     * \return The output stream of null if there is no such output stream
     *   or error occurs.
     */
    virtual OutputStream *getOutputStream(const std::string &filename) {
        return storage->getOutputStream(filename);
    }    
};
    
} // namespace
    
} // namespace

#endif // AE_SDL2_IO_RW_OPS_FILE_SYSTEM_H