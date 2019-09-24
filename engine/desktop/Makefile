CCFLAGS := $(CCFLAGS)\
    -Wno-long-long\
    -I$(SDL2_INCLUDE)\
    -I$(SDL2_MIXER_INCLUDE)\
    -I$(AE_ROOT_DIR)/common/luaprofiler/src/cpp/include\
    -I$(AE_ROOT_DIR)/common/luadebugger/src/cpp/include\
    -I$(AE_ROOT_DIR)/common/util/src/cpp/include\
    -I$(AE_ROOT_DIR)/common/core/src/cpp/include\
    -I$(AE_ROOT_DIR)/common/io/src/cpp/include\
    -I$(AE_ROOT_DIR)/common/math/src/cpp/include\
    -I$(AE_ROOT_DIR)/common/image/src/cpp/include\
    -I$(AE_ROOT_DIR)/common/texture/src/cpp/include\
    -I$(AE_ROOT_DIR)/common/audio/src/cpp/include\
    -I$(AE_ROOT_DIR)/common/engine/src/cpp/include\
    -I$(AE_ROOT_DIR)/sdl2/core/src/cpp/include\
    -I$(AE_ROOT_DIR)/sdl2/audio/src/cpp/include

# The name of the executable file 
EXEC = ae

# The flags passed while compiling and linking the executable
EXEC_CCFLAGS =\
    -Llauncher -laedesktoplauncher\
    -Lengine -laedesktopengine\
    -Lplugins -laedesktopplugins\
    -Limage -laedesktopimage\
    -Lsystem -laedesktopsystem\
    -Lio -laedesktopio\
    -Lcore -laedesktopcore\
    -Ljsoncpp -ljsoncpp\
    -L$(AE_ROOT_DIR)/sdl2/core -laesdl2core\
    -L$(AE_ROOT_DIR)/sdl2/audio -laesdl2audio\
    -L$(AE_ROOT_DIR)/common/engine -laeengine\
    -L$(AE_ROOT_DIR)/common/audio -laeaudio\
    -L$(AE_ROOT_DIR)/common/texture -laetexture\
    -L$(AE_ROOT_DIR)/common/math -laemath\
    -L$(AE_ROOT_DIR)/common/image -laeimage\
    -L$(AE_ROOT_DIR)/common/io -laeio\
    -L$(AE_ROOT_DIR)/common/core -laecore\
    -L$(AE_ROOT_DIR)/common/util -laeutil\
    -L$(AE_ROOT_DIR)/common/luaprofiler -laeluaprofiler\
    -L$(AE_ROOT_DIR)/common/luadebugger -laeluadebugger\
    -L$(LIBPNG_LIB) -lpng\
    -L$(ZLIB_LIB) -lz
    
ifeq ($(PLATFORM),windows)
EXEC_CCFLAGS := $(EXEC_CCFLAGS)\
    -L$(SDL2_LIB) -lSDL2\
    -lmingw32 -lSDL2\
    -L$(SDL2_MIXER_LIB) -lSDL2_mixer\
    -lshlwapi\
    -static-libstdc++\
    -L$(LUA_LIB) -llua53\
    -lopengl32
#    -lopengl32 -lshlwapi\
EXEC_CCFLAGS := $(EXEC_CCFLAGS) -mwindows -mconsole
endif
ifeq ($(PLATFORM),linux)
EXEC_CCFLAGS := $(EXEC_CCFLAGS)\
    -L$(LUA_LIB) -llua
endif
ifeq ($(PLATFORM),osx)
EXEC_CCFLAGS := $(EXEC_CCFLAGS)\
    -framework OpenGL\
    -F$(LIBS_DIR)/osx/SDL2-$(SDL2_VERSION)\
    -framework SDL2\
    -F$(LIBS_DIR)/osx/SDL2_mixer-$(SDL2_MIXER_VERSION)\
    -framework SDL2_mixer\
    -L$(LUA_LIB) -llua53
endif

export CCFLAGS
    
all:
	$(MAKE) -C jsoncpp/src/cpp -f Makefile all
	$(MAKE) -C core/src/cpp -f Makefile all
	$(MAKE) -C io/src/cpp -f Makefile all
	$(MAKE) -C system/src/cpp -f Makefile all
	$(MAKE) -C image/src/cpp -f Makefile all
	$(MAKE) -C plugins/src/cpp -f Makefile all
	$(MAKE) -C engine/src/cpp -f Makefile all
	$(MAKE) -C launcher/src/cpp -f Makefile all
	$(CC) -o $(EXEC) $(CCFLAGS) $(EXEC_CCFLAGS)

clean:
	$(MAKE) -C jsoncpp/src/cpp -f Makefile clean
	$(MAKE) -C core/src/cpp -f Makefile clean
	$(MAKE) -C io/src/cpp -f Makefile clean
	$(MAKE) -C system/src/cpp -f Makefile clean
	$(MAKE) -C image/src/cpp -f Makefile clean
	$(MAKE) -C plugins/src/cpp -f Makefile clean
	$(MAKE) -C engine/src/cpp -f Makefile clean
	$(MAKE) -C launcher/src/cpp -f Makefile clean
	rm -f $(EXEC) $(EXEC).exe

.PHONY: all clean