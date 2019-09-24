ifeq ($(PLATFORM),osx)
CPP_PLATFORM := unix
endif
ifndef CPP_PLATFORM
CPP_PLATFORM := $(PLATFORM)
endif

INCLUDE = include
CCFLAGS := $(CCFLAGS) -I$(INCLUDE) -I$(CPP_PLATFORM)/include

LIB = ../../libaedesktopsystem.a
CPPFILES = $(wildcard *.cpp) $(wildcard $(CPP_PLATFORM)/*.cpp)
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