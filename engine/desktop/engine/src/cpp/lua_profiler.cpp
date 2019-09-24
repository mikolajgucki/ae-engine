#include <cstdio>
#include <cstring>
#include <sstream>

#include "Log.h"
#include "OutputStream.h"
#include "luap_List.h"
#include "luap_Call.h"
#include "lua_profiler.h"

using namespace std;
using namespace ae::io;

namespace ae {

namespace profiler {
    
namespace lua {
    
/// The log tag.
static const char *logTag = "profiler";
 
/** */
static bool write(OutputStream *output,const char *filename,const char *str) {
    unsigned int istr;
    for (istr = 0; istr < strlen(str); istr++) {
        if (output->write(str[istr]) == false) {
            ostringstream err;
            err << "Failed to write to file " << filename << ": " <<
                output->getError();
            Log::error(logTag,err.str());
            output->close();            
            return false;
        }                
    }    
    
    return true;
}

/** */
void dumpLuaProfilerLogToCSV(luap_List *log,ae::io::FileSystem *fileSystem,
    const char *filename) {
// create file
    if (fileSystem->createFile(filename) == false) {
        ostringstream err;
        err << "Failed to create file " << filename << ": " <<
            fileSystem->getError();
        Log::error(logTag,err.str());
        return;
    }
    
// get output stream
    OutputStream *output = fileSystem->getOutputStream(filename);
    if (output == (OutputStream *)0) {
        ostringstream err;
        err << "Failed to create file " << filename << ": " <<
            fileSystem->getError();
        Log::error(logTag,err.str());
        return;
    }   

// open
    if (output->open() == false) {
        ostringstream err;
        err << "Failed to open file " << filename << ": " <<
            fileSystem->getError();
        Log::error(logTag,err.str());
        return;        
    }
    
// header
    const char *header = "Source file,Line defined,Name,Hits,Total time [ms],"
        "Avg. time [ms/hit],Memory allocated [bytes],"
        "Total memory allocated [bytes],Avg. memory allocated [bytes/hit]"
        "\x0d\0xa";

    if (write(output,filename,header) == false) {
        return;        
    }        
    
// for each call
    for (int ilog = 0; ilog < luap_GetListSize(log); ilog++) {
        luap_Call *call = (luap_Call *)luap_GetListItem(log,ilog);
        int avgTime = (int)(call->totalTime / call->hits);
        long memoryAllocated = call->totalMemoryAllocated -
            call->subcallsMemoryAllocated;
        int avgMemoryAllocated = (int)(memoryAllocated / call->hits);
        
        ostringstream entry;
        entry << call->source << ","
            << call->linedef << ","
            << call->name << "," 
            << call->hits << ","
            << call->totalTime << ","
            << avgTime << ","
            << memoryAllocated << ","
            << call->totalMemoryAllocated << ","
            << avgMemoryAllocated << (char)13 << (char)10;
        
        if (write(output,filename,entry.str().c_str()) == false) {
            return;        
        }      
    }
    
// close
    if (output->close() == false) {
        ostringstream err;
        err << "Failed to close file " << filename << ": " <<
            fileSystem->getError();
        Log::error(logTag,err.str());
        return;        
    }
}
    
} // namespace
    
} // namespace
    
} // namespace