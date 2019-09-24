#ifndef AE_BASE16_H
#define AE_BASE16_H

#include <string>
#include "Error.h"

namespace ae {

namespace io {
  
/**
 * \brief Encodes/decodes using Base16.
 */
class Base16:public Error {
public:
    /** */
    Base16():Error() {
    }
    
    /** */
    virtual ~Base16() {
    }
    
    /** */
    bool encode(int in,unsigned char &out);
    
    /** */
    bool decode(unsigned char in,int &out);
    
    /**
     * \brief Encodes a character.
     * \param in The input character.
     * \param out The output array.
     */
    void encode(unsigned char in,unsigned char *out);
    
    /**
     * \brief Encodes a string.
     * \param in The input array.
     * \param size The number of bytes to encode.
     * \param out The output array.
     */
    void encode(const unsigned char *in,size_t size,unsigned char *out);
    
    /**
     * \brief Decodes a character.
     * \param in The input array.
     * \param out The output character.
     * \return <code>true</code> on success, <code>false</code> otherwise.
     */
    bool decode(const unsigned char *in,unsigned char &out);
    
    /**
     * \brief Decodes a string.
     * \param in The input array.
     * \param size The number of bytes to decode.
     * \param out The output character.
     * \return <code>true</code> on success, <code>false</code> otherwise.
     */
    bool decode(const unsigned char *in,size_t size,unsigned char *out);
};

} // namespace

} // namespace

#endif // AE_BASE16_H