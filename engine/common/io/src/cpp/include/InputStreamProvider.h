#ifndef AE_INPUT_STREAM_PROVIDER_H
#define AE_INPUT_STREAM_PROVIDER_H

#include <string>

#include "Error.h"
#include "InputStream.h"

namespace ae {
    
namespace io {
  
/** 
 * \brief Provides input streams of given identifiers (typically file names).
 */
class InputStreamProvider:public Error {
public:
    /** */
    InputStreamProvider():Error() {
    }
    
    /**
     * \brief Gets an input stream of a given identifier.
     * \param id The input stream identifier (typically file name).
     * \return The input stream or null if there is no such input stream
     *   or error occurs.
     */
    virtual InputStream *getInputStream(const std::string &id) = 0;
};    
    
} // namespace
    
} // namespace

#endif // AE_INPUT_STREAM_PROVIDER_H