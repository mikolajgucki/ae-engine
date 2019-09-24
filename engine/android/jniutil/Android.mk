LOCAL_PATH := $(call my-dir)
LOCAL_ABS_PATH := $(abspath $(LOCAL_PATH))
include $(CLEAR_VARS)

# The path to the Lua module
LUA_PATH = $(LOCAL_ABS_PATH)/../lua
include $(LUA_PATH)/includes.mk

# The path to the AndEngine module
AE_PATH = $(LOCAL_ABS_PATH)/../ae
include $(AE_PATH)/includes.mk

# The path to the AndEngine SDL2 module
AE_SDL2_PATH = $(LOCAL_ABS_PATH)/../ae_sdl2
include $(AE_SDL2_PATH)/includes.mk

# The path to the JNIUtil module
JNI_UTIL_PATH = $(LOCAL_ABS_PATH)
include $(JNI_UTIL_PATH)/includes.mk

LOCAL_MODULE     := ae_jniutil
LOCAL_SRC_FILES  := \
    $(wildcard $(LOCAL_ABS_PATH)/src/cpp/*.cpp)
LOCAL_LDLIBS := -llog -landroid -lGLESv2 -lz
LOCAL_SHARED_LIBRARIES := ae

include $(BUILD_SHARED_LIBRARY)
