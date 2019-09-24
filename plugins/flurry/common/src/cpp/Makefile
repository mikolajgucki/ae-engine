INCLUDE = include
CCFLAGS := $(CCFLAGS) -I$(INCLUDE)\
    -I$(AE_ROOT_DIR)/common/core/src/cpp/include\
    -I$(AE_ROOT_DIR)/common/engine/src/cpp/include\
    -I$(AE_ROOT_DIR)/common/io/src/cpp/include\
    -I$(AE_ROOT_DIR)/common/math/src/cpp/include\
    -I$(AE_ROOT_DIR)/common/image/src/cpp/include\
    -I$(AE_ROOT_DIR)/common/texture/src/cpp/include\
    -I$(AE_ROOT_DIR)/common/gles2/src/cpp/include\
    -I$(AE_ROOT_DIR)/common/audio/src/cpp/include

LIB = ../../libaeflurry.a
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