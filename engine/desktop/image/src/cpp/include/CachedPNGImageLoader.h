#ifndef AE_IMAGE_CACHED_PNG_IMAGE_LOADER_H
#define AE_IMAGE_CACHED_PNG_IMAGE_LOADER_H

#include <vector>

#include "System.h"
#include "FileInputStreamProvider.h"
#include "PNGImageLoader.h"

namespace ae {
    
namespace image {
 
/**
 * \brief Image loader which caches the loaded PNG images and reloads them if
 *     the file modification time changes.
 */
class CachedPNGImageLoader:public ImageLoader {
    /**
     * \brief A cache entry.
     */
    class CacheEntry {
        /// The image identifier.
        const std::string imageId;
        
        /// The image.
        Image *image;
        
        /// The modification time of the image file.
        unsigned long modificationTime;
        
    public:
        /**
         * \brief Constructs a CacheEntry.
         * \param imageId_ The image identifier.
         * \param image_ The image.
         * \param modificationTime The modification time of the image file.
         */
        CacheEntry(const std::string &imageId_,Image *image_,
            unsigned long modificationTime_):imageId(imageId_),image(image_),
            modificationTime(modificationTime_) {
        }
        
        /**
         * \brief Gets the image identifier.
         * \return The image identifier.
         */
        const std::string& getImageId() const {
            return imageId;
        }
        
        /**
         * \brief Gets the image.
         * \return The image.
         */
        Image *getImage() const {
            return image;
        }
        
        /**
         * \brief Gets the modification time of the image file.
         * \return The modification time.
         */
        unsigned long getModificationTime() const {
            return modificationTime;
        }
        
        /**
         * \brief Sets the modification time.
         * \param time The modification time.
         */
        void setModificationTime(unsigned long time) {
            modificationTime = time;
        }
    };
    
    /// The system-related methods.
    ae::system::System sys;
    
    /// The input stream provider for the wrapped image loader.
    ae::io::FileInputStreamProvider *inputStreamProvider;
    
    /// The wrapped loader.
    PNGImageLoader *loader;
    
    /// The cached images.
    std::vector<CacheEntry *> cache;
    
    /**
     * \brief Gets the modification time of the file containing an image.
     * \param id The image identifier.
     * \param time The result modification time.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> otherwise.
     */
    bool getModificationTime(const std::string &id,unsigned long &time);
    
    /**
     * \brief Adds an entry to the cache.
     * \param id The image identifier.
     * \param image The image.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> otherwise.
     */
    bool addCacheEntry(const std::string &id,Image *image);
    
    /**
     * \brief Makes a deep copy of an image.
     * \param image The source image.
     * \return The image copy or <code>null</code> on error.
     */
    Image *copyImage(Image *image);
    
public:
    /**
     * \brief Constructs a CachedPNGImageLoader.
     * \param loader_ The wrapped PNG loader.
     * \param inputStreamProvider_ The input stream provider for the wrapped
     *     image loader.
     */     
    CachedPNGImageLoader(PNGImageLoader *loader_,
        ae::io::FileInputStreamProvider *inputStreamProvider_):ImageLoader(),
        sys(),inputStreamProvider(inputStreamProvider_),loader(loader_),
        cache() {            
    }
    
    /** */
    virtual ~CachedPNGImageLoader();
    
    /** */
    virtual Image *loadImage(const std::string &id);
    
    /**
     * \brief Discards the cache entries with images for which the file
     *     modification date has changed.  
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> otherwise.
     */
    bool update();
};
    
} // namespace

} // namespace

#endif // AE_IMAGE_CACHED_PNG_IMAGE_LOADER_H