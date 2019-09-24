#include <cmath>
#include "ImageUtil.h"

using namespace std;

namespace ae {
    
namespace image {
 
/** */
Image::Color ImageUtil::linear(Image::Color a,Image::Color b,double t) {
    return Image::Color(
        (int)linearInterpolation.interpolate(a.r,b.r,t),
        (int)linearInterpolation.interpolate(a.g,b.g,t),
        (int)linearInterpolation.interpolate(a.b,b.b,t),
        (int)linearInterpolation.interpolate(a.a,b.a,t));
}    
    
/** */
void ImageUtil::put(Image *dst,int xdst,int ydst,Image *src,int xsrc,int ysrc,
    int width,int height) {
//
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
        // source pixel coordinates
            int xs = x + xsrc;
            int ys = y + ysrc;
            
        // check if the source pixel is outside the source image        
            if (src->isOutside(xs,ys)) {
                continue;
            }
                
        // destination pixel coordinates
            int xd = x + xdst;
            int yd = y + ydst;
            
        // check if the destination pixel is outside the destination image
            if (dst->isOutside(xd,yd)) {
                continue;
            }
            
        // source pixel
            int r = src->getRed(xs,ys);
            int g = src->getGreen(xs,ys);
            int b = src->getBlue(xs,ys);
            int a = src->getAlpha(xs,ys);
            
        // destination pixel
            dst->setRGBA(xd,yd,r,g,b,a);
        }
    }        
}

/** */
void ImageUtil::draw(Image *dst,int xdst,int ydst,Image *src,int xsrc,int ysrc,
    int width,int height) {
//
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
        // source pixel coordinates
            int xs = x + xsrc;
            int ys = y + ysrc;
            
        // check if the source pixel is outside the source image        
            if (src->isOutside(xs,ys)) {
                continue;
            }
                
        // destination pixel coordinates
            int xd = x + xdst;
            int yd = y + ydst;
            
        // check if the destination pixel is outside the destination image
            if (dst->isOutside(xd,yd)) {
                continue;
            }
            
        // pixels
            Image::Color srcColor = src->getColor(xs,ys);
            Image::Color dstColor = dst->getColor(xd,yd);
            
        // destination pixel
            double t = srcColor.a / 255.0;
            dst->setColor(xd,yd,linear(srcColor,dstColor,1 - t));
        }
    }        
}

/** */
Image::Color ImageUtil::getSubpixelColor(Image *src,double x,double y) {
    x -= 0.5;
    y -= 0.5;
    
    int xi = (int)floor(x);
    int yi = (int)floor(y);
    
    double xf = x - xi;
    double yf = y - yi;
    
    int x0 = xi;
    if (x0 < 0) {
        x0 = 0;
    }
    
    int y0 = yi;
    if (y0 < 0) {
        y0 = 0;
    }
    
    int x1 = xi + 1;
    if (x1 >= src->getWidth()) {
        x1 = src->getWidth() - 1;
    }
    
    int y1 = yi + 1;
    if (y1 >= src->getHeight()) {
        y1 = src->getHeight() - 1;
    }
    
    Image::Color c00 = src->getColor(x0,y0);
    Image::Color c10 = src->getColor(x1,y0);
    Image::Color c11 = src->getColor(x1,y1);
    Image::Color c01 = src->getColor(x0,y1);
    
    Image::Color c0 = linear(c00,c10,xf);
    Image::Color c1 = linear(c01,c11,xf);
    
    return linear(c0,c1,yf);
}

/** */
Image *ImageUtil::scale(Image *src,int width,int height) {
// data
    size_t size = width * height * Image::BYTES_PER_PIXEL;
    unsigned char *data = new (nothrow) unsigned char[size];
    if (data == (unsigned char *)0) {
        setNoMemoryError();
        return (Image *)0;
    }
    
// destination image
    Image *dst = new (nothrow) Image(width,height,data);
    if (dst == (Image *)0) {
        delete[] data;
        setNoMemoryError();
        return (Image *)0;
    }
    
    for (int y = 0; y < height; y++) {
        double ys = y * src->getHeight() / (double)height;
        
        for (int x = 0; x < width; x++) {
            double xs = x * src->getWidth() / (double)width;
            
            Image::Color color = getSubpixelColor(src,xs,ys);
            dst->setColor(x,y,color);
        }
    }
    
    return dst;
}
    
} // namespace

} // namespace