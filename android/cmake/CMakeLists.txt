cmake_minimum_required(VERSION 3.4.1)

# libPNG
file(GLOB lpng_sources "src/main/cpp/lpng/*.c")
add_library(ae_png SHARED ${lpng_sources})
target_link_libraries(ae_png z)
set_target_properties(ae_png PROPERTIES LIBRARY_OUTPUT_DIRECTORY
    "${CMAKE_CURRENT_SOURCE_DIR}/build/jni/ae_png/${ANDROID_ABI}")

# Lua
file(GLOB lua_sources "src/main/cpp/lua/src/*.c")
list(REMOVE_ITEM lua_sources "${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/lua/src/lua.c")
list(REMOVE_ITEM lua_sources "${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/lua/src/luac.c")
add_library(ae_lua SHARED ${lua_sources})
set_target_properties(ae_lua PROPERTIES LIBRARY_OUTPUT_DIRECTORY
    "${CMAKE_CURRENT_SOURCE_DIR}/build/jni/ae_lua/${ANDROID_ABI}")

# SDL2
file(GLOB SDL2_sources
    "src/main/cpp/SDL2/src/*.c"
    "src/main/cpp/SDL2/src/audio/*.c"
    "src/main/cpp/SDL2/src/audio/android/*.c"
    "src/main/cpp/SDL2/src/audio/dummy/*.c"
    "src/main/cpp/SDL2/src/atomic/SDL_atomic.c"
    "src/main/cpp/SDL2/src/atomic/SDL_spinlock.c"
    "src/main/cpp/SDL2/src/core/android/*.c"
    "src/main/cpp/SDL2/src/cpuinfo/*.c"
    "src/main/cpp/SDL2/src/dynapi/*.c"
    "src/main/cpp/SDL2/src/events/*.c"
    "src/main/cpp/SDL2/src/file/*.c"
    "src/main/cpp/SDL2/src/haptic/*.c"
    "src/main/cpp/SDL2/src/haptic/dummy/*.c"
    "src/main/cpp/SDL2/src/joystick/*.c"
    "src/main/cpp/SDL2/src/joystick/android/*.c"
    "src/main/cpp/SDL2/src/loadso/dlopen/*.c"
    "src/main/cpp/SDL2/src/power/*.c"
    "src/main/cpp/SDL2/src/power/android/*.c"
    "src/main/cpp/SDL2/src/filesystem/android/*.c"
    "src/main/cpp/SDL2/src/filesystem/dummy/*.c"
    "src/main/cpp/SDL2/src/render/*.c"
    "src/main/cpp/SDL2/src/render/opengl/*.c"
    "src/main/cpp/SDL2/src/render/opengles/*.c"
    "src/main/cpp/SDL2/src/render/opengles2/*.c"
    "src/main/cpp/SDL2/src/render/software/*.c"
    "src/main/cpp/SDL2/src/stdlib/*.c"
    "src/main/cpp/SDL2/src/thread/*.c"
    "src/main/cpp/SDL2/src/thread/pthread/*.c"
    "src/main/cpp/SDL2/src/timer/*.c"
    "src/main/cpp/SDL2/src/timer/unix/*.c"
    "src/main/cpp/SDL2/src/video/*.c"
    "src/main/cpp/SDL2/src/video/android/*.c")      
include_directories("src/main/cpp/SDL2/include")
add_library(SDL2 SHARED ${SDL2_sources})
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -DGL_GLEXT_PROTOTYPES")
target_link_libraries(SDL2 dl GLESv1_CM GLESv2 log android)
set_target_properties(SDL2 PROPERTIES LIBRARY_OUTPUT_DIRECTORY
    "${CMAKE_CURRENT_SOURCE_DIR}/build/jni/SDL2/${ANDROID_ABI}")

# AE Engine
file(GLOB ae_sources
    "src/main/cpp/ae/audio/*.cpp"
    "src/main/cpp/ae/core/*.cpp"
    "src/main/cpp/ae/engine/*.cpp"
    "src/main/cpp/ae/image/*.cpp"
    "src/main/cpp/ae/io/*.cpp"
    "src/main/cpp/ae/math/*.cpp"
    "src/main/cpp/ae/texture/*.cpp"
    "src/main/cpp/ae/util/*.cpp")
include_directories(
    "src/main/cpp/lpng"
    "src/main/cpp/lua/src"
    "src/main/cpp/ae/audio/include"
    "src/main/cpp/ae/core/include"
    "src/main/cpp/ae/engine/include"
    "src/main/cpp/ae/image/include"
    "src/main/cpp/ae/io/include"
    "src/main/cpp/ae/math/include"
    "src/main/cpp/ae/texture/include"
    "src/main/cpp/ae/util/include")
add_library(ae SHARED ${ae_sources})
target_link_libraries(ae ae_lua ae_png)
set_target_properties(ae PROPERTIES LIBRARY_OUTPUT_DIRECTORY
    "${CMAKE_CURRENT_SOURCE_DIR}/build/jni/ae/${ANDROID_ABI}")

# JNI utility
file(GLOB jniutil_sources "src/main/cpp/jniutil/*.cpp")
include_directories("src/main/cpp/jniutil/include")
add_library(ae_jniutil SHARED ${jniutil_sources})
target_link_libraries(ae_jniutil ae)
set_target_properties(ae_jniutil PROPERTIES LIBRARY_OUTPUT_DIRECTORY
    "${CMAKE_CURRENT_SOURCE_DIR}/build/jni/ae_jniutil/${ANDROID_ABI}")

# Audio
file(GLOB audio_sources "src/main/cpp/audio/*.cpp")
include_directories("src/main/cpp/audio/include")
add_library(ae_audio SHARED ${audio_sources})
target_link_libraries(ae_audio ae_jniutil SDL2)
set_target_properties(ae_audio PROPERTIES LIBRARY_OUTPUT_DIRECTORY
    "${CMAKE_CURRENT_SOURCE_DIR}/build/jni/ae_audio/${ANDROID_ABI}")

# AE Engine SDL2
file(GLOB ae_sdl2_sources
    "src/main/cpp/SDL2/src/main/android/*.c"
    "src/main/cpp/ae_sdl2/core/*.cpp"
    "src/main/cpp/ae_sdl2/engine/*.cpp"
    "src/main/cpp/ae_sdl2/io/*.cpp"
    "src/main/cpp/ae_sdl2/sdl2/*.cpp")
include_directories(
    "src/main/cpp/SDL2/include"
    "src/main/cpp/audio/include"
    "src/main/cpp/ae_sdl2/core/include"
    "src/main/cpp/ae_sdl2/engine/include"
    "src/main/cpp/ae_sdl2/io/include"
    "src/main/cpp/ae_sdl2/sdl2/include")
add_library(ae_sdl2 SHARED ${ae_sdl2_sources})
target_link_libraries(ae_sdl2 ae ae_audio SDL2)
set_target_properties(ae_sdl2 PROPERTIES LIBRARY_OUTPUT_DIRECTORY
    "${CMAKE_CURRENT_SOURCE_DIR}/build/jni/ae_sdl2/${ANDROID_ABI}")

