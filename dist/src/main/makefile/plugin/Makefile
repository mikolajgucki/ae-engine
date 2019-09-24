# This makefile is used build a desktop plugin.
# Custom properties:
#   CUSTOM_CCFLAGS
#   CUSTOM_CFILES
#   CUSTOM_CPPFILES
#   CUSTOM_DEPS
#   CUSTOM_LIB_CCFLAGS

CC = g++
MAKE = make
CCFLAGS = -g -Wall -pedantic -Wextra -Wno-unused-parameter -Wno-newline-eof

# The root directory
ROOT_DIR = $(abspath .)

# The directory with the plugin
PLUGIN_DIR = $(abspath $(ROOT_DIR)/../../..)

# Includes
INCLUDE = include
CCFLAGS := $(CCFLAGS) -I$(INCLUDE)
CCFLAGS := $(CCFLAGS) -I$(AE_DIST)/src/lua/include
CCFLAGS := $(CCFLAGS) -I$(AE_DIST)/src/SDL2/include
CCFLAGS := $(CCFLAGS) -I$(AE_DIST)/src/ae/include
CCFLAGS := $(CCFLAGS) -I$(AE_DIST)/desktop/src/include
CCFLAGS := $(CCFLAGS) -I$(PLUGIN_DIR)/common/src/cpp/include
CCFLAGS := $(CCFLAGS) $(CUSTOM_CCFLAGS)

# The flags passed while linking the library
LIB_CCFLAGS =\
    -L$(AE_DIST)/desktop/libs/$(PLATFORM) -laedesktopengine\
    -L$(AE_DIST)/desktop/libs/$(PLATFORM) -laedesktopplugins\
    -L$(AE_DIST)/desktop/libs/$(PLATFORM) -laedesktopio\
    -L$(AE_DIST)/desktop/libs/$(PLATFORM) -laedesktopcore\
    -L$(AE_DIST)/desktop/libs/$(PLATFORM) -laedesktopsystem\
    -L$(AE_DIST)/desktop/libs/$(PLATFORM) -laeengine\
    -L$(AE_DIST)/desktop/libs/$(PLATFORM) -laetexture\
    -L$(AE_DIST)/desktop/libs/$(PLATFORM) -laeimage\
    -L$(AE_DIST)/desktop/libs/$(PLATFORM) -laeaudio\
    -L$(AE_DIST)/desktop/libs/$(PLATFORM) -laecore\
    -L$(AE_DIST)/desktop/libs/$(PLATFORM) -laeio\
    -L$(AE_DIST)/desktop/libs/$(PLATFORM) -laemath\
    -L$(AE_DIST)/desktop/libs/$(PLATFORM) -laeutil\
    -L$(AE_DIST)/desktop/libs/$(PLATFORM) -ljsoncpp\
    $(CUSTOM_LIB_CCFLAGS)

ifeq ($(PLATFORM),windows)
LIB_DIR = $(BUILD_DIR)/windows
LIB = $(LIB_DIR)/ae_$(PLUGIN_NAME).dll
LIB_CCFLAGS := $(LIB_CCFLAGS)\
    -lshlwapi\
    -L$(AE_DIST)/bin -llua53\
    -Wl,--export-all-symbols
endif
ifeq ($(PLATFORM),osx)
LIB_DIR = $(BUILD_DIR)/osx
LIB = $(LIB_DIR)/libae_$(PLUGIN_NAME).dylib
LIB_CCFLAGS := $(LIB_CCFLAGS)\
    -framework OpenGL\
    -L$(AE_DIST)/bin -llua53
endif
CPPFILES = $(wildcard *.cpp) $(wildcard $(PLUGIN_DIR)/common/src/cpp/*.cpp)
OBJS = $(CPPFILES:.cpp=.o) $(CUSTOM_CFILES:.c=.o) $(CUSTOM_CPPFILES:.cpp=.o)
DEPS = $(CPPFILES) $(wildcard $(INCLUDE)/*.h) $(CUSTOM_DEPS)

all: $(OBJS) $(LIB)

ifneq ($(strip $(OBJS)),)
ifeq ($(PLATFORM),windows)
$(LIB): $(LIB_DIR) $(OBJS)
	$(CC) -o $(LIB) $(OBJS) $(LIB_CCFLAGS) -shared
endif
ifeq ($(PLATFORM),osx)
$(LIB): $(LIB_DIR) $(OBJS)
	$(CC) -o $(LIB) $(OBJS) $(LIB_CCFLAGS) -dynamiclib
endif
else
$(LIB):
	$(info No object files to link.)
endif

$(LIB_DIR):
	mkdir $(LIB_DIR)

%.o: %.cpp $(DEPS)
	$(CC) -o $@ -c $< $(CCFLAGS)

%.o: %.c $(DEPS)
	$(CC) -o $@ -c $< $(CCFLAGS)

clean:
	rm -f $(OBJS) $(LIB)

.PHONY: clean all