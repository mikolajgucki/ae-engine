#ifndef AE_SCREENSHOT_H
#define AE_SCREENSHOT_H

#include <string>
#include "Error.h"
#include "PNGWriter.h"

namespace ae {
    
namespace engine {
    
namespace desktop {
    
/** */
class Screenshot:public Error {
    /** */
    char *data;
    
    /** */
    int width;
    
    /** */
    int height;
    
    /** */
    ::ae::image::PNGWriter pngWriter;
    
public:
    /** */
    Screenshot():Error(),data((char *)0),width(0),height(0),pngWriter() {
    }
    
    /** */
    ~Screenshot() {
        if (data != (char *)0) {
            delete[] data;
        }
    }
    
    /** */
    void setSize(int width_,int height_);
    
    /** */
    bool take(const std::string& filename);
};
    
} // namespace

} // namespace
    
} // namespace

#endif // AE_SCREENSHOT_H