INCLUDE = include
CCFLAGS := $(CCFLAGS) -I$(INCLUDE)\
    -I$(SDL2_INCLUDE)\
    -I$(SDL2_MIXER_INCLUDE)\
    -I$(AE_ROOT_DIR)/common/core/src/cpp/include\
    -I$(AE_ROOT_DIR)/common/engine/src/cpp/include    

LIB = ../../libaesdl2core.a
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