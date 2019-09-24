#ifndef AE_MIXER_SOUND_H
#define AE_MIXER_SOUND_H

#include <string>
#include "Error.h"

namespace ae {
    
namespace audio {
  
/**
 * \brief Represents a sound sample.
 */
class Sound:public Error {
    /// The sound identifier.
    const std::string id;
    
public:
    /** */
    Sound(const std::string& id_):Error(),id(id_) {
    }
    
    /** */
    virtual ~Sound() {
    }
    
    /**
     * \brief Gets the sound identifier.
     * \return The sound identifier.
     */
    const std::string &getId() const {
        return id;
    }
    
    /**
     * \brief Plays the sound.
     * \return <code>true</code> on success, <code>false</code> otherwise.
     */
    virtual bool play() = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_MIXER_SOUND_H