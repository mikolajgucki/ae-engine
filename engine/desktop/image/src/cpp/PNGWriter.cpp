#include <cerrno>
#include <cstring>
#include <sstream>
#include <png.h>
#include "PNGWriter.h"

using namespace std;

namespace ae {
    
namespace image {

/** */
bool PNGWriter::write(const std::string& filename,int width,int height,
    char *data,bool flipY) {
// file
    FILE *file = fopen(filename.c_str(),"wb");
    if (file == (FILE *)0) {
        ostringstream err;
        err << "Failed to open file " << filename + " for writing: " <<
            strerror(errno);
        setError(err.str());
        return false;
    }
    
// write structure
    png_structp pngPtr = png_create_write_struct(PNG_LIBPNG_VER_STRING,
        (png_voidp)0,(png_error_ptr)0,(png_error_ptr)0);
    if (pngPtr == (png_structp)0) {
        fclose(file);
        setError("Failed to create PNG write structure");
        return false;
    }
    
// info structure
    png_infop infoPtr = png_create_info_struct(pngPtr);
    if (infoPtr == (png_infop)0) {
        fclose(file);
        setError("Failed to create PNG info structure");
        return false;
    }
   
// exception handling
    if (setjmp(png_jmpbuf(pngPtr))) {
        fclose(file);
        setError("Failed to set PNG exception handling");
        return false;
    }
    
    png_init_io(pngPtr,file);
// write header
    png_set_IHDR(pngPtr,infoPtr,width,height,
         8,PNG_COLOR_TYPE_RGB,PNG_INTERLACE_NONE,
         PNG_COMPRESSION_TYPE_BASE,PNG_FILTER_TYPE_BASE);    

// write info
    png_write_info(pngPtr,infoPtr);
    
// write rows
    for (int y = 0; y < height; y++) {
        int yr = y;
        if (flipY == true) {
            yr = height - y - 1;
        }
        int offset = yr * width * 3;
        png_write_row(pngPtr,(png_bytep)(data + offset));
    }
    
// end writing
    png_write_end(pngPtr,(png_infop)0);
    
    fclose(file);
    return true;
}
    
} // namespace
    
} // namespace