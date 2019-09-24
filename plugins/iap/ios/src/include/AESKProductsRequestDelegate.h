#import <StoreKit/StoreKit.h>
#include "AEIAPProductsListener.h"

@interface AESKProductsRequestDelegate : NSObject<SKProductsRequestDelegate>

/** */
@property (nonatomic) AEIAPProductsListener *productsListener;

@end