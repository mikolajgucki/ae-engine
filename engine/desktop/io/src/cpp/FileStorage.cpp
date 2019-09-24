#include <memory>
#include <vector>
#include <cstdio>

#include "Log.h"
#include "io_util.h"
#include "FileStorage.h"

using namespace std;

namespace ae {
    
namespace io {
    
/** */
void FileStorage::create(const string &dir) {
    vector<string> dirs;
    dirs.push_back(dir);
    
// input stream provider
    inputStreamProvider = new (nothrow) FileInputStreamProvider(dirs);
    if (inputStreamProvider == (FileInputStreamProvider *)0) {
        setNoMemoryError();
        return;
    }
    
// output stream provider
    outputStreamProvider = new (nothrow) FileOutputStreamProvider(dir);
    if (outputStreamProvider == (FileOutputStreamProvider *)0) {
        setNoMemoryError();
        return;
    }
}

/** */
FileStorage::~FileStorage() {
    if (inputStreamProvider != (FileInputStreamProvider *)0) {
        delete inputStreamProvider;
    }
    
    if (outputStreamProvider != (FileOutputStreamProvider *)0) {
        delete outputStreamProvider;
    }
}

/** */
const string FileStorage::getPath(const std::string &filename) {
    char separator = getFileSeparator();
    
    string path;
    path.append(dir);
    path.append(&separator,1);
    path.append(filename);
    
    return path;    
}

/** */
bool FileStorage::createFile(const std::string &filename) {
    const string path = getPath(filename);

// create
    FILE *file = fopen(path.c_str(),"a+");
    if (file == (FILE *)0) {
        stringstream err;
        err << "Failed to create file " << filename;
        setError(err.str());
        return false;
    }
    
// close
    if (fclose(file) != 0) {
        stringstream err;
        err << "Failed to create file " << filename;
        setError(err.str());
        return false;
    }    
    
    return true;
}

/** */
InputStream *FileStorage::getInputStream(const std::string &filename) {
    InputStream *stream = inputStreamProvider->getInputStream(filename);
    if (stream == (InputStream *)0) {
        setError(inputStreamProvider->getError());
        return (InputStream *)0;
    }
    
    return stream;
}

/** */
OutputStream *FileStorage::getOutputStream(const std::string &filename) {
    OutputStream *stream = outputStreamProvider->getOutputStream(filename);
    if (stream == (OutputStream *)0) {
        setError(outputStreamProvider->getError());
        return (OutputStream *)0;
    }
    
    return stream;
}
    
} // namespace
    
} // namespace