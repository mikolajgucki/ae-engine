#ifndef AE_PROPERTY_PARSER_H
#define AE_PROPERTY_PARSER_H

#include <string>
#include "Error.h"

namespace ae {
    
namespace io {
 
/**
 * \brief Parses a property from a string.
 */
class PropertyParser:public Error {
    /** */
    enum {
        SEPARATOR = '=' ///< The character separating key and value
    };
    
public:
    /** */
    PropertyParser():Error() {
    }
    
    /** */
    virtual ~PropertyParser() {
    }
    
    /**
     * \brief Parses a property.
     * \param str The string to parse.
     * \param key The result key.
     * \param value The result value.
     * \return <code>true</code> on success, otherwise sets error and returns
     *   <code>false</code>.
     */
    bool parse(const std::string &str,std::string &key,std::string &value);
};
    
} // namespace
    
} // namespace

#endif // AE_PROPERTY_PARSER_H