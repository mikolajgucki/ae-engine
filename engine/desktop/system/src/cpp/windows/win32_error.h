#ifndef AE_WIN32_ERROR_H
#define AE_WIN32_ERROR_H

#include <string>
#include "Error.h"

namespace ae {

namespace system {
    
/**
 * \brief Gets the last error (GetLastError()) as string.
 * \param str The string in which to store the error string.
 * \return true if GetLastError() return error, false otherwise.
 */
bool getLastErrorAsString(std::string &str);

/**
 * \brief Gets the last error (GetLastError()) as string.
 * \param error The error.
 * \return true if GetLastError() return error, false otherwise.
 */
bool getLastErrorAsString(Error *error);
    
} // namespace

} // namespace

#endif // AE_WIN32_ERROR_H