#ifndef AE_BASIC_MAT4_H
#define AE_BASIC_MAT4_H

#include <cmath>
#include "BasicVec3.h"

namespace ae {
    
namespace math {

/**
 * \brief The template class for 4x4 matrix.
 */
template<class T> class BasicMat4 {
    /** */
    enum {
        /** The number of elements in the matrix. */
        ELEMENT_COUNT = 16
    };
    
    /**
     * The matrix elements (column-major to fit OpenGL). The indices laid out
     * in the matrix are:
     *  0  4   8  12
     *  1  5   9  13
     *  2  6  10  14
     *  3  7  11  15
     */
    T matrix[ELEMENT_COUNT];
    
public:
    /** */
    BasicMat4() {
        identity();
    }
    
    /**
     * \brief Sets the values of the matrix elements.
     */
    void set(
        T a11,T a12,T a13,T a14,
        T a21,T a22,T a23,T a24,
        T a31,T a32,T a33,T a34,
        T a41,T a42,T a43,T a44) {
    //
        matrix[0] = a11;
        matrix[1] = a21;
        matrix[2] = a31;
        matrix[3] = a41;
        
        matrix[4] = a12;
        matrix[5] = a22;
        matrix[6] = a32;
        matrix[7] = a42;
        
        matrix[8] = a13;
        matrix[9] = a23;
        matrix[10] = a33;
        matrix[11] = a43;
        
        matrix[12] = a14;
        matrix[13] = a24;
        matrix[14] = a34;
        matrix[15] = a44;
    }
    
    /**
     * \brief Gets a matrix value by its ij location.
     *
     * @param i The i location (1-based).
     * @param j The j location (1-based).
     * @return The value.
     */
    T get(int i,int j) const {
        return matrix[(j - 1) * 4 + (i - 1)];
    }
    
    /**
     * \brief Assigns the values of another matrix to this matrix.
     * \param m The other matrix.
     */
    void assign(const BasicMat4<T> *m) {
        for (int index = 0; index < ELEMENT_COUNT; index++) {
            matrix[index] = m->matrix[index];
        }
    }
    
    /**
     * \brief Loads the identity matrix.
     */
    void identity() {
        set(
            1,0,0,0,
            0,1,0,0,
            0,0,1,0,
            0,0,0,1);
    }
    
    /**
     * \brief Multiplies two matrices and stores the result in this matrix.
     *
     * @param matrixA The first matrix.
     * @param matrixB The second matrix.
     */
    void mul(const BasicMat4<T> *matrixA,const BasicMat4<T> *matrixB) {
        const T *a = matrixA->asArray();
        const T *b = matrixB->asArray();
        
        matrix[0] = a[0]*b[0] + a[4]*b[1] + a[8]*b[2] + a[12]*b[3];
        matrix[1] = a[1]*b[0] + a[5]*b[1] + a[9]*b[2] + a[13]*b[3];
        matrix[2] = a[2]*b[0] + a[6]*b[1] + a[10]*b[2] + a[14]*b[3];
        matrix[3] = a[3]*b[0] + a[7]*b[1] + a[11]*b[2] + a[15]*b[3];

        matrix[4] = a[0]*b[4] + a[4]*b[5] + a[8]*b[6] + a[12]*b[7];
        matrix[5] = a[1]*b[4] + a[5]*b[5] + a[9]*b[6] + a[13]*b[7];
        matrix[6] = a[2]*b[4] + a[6]*b[5] + a[10]*b[6] + a[14]*b[7];
        matrix[7] = a[3]*b[4] + a[7]*b[5] + a[11]*b[6] + a[15]*b[7];

        matrix[8] = a[0]*b[8] + a[4]*b[9] + a[8]*b[10] + a[12]*b[11];
        matrix[9] = a[1]*b[8] + a[5]*b[9] + a[9]*b[10] + a[13]*b[11];
        matrix[10] = a[2]*b[8] + a[6]*b[9] + a[10]*b[10] + a[14]*b[11];
        matrix[11] = a[3]*b[8] + a[7]*b[9] + a[11]*b[10] + a[15]*b[11];

        matrix[12] = a[0]*b[12] + a[4]*b[13] + a[8]*b[14] + a[12]*b[15];
        matrix[13] = a[1]*b[12] + a[5]*b[13] + a[9]*b[14] + a[13]*b[15];
        matrix[14] = a[2]*b[12] + a[6]*b[13] + a[10]*b[14] + a[14]*b[15];
        matrix[15] = a[3]*b[12] + a[7]*b[13] + a[11]*b[14] + a[15]*b[15];
    }
    
    /**
     * \brief Loads the translation matrix.
     * \param x The X-coordinate of the translation vector.
     * \param y The Y-coordinate of the translation vector.
     * \param z The Z-coordinate of the translation vector.
     */
    void translate(T x,T y,T z) {
        set(
            1,0,0,x,
            0,1,0,y,
            0,0,1,z,
            0,0,0,1);
    }
    
    /**
     * \brief Loads the scale matrix.
     * \param sx The scale factor along the X-axis.
     * \param sy The scale factor along the Y-axis.
     * \param sz The scale factor along the Z-axis.
     */
    void scale(T sx,T sy,T sz) {
        set(
            sx,0, 0, 0,
            0, sy,0, 0,
            0, 0, sz,0,
            0, 0, 0, 1);            
    }
    
    /**
     * \brief Loads the translation and scale matrix.
     * \param x The X-coordinate of the translation vector.
     * \param y The Y-coordinate of the translation vector.
     * \param z The Z-coordinate of the translation vector.
     * \param sx The scale factor along the X-axis.
     * \param sy The scale factor along the Y-axis.
     * \param sz The scale factor along the Z-axis.
     */
    void translateAndScale(T x,T y,T z,T sx,T sy,T sz) {
        set(
            sx,0, 0, x,
            0, sy,0, y,
            0, 0, sz,z,
            0, 0, 0, 1);         
    }
    
    /**
     * \brief Loads the rotation matrix about the Z-axis.
     * \param angle The angle in radians.
     */
    void rotateOZ(double angle) {
        T sine = (T)sin(angle);
        T cosine = (T)cos(angle);
        
        set(
            cosine, -sine,0,0,
            sine,  cosine,0,0,
            0,     0,     1,0,
            0,     0,     0,1);         
    }
    
    /**
     * \brief Multiplies a vector by this matrix.
     * \param v The vector to multiply.
     * \param r The vector in which to store the result.
     */
    void mul(const BasicVec3<T>& v,BasicVec3<T>& r) {
        T w =
            v.x * matrix[3] + v.y * matrix[7] + v.z * matrix[11] + matrix[15];
            
        r.x = (v.x * matrix[0] + v.y * matrix[4] +
            v.z * matrix[8] + matrix[12]) / w;
        r.y = (v.x * matrix[1] + v.y * matrix[5] +
            v.z * matrix[9] + matrix[13]) / w;
        r.z = (v.x * matrix[2] + v.y * matrix[6] +
            v.z * matrix[10] + matrix[14]) / w;
    }
    
    /**
     * \brief Gets the matrix as column-major array.
     * \return The matrix as column-major array.
     */ 
    const T* asArray() const {
        return matrix;
    }
};
    
} // namespace
    
} // namespace

#endif // AE_BASIC_MAT4_H