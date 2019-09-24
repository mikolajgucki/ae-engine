#include <sstream>
#include "io_util.h"
#include "FileInputStream.h"
#include "InputStreamContentReader.h"
#include "DesktopPluginCfg.h"

using namespace std;
using namespace ae::io;

namespace ae {

namespace engine {

namespace desktop {
    
/** */
bool DesktopPluginCfg::readJSONCfg(Json::Value &value) {
    ostringstream filenameStr;
    filenameStr << "simulator/plugins/" + pluginName << ".json";
    string filename = filenameStr.str();
    
// must exist
    if (checkFileExists(filename) == false) {
        ostringstream err;
        err << "File " << filename << " not found";
        setError(err.str());
        return false;
    }
    
// open the file
    FileInputStream *input = new FileInputStream(filename);
    if (input->open() == false) {
        setError(input->getError());
        return false;
    }   
    
// read the file
    InputStreamContentReader contentReader(input);
    string content;
    if (contentReader.readAll(content) == false) {
        setError(contentReader.getError());
        return false;
    }
    
// close file
    if (input->close() == false) {
        setError(input->getError());
        return false;
    }    
    
// parse
    Json::Reader jsonReader;
    if (jsonReader.parse(content,value) == false) {
        ostringstream err;
        err << "Error parsing JSON " << filename << ": " <<
            jsonReader.getFormattedErrorMessages();
        setError(err.str());
        return false;
    }    
    
    return true;
}
    
} // namespace

} // namespace

} // namespace