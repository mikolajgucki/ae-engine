#include <cstdio>
#include <sstream>
#include "MessagePack.h"
#include "MessagePackPacker.h"
#include "MessagePackUnpacker.h"
#include "MessagePackMap.h"

using namespace std;
using namespace ae::io;

namespace ae {
    
namespace util {
 
/** */
MessagePackMap::~MessagePackMap() {
    map<std::string,MessagePackMap *>::iterator mapItr; 
    for (mapItr = mapMap.begin(); mapItr != mapMap.end(); ++mapItr) {
        MessagePackMap *map = mapItr->second;
        delete map;
    }
}
    
/** */
bool MessagePackMap::write(OutputStream *output) {
    MessagePackPacker packer;

// size    
    unsigned int size = intMap.size() + strMap.size() + mapMap.size();
    if (packer.writeMap16(output,size) == false) {
        setError(packer.getError());
        return false;
    }
    
// integers
    map<string,int>::iterator intItr;
    for (intItr = intMap.begin(); intItr != intMap.end(); ++intItr) {
    // key
        if (packer.writeStr16(output,intItr->first) == false) {
            setError(packer.getError());
            return false;            
        }
        
    // value
        if (packer.writeInt32(output,intItr->second) == false) {
            setError(packer.getError());
            return false;            
        }
    }
    
// strings
    map<string,string>::iterator strItr;
    for (strItr = strMap.begin(); strItr != strMap.end(); ++strItr) {
    // key
        if (packer.writeStr16(output,strItr->first) == false) {
            setError(packer.getError());
            return false;            
        }
        
    // value
        if (packer.writeStr16(output,strItr->second) == false) {
            setError(packer.getError());
            return false;            
        }
    }
    
// maps
    map<std::string,MessagePackMap *>::iterator mapItr; 
    for (mapItr = mapMap.begin(); mapItr != mapMap.end(); ++mapItr) {
    // key
        if (packer.writeStr16(output,mapItr->first) == false) {
            setError(packer.getError());
            return false;            
        }
    
    // value
        MessagePackMap *map = mapItr->second;
        if (map->write(output) == false) {
            return false;
        }
    }
    
    return true;
}

/** */
bool MessagePackMap::read(InputStream *input) {
    MessagePackUnpacker unpacker;

// type
    int type;
    if (unpacker.readType(input,type) == false) {
        setError(unpacker.getError());
        return false;
    }
    if (type != MessagePack::MSG_PACK_MAP16) {
        ostringstream err;
        err << "Unexpected type " << type;
        setError(err.str());
        return false;
    }
    
// size
    unsigned int size;
    if (unpacker.readMap16(input,size) == false) {
        setError(unpacker.getError());
        return false;
    }
    
// for each entry
    for (unsigned int index = 0; index < size; index++) {
    // key type
        int keyType;
        if (unpacker.readType(input,keyType) == false) {
            setError(unpacker.getError());
            return false;
        }        
        if (keyType != MessagePack::MSG_PACK_STR16) {
            setError("Only 16-bit strings supported as key");
            return false;
        }
        
    // key
        string key;
        if (unpacker.readStr16(input,key) == false) {
            setError(unpacker.getError());
            return false;            
        }
        
    // value type
        int valueType;
        if (unpacker.readType(input,valueType) == false) {
            setError(unpacker.getError());
            return false;
        }    
        
    // integer value
        if (valueType == MessagePack::MSG_PACK_INT32) {
            long value;
            if (unpacker.readInt32(input,value) == false) {
                setError(unpacker.getError());
                return false;
            }
            putInt(key,value);
            continue;
        }
        
    // string value
        if (valueType == MessagePack::MSG_PACK_STR16) {
            string value;
            if (unpacker.readStr16(input,value) == false) {
                setError(unpacker.getError());
                return false;
            }
            putStr(key,value);
            continue;
        }
        
    // map value
        if (valueType == MessagePack::MSG_PACK_MAP16) {
            MessagePackMap *value = new (nothrow) MessagePackMap();
            if (value == (MessagePackMap *)0) {
                return false;
            }
            if (value->read(input) == false) {
                return false;
            }
            putMap(key,value);
            continue;
        }
        
        setError("Unsupported value type");
        return false;
    }
    
    return true;
}
    
} // namespace
    
} // namespace