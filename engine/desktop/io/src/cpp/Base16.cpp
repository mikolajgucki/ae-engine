#include <iostream>
#include "Base16.h"

using namespace std;

namespace ae {

namespace io {

/** */
bool Base16::encode(int in,unsigned char &out) {
    if (in < 10) {
        out =  (unsigned char)('0' + in);
        return true;
    }
    if (in < 16) {
        out = (unsigned char)('a' + in - 10);
        return true;
    }
    
    return false;
}

/** */
bool Base16::decode(unsigned char in,int &out) {
    if (in >= '0' && in <= '9') {
        out = (int)in - (int)'0';
        return true;
    }
    if (in >= 'a' && in <= 'f') {
        out = (int)in - (int)'a' + 10;
        return true;
    }
    if (in >= 'A' && in <= 'F') {
        out = (int)in - (int)'A' + 10;
        return true;
    }
    
    return false;
}
    
/** */
void Base16::encode(unsigned char in,unsigned char *out) {
    int v0 = (int)in & 0x0f;
    int v1 = ((int)in >> 4) & 0x0f;
    
    encode(v1,out[0]);
    encode(v0,out[1]);
}    
    
/** */
void Base16::encode(const unsigned char *in,size_t size,unsigned char *out) {
    for (unsigned int index = 0; index < size; index++) {
        unsigned int offset = index * 2;
        encode(in[index],(out + offset)); 
    }
}

/** */
bool Base16::decode(const unsigned char *in,unsigned char &out) {
    int v1;
    if (decode(in[0],v1) == false) {
        setError("Invalid Base16 character");
        return false;
    }
    
    int v0;
    if (decode(in[1],v0) == false) {
        setError("Invalid Base16 character");
        return false;
    }
    
    out = (v1 << 4) + v0; 
    return true;
}

/** */
bool Base16::decode(const unsigned char *in,size_t size,unsigned char *out) {
    for (unsigned int index = 0; index < size / 2; index++) {
        unsigned int offset = index * 2;
        if (decode((in + offset),out[index]) == false) {
            return false;
        }
    }
    
    return true;
}

} // namespace

} // namespace

