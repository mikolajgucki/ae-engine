#ifndef AE_COMM_CONSOLE_H
#define AE_COMM_CONSOLE_H

#include <cstdio>
#include <string>

namespace ae {
    
namespace engine {

namespace desktop {
  
/**
 * \brief The console for the communication.
 */
class CommConsole {
public:
    /** */
    static void print(const std::string &str) {
        fprintf(stderr,"%s",str.c_str());
        fflush(stderr);
    }
    
    /** */
    static void trace(const std::string &str) {
        print(str);
    }
};
    
} // namespace

} // namespace

} // namespace


#endif // AE_COMM_CONSOLE_H