#include "io_util.h"
#include "FileInputStream.h"
#include "FileInputStreamProvider.h"

using namespace std;

namespace ae {
    
namespace io {
  
/** */
bool FileInputStreamProvider::getPath(const string &filename,string &path) {
    char separator = getFileSeparator();
    
    vector<string>::iterator itr;
    for (itr = dirs.begin(); itr != dirs.end(); ++itr) {
    // build the fill name preceeded with the directory
        path.clear();
        path.append(*itr);
        path.append(&separator,1);
        path.append(filename);
        
    // check if the file exists
        if (checkFileExists(path) == true) {
            return true;
        }
    }
    
// error
    ostringstream err;
    err << "File " << filename << " not found";
    setError(err.str());
    
    return false;
}
    
/** */
InputStream *FileInputStreamProvider::getInputStream(const string &filename) {
    string path;
    if (getPath(filename,path) == true) {
        return new FileInputStream(path);        
    }

    return (InputStream *)0; 
}
    
} // namespace
    
} // namespace