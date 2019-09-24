#ifndef AE_GLES2_VERTEX_BUFFER_DATA_H
#define AE_GLES2_VERTEX_BUFFER_DATA_H

#include "Error.h"
#include "ae_GLES2.h"

namespace ae {
    
namespace gles2 {
  
/**
 * \brief Manages data passed to vertex buffer objects.
 */
class VertexBufferData:public Error {
    /// The size in bytes of single vertex data.
    int stride;
    
    /// The maximum number of vertices the buffer can hold.
    int capacity;
    
    /// The buffer data.
    unsigned char *data;
    
    /**
     * \brief Gets the data as unsigned char pointer.
     * \param index The vertex index.
     * \param offset The offset in bytes from the beginning of the vertex data.
     * \return The data.
     */
    unsigned char *getPointer(int index,int offset) {
    // offset in bytes from the beginning of the data
        size_t dataOffset = index * stride + offset;
        
    // cast to pointer
        unsigned char *pointer = data + dataOffset;
        return pointer;
    }
    
    /**
     * \brief Gets the data as GLfloat pointer.
     * \param index The vertex index.
     * \param offset The offset in bytes from the beginning of the vertex data.
     * \return The GLfloat data.
     */
    GLfloat *getGLfloatData(int index,int offset) {
        return (GLfloat *)getPointer(index,offset);
    }
    
    /**
     * \brief Gets the data as GLushort pointer.
     * \param index The vertex index.
     * \param offset The offset in bytes from the beginning of the vertex data.
     * \return The GLushort data.
     */
    GLushort *getGLushortData(int index,int offset) {
        return (GLushort *)getPointer(index,offset);
    }
    
    /**
     * \brief Gets the data as GLubyte pointer.
     * \param index The vertex index.
     * \param offset The offset in bytes from the beginning of the vertex data.
     * \return The GLubyte data.
     */
    GLubyte *getGLubyteData(int index,int offset) {
        return (GLubyte *)getPointer(index,offset);
    }    
    
public:
    /**
     * \brief Constructs a VertexBufferData.
     * \param stride_ The size in bytes of single vertex data.
     * \param capacity_ The maximum number of vertices the buffer can hold.
     */
    VertexBufferData(int stride_,int capacity_):Error(),stride(stride_),
        capacity(capacity_),data((unsigned char *)0) {
    }

    /** */
    virtual ~VertexBufferData() {
        free();
    }
    
    /**
     * \brief Allocates the data.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> otherwise.  
     */
    bool allocate();
    
    /**
     * \brief Frees the data.
     */
    void free();
    
    /**
     * \brief Sets a single GL float in vertex data.
     * \param index The vertex index.
     * \param offset The offset in bytes from the beginning of the vertex data.
     * \param v0 The value to set.
     */
    void set1f(int index,int offset,GLfloat v0);
    
    /**
     * \brief Sets two GL floats in vertex data.
     * \param index The vertex index.
     * \param offset The offset in bytes from the beginning of the vertex data.
     * \param v0 The 1st value to set.
     * \param v1 The 2nd value to set.
     */
    void set2f(int index,int offset,GLfloat v0,GLfloat v1);
    
    /**
     * \brief Sets three GL floats in vertex data.
     * \param index The vertex index.
     * \param offset The offset in bytes from the beginning of the vertex data.
     * \param v0 The 1st value to set.
     * \param v1 The 2nd value to set.
     * \param v2 The 3rd value to set.
     */
    void set3f(int index,int offset,GLfloat v0,GLfloat v1,GLfloat v2);
    
    /**
     * \brief Sets four GL floats in vertex data.
     * \param index The vertex index.
     * \param offset The offset in bytes from the beginning of the vertex data.
     * \param v0 The 1st value to set.
     * \param v1 The 2nd value to set.
     * \param v2 The 3rd value to set.
     * \param v3 The 4th value to set.
     */
    void set4f(int index,int offset,GLfloat v0,GLfloat v1,GLfloat v2,
        GLfloat v3);   
    
    /**
     * \brief Sets a single GL unsigned short in vertex data.
     * \param index The vertex index.
     * \param offset The offset in bytes from the beginning of the vertex data.
     * \param v0 The value to set.
     */
    void set1us(int index,int offset,GLushort v0);
    
    /**
     * \brief Sets two GL unsigned shorts in vertex data.
     * \param index The vertex index.
     * \param offset The offset in bytes from the beginning of the vertex data.
     * \param v0 The value to set.
     * \param v1 The value to set.
     */
    void set2us(int index,int offset,GLushort v0,GLushort v1);
    
    /**
     * \brief Sets three GL unsigned shorts in vertex data.
     * \param index The vertex index.
     * \param offset The offset in bytes from the beginning of the vertex data.
     * \param v0 The value to set.
     * \param v1 The value to set.
     * \param v2 The value to set.
     */
    void set3us(int index,int offset,GLushort v0,GLushort v1,GLushort v2);
    
    /**
     * \brief Sets four GL unsigned shorts in vertex data.
     * \param index The vertex index.
     * \param offset The offset in bytes from the beginning of the vertex data.
     * \param v0 The value to set.
     * \param v1 The value to set.
     * \param v2 The value to set.
     * \param v3 The value to set.
     */
    void set4us(int index,int offset,GLushort v0,GLushort v1,GLushort v2,
        GLushort v3);
    
    /**
     * \brief Sets a single GL unsigned byte in vertex data.
     * \param index The vertex index.
     * \param offset The offset in bytes from the beginning of the vertex data.
     * \param v0 The value to set.
     */
    void set1ub(int index,int offset,GLubyte v0);
    
    /**
     * \brief Sets two GL unsigned bytes in vertex data.
     * \param index The vertex index.
     * \param offset The offset in bytes from the beginning of the vertex data.
     * \param v0 The value to set.
     * \param v1 The value to set.
     */
    void set2ub(int index,int offset,GLubyte v0,GLubyte v1);    
    
    /**
     * \brief Sets three GL unsigned bytes in vertex data.
     * \param index The vertex index.
     * \param offset The offset in bytes from the beginning of the vertex data.
     * \param v0 The value to set.
     * \param v1 The value to set.
     * \param v2 The value to set.
     */
    void set3ub(int index,int offset,GLubyte v0,GLubyte v1,GLubyte v2);    
    
    /**
     * \brief Sets four GL unsigned bytes in vertex data.
     * \param index The vertex index.
     * \param offset The offset in bytes from the beginning of the vertex data.
     * \param v0 The value to set.
     * \param v1 The value to set.
     * \param v2 The value to set.
     * \param v3 The value to set.
     */
    void set4ub(int index,int offset,GLubyte v0,GLubyte v1,GLubyte v2,
        GLubyte v3);     
    
    /**
     * \brief Gets the stride.
     * \return The stride.
     */
    int getStride() const {
        return stride;
    }
    
    /**
     * \brief Gets the data size in bytes.
     * \return The data size in bytes.
     */
    int getSize() const {
        return stride * capacity;
    }
    
    /**
     * \brief Gets the pointer to the data.
     * \param offset The offset from the beginning of the data.
     * \return The pointer to the data.
     */
    void *getPointer(int offset = 0) const {
        return data + offset;
    }
};
    
} // namespace

} // namespace

#endif // AE_GLES2_VERTEX_BUFFER_DATA_H