# includes
CUSTOM_CCFLAGS := ${CUSTOM_CCFLAGS}\
    -I$(PLUGIN_DIR)/libs/$(PLATFORM)/glew-1.10.0/include\
    -I$(AE_DIST)/plugins/gpu/common/src/include
    
# libs
ifeq ($(PLATFORM),windows)
CUSTOM_LIB_CCFLAGS := ${CUSTOM_LIB_CCFLAGS}\
    -L$(AE_DIST)/plugins/gpu/desktop/windows -lae_gpu\
    -L$(PLUGIN_DIR)/libs/$(PLATFORM)/glew-1.10.0/lib\
    -lglew32 -lopengl32 -Wl,--subsystem,windows
endif
ifeq ($(PLATFORM),osx)
CUSTOM_LIB_CCFLAGS := ${CUSTOM_LIB_CCFLAGS}\
    -L$(AE_DIST)/plugins/gpu/desktop/osx -lae_gpu
endif    
    
include $(AE_DIST)/makefile/plugin/Makefile