#include <memory>
#include "VertexBufferData.h"

using namespace std;

namespace ae {
    
namespace gles2 {
    
/** */
bool VertexBufferData::allocate() {
    if (data != (unsigned char *)0) {
        return true;
    }
    
    size_t size = getSize();  
    data = new (nothrow) unsigned char[size];
    if (data == (unsigned char *)0) {
        setNoMemoryError();
        return false;
    }
    
    return true;
}

/** */
void VertexBufferData::free() {
    if (data != (unsigned char *)0) {
        delete[] data;
        data = (unsigned char *)0;
    }
}
 
/** */
void VertexBufferData::set1f(int index,int offset,GLfloat v0) {
    GLfloat *floatData = getGLfloatData(index,offset);
    (*floatData) = v0;
}

/** */
void VertexBufferData::set2f(int index,int offset,GLfloat v0,GLfloat v1) {
    GLfloat *floatData = getGLfloatData(index,offset);    
    (*floatData) = v0;
    floatData++;
    (*floatData) = v1;
}

/** */
void VertexBufferData::set3f(int index,int offset,GLfloat v0,GLfloat v1,
    GLfloat v2) {
//
    GLfloat *floatData = getGLfloatData(index,offset);    
    (*floatData) = v0;
    floatData++;
    (*floatData) = v1;
    floatData++;
    (*floatData) = v2;
}

/** */
void VertexBufferData::set4f(int index,int offset,GLfloat v0,GLfloat v1,
    GLfloat v2,GLfloat v3) {
//
    GLfloat *floatData = getGLfloatData(index,offset);    
    (*floatData) = v0;
    floatData++;
    (*floatData) = v1;
    floatData++;
    (*floatData) = v2;
    floatData++;
    (*floatData) = v3;
}

/** */
void VertexBufferData::set1us(int index,int offset,GLushort v0) {
    GLushort *ushortData = getGLushortData(index,offset);
    (*ushortData) = v0;
}

/** */
void VertexBufferData::set2us(int index,int offset,GLushort v0,GLushort v1) {
    GLushort *ushortData = getGLushortData(index,offset);
    (*ushortData) = v0;
    ushortData++;
    (*ushortData) = v1;
}

/** */
void VertexBufferData::set3us(int index,int offset,GLushort v0,GLushort v1,
    GLushort v2) {
//
    GLushort *ushortData = getGLushortData(index,offset);
    (*ushortData) = v0;
    ushortData++;
    (*ushortData) = v1;
    ushortData++;
    (*ushortData) = v2;
}

/** */
void VertexBufferData::set4us(int index,int offset,GLushort v0,GLushort v1,
    GLushort v2,GLushort v3) {
//
    GLushort *ushortData = getGLushortData(index,offset);
    (*ushortData) = v0;
    ushortData++;
    (*ushortData) = v1;
    ushortData++;
    (*ushortData) = v2;
    ushortData++;
    (*ushortData) = v3;
}

/** */
void VertexBufferData::set1ub(int index,int offset,GLubyte v0) {
    GLubyte *ubyteData = getGLubyteData(index,offset);
    (*ubyteData) = v0;
}

/** */
void VertexBufferData::set2ub(int index,int offset,GLubyte v0,GLubyte v1) {
    GLubyte *ubyteData = getGLubyteData(index,offset);
    (*ubyteData) = v0;
    ubyteData++;
    (*ubyteData) = v1;
}

/** */
void VertexBufferData::set3ub(int index,int offset,GLubyte v0,GLubyte v1,
    GLubyte v2) {
//
    GLubyte *ubyteData = getGLubyteData(index,offset);
    (*ubyteData) = v0;
    ubyteData++;
    (*ubyteData) = v1;
    ubyteData++;
    (*ubyteData) = v2;
}

/** */
void VertexBufferData::set4ub(int index,int offset,GLubyte v0,GLubyte v1,
    GLubyte v2,GLubyte v3) {
//
    GLubyte *ubyteData = getGLubyteData(index,offset);
    (*ubyteData) = v0;
    ubyteData++;
    (*ubyteData) = v1;
    ubyteData++;
    (*ubyteData) = v2;
    ubyteData++;
    (*ubyteData) = v3;
}

} // namespace

} // namespace