#import <StoreKit/StoreKit.h>
#include "IAPCallback.h"

@interface AESKPaymentTransactionObserver :
    NSObject<SKPaymentTransactionObserver>

/** */
@property (nonatomic) ae::iap::IAPCallback *iapCallback;

@end