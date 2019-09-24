#ifndef AE_DYN_LIB_H
#define AE_DYN_LIB_H

#include <string>
#include "Error.h"

namespace ae {
    
namespace system {
    
/**
 * \brief Loads dynamic libraries.
 */
class DynLib:public Error {
    /// The implementation specific data.
    void *data;
    
    /// The name of the library file.
    const std::string filename;
    
public:
    /**
     * \brief Constructs a dynamic library loader.
     * \param filename_ The name of the library file.
     */
    DynLib(const std::string& filename_):Error(),data((void *)0),
        filename(filename_) {
    }
    
    /**
     * \brief Opens the library.
     * \return <tt>true</tt> on success, <tt>false</tt> otherwise.
     */
    bool open();
    
    /**
     * \brief Gets the address of a function or variable.
     * \param name The function/variable name.
     * \return The address of the function/variable or null on error.
     */
    void *getAddress(const std::string& name);
    
    /**
     * \brief Closes the library.
     * \return <tt>true</tt> on success, <tt>false</tt> otherwise.
     */
    bool close();
};
    
} // namespace
    
} // namespace

#endif // AE_DYN_LIB_H