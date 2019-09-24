# includes
CUSTOM_CCFLAGS := ${CUSTOM_CCFLAGS}\
    -I$(BUILD_DIR)/spine/include\
    -I$(AE_DIST)/plugins/gles2/common/src/include\
    -I$(AE_DIST)/plugins/gles2/desktop/src/include
    
# libs
ifeq ($(PLATFORM),windows)
CUSTOM_LIB_CCFLAGS := ${CUSTOM_LIB_CCFLAGS}\
    -L$(AE_DIST)/plugins/gles2/desktop/windows -lae_gles2 -lglew32 -lae_gles2\
    -lopengl32
endif
ifeq ($(PLATFORM),osx)
CUSTOM_LIB_CCFLAGS := ${CUSTOM_LIB_CCFLAGS}\
    -L$(AE_DIST)/plugins/gles2/desktop/osx -lae_gles2 
endif

# files to compile
CUSTOM_CFILES = $(wildcard $(BUILD_DIR)/spine/*.c)

include $(AE_DIST)/makefile/plugin/Makefile
