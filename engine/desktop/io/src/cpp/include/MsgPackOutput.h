#ifndef AE_MSG_PACK_OUTPUT_H
#define AE_MSG_PACK_OUTPUT_H

#include "Error.h"

namespace ae {

namespace io {
  
/**
 * \brief Message pack output.
 */
class MsgPackOutput:public Error {
public:
    /** */
    MsgPackOutput():Error() {
    }
    
    /** */
    virtual ~MsgPackOutput() {
    }
    
    virtual bool write(const MsgPack 
};

} // namespace

} // namespace

#endif // AE_MSG_PACK_OUTPUT_H