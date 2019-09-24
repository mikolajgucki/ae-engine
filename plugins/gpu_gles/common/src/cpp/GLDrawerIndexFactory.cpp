#include <memory>

#include "GLDrawerIndex.h"
#include "GLDrawerIndexFactory.h"

using namespace std;
using namespace ae::gpu;

namespace ae {

namespace gles {
    
/** */
DrawerIndex *GLDrawerIndexFactory::createDrawerIndex(int capacity) {
    GLDrawerIndex *index = new (nothrow) GLDrawerIndex(capacity);
    if (index == (GLDrawerIndex *)0) {
        setNoMemoryError();
        return (DrawerIndex *)0;
    }
    if (index->chkError()) {
        setError(index->getError());
        delete index;
        return (DrawerIndex *)0;
    }
    
    return index;
}
    
} // namespace

} // namespace