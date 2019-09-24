#include <map>
#include <iostream>
#include <sstream>

#include "string_util.h"
#include "PropertyParser.h"
#include "PropertiesReader.h"

using namespace std;
using namespace ae::util;

namespace ae {

namespace io {
    
/** */    
bool PropertiesReader::readLine(string &line,bool &hasMore) {
    hasMore = true;
    
    int column = 0;
    char ch;
    
    while (true) {        
        int value;
        if (input->read(value) == false) {
            setError(input->getError());
            return false;
        }
        if (value == InputStream::NO_DATA) {
            if (column == 0) {
                hasMore = false;
                return true;
            }            
            break;
        }
        column++;
        
        if (value == CR) {
            continue;
        }
        if (value == LF) {
            break;
        }
        
        ch = (char)value;
        line.append(&ch,1);
    }
    
    return true;
}

/** */
bool PropertiesReader::nextLine(string &line,bool &hasMore) {
    string part;
    
    while (true) {
        part.clear();
        if (readLine(part,hasMore) == false) {
            return false;
        }
        if (hasMore == false) {
            break;
        }        
        ltrim(part," ");
        size_t length = part.length();
        
    // read and append the next line?
        char last = part[length - 1];
        if (last != (char)CONTINUE) {
            line.append(part);
            break;
        }
        part.erase(length - 1,1); // remove the continue character
        line.append(part);
    }
    
    return true;
}
    
/** */
bool PropertiesReader::isEmpty(string line) {
    for (size_t index = 0; index < line.size(); index++) {
        char ch = line[index];
        if (ch != ' ') {
            return false;
        }
    }
    
    return true;
}

/** */
bool PropertiesReader::read(map<std::string,std::string> &properties) {
    PropertyParser propertyParser;
    int lineNo = 0;
    string line;
    
    while (true) {
    // read line
        line.clear();
        bool hasMore;
        if (nextLine(line,hasMore) == false) {
            return false;
        }
        if (hasMore == false) {
            break;
        }
        lineNo++;
        
    // comment or empty
        if (line[0] == (char)COMMENT || isEmpty(line) == true) {
            continue;
        }
        
        string key;
        string value;
    // parse
        if (propertyParser.parse(line,key,value) == false) {
            ostringstream msg;
            msg << propertyParser.getError() << " (in line " << lineNo << ")";
            setError(msg.str());
            return false;
        }
        
        properties[key] = value;
    }
    
    return true;
}

} // namespace
    
} // namespace
