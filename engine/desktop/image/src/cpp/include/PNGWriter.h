#ifndef AE_PNG_WRITER_H
#define AE_PNG_WRITER_H

#include <string>
#include "Error.h"

namespace ae {
    
namespace image {
  
/**
 * \brief PNG writer.
 */
class PNGWriter:public Error {
public:
    /** */
    PNGWriter():Error() {
    }
    
    /**
     * \brief Writes an RGB image.
     * \param filename The name of the file.
     * \param width The image width.
     * \param height The image height.
     * \param data The image RGB data.
     * \param flipY Indicates if to flip along the Y axis.
     * \return <code>true</code> on success, <code>false</code> otherwise.
     */
    bool write(const std::string& filename,int width,int height,char *data,
        bool flipY = false);
};
    
} // namespace
    
} // namespace

#endif // AE_PNG_WRITER_H