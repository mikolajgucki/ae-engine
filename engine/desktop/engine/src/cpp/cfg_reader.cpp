#include <cstdlib>
#include <string>
#include <sstream>
#include <vector>
#include <map>

#include "Log.h"
#include "FileInputStream.h"
#include "PropertiesReader.h"
#include "cfg_reader.h"

using namespace std;
using namespace ae::io;

namespace ae {
    
namespace engine {
    
namespace desktop {

/// The log tag.
static const char *const logTag = "desktop.engine";

/**
 * \brief Reads the configuration properties.
 * \param filename The name of the configuration file.
 * \param props The result properties.
 * \return <code>true</code> on success, <code>false</code> otherwise.
 */
static bool readCfgProperties(string filename, map<string,string> &props) {
    ostringstream info;    
    info << "Reading configuration from file " << filename;
    Log::info(logTag,info.str());
    
// open file
    FileInputStream *input = new FileInputStream(filename);
    if (input->open() == false) {
        Log::error(logTag,input->getError());
        return false;
    }
    
// read properties
    PropertiesReader reader(input);
    if (reader.read(props) == false) {
        Log::error(logTag,reader.getError());
        return false;
    }
    
// close file
    if (input->close() == false) {
        Log::error(logTag,input->getError());
        return false;
    }
    
    return true;
}

/**
 * \brief Reads and parses the configuration.
 */
DesktopEngineCfg *readCfg(map<string,string> extraProps) {
    string filename = "ae.properties"; // default
    
// read the configuration
    map<string,string> props;
    if (readCfgProperties(filename,props) == false) {
        exit(-1);
    }
    
// extra properties
    map<string,string>::iterator extraItr = extraProps.begin();
    for (; extraItr != extraProps.end(); extraItr++) {
        props[extraItr->first] = extraItr->second;        
    }

// log the read configuration
    Log::debug(logTag,"Read configuration properties:");
    map<string,string>::iterator itr;
    for (itr = props.begin(); itr != props.end(); ++itr) {
        ostringstream msg;
        msg << "  " << itr->first << " = " << itr->second;
        Log::debug(logTag,msg.str());
    }
    
// create desktop engine configuration
    DesktopEngineCfg *cfg = new DesktopEngineCfg(props);
    if (cfg->chkError()) {
        Log::error(logTag,cfg->getError());
        exit(-1);
    }
    
    return cfg;
}
    
} // namespace
    
} // namespave
    
} // namespace