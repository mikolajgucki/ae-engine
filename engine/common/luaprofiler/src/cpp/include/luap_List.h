#ifndef AE_LUAP_LIST_H
#define AE_LUAP_LIST_H

/**
 * \brief TODO
 */
typedef struct {
    /** */
    int size;
    
    /** */
    int capacity;
    
    /** */
    void **data;
} luap_List;

/** */
luap_List *luap_CreateList(void);

/** */
int luap_GetListSize(luap_List *list);

/** */
void luap_AddToList(luap_List *list,void *item);

/** */
void *luap_GetListItem(luap_List *list,int index);

/** */
void luap_RemoveLastListItem(luap_List *list);

/** */
void luap_DeleteList(luap_List *list);

#endif // AE_LUAP_LIST_H