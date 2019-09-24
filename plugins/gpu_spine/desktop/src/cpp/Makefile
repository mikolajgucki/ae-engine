# includes
CUSTOM_CCFLAGS := ${CUSTOM_CCFLAGS}\
    -I$(BUILD_DIR)/spine/include\
    -I$(AE_DIST)/plugins/gpu/common/src/include\
    -I$(AE_DIST)/plugins/gpu/desktop/src/include
    
# libs
ifeq ($(PLATFORM),windows)
CUSTOM_LIB_CCFLAGS := ${CUSTOM_LIB_CCFLAGS}\
    -L$(AE_DIST)/plugins/gpu/desktop/windows -lae_gpu -lglew32 -lae_gpu\
    -lopengl32
endif
ifeq ($(PLATFORM),osx)
CUSTOM_LIB_CCFLAGS := ${CUSTOM_LIB_CCFLAGS}\
    -L$(AE_DIST)/plugins/gpu/desktop/osx -lae_gpu 
endif

# files to compile
CUSTOM_CFILES = $(wildcard $(BUILD_DIR)/spine/*.c)

include $(AE_DIST)/makefile/plugin/Makefile
