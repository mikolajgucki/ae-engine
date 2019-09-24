#include "ae_sdl2.h"

/** */
extern "C" {
    /** */
    int SDL_main(int argc,char *argv[]);
};

/** */
int SDL_main(int argc,char *argv[]) {
    return ae_sdl_main(argc,argv);
}