#include "FileOutputStream.h"

using namespace std;

namespace ae {
    
namespace io {
    
/** */
FileOutputStream::FileOutputStream(const string &filename_):
    OutputStream(),filename(filename_),file((FILE *)0) {
}

/** */
bool FileOutputStream::open() {
    if (file != (FILE *)0) {
        ostringstream err;
        err << "File " << filename << " already open";
        setError(err.str());
        return false;        
    }
    
    file = fopen(filename.c_str(),"wb+");
    if (file == (FILE *)0) {
        stringstream err;
        err << "Failed to open file " << filename;
        setError(err.str());
        return false;
    }
    
    return true;    
}

/** */
bool FileOutputStream::write(int value) {
    if (file == (FILE *)0) {
        ostringstream err;
        err << "File " << filename << " not open";
        setError(err.str());
        return false;
    }
    
    unsigned char ch = (unsigned char)(value & 0xff);
    size_t size = fwrite(&ch,1,1,file);
    if (size == 0) {
        ostringstream err;
        err << "Error writing to file " << filename;
        setError(err.str());
        return false;
    }
    
    return true;
}

/** */
bool FileOutputStream::close() {
// if not open or already closed
    if (file == (FILE *)0) {
        return true;
    }
    
// flush
    if (fflush(file) != 0) {
        stringstream err;
        err << "Failed to flush file " << filename;
        setError(err.str());
        return false;
    }

// close
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