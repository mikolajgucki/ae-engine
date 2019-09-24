ifeq ($(PLATFORM),windows)
CUSTOM_CCFLAGS := -I$(PLUGIN_DIR)/libs/$(PLATFORM)/glew-1.10.0/include
CUSTOM_LIB_CCFLAGS := -L$(PLUGIN_DIR)/libs/$(PLATFORM)/glew-1.10.0/lib\
    -lglew32 -lopengl32 -Wl,--subsystem,windows
endif
ifeq ($(PLATFORM),osx)
# OS X specific options goes here
endif
include $(AE_DIST)/makefile/plugin/Makefile

