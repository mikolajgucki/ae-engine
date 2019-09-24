#ifndef AE_IMAGE_PNG_IMAGE_LOADER_H
#define AE_IMAGE_PNG_IMAGE_LOADER_H

#include <string>

#include "png.h"
#include "InputStream.h"
#include "InputStreamProvider.h"
#include "ImageLoader.h"

namespace ae {
    
namespace image {
    
/**
 * \brief Loads images from PNG files.
 */
class PNGImageLoader:public ImageLoader {
    /** */
    enum {
        /** */
        PNG_SIGNATURE_SIZE = 8
    };
    
    /// The stream provider from which the images are read
    ae::io::InputStreamProvider *streamProvider;
    
    /// The read structure.
    png_structp pngPtr;
    
    /// The info structure.
    png_infop pngInfo;
    
    /// The stream from which the data is read.
    ae::io::InputStream *stream;
    
    /// The image data,    
    unsigned char *data;
    
    /// The pointers to the rows of the data the image is read to.
    png_bytep *rows;
    
    /**
     * \brief Cleans up and sets error.
     * \param error The error to set.
     */
    void fail(const std::string &error);
    
public:
    /// The log tag.
    static const char * const logTag;    
    
    /**
     * \brief Constructs a PNGImageLoader.
     * \param streamProvider_ Providers the streams from which the images
     *    are read.
     */
    PNGImageLoader(ae::io::InputStreamProvider *streamProvider_):
        ImageLoader(),streamProvider(streamProvider_),pngPtr((png_structp)0),
        pngInfo((png_infop)0),stream((ae::io::InputStream *)0),
        data((unsigned char *)0),rows((png_bytep *)0) {        
    }
    
    /**
     * \brief Gets the input stream from which the image is being read.
     * \return The input stream.
     */
    ae::io::InputStream *getStream() {
        return stream;
    }
    
    /**
     * \brief Loads the image of given identifier.
     * \param id The image identifier.
     * \return The loaded image.
     */
    virtual Image *loadImage(const std::string &id);    
};
    
} // namespace
    
} // namespace

#endif // AE_IMAGE_PNG_IMAGE_LOADER_H