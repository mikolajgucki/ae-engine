#include "MessagePackMap.h"
#include "CommConsole.h"
#include "CommProtocol.h"
#include "CommLogger.h"

using namespace std;
using namespace ae::util;

namespace ae {
 
namespace engine {
 
namespace desktop {
 
/** */
void CommLogger::log(int level,const char *tag,const char *msg) {
    MessagePackMap map;
    CommProtocol::addMsgId(map,CommProtocol::MSG_ID_LOG);
    map.putInt("level",level);
    map.putStr("tag",tag);
    map.putStr("msg",msg);
    
    if (map.write(output) == false) {
        // TODO Handle error.
    }
    if (output->flush() == false) {
        // TODO Handle error.
    }
}
 
/** */
void CommLogger::trace(const char *tag,const char *msg) {
    log(CommProtocol::LOG_LEVEL_TRACE,tag,msg);
}
 
/** */
void CommLogger::debug(const char *tag,const char *msg) {
    log(CommProtocol::LOG_LEVEL_DEBUG,tag,msg);
}
 
/** */
void CommLogger::info(const char *tag,const char *msg) {
    log(CommProtocol::LOG_LEVEL_INFO,tag,msg);
}
 
/** */
void CommLogger::warning(const char *tag,const char *msg) {
    log(CommProtocol::LOG_LEVEL_WARNING,tag,msg);
}
 
/** */
void CommLogger::error(const char *tag,const char *msg) {
    log(CommProtocol::LOG_LEVEL_ERROR,tag,msg);
}

/** */
void CommLogger::noMemory() {
    CommConsole::print("Out of memory");
    exit(-1);
}

} // namespace

} // namespace

} // namespace
