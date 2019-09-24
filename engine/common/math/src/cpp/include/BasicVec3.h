#ifndef AE_BASIC_VEC3_H
#define AE_BASIC_VEC3_H

namespace ae {

namespace math {
    
/**
 * \brief The template class for a 3D vector.
 */
template<class T> class BasicVec3 {
public:
    /// The X coordinate.
    T x;
    
    /// The Y coordinate.
    T y;
    
    /// The Z coordinate.
    T z;
    
    /** */
    BasicVec3():x(0),y(0),z(0) {
    }
    
    /**
     * \brief Tests if a 2D point is inside a 2D triangle.
     * \param p The point.
     * \param pa A triangle vertex.
     * \param pb A triangle vertex.
     * \param pc A triangle vertex.
     * \param epsilon The triangle edge thinkness.
     */
    static bool inside2DTriangle(const BasicVec3<T>& p,const BasicVec3<T>& pa,
        const BasicVec3<T>& pb,const BasicVec3<T>& pc,T epsilon) {
    // calculate barycentric coordinates
        T denominator = ((pb.y - pc.y) * (pa.x - pc.x) +
            (pc.x - pb.x) * (pa.y - pc.y));
        
        T a = ((pb.y - pc.y) * (p.x - pc.x) +
            (pc.x - pb.x) * (p.y - pc.y)) / denominator;
        T b = ((pc.y - pa.y) * (p.x - pc.x) +
            (pa.x - pc.x) * (p.y - pc.y)) / denominator;
        T c = 1 - a - b;
        
        return
            a >= -epsilon && a <= 1 + epsilon &&
            b >= -epsilon && b <= 1 + epsilon &&
            c >= -epsilon && c <= 1 + epsilon;
    }
};
 
} // namespace
    
} // namespace

#endif // AE_BASIC_VEC3_H