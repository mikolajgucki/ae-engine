CC = g++
MAKE = make
CCFLAGS = -g -Wall -pedantic -Wextra -Wno-unused-parameter -Wno-newline-eof

# AE Engine CC flags
CCFLAGS := $(CCFLAGS)

# The root directory
AE_ROOT_DIR = $(abspath .)

# The directory with the plugins
PLUGINS_DIR = $(abspath $(AE_ROOT_DIR)/../plugins)

# The directory with the libraries
LIBS_DIR = $(abspath $(AE_ROOT_DIR)/../libs)

LUA_INCLUDE = $(abspath $(LIBS_DIR)/$(PLATFORM)/lua-$(LUA_VERSION)/include)
LUA_LIB = $(abspath $(LIBS_DIR)/$(PLATFORM)/lua-$(LUA_VERSION)/lib)

ZLIB_INCLUDE = $(abspath $(LIBS_DIR)/$(PLATFORM)/zlib-$(ZLIB_VERSION)/include)
ZLIB_LIB = $(abspath $(LIBS_DIR)/$(PLATFORM)/zlib-$(ZLIB_VERSION)/lib)

LIBPNG_INCLUDE = $(abspath $(LIBS_DIR)/$(PLATFORM)/libpng-$(LIBPNG_VERSION)/include)
LIBPNG_LIB = $(abspath $(LIBS_DIR)/$(PLATFORM)/libpng-$(LIBPNG_VERSION)/lib)

ifeq ($(PLATFORM),windows)
SDL2_INCLUDE = $(abspath $(LIBS_DIR)/$(PLATFORM)/SDL2-$(SDL2_VERSION)/i686-w64-mingw32/include/SDL2)
SDL2_LIB = $(abspath $(LIBS_DIR)/$(PLATFORM)/SDL2-$(SDL2_VERSION)/i686-w64-mingw32/lib)
endif
ifeq ($(PLATFORM),osx)
SDL2_INCLUDE = $(abspath $(LIBS_DIR)/$(PLATFORM)/SDL2-$(SDL2_VERSION)/SDL2.framework/Versions/A/Headers)
SDL2_LIB = $(abspath $(LIBS_DIR)/$(PLATFORM)/SDL2-$(SDL2_VERSION)/lib)
endif

ifeq ($(PLATFORM),windows)
SDL2_MIXER_INCLUDE = $(abspath $(LIBS_DIR)/$(PLATFORM)/SDL2_mixer-$(SDL2_MIXER_VERSION)/i686-w64-mingw32/include/SDL2)
SDL2_MIXER_LIB = $(abspath $(LIBS_DIR)/$(PLATFORM)/SDL2_mixer-$(SDL2_MIXER_VERSION)/i686-w64-mingw32/lib)
endif
ifeq ($(PLATFORM),osx)
SDL2_MIXER_INCLUDE = $(abspath $(LIBS_DIR)/$(PLATFORM)/SDL2_mixer-$(SDL2_MIXER_VERSION)/SDL2_mixer.framework/Versions/A/Headers)
SDL2_MIXER_LIB = $(abspath $(LIBS_DIR)/$(PLATFORM)/SDL2_mixer-$(SDL2_MIXER_VERSION)/i686-w64-mingw32/lib)
endif

CCFLAGS := $(CCFLAGS) -I$(LUA_INCLUDE) -I$(ZLIB_INCLUDE) -I$(LIBPNG_INCLUDE)

export CC MAKE CCFLAGS PLATFORM AE_ROOT_DIR LIBS_DIR LUA_INCLUDE LUA_LIB\
    ZLIB_INCLUDE ZLIB_LIB LIBPNG_INCLUDE LIBPNG_LIB SDL2_INCLUDE SDL2_LIB\
    SDL2_MIXER_INCLUDE SDL2_MIXER_LIB PLUGINS_DIR    
    
all: platform
	$(MAKE) -C common -f Makefile all
	$(MAKE) -C sdl2 -f Makefile all
	$(MAKE) -C desktop -f Makefile all

clean:
	$(MAKE) -C common -f Makefile clean
	$(MAKE) -C sdl2 -f Makefile clean
	$(MAKE) -C desktop -f Makefile clean

platform:
ifndef PLATFORM
	$(error PLATFORM variable not set. Must be one of: windows, osx)
endif

.PHONY: all clean platform