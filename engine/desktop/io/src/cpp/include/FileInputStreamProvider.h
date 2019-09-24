#ifndef AE_IO_FILE_INPUT_STREAM_PROVIDER_H
#define AE_IO_FILE_INPUT_STREAM_PROVIDER_H

#include <string>
#include <vector>

#include "InputStreamProvider.h"

namespace ae {
    
namespace io {
  
/**
 * \brief Provides input stream of given file names. Looks for the files
 *   in given list of directories.
 */
class FileInputStreamProvider:public InputStreamProvider {
    /// The directories in which to search for the files.
    std::vector<std::string> dirs;
    
public:
    /** 
     * \brief Constructs a FileInputStreamProvider.
     * \param _dirs The directories in which to search for the files.
     */
    FileInputStreamProvider(std::vector<std::string> _dirs):dirs(_dirs) {
    }
    
    /** */
    virtual ~FileInputStreamProvider() {
    }
    
    /**
     * \brief Gets the path to a file.
     * \param filename The name of the file.
     * \param path The result path.
     * \return <code>true</code> if the file exists, <code>false</code>
     *     otherwise.
     */
    bool getPath(const std::string &filename,std::string &path);
    
    /** */
    virtual InputStream *getInputStream(const std::string &filename);
};
    
} // namespace
    
} // namespace

#endif // AE_IO_FILE_INPUT_STREAM_PROVIDER_H