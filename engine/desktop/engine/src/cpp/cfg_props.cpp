#include <cstdlib>
#include <sstream>

#include "Log.h"
#include "cfg_props.h"

using namespace std;

namespace ae {
    
namespace engine {
    
namespace desktop {
    
/// The log tag.
static const char *const logTag = "desktop.engine";
    
/** */
void replaceEnvVars(string &str,map<string,string> &extraVars) {
    size_t pos = 0;
    while (true) {
        size_t start = str.find('{',pos);
        if (start == string::npos) {
            return;
        }
        
        size_t end = str.find('}',start);
        if (end == string::npos) {
            return;
        }
        
        size_t length = end - start - 1;
        start++;
        
    // name
        string name = str.substr(start,length);
        
    // take from environment
        const char *value = getenv(name.c_str());
        
    // take from extra variables
        if (value == (const char *)0) {
            map<string,string>::iterator itr = extraVars.find(name);
            if (itr != extraVars.end()) {
                value = itr->second.c_str();
            }
        }
        
    // not found
        if (value == (const char *)0) {
            ostringstream err;
            err << "Unknown variable " << name <<
                " (reference found in configuration properties)";
            Log::error(logTag,err.str());
            exit(-1);
        }            
        
        str.replace(start - 1,length + 2,string(value));        
        pos = end;
    }
}

} // namespace
    
} // namespave
    
} // namespace