#ifndef AE_IO_OUTPUT_STREAM_PROVIDER_H
#define AE_IO_OUTPUT_STREAM_PROVIDER_H

#include <string>

#include "Error.h"
#include "OutputStream.h"

namespace ae {
    
namespace io {
    
/**
 * \brief Provides output streams of given identifiers (typically file names).
 */
class OutputStreamProvider:public Error {
public:
    /** */
    OutputStreamProvider():Error() {
    }
    
    /**
     * \brief Gets an output stream of a given identifier.
     * \param id The output stream identifier (typically file name).
     * \return The output stream of null if there is no such output stream
     *   or error occurs.
     */
    virtual OutputStream *getOutputStream(const std::string &id) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_IO_OUTPUT_STREAM_PROVIDER_H