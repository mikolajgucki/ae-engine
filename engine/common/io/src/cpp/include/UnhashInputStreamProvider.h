#ifndef AE_UNHASH_INPUT_STREAM_PROVIDER_H
#define AE_UNHASH_INPUT_STREAM_PROVIDER_H

#include "InputStreamProvider.h"

namespace ae {
    
namespace io {

/**
 * \brief Provides unhash input streams returned by the underlying 
 *   input stream provider.
 */
class UnhashInputStreamProvider:public InputStreamProvider {
    /// The wrapped provider.
    InputStreamProvider *provider;
    
public:
    /** */
    UnhashInputStreamProvider(InputStreamProvider *provider_):
        InputStreamProvider(),provider(provider_) {
    }
    
    /** */
    virtual ~UnhashInputStreamProvider() {
        delete provider;
    }
    
    /** */
    virtual InputStream *getInputStream(const std::string &id);
};
    
} // namespace
    
} // namespace
    
#endif // AE_UNHASH_INPUT_STREAM_PROVIDER_H