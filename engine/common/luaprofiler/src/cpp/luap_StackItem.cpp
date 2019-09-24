#include <cstdlib>
#include "luap_StackItem.h"

/** */
luap_StackItem *luap_CreateStackItem() {
    luap_StackItem *item = (luap_StackItem *)malloc(sizeof(luap_StackItem));
    if (item == (luap_StackItem *)0) {
        return (luap_StackItem *)0;        
    }
    
    item->startTime = 0;
    item->startUsedMemory = 0;
    item->subcallsMemoryAllocated = 0;
    
    return item;
}

/** */
void luap_DeleteStackItem(luap_StackItem *item) {
    free(item);
}