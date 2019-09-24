#ifndef AE_COMM_MAP_WRITER_H
#define AE_COMM_MAP_WRITER_H

#include "SDL.h"
#include "Error.h"
#include "OutputStream.h"
#include "MessagePackMap.h"

namespace ae {
    
namespace engine {

namespace desktop {
    
/**
 * \brief Message pack map writer for the communication.
 */
class CommMapWriter:public Error {
    /// The mutex which locks the access to the output stream.
    SDL_mutex *mutex;
    
    /// The output stream.
    ae::io::OutputStream *output;

    /** */
    void create();
    
public:
    /** */
    CommMapWriter(ae::io::OutputStream *output_):Error(),
        mutex((SDL_mutex *)0),output(output_) {
    //
        create();
    }
    
    /**
     * \brief Writes a message pack map.
     * \param map The map to write.
     * \return <code>true</code> on success, <code>false</code> on error.
     */
    bool write(ae::util::MessagePackMap& map);
};

} // namespace

} // namespace

} // namespace

#endif // AE_COMM_MAP_WRITER_H