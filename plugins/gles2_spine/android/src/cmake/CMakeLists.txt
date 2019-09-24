cmake_minimum_required(VERSION 3.4.1)
set(CMAKE_VERBOSE_MAKEFILE on)

file(GLOB plugin_sources
    "src/main/cpp/spine/*.c"
    "src/main/cpp/*.c"
    "src/main/cpp/*.cpp")
include_directories(
    "${AE_DIST}/src/lua/include"
    "${AE_DIST}/src/SDL2/include"
    "${AE_DIST}/src/ae/include"
    "${AE_DIST}/android/src/ae/include"
    "${AE_DIST}/plugins/gles2/common/src/include"
    "src/main/cpp/spine/include"
    "src/main/cpp/include")
link_directories(
    "${AE_DIST}/android/libs/jni/${ANDROID_ABI}"
    "${AE_DIST}/plugins/gles2/android/libs/jni/${ANDROID_ABI}")
add_library(ae_gles2_spine SHARED ${plugin_sources})
target_link_libraries(ae_gles2_spine ae_gles2 ae_sdl2 ae ae_lua GLESv2)
set_target_properties(ae_gles2_spine PROPERTIES LIBRARY_OUTPUT_DIRECTORY
    "${CMAKE_CURRENT_SOURCE_DIR}/build/jni/${ANDROID_ABI}")
