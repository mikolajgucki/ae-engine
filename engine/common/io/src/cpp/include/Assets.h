#ifndef AE_IO_ASSETS_H
#define AE_IO_ASSETS_H

#include <string>

#include "Error.h"
#include "InputStream.h"

namespace ae {
    
namespace io {
  
/**
 * \brief The superclass for asset access providers.
 */
class Assets:public Error {
public:
    /**
     * \brief Constructs Assets.
     */
    Assets():Error() {
    }
    
    /** */
    virtual ~Assets() {
    }
    
    /**
     * \brief Gets an input stream of a given file name.
     * \param filename The file name.
     * \return The input stream or null if there is no such input stream
     *   or error occurs.
     */    
    virtual InputStream *getInputStream(const std::string &filename) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_IO_ASSETS_H