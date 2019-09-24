#ifndef AE_PROPERTIES_READER_H
#define AE_PROPERTIES_READER_H

#include <map>
#include <string>

#include "Error.h"
#include "InputStream.h"

namespace ae {
    
namespace io {
  
/**
 * \brief Reads properties of as a key/value map of strings.
 */
 class PropertiesReader:public Error {
    /** */
    enum {
        LF = 10, ///< Line feed
        CR = 13, ///< Carriage return
        COMMENT = '#', ///< The comment character               
        CONTINUE = '\\' ///< The continue character        
    };
    
    /// The stream from which to read the properties
    InputStream *input;
    
    /**
     * \brief Reads a line from the input.
     * \param line The string in which to store the read line.
     * \param hasMore Indicates the next line could be read.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> on error.
     */
    bool readLine(std::string &line,bool &hasMore);
    
    /**
     * \brief Gets the next line from the input.
     * \param line The string in which to store the read line.
     * \param hasMore Indicates the next line could be read.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> on error.
     */
    bool nextLine(std::string &line,bool &hasMore);
    
    /**
     * \brief Tests wheter a line is empty (contains spaces or no character
     *     at all).
     * \param line The line.
     * \return <code>true</code> if empty, <code>false</code> otherwise.
     */
    bool isEmpty(std::string line);
    
public:
    /**
     * \brief Constructs a PropertiesReader.
     * \param _input The input stream from which to read the properties.
     */
    PropertiesReader(InputStream *_input):input(_input) {
    }
    
    /**
     * \brief Reads the properties.
     * \param properties The placeholder for the properties.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> on error.
     */
    bool read(std::map<std::string,std::string> &properties);
};
    
} // namespace
    
} // namespace

#endif // AE_PROPERIES_READER_H