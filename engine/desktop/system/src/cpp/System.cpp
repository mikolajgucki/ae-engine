#include "ae_platform.h"
#include "System.h"

namespace ae {
    
namespace system {
    
/** */
System::OS System::getOS() {
#if defined AE_WIN32
    return OS_WIN32;
#elif defined AE_MACOSX
    return OS_MACOSX;
#else
    return OS_UNKNOWN;
#endif
}
    
} // namespace

} // namespace