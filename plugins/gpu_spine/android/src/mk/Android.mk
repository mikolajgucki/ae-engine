LOCAL_PATH := $(call my-dir)

# Prebuilt libraries
include $(AE_DIST)/android/src/mk/shared_libs.mk

# GLES2 plugin libraries
include $(CLEAR_VARS)
LOCAL_MODULE := ae_gpu
LOCAL_SRC_FILES := $(AE_DIST)/plugins/gpu/android/libs/$(TARGET_ARCH_ABI)/libae_gpu.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
# Header files 
include $(AE_DIST)/android/src/mk/includes.mk
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include

# GPU plugin header files
LOCAL_C_INCLUDES += $(AE_DIST)/plugins/gpu/common/src/include
LOCAL_C_INCLUDES += $(AE_DIST)/plugins/gpu/android/src/include

LOCAL_MODULE     := ae_gpu_spine
LOCAL_SRC_FILES  := \
 $(wildcard $(LOCAL_PATH)/*.cpp)\
 $(wildcard $(LOCAL_PATH)/*.c)
LOCAL_LDLIBS     := -llog -landroid -lGLESv2 -lz
LOCAL_SHARED_LIBRARIES := ae_lua ae_jniutil ae SDL2 ae_sdl2 ae_gpu

include $(BUILD_SHARED_LIBRARY)
