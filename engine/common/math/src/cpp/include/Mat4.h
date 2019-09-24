#ifndef AE_MAT4_H
#define AE_MAT4_H

#include "BasicMat4.h"

namespace ae {
    
namespace math {
    
/// The type of the default matrix elements.
typedef float Mat4ElementType;
    
/// The default 4x4 matrix.
typedef BasicMat4<Mat4ElementType> Mat4;

/// The identity matrix.
extern const Mat4 MAT4_IDENTITY;
    
} // namespace
    
} // namespace

#endif // AE_MAT4_H