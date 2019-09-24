#ifndef AE_IMAGE_IMAGE_H
#define AE_IMAGE_IMAGE_H

namespace ae {
  
namespace image {    
    
/**
 * \brief Represents a 2D RGBA bitmap image.
 */
class Image {
public:
    /**
     * \brief Represents a RGBA color.
     */
    class Color {
    public:
        /// The red component.
        int r;        
        
        /// The green component;
        int g;
        
        /// The blue component.
        int b;        
        
        /// The alpha component;
        int a;
        
        /** */
        Color():r(0),g(0),b(0),a(255) {
        }
        
        /** */
        Color(int r_,int g_,int b_,int a_):r(r_),g(g_),b(b_),a(a_) {
        }
    };
    
private:    
    /// The width of the image.
    int width;
    
    /// The height of the image.
    int height;
    
    /// The image data.
    unsigned char *data;
    
public:
    /** */
    enum {
        /// The red offset within a pixel.
        RED_OFFSET = 0,
        
        /// The green offset within a pixel.
        GREEN_OFFSET = 1,
        
        /// The blue offset within a pixel.
        BLUE_OFFSET = 2,
        
        /// The alpha offset within a pixel.
        ALPHA_OFFSET = 3,
        
        /// The bytes per pixel.
        BYTES_PER_PIXEL = 4
    };
    
    /**
     * \brief Constructs an Image.
     * \param width_ The width of the image.
     * \param height_ The height of the image.
     * \param data_ The RGBA image data (deleted in the destructor).
     */
    Image(int width_,int height_,unsigned char *data_):
        width(width_),height(height_),data(data_) {
    }
    
    /** */
    virtual ~Image() {
        deleteData();
    }
    
    /**
     * \brief Gets the image width.
     * \return The image width.
     */
    int getWidth() const {
        return width;
    }
    
    /**
     * \brief Gets the image height.
     * \return The image height.
     */
    int getHeight() const {
        return height;
    }
    
    /**
     * \brief Gets the image data.
     * \return The image RGBA data.
     */
    unsigned char *getData() const {
        return data;
    }   
    
    /**
     * \brief Checks if a pixel is outside the image.
     * \param x The X coordinate.
     * \param y The Y coordinate.
     * \return <code>true</code> if outside, <code>false</code> otherwise.
     */
    bool isOutside(int x,int y) {
        return x < 0 || x >= width || y < 0 || y >= height;
    }
    
    /**
     * \brief Gets a pixel offset.
     * \param x The X coordinate.
     * \param y The Y coordinate.
     * \return The offset of the red component of the pixel at the coordinates.
     */
    int getOffset(int x,int y,int offset = 0) {
        return y * width * BYTES_PER_PIXEL + x * BYTES_PER_PIXEL + offset;
    }     
    
    /**
     * \brief Gets the red component of a pixel.
     * \param x The X coordinate.
     * \param y The Y coordinate.
     * \return The red component.
     */
    int getRed(int x,int y) {
        return data[getOffset(x,y,RED_OFFSET)];
    }
    
    /**
     * \brief Gets the green component of a pixel.
     * \param x The X coordinate.
     * \param y The Y coordinate.
     * \return The green component.
     */
    int getGreen(int x,int y) {
        return data[getOffset(x,y,GREEN_OFFSET)];
    }    
    
    /**
     * \brief Gets the blue component of a pixel.
     * \param x The X coordinate.
     * \param y The Y coordinate.
     * \return The blue component.
     */
    int getBlue(int x,int y) {
        return data[getOffset(x,y,BLUE_OFFSET)];
    }
    
    /**
     * \brief Gets the alpha component of a pixel.
     * \param x The X coordinate.
     * \param y The Y coordinate.
     * \return The alpha component.
     */
    int getAlpha(int x,int y) {
        return data[getOffset(x,y,ALPHA_OFFSET)];
    }
    
    /**
     * \brief Sets a pixel.
     * \param x The X coordinate.
     * \param y The Y coordinate.     
     * \param red The red component.
     * \param green The green component.
     * \param blue The blue component.
     * \param alpha The alpha component.
     */
    void setRGBA(int x,int y,int red,int green,int blue,int alpha) {
        int offset = getOffset(x,y);
        data[offset + RED_OFFSET] = (unsigned char)red;
        data[offset + GREEN_OFFSET] = (unsigned char)green;
        data[offset + BLUE_OFFSET] = (unsigned char)blue;
        data[offset + ALPHA_OFFSET] = (unsigned char)alpha;
    }
    
    /**
     * \brief Gets a color of a pixel.
     * \param x The X coordinate.
     * \param y The Y coordinate. 
     * \return The color.     
     */
    Color getColor(int x,int y) {
        int offset = getOffset(x,y);
        return Color(
            data[offset + RED_OFFSET],
            data[offset + GREEN_OFFSET],
            data[offset + BLUE_OFFSET],
            data[offset + ALPHA_OFFSET]);
    }
    
    /**
     * \brief Sets a color of a pixel.
     * \param x The X coordinate.
     * \param y The Y coordinate. 
     * \param color The color.         
     */
    void setColor(int x,int y,Color color) {
        int offset = getOffset(x,y);
        data[offset + RED_OFFSET] = (unsigned char)color.r;
        data[offset + GREEN_OFFSET] = (unsigned char)color.g;
        data[offset + BLUE_OFFSET] = (unsigned char)color.b;
        data[offset + ALPHA_OFFSET] = (unsigned char)color.a;        
    }
    
    /**
     * \brief Deletes the image data.
     */
    void deleteData() {
        if (data != (unsigned char *)0) {
            delete[] data;
            data = (unsigned char *)0;            
        }
    }
};

} // namespace

} // namespace

#endif // AE_IMAGE_IMAGE_H