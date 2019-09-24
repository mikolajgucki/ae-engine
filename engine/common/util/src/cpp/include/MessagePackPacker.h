#ifndef AE_UTIL_MESSAGE_PACK_PACKER_H
#define AE_UTIL_MESSAGE_PACK_PACKER_H

#include <string>

#include "Error.h"
#include "MessagePack.h"
#include "OutputStream.h"

namespace ae {

namespace util {
  
/**
 * \brief Contains methods for packing message packs.
 */
class MessagePackPacker:public Error {
    /** Writes a single byte. */
    bool write(ae::io::OutputStream *output,int v0);

    /** Writes two bytes. */
    bool write(ae::io::OutputStream *output,int v0,int v1);
    
    /** Writes three bytes. */    
    bool write(ae::io::OutputStream *output,int v0,int v1,int v2);
    
    /** Writes four bytes. */
    bool write(ae::io::OutputStream *output,int v0,int v1,int v2,int v3);    
    
    /**
     * \brief Writes a 16-bit unsigned length.
     * \param output The stream to which to write.
     * \param length The length.
     * \return <code>false</code> on failure and sets the error,
     *   <code>true</code> on success.
     */
    bool writeLength(ae::io::OutputStream *output,int length);
    
public:
    /** */
    MessagePackPacker():Error() {
    }
    
    /**
     * \brief Writes a boolean value.
     * \param output The stream to which to write.
     * \param value The value.
     * \return <code>false</code> on failure and sets the error,
     *   <code>true</code> on success.
     */
    bool writeBool(ae::io::OutputStream *output,bool value);
    
    /**
     * \brief Writes nil.
     * \param output The stream to which to write.
     * \return <code>false</code> on failure and sets the error,
     *   <code>true</code> on success.
     */
    bool writeNil(ae::io::OutputStream *output);
    
    /**
     * \brief Writes a 16-bit signed integer.
     * \param output The stream to which to write.
     * \param value The value.     
     * \return <code>false</code> on failure and sets the error,
     *   <code>true</code> on success.
     */ 
    bool writeInt16(ae::io::OutputStream *output,int value);
    
    /**
     * \brief Writes a 32-bit signed integer.
      * \param output The stream to which to write.
     * \param value The value.     
     * \return <code>false</code> on failure and sets the error,
     *   <code>true</code> on success.
     */
    bool writeInt32(ae::io::OutputStream *output,long value);
    
    /**
     * \brief Writes a 16-bit string.
     * \param output The stream to which to write.
     * \param value The value.     
     * \return <code>false</code> on failure and sets the error,
     *   <code>true</code> on success.
     */
    bool writeStr16(ae::io::OutputStream *output,const std::string &str);
    
    /**
     * \brief Writes a 16-bit map.
     * \param output The stream to which to write.
     * \param length The number of entries in the map.     
     * \return <code>false</code> on failure and sets the error,
     *   <code>true</code> on success.
     */
    bool writeMap16(ae::io::OutputStream *output,unsigned int length);
};

} // namespace
    
} // namespace

#endif // AE_UTIL_MESSAGE_PACK_PACKER_H