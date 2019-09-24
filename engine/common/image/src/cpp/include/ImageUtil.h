#ifndef AE_IMAGE_UTIL_H
#define AE_IMAGE_UTIL_H

#include "LinearInterpolation.h"
#include "Image.h"
#include "Error.h"

namespace ae {
    
namespace image {
  
/**
 * \brief Provides image utility methods.
 */
class ImageUtil:public Error {
    /// The interpolation.
    ::ae::math::LinearInterpolation linearInterpolation;
    
    /** Color linear interpolation. */
    Image::Color linear(Image::Color a,Image::Color b,double t);
    
public:
    /** */
    ImageUtil():Error(),linearInterpolation() {
    }
    
    /** */
    virtual ~ImageUtil() {
    }
    
    /**
     * \brief Puts an image (does not take alpha channel into account).
     * \param dst The destination image in which to put.
     * \param xdst The destination X coordinate.
     * \param ydst The destination Y coordinate.
     * \param src The source image.
     * \param xsrc The source X coordinate.
     * \param ysrc The source Y coordinate.
     * \param width The width.
     * \param height The height.
     */
    void put(Image *dst,int xdst,int ydst,Image *src,int xsrc,int ysrc,
        int width,int height);
    
    
    /**
     * \brief Draws an image (takes alpha alpha into account).
     * \param dst The destination image in which to put.
     * \param xdst The destination X coordinate.
     * \param ydst The destination Y coordinate.
     * \param src The source image.
     * \param xsrc The source X coordinate.
     * \param ysrc The source Y coordinate.
     * \param width The width.
     * \param height The height.
     */
    void draw(Image *dst,int xdst,int ydst,Image *src,int xsrc,int ysrc,
        int width,int height);    
    
    /**
     * \brief Gets a color of a pixel with subpixel precision.
     * \param src The source image.
     * \param x The X coordinate.
     * \param y The Y coordinate.
     * \return The subpixel color.
     */
    Image::Color getSubpixelColor(Image *src,double x,double y);
    
    /**
     * \brief Scales an image.
     * \param src The source image.
     * \param width The width of the scaled image.
     * \param height The height of the scaled image.
     * \return The scaled image.
     */
    Image *scale(Image *src,int width,int height);
};
    
} // namespace
    
} // namespace

#endif //  AE_IMAGE_UTIL_H