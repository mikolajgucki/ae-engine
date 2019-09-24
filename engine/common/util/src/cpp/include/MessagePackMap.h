#ifndef AE_MESSAGE_PACK_MAP_H
#define AE_MESSAGE_PACK_MAP_H

#include <string>
#include <map>
#include <sstream>

#include "Error.h"
#include "InputStream.h"
#include "OutputStream.h"

namespace ae {
    
namespace util {
  
/**
 * \brief Message pack map.
 */
class MessagePackMap:public Error {
    /// The string map.
    std::map<std::string,std::string> strMap;
    
    /// The integer map.
    std::map<std::string,int> intMap;
    
    /// The map of child maps.
    std::map<std::string,MessagePackMap *> mapMap;
    
public:
    /** */
    MessagePackMap():Error(),strMap(),intMap(),mapMap() {
    }
    
    /** */
    virtual ~MessagePackMap();
    
    /** */
    bool hasInt(const std::string &key) {
        return intMap.find(key) != intMap.end();
    }
    
    /** */
    int getInt(const std::string &key) {
        return intMap[key];
    }
    
    /** */
    int getInt(const std::string &key,int defaultValue) {
        if (hasInt(key) == false) {
            return defaultValue;
        }
        return getInt(key);
    }
    
    /** */
    void putInt(const std::string &key,int value) {
        intMap[key] = value;
    }    
    
    /** */
    bool hasStr(const std::string &key) {
        return strMap.find(key) != strMap.end();
    }
    
    /** */
    std::string getStr(const std::string &key) {
        return strMap[key];
    }
    
    /** */
    std::string getStr(const std::string &key,
        const std::string &defaultValue) {
    //
        if (hasStr(key) == false) {
            return defaultValue;
        }
        return getStr(key);
    }
    
    /** */
    void putStr(const std::string &key,std::string value) {
        strMap[key] = value;
    }    
    
    /** */
    bool hasMap(const std::string &key) {
        return mapMap.find(key) != mapMap.end();
    }
    
    /** */
    MessagePackMap *getMap(const std::string &key) {
        return mapMap[key];
    }
    
    /** */
    void putMap(const std::string &key,MessagePackMap *map) {
        mapMap[key] = map;
    }
    
    /**
     * \brief Writes the map to a stream.
     * \param output The output stream.
     * \return <code>true</code> on success, <code>false</code> otherwise.
     */
    bool write(ae::io::OutputStream *output);
    
    /**
     * \brief Reads a map from a stream.
     * \param input The input stream.
     * \return <code>true</code> on success, <code>false</code> otherwise.
     */
    bool read(ae::io::InputStream *input);
};
    
} // namespace
    
} // namespace

#endif // AE_MESSAGE_PACK_MAP_H