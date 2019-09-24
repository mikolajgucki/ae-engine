#include <cstring>
#include "Key.h"

namespace ae {
    
namespace engine {

/** */
const Key Key::BACK(1,"back");

/** */
const Key Key::MENU(2,"menu");
    
/** */
const Key *Key::KEYS[] = {
    &Key::BACK,
    &Key::MENU,
    (Key *)0    
};

/** */
const Key *Key::findKeyByName(const char *name) {
    for (int index = 0; KEYS[index] != (Key *)0; index++) {
        const Key *key = KEYS[index];
        if (strcmp(key->getName(),name) == 0) {
            return key;
        }
    }
    
    return (const Key *)0;
}

} // namespace

} // namespace
