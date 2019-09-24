#ifndef AE_INPUT_STREAM_CONTENT_READER_H
#define AE_INPUT_STREAM_CONTENT_READER_H

#include "Error.h"
#include "InputStream.h"

namespace ae {
    
namespace io {
  
/**
 * \brief Reads the content of an input stream.
 */
class InputStreamContentReader:public Error {
    /// The stream from which to read.
    InputStream *stream;
    
public:
    /**
     * \brief Constructs an InputStreamContentReader.
     * \param _stream The stream to read.
     */
    InputStreamContentReader(InputStream *_stream):stream(_stream) {
    }
    
    /**
     * \brief Reads the content of the input stream.
     * \param content The result content.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> on error.
     */
    bool readAll(std::string &content);
};    
    
} // namespace
    
} // namespace

#endif // AE_INPUT_STREAM_CONTENT_READER_H