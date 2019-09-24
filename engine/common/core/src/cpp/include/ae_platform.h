#ifndef AE_PLATFORM_H
#define AE_PLATFORM_H

#if defined(__ANDROID__)
#define AE_ANDROID 1
#endif

#if defined(__APPLE__)
#include "AvailabilityMacros.h"
#include "TargetConditionals.h"
#if TARGET_OS_IPHONE
#define AE_IOS 1
#else
#define AE_MACOSX 1
#endif // TARGET_OS_IPHONE
#endif // define(__APPLE__)

#if defined(WIN32) || defined(_WIN32)
#define AE_WIN32 1
#endif

#endif // AE_PLATFORM_H