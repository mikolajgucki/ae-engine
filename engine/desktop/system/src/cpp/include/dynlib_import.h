#ifndef AE_DYNLIB_IMPORT_H
#define AE_DYNLIB_IMPORT_H

#ifdef WIN32
#define DYNLIB_IMPORT __declspec(dllimport)
#define DYNLIB_IMPORT_CALL _cdecl
#else
#define DYNLIB_IMPORT
#define DYNLIB_IMPORT_CALL
#endif

#endif // AE_DYNLIB_IMPORT_H