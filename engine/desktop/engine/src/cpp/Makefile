INCLUDE = include
CCFLAGS := $(CCFLAGS) -I$(INCLUDE)\
    -I$(AE_ROOT_DIR)/sdl2/audio/src/cpp/include\
    -I$(AE_ROOT_DIR)/sdl2/core/src/cpp/include\
    -I$(AE_ROOT_DIR)/sdl2/sdl2/src/cpp/include\
    -I$(AE_ROOT_DIR)/desktop/jsoncpp/src/cpp/include\
    -I$(AE_ROOT_DIR)/desktop/core/src/cpp/include\
    -I$(AE_ROOT_DIR)/desktop/io/src/cpp/include\
    -I$(AE_ROOT_DIR)/desktop/system/src/cpp/include\
    -I$(AE_ROOT_DIR)/desktop/image/src/cpp/include\
    -I$(AE_ROOT_DIR)/desktop/texture/src/cpp/include\
    -I$(AE_ROOT_DIR)/desktop/lualibs/src/cpp/include\
    -I$(AE_ROOT_DIR)/desktop/plugins/src/cpp/include

LIB = ../../libaedesktopengine.a
CPPFILES = $(wildcard *.cpp) $(wildcard $(PLATFORM)/*.cpp)
OBJS = $(CPPFILES:.cpp=.o)
DEPS = $(wildcard *.cpp) $(wildcard $(INCLUDE)/*.h)

all: $(OBJS) $(LIB)

$(LIB): $(OBJS)
	ar r $(LIB) $(OBJS)

%.o: %.cpp $(DEPS)
	$(CC) -o $@ -c $< $(CCFLAGS)

clean:
	rm -f $(OBJS) $(LIB)

.PHONY: clean all