#include <cstdio>
#include <cstdlib>
#include "luap_List.h"

/** */
#define DEFAULT_CAPACITY 16384

/** */
luap_List *luap_CreateList(void) {
// create list
    luap_List *list = (luap_List *)malloc(sizeof(luap_List));
    if (list == (luap_List *)0) {
        return (luap_List *)0;
    }
    
// create data
    list->size = 0;
    list->capacity = DEFAULT_CAPACITY;
    list->data = (void **)malloc(list->capacity * sizeof(void *));
    if (list->data == (void **)0) {
        free(list);
        return (luap_List *)0;
    }
    
    return list;
}

/** */
int luap_GetListSize(luap_List *list) {
    return list->size;
}

/** */
void luap_AddToList(luap_List *list,void *item) {
    if (list->capacity == list->size) {
        return;
    }        
    
    list->data[list->size] = item;
    list->size++;
}

/** */
void *luap_GetListItem(luap_List *list,int index) {
    if (list->size == 0) {
        return (void *)0;
    }
    
    return list->data[index];
}

/** */
void luap_RemoveLastListItem(luap_List *list) {
    if (list->size == 0) {
        return;
    }        
    
    list->size--;
}

/** */
void luap_DeleteList(luap_List *list) {
    free(list->data);
    free(list);
}