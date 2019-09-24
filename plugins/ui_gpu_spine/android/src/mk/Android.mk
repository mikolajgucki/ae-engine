LOCAL_PATH := $(call my-dir)

# Prebuilt libraries
include $(AE_DIST)/android/src/mk/shared_libs.mk

include $(CLEAR_VARS)
# Header files 
include $(AE_DIST)/android/src/mk/includes.mk
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include

LOCAL_MODULE     := ae_ui_gpu_spine
LOCAL_SRC_FILES  := \
 $(wildcard $(LOCAL_PATH)/*.cpp)\
 $(wildcard $(LOCAL_PATH)/*.c)
LOCAL_LDLIBS     := -llog -landroid -lz
LOCAL_SHARED_LIBRARIES := ae_lua ae_jniutil ae SDL2 ae_sdl2

include $(BUILD_SHARED_LIBRARY)
