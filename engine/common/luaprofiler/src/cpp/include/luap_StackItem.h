#ifndef AE_LUAP_STACK_ITEM_H
#define AE_LUAP_STACK_ITEM_H

/** */
typedef struct {
    /** */
    long startTime;
    
    /** */
    long startUsedMemory;
    
    /** */
    long subcallsMemoryAllocated;
} luap_StackItem;

/** */
luap_StackItem *luap_CreateStackItem();

/** */
void luap_DeleteStackItem(luap_StackItem *item);

#endif // AE_LUAP_STACK_ITEM_H