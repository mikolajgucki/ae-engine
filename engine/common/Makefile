all:
	$(MAKE) -C luaprofiler/src/cpp -f Makefile all
	$(MAKE) -C luadebugger/src/cpp -f Makefile all
	$(MAKE) -C util/src/cpp -f Makefile all
	$(MAKE) -C core/src/cpp -f Makefile all
	$(MAKE) -C io/src/cpp -f Makefile all
	$(MAKE) -C math/src/cpp -f Makefile all
	$(MAKE) -C image/src/cpp -f Makefile all
	$(MAKE) -C texture/src/cpp -f Makefile all
	$(MAKE) -C audio/src/cpp -f Makefile all
	$(MAKE) -C engine/src/cpp -f Makefile all

clean:
	$(MAKE) -C luaprofiler/src/cpp -f Makefile clean
	$(MAKE) -C luadebugger/src/cpp -f Makefile clean
	$(MAKE) -C util/src/cpp -f Makefile clean
	$(MAKE) -C core/src/cpp -f Makefile clean
	$(MAKE) -C io/src/cpp -f Makefile clean
	$(MAKE) -C math/src/cpp -f Makefile clean
	$(MAKE) -C image/src/cpp -f Makefile clean
	$(MAKE) -C texture/src/cpp -f Makefile clean
	$(MAKE) -C audio/src/cpp -f Makefile clean
	$(MAKE) -C engine/src/cpp -f Makefile clean

.PHONY: all clean