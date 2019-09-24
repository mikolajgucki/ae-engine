#ifndef AE_UTIL_MESSAGE_PACK_UNPACKER_H
#define AE_UTIL_MESSAGE_PACK_UNPACKER_H

#include <string>

#include "Error.h"
#include "MessagePack.h"
#include "InputStream.h"

namespace ae {
    
namespace util {
    
/**
 * \brief Contains methods for unpacking message packs.
 */    
class MessagePackUnpacker:public Error {
    /** Reads a single byte. */
    bool read(ae::io::InputStream *input,int &v0);
    
    /**
     * \brief Reads the 16-bit unsigned length.
     * \param input The input stream from which to read.
     * \param length The result length.
     * \return <code>false</code> on failure and sets the error,
     *   <code>true</code> on success.
     */
    bool readLength(ae::io::InputStream *input,unsigned int &length);
    
public:
    /** */
    enum {
        /** Indicates end-of-file. */
        END_OF_FILE = -1
    };
    
    /** */
    MessagePackUnpacker():Error() {
    }
    
    /**
     * \brief Reads the value type.
     * \param input The input stream from which to read.
     * \param type The result value type or EOF if the end of the file has
     *   been reached.
     * \return <code>false</code> on failure and sets the error,
     *   <code>true</code> on success.
     */
    bool readType(ae::io::InputStream *input,int &type);
    
    /**
     * \brief Reads a 16-bit signed integer.
     * \param input The input stream from which to read.
     * \param value The result value.
     * \return <code>false</code> on failure and sets the error,
     *   <code>true</code> on success.
     */
    bool readInt16(ae::io::InputStream *input,int &value);
    
    /**
     * \brief Reads a 32-bit signed integer.
     * \param input The input stream from which to read.
     * \param value The result value.
     * \return <code>false</code> on failure and sets the error,
     *   <code>true</code> on success.
     */
    bool readInt32(ae::io::InputStream *input,long &value);
    
    /**
     * \brief Reads a 16-bit string.
     * \param input The input stream from which to read.
     * \param value The result value.
     * \return <code>false</code> on failure and sets the error,
     *   <code>true</code> on success.
     */    
    bool readStr16(ae::io::InputStream *input,std::string &value);
    
    /**
     * \brief Reads the length of a 16-bit map.
     * \param input The input stream from which to read.
     * \param length The result length.
     * \return <code>false</code> on failure and sets the error,
     *   <code>true</code> on success.
     */    
    bool readMap16(ae::io::InputStream *input,unsigned int &length);
};
    
} // namespace
    
} // namespace

#endif // AE_UTIL_MESSAGE_PACK_UNPACKER_H