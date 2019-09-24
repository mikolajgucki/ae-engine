#ifndef AE_DEFAULT_DRAWER_H
#define AE_DEFAULT_DRAWER_H

#include "Error.h"
#include "Color.h"
#include "Mat4.h"
#include "Texture.h"
#include "DrawerIndex.h"
#include "GPUQueue.h"
#include "DefaultDrawerFeatures.h"

namespace ae {

namespace gpu {
  
/**
 * \brief The abstract default drawer. Implementations are provided by other
 *   plugins. 
 */
class DefaultDrawer:public Error {
    /// The features.
    DefaultDrawerFeatures features;
    
    /// The capacity.
    int capacity;
    
    /// The drawer color applied to all the vertices.
    ::ae::math::Color color;
    
protected:
    /** */
    DefaultDrawer(DefaultDrawerFeatures features_,int capacity_):Error(),
        features(features_),capacity(capacity_) {
    }
    
    /** */
    bool checkVertexPosition(int position) {
        if (position >= capacity) {
            setError("Invalid vertex position");
            return false;
        }
        
        return true;
    }
    
public:
    /** */
    virtual ~DefaultDrawer() {
    }
    
    /**
     * \brief Gets the drawer features.
     * \return The features.
     */
    const DefaultDrawerFeatures& getFeatures() const {
        return features;
    }
    
    /**
     * \brief Gets the drawer capacity.
     * \return The drawer capacity.
     */
    int getCapacity() const {
        return capacity;
    }
    
    /**
     * \brief Sets the color applied to all the vertices.
     * \param color The color.
     */
    void setColor(::ae::math::Color color_) {
        color = color_;
    }
    
    /**
     * \brief Gets the color applied to all the vertices.
     * \return The color.
     */
    const ::ae::math::Color getColor() const {
        return color;
    }
    
    /**
     * \brief Makes the drawer active so that all the following operations
     *   changing the data are made on the drawer.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.     
     */
    virtual bool activate() = 0;
    
    /**
     * \brief Sets vertex coordinates.
     * \param position The vertex position.
     * \param x The X coordinate.
     * \param y The Y coordinate.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.     
     */
    virtual bool setVertexCoords(int position,float x,float y) = 0;
    
    /**
     * \brief Sets a vertex color.
     * \param position The vertex position.
     * \param color The color.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.
     */
    virtual bool setVertexColor(int position,::ae::math::Color color) = 0;
    
    /**
     * \brief Sets texture coordinates.
     * \param position The vertex position.
     * \param u The U coordinate.
     * \param v The V coordinate.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.       
     */
    virtual bool setTextureCoords(int position,float u,float v) = 0;
    
    /**
     * \brief Sets vertex and texture coordinates.
     * \param position The vertex position.
     * \param x The X coordinate.
     * \param y The Y coordinate.     
     * \param u The U coordinate.
     * \param v The V coordinate.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.      
     */
    virtual bool setCoords(int position,float x,float y,float u,float v) = 0;
    
    /**
     * \brief Moves the buffer to GPU.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.       
     */
    virtual bool moveToGPU() = 0;
    
    /**
     * \brief Deletes the drawer data kept on the CPU side.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.       
     */
    virtual bool deleteCPUData() = 0;
    
    /**
     * \brief Enqueues a command to draw triangles.
     * \param queue The queue.
     * \param first The position of the first vertex to draw.
     * \param count The count of the vertices to draw.
     * \param drawerIndex The drawer index (can be null).
     * \param texture The texture (can be null).
     * \param transformation The transformation matrix (can be null).
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.      
     */
    virtual bool enqueue(GPUQueue *queue,int first,int count,
        DrawerIndex *drawerIndex,::ae::texture::Texture *texture,
        const ::ae::math::Mat4 *transformation) = 0;
};
    
} // namespace

} // namespace

#endif // AE_DEFAULT_DRAWER_H