#include <memory>
#include "GLDrawerIndex.h"

using namespace std;

namespace ae {

namespace gles {
 
/** */
void GLDrawerIndex::create() {
    data = new (nothrow) GLushort[getCapacity()];
    if (data == (GLushort *)0) {
        setNoMemoryError();
        return;
    }
}
    
/** */
bool GLDrawerIndex::setValue(int position,int value) {
    if (checkPosition(position) == false) {
        return false;
    }
    
    data[position] = (GLushort)value;
    return true;
}

/** */
void GLDrawerIndex::use() {
}
    
} // namespace

} // namespace