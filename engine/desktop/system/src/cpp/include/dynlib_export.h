#ifndef AE_DYNLIB_EXPORT_H
#define AE_DYNLIB_EXPORT_H

#ifdef WIN32
#define DYNLIB_EXPORT __declspec(dllexport)
#define DYNLIB_EXPORT_CALL __cdecl
#else
#define DYNLIB_EXPORT
#define DYNLIB_EXPORT_CALL
#endif

#endif // AE_DYNLIB_EXPORT_H