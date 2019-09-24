cmake_minimum_required(VERSION 3.4.1)
set(CMAKE_VERBOSE_MAKEFILE on)

file(GLOB plugin_sources
    "src/main/cpp/*.c"
    "src/main/cpp/*.cpp")
include_directories(
    "${AE_DIST}/src/lua/include"
    "${AE_DIST}/src/SDL2/include"
    "${AE_DIST}/src/ae/include"
    "${AE_DIST}/android/src/ae/include"
    "src/main/cpp/include")
link_directories(
    "${AE_DIST}/android/libs/jni/${ANDROID_ABI}")
add_library(ae_iap SHARED ${plugin_sources})
target_link_libraries(ae_iap ae_sdl2 ae ae_lua ae_jniutil SDL2)
set_target_properties(ae_iap PROPERTIES LIBRARY_OUTPUT_DIRECTORY
    "${CMAKE_CURRENT_SOURCE_DIR}/build/jni/${ANDROID_ABI}")
