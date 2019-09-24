#ifndef AE_IO_STORAGE_H
#define AE_IO_STORAGE_H

#include <string>

#include "Error.h"
#include "InputStream.h"
#include "OutputStream.h"

namespace ae {
    
namespace io {
  
/**
 * \brief The internal storage superclass.
 */
class Storage:public Error {
public:
    /**
     * \brief Constructs a Storage.
     */
    Storage():Error() {
    }
    
    /** */
    virtual ~Storage() {
    }
    
    /**
     * \brief Creates a file if it does not exist. Does nothing if the file
     *   already exists.
     * \param filename The name of the file.
     * \return <code>true</code> on success, <code>false</code> on failure
     *   (error is set).
     */
    virtual bool createFile(const std::string &filename) = 0;
    
    /**
     * \brief Gets an input stream of a given file name.
     * \param filename The file name.
     * \return The input stream or null if there is no such input stream
     *   or error occurs.
     */
    virtual InputStream *getInputStream(const std::string &filename) = 0;

    /**
     * \brief Gets an output stream of a given file name.
     * \param filename The file name.
     * \return The output stream of null if there is no such output stream
     *   or error occurs.
     */
    virtual OutputStream *getOutputStream(const std::string &filename) = 0;    
};
    
} // namespace
    
} // namespace

#endif // AE_IO_STORAGE_H