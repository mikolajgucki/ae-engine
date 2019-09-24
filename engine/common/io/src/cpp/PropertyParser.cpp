#include "PropertyParser.h"

using namespace std;

namespace ae {
    
namespace io {
    
/** */
bool PropertyParser::parse(const string &str,string &key,string &value) {
    // find separator
    size_t indexOf = str.find((char)SEPARATOR);
    if (indexOf == string::npos) {
        setError("Missing separator");
        return false;
    }
    
    key = str.substr(0,indexOf);
    value = str.substr(indexOf + 1,str.size() - indexOf - 1);
    
    return true;
}
    
} // namespace
    
} // namespace