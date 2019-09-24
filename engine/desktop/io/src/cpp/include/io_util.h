#ifndef AE_IO_UTIL_H
#define AE_IO_UTIL_H

#include <string>

#include "InputStream.h"

namespace ae {
    
namespace io {

/**
 * \brief Gets the path components separator.
 * \return The separator.
 */
char getFileSeparator();

/**
 * \brief Checks if a file or directory exists.
 * \param filename The name of the file.
 * \return <code>true</code> if the file exists, <code>false</code> otherwise.
 */
bool checkFileExists(const std::string &filename);

} // namespace
    
} // namespace

#endif // AE_IO_UTIL_H