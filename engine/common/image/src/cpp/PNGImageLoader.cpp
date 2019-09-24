#include <sstream>

#include "png.h"
#include "Log.h"
#include "InputStream.h"
#include "PNGImageLoader.h"

using namespace std;
using namespace ae::io;

namespace ae {

namespace image {
    
/// The log tag.
const char * const PNGImageLoader::logTag = "PNGImageLoader";

/**
 * \brief The PNG read function.
 */
static void pngReadFunc(png_structp pngPtr,png_bytep data,png_size_t size) {
    png_voidp ioPtr = png_get_io_ptr(pngPtr);
    PNGImageLoader *loader = (PNGImageLoader *)ioPtr;
    int readSize = loader->getStream()->read(data,size);
    if (readSize == InputStream::ERROR) {
        Log::error(PNGImageLoader::logTag,loader->getStream()->getError());
        // TODO Report error so that image loading fails.
    }
    if (readSize != (int)size) {
        ostringstream err;
        err << "Read less than requested, requested " << size <<
            ", read " << readSize << " bytes";
        Log::error(PNGImageLoader::logTag,err.str());
        // TODO Report error so that image loading fails.
    }
}

/**
 * \brief The PNG error function.
 */
static void pngErrorFunc(png_structp pngPtr,png_const_charp msg) {
    Log::error(PNGImageLoader::logTag,msg);
    longjmp(png_jmpbuf(pngPtr),1);
}

/**
 * \brief The PNG warning function.
 */
static void pngWarningFunc(png_structp pngPtr,png_const_charp msg) {
    Log::warning(PNGImageLoader::logTag,msg);
}

/** */
void PNGImageLoader::fail(const string &error) {
    if (pngPtr != (png_structp)0) {
        if (pngInfo != (png_infop)0) {
            png_destroy_read_struct(&pngPtr,&pngInfo,(png_infopp)0); 
        }
        else {
            png_destroy_read_struct(&pngPtr,(png_infopp)0,(png_infopp)0);
        }
    }
    
    if (stream != (InputStream *)0) {
        stream->close();
        delete stream;
    }
    
    if (data != (unsigned char *)0) {
        delete data;
    }
    if (rows != (png_bytep *)0) {
        delete rows;
    }
    
    setError(error);
}
    
/** */
Image *PNGImageLoader::loadImage(const string &id) {
    pngPtr = (png_structp)0;
    pngInfo = (png_infop)0;
    stream = (InputStream *)0;
    data = (unsigned char *)0;
    rows = (png_bytep *)0;
    
// read structure
    pngPtr = png_create_read_struct(PNG_LIBPNG_VER_STRING,
        (png_voidp)0,pngErrorFunc,pngWarningFunc);
    if (pngPtr == (png_structp)0) {
        ostringstream err;
        err << "Error reading PNG image " << id <<
            " (png_create_read_struct failed)";
        fail(err.str());
        return (Image *)0;
    }
    png_set_error_fn(pngPtr,this,pngErrorFunc,pngWarningFunc);
    
// info structure
    pngInfo = png_create_info_struct(pngPtr);
    if (pngInfo == (png_infop)0) {
        ostringstream err;
        err << "Error reading PNG image " << id <<
            " (png_create_info_struct failed)";
        fail(err.str());
        return (Image *)0;
    }
    
// error handling
    if (setjmp(png_jmpbuf(pngPtr)) != 0) {
        ostringstream err;
        err << "Error reading PNG image " << id;
        fail(err.str());
        return (Image *)0;    
    }
    
// input stream
    stream = streamProvider->getInputStream(id);
    if (streamProvider->chkError()) {
        fail(streamProvider->getError());
        return (Image *)0;
    }
    if (stream == (InputStream *)0) {
        ostringstream err;
        err << "Image " << id << " not found";
        fail(err.str());
        return (Image *)0;
    }
    
// read function
    png_set_read_fn(pngPtr,this,pngReadFunc);
    
// open the input stream
    if (stream->open() == false) {
        fail(stream->getError());
        return (Image *)0;
    }
    
// read signaure
    char signature[PNG_SIGNATURE_SIZE];
    if (stream->read(signature,PNG_SIGNATURE_SIZE) < PNG_SIGNATURE_SIZE) {
        if (stream->chkError()) {
            fail(stream->getError());
            return (Image *)0;
        }
        ostringstream err;
        err << "Failed to read PNG signature from file " << id;
        fail(err.str());
        return (Image *)0;
    }
    png_set_sig_bytes(pngPtr,PNG_SIGNATURE_SIZE);
    
// check signature
    if (png_sig_cmp((png_const_bytep)signature,0,PNG_SIGNATURE_SIZE)) {
        ostringstream err;
        err << "Invalid PNG signature in file " << id;
        fail(err.str());
        return (Image *)0;
    } 
    
// read and update info
    png_read_info(pngPtr,pngInfo);
    png_read_update_info(pngPtr,pngInfo);
    
// check color type
    int colorType = (int)png_get_color_type(pngPtr,pngInfo);
    if (colorType != PNG_COLOR_TYPE_RGB_ALPHA) {
        ostringstream err;
        err << "Unsupported color type in image " << id;
        fail(err.str());
        return (Image *)0;        
    }
    
// check bit depth
    int bitDepth = (int)png_get_bit_depth(pngPtr,pngInfo);
    if (bitDepth != 8) {
        ostringstream err;
        err << "Unsupported bit depth in image " << id;
        fail(err.str());
        return (Image *)0;        
    }
    
// image size
    int width = png_get_image_width(pngPtr,pngInfo);
    int height = png_get_image_height(pngPtr,pngInfo);    
    
// allocate data
    int rowbytes = (int)png_get_rowbytes(pngPtr,pngInfo);
    data = new (nothrow) unsigned char[height * rowbytes];
    if (data == (unsigned char *)0) {
        ostringstream err;
        err << "Failed to allocate memory while reading image " << id;
        fail(err.str());
        return (Image *)0;
    }
  
// row pointers
    png_bytep *rowPointers = new (nothrow) png_bytep[height];
    if (rowPointers == (png_bytep *)0) {
        ostringstream err;
        err << "Failed to allocate memory while reading image " << id;
        fail(err.str());
        return (Image *)0;        
    }
    for (int index = 0; index < height; index++) {
        rowPointers[index] = (data + index * rowbytes);
    }
    
// read the image in one go
    png_read_image(pngPtr,rowPointers);
    delete []rowPointers;
    
// read the image data (row by row)
/*
    for (int index = 0; index < height; index++) {
        png_read_row(pngPtr,(data + index * rowbytes),(png_bytep)0);
    }
*/
    
// close the stream
    if (stream->close() == false) {
        fail(stream->getError());
        return (Image *)0;
    }
    delete stream;
    
    return new Image(width,height,data);
}
    
} // namespace
    
} // namespace
