#include <sstream>

#include "Log.h"
#include "CachedPNGImageLoader.h"

using namespace std;

namespace ae {

namespace image {
 
/// The log tag.
static const char *logTag = "image.cache";
    
/** */
bool CachedPNGImageLoader::getModificationTime(const string &id,
    unsigned long &time) {
// path
    string path;
    if (inputStreamProvider->getPath(id,path) == false) {
        setError(inputStreamProvider->getError());
        return false;
    }
    
// modification time
    if (sys.getFileModificationTime(path,time) == false) {
        setError(sys.getError());
        return false;
    } 

    return true;     
}
    
/** */
bool CachedPNGImageLoader::addCacheEntry(const string &id,Image *image) {
// modification time
    unsigned long modificationTime;
    if (getModificationTime(id,modificationTime) == false) {
        setError(sys.getError());
        return false;
    }        
    
// entry
    CacheEntry *entry = new CacheEntry(id,image,modificationTime);
    cache.push_back(entry);
    
    return true;
}

/** */
Image *CachedPNGImageLoader::copyImage(Image *image) {
// copy data
    size_t size = (size_t)(image->getWidth() * image->getHeight() *
        Image::BYTES_PER_PIXEL);
    unsigned char *data = new (nothrow) unsigned char[size];
    if (data == (unsigned char *)0) {
        setNoMemoryError();
        return (Image *)0;
    }
    memcpy(data,image->getData(),size);
    
// copy image
    Image *copy = new (nothrow) Image(
        image->getWidth(),image->getHeight(),data);
    if (copy == (Image *)0) {
        delete[] data;
        setNoMemoryError();
        return (Image *)0;        
    }
    
    return copy;
}
    
/** */
Image *CachedPNGImageLoader::loadImage(const std::string &id) {
// find entry of the given identifier
    vector<CacheEntry *>::iterator itr = cache.begin();
    for (;itr != cache.end(); ++itr) {
        CacheEntry *entry = *itr;
        if (entry->getImageId() == id) {
            ostringstream msg;
            msg << "Image " << id << " already cached";
            Log::trace(logTag,msg.str());
            
        // return a copy as the returned image might be deleted
            Image *copy = copyImage(entry->getImage());
            return copy;
        }
    }
    
    ostringstream msg;
    msg << "Loading image " << id;
    Log::trace(logTag,msg.str());
        
// image
    Image *loadedImage = loader->loadImage(id);
    if (loadedImage == (Image *)0) {
        setError(loader->getError());
        return (Image *)0;
    }
    
// cache entry
    if (addCacheEntry(id,loadedImage) == false) {
        delete loadedImage;
        return (Image *)0;
    }
    
// return a copy as the returned image might be deleted
    Image *copy = copyImage(loadedImage);
    return copy;
}
    
/** */
bool CachedPNGImageLoader::update() {
    unsigned long time;
    
    vector<CacheEntry *>::iterator itr = cache.begin();
    while (itr != cache.end()) {
        CacheEntry *entry = *itr;
        if (getModificationTime(entry->getImageId(),time) == false) {
            return false;
        }
        
        if (entry->getModificationTime() < time) {
            ostringstream msg;
            msg << "Discarding out-of-date image " << entry->getImageId() <<
                " (will be reloaded)";
            Log::trace(logTag,msg.str());
            
        // remove from cache
            itr = cache.erase(itr);
            
        // delete
            delete entry->getImage();
            delete entry;
        }
        else {
            ++itr;
        }
    }
   
    return true;
}

/** */
CachedPNGImageLoader::~CachedPNGImageLoader() {
    vector<CacheEntry *>::iterator itr;
    for (itr = cache.begin(); itr != cache.end(); ++itr) {
        CacheEntry *entry = *itr;
        delete entry;
    }
}

} // namespace
    
} // namespace
