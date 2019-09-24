cmake_minimum_required(VERSION 3.4.1)

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
add_library(ae_http SHARED ${plugin_sources})
target_link_libraries(ae_http ae_sdl2 ae ae_lua ae_jniutil SDL2)
set_target_properties(ae_http PROPERTIES LIBRARY_OUTPUT_DIRECTORY
    "${CMAKE_CURRENT_SOURCE_DIR}/build/jni/${ANDROID_ABI}")
