#ifndef AE_LUA_PROFILER_H
#define AE_LUA_PROFILER_H

#include "FileSystem.h"
#include "luap_List.h"

namespace ae {

namespace profiler {
    
namespace lua {
    
/**
 * \brief Dumps Lua profiler log to a CSV file.
 * \param log The profiler log.
 * \param fileSystem The file system to which to write.
 * \param filename The name of the file to which to write.
 */
void dumpLuaProfilerLogToCSV(luap_List *log,ae::io::FileSystem *fileSystem,
    const char *filename);
    
} // namespace
    
} // namespace
    
} // namespace

#endif // AE_LUA_PROFILER_H