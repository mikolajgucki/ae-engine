#ifndef AE_TEXTURE_TEXTURE_H
#define AE_TEXTURE_TEXTURE_H

#include <string>
#include "Error.h"

namespace ae {
  
namespace texture {
    
/**
 * \brief Represents a texture.
 */
class Texture:public Error {
protected:
    /** The texture identifier. */
    const std::string id;
    
    /// The texture width.
    int width;
    
    /// The texture height.
    int height;
    
protected:
    /**
     * \brief Sets the texture size.
     * \param width_ The width.
     * \param height_ The height.
     */
    void setSize(int width_,int height_) {
        width = width_;
        height = height_;        
    }
    
public:
    /**
     * \brief Constructs a Texture.
     * \param _id The texture identifier.
     */
    Texture(const std::string _id):Error(),id(_id) {
    }
    
    /** */
    virtual ~Texture() {
    }
    
    /** 
     * \brief Gets the texture identifier.
     * \return The texture identifier.
     */
    const std::string& getId() const {
        return id;
    }
    
    /**
     * \brief Gets the texture width.
     * \return The texture width.
     */
    int getWidth() const {
        return width;
    }
    
    /**
     * \brief Gets the texture height.
     * \return The texture height.
     */
    int getHeight() const {
        return height;
    }
};

} // namespace

} // namespace

#endif // AE_TEXTURE_TEXTURE_H