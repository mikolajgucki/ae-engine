LOCAL_PATH := $(call my-dir)
LOCAL_ABS_PATH := $(abspath $(LOCAL_PATH))
include $(CLEAR_VARS)

# The path to the AE engine module
AE_PATH = $(LOCAL_ABS_PATH)/../ae
include $(AE_PATH)/includes.mk

# The path to the JNI utilities module
JNI_UTIL_PATH = $(LOCAL_ABS_PATH)/../jniutil
include $(JNI_UTIL_PATH)/includes.mk

LOCAL_C_INCLUDES += \
    $(LOCAL_ABS_PATH)/src/cpp/include \

LOCAL_MODULE     := ae_audio
LOCAL_SRC_FILES  := \
    $(wildcard $(LOCAL_ABS_PATH)/src/cpp/*.cpp)
LOCAL_LDLIBS := -llog -landroid -lGLESv2 -lz
LOCAL_SHARED_LIBRARIES := ae ae_jniutil SDL2

include $(BUILD_SHARED_LIBRARY)
