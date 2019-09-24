#include <sstream>

#include "string_util.h"
#include "DefaultDrawerFeatures.h"

using namespace std;
using namespace ae::util;

namespace ae {

namespace gpu {
  
/** */
static const char *const colorStr = "color";
static const char *const vertexColorStr = "vertex.color";
static const char *const textureStr = "texture";

/** */
static const int colorIndex = 1;
static const int vertexColorIndex = 2;
static const int textureIndex = 4;

/** */
int DefaultDrawerFeatures::getIndex() const {
    int index = 0;
    
    if (color) {
        index += colorIndex;
    }
    if (vertexColor) {
        index += vertexColorIndex;
    }
    if (texture) {
        index += textureIndex;
    }
    
    return index;
}

/** */
int DefaultDrawerFeatures::getMaxIndex() {
    return colorIndex + vertexColorIndex + textureIndex;
}

/** */
const string DefaultDrawerFeatures::toString() const {
    ostringstream str;
    
    if (hasColor()) {
        str << colorStr;
    }
    if (hasVertexColor()) {
        if (str.str().empty() == false) {
            str << ",";
        }
        str << vertexColorStr;
    }
    if (hasTexture()) {
        if (str.str().empty() == false) {
            str << ",";
        }
        str << textureStr;
    }
    
    return str.str();
}
    
/** */
bool DefaultDrawerFeatures::fromString(const string& str,
    DefaultDrawerFeatures *features) {
//
    vector<string> tokens = split(str,',');
    vector<string>::iterator itr = tokens.begin();
    for (; itr != tokens.end(); ++itr) {
        if (*itr == colorStr) {
            features->setColor(true);
            continue;
        }
        
        if (*itr == vertexColorStr) {
            features->setVertexColor(true);
            continue;
        }
        
        if (*itr == textureStr) {
            features->setTexture(true);
            continue;
        }
        
        return false;
    }
    
    return true;
}
    
} // namespace

} // namespace
