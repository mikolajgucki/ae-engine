#ifndef AE_DEFAULT_DRAWER_FEATURES_H
#define AE_DEFAULT_DRAWER_FEATURES_H

#include <string>

namespace ae {

namespace gpu {
  
/**
 * \brief The default drawer features.
 */
class DefaultDrawerFeatures {
    /// The color.
    bool color;
    
    /// The vertex color.
    bool vertexColor;
    
    /// The texture.
    bool texture;
    
public:
    DefaultDrawerFeatures():color(false),vertexColor(false),
        texture(false) {
    //
    }
    
    /** */
    void setColor(bool color_) {
        color = color_;
    }
    
    /** */
    bool hasColor() const {
        return color;
    }
    
    /** */
    void setVertexColor(bool vertexColor_) {
        vertexColor = vertexColor_;
    }
    
    /** */
    bool hasVertexColor() const {
        return vertexColor;
    }
    
    /** */
    void setTexture(bool texture_) {
        texture = texture_;
    }
    
    /** */
    bool hasTexture() const {
        return texture;
    }
    
    /**
     * \brief Gets the features configuration index.
     * \return The index.
     */
    int getIndex() const;
    
    /**
     * \brief Gets the maximum features configuration index.
     * \return The maximum index.
     */
    static int getMaxIndex();
    
    /** */
    const std::string toString() const;
    
    /**
     * \brief Converts string to features.
     * \param str The string.
     * \param features The features to set.
     * \return <code>true</code> on success, <code>false</code> otherwise.
     */
    static bool fromString(const std::string& str,
        DefaultDrawerFeatures *features);
};
    
} // namespace

} // namespace

#endif // AE_DEFAULT_DRAWER_FEATURES_H