#include "io_util.h"
#include "FileOutputStream.h"
#include "FileOutputStreamProvider.h"

using namespace std;

namespace ae {
    
namespace io {
    
OutputStream *FileOutputStreamProvider::getOutputStream(const string &filename) {
    char separator = getFileSeparator();
    
    string path;
    path.append(dir);
    path.append(&separator,1);
    path.append(filename);
    
    return new FileOutputStream(path);
}
    
} // namespace
    
} // namespace