#include <cstdlib>
#include <sstream>
#include "string_util.h"

using namespace std;

namespace ae {
    
namespace util {
 
/** */
bool startsWith(const string& str,const string& prefix) {
    if (prefix.length() > str.length()) {
        return false;
    }
    return prefix == str.substr(0,prefix.length());
}    
    
/** */
static void split(const string &str,char delim,vector<string> &items) {
    stringstream strStream(str);
    string item;
    while (getline(strStream,item,delim)) {
        items.push_back(item);
    }
}

/** */
vector<string> split(const string &str,char delim) {
    vector<string> items;
    split(str,delim,items);
    return items;
}
    
/** */
void ltrim(string &str,const string &characters) {
    size_t index = str.find_first_not_of(characters);
    str.erase(0,index);
}

/** */
bool isInt(const string &str) {
    unsigned int index = 0;
    
// negative
    if (str.at(0) == '-') {
        index++;
    }
    
// ciphers
    while (index < str.length()) {
        int ch = (int)str.at(index);
        if (ch < (int)'0' || ch > (int)'9') {
            return false;
        }
        
        index++;
    }
    
    return true;
}

/** */
bool parseInt(const std::string &str,int &value) {
    if (isInt(str) == false) {
        return false;
    }
    
    value = (int)strtol(str.c_str(),(char **)0,10);
    return true;
}

} // namespace
    
} // namespace