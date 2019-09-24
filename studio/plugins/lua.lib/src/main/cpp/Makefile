# This file is called from Ant.

ifeq ($(PLATFORM),windows)
LUA_LIB_MAKEFILE := Makefile.windows
endif
ifeq ($(PLATFORM),osx)
LUA_LIB_MAKEFILE := Makefile.osx
endif

all:
	$(MAKE) -f $(LUA_LIB_MAKEFILE) all
	
clean:
	$(MAKE) -f $(LUA_LIB_MAKEFILE) clean