#ifndef AE_IO_FILE_ASSETS_H
#define AE_IO_FILE_ASSETS_H

#include <string>
#include <vector>

#include "InputStream.h"
#include "FileInputStreamProvider.h"
#include "Assets.h"

namespace ae {
    
namespace io {
  
/**
 * \brief Assets access reading from files using functions from
 *   <code>cstdio</code>.
 */
class FileAssets:public Assets {
    /// The file input stream provider.
    FileInputStreamProvider *inputStreamProvider;
    
    /**
     * \brief Creates the file assets.
     * \param dir_ The directory with the assets.
     */
    void create(const std::vector<std::string> &dirs);
    
public:
    /**
     * \brief Constructs a FileAssets.
     * \param dirs The directories with the assets.
     */
    FileAssets(const std::vector<std::string> &dirs):Assets(),
        inputStreamProvider((FileInputStreamProvider *)0) {
    //
        create(dirs);
    }
    
    /** */
    virtual ~FileAssets();
    
    /** */
    virtual InputStream *getInputStream(const std::string &filename);
};
    
} // namespace
    
} // namespace

#endif // AE_IO_FILE_ASSETS_H