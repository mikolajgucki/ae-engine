#include "Log.h"
#include "FileInputStream.h"

using namespace std;

namespace ae {

namespace io {
    
/** */
FileInputStream::FileInputStream(const string &filename_):
    InputStream(),filename(filename_),file((FILE *)0) {
}
 
/** */
bool FileInputStream::open() {
    if (file != (FILE *)0) {
        ostringstream err;
        err << "File " << filename << " already open";
        setError(err.str());
        return false;        
    }
    
    file = fopen(filename.c_str(),"rb");
    if (file == (FILE *)0) {
        stringstream err;
        err << "Failed to open file " << filename;
        setError(err.str());
        return false;
    }
    
    return true;
}

/** */
bool FileInputStream::read(int &value) {
    if (file == (FILE *)0) {
        ostringstream err;
        err << "File " << filename << " not open";
        setError(err.str());
        return false;
    }
    
    unsigned char ch;
    size_t size = fread(&ch,1,1,file);
    if (size == 0) {
        if (ferror(file) != 0) {
            ostringstream err;
            err << "Error reading from file " << filename;
            setError(err.str());
            return false;
        }
        
        value = NO_DATA;
        return true;
    }
    
    value = (int)(ch & 0xff);
    return true;
}

/** */
bool FileInputStream::close() {
// if not open or already closed
    if (file == (FILE *)0) {
        return true;
    }
    
    if (fclose(file) != 0) {
        stringstream err;
        err << "Failed to close file " << filename;
        setError(err.str());
        return false;
    }
    
    file = (FILE *)0;
    return true;
}
    
} // namespace
    
} // namespace
