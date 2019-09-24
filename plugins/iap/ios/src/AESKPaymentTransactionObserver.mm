#include <sstream>
#include "Log.h"
#include "AESKPaymentTransactionObserver.h"

using namespace std;
using namespace ae;

/// The log tag;
static const char *logTag = "AEIAP";

@implementation AESKPaymentTransactionObserver

/** */
-(void)paymentQueue:(SKPaymentQueue *)queue
    updatedTransactions:(NSArray *)transactions {
// log
    ostringstream msg;
    msg << "[AESKPaymentTransactionObserver updatedTransactions] " <<
        "(count equals " << [transactions count] << ")";
    Log::trace(logTag,msg.str());
    
// for each transaction
    for (SKPaymentTransaction *transaction in transactions) {
        NSString *productId = transaction.payment.productIdentifier;
        
        switch (transaction.transactionState) {
        // purchasing
            case SKPaymentTransactionStatePurchasing:
            {
            // log
                ostringstream msg;
                msg << "Purchasing product " << [productId UTF8String];
                Log::trace(logTag,msg.str());
                
            // callback
                const char *productIdCStr = [[[transaction payment] productIdentifier] UTF8String];
                _iapCallback->purchasing(productIdCStr);
                
                break;
            }

        // deferred
            case SKPaymentTransactionStateDeferred:
            {
            // log
                ostringstream msg;
                msg << "Deferred purchase of product " << [productId UTF8String];
                Log::trace(logTag,msg.str());
                
            // callback
                const char *productIdCStr = [[[transaction payment] productIdentifier] UTF8String];
                _iapCallback->deferred(productIdCStr);
                
                break;
            }

        // purchased
            case SKPaymentTransactionStatePurchased:
            {
            // log
                ostringstream msg;
                msg << "Purchased product " << [productId UTF8String];
                Log::trace(logTag,msg.str());
                
            // callback
                const char *productIdCStr = [[[transaction payment] productIdentifier] UTF8String];
                _iapCallback->purchased(productIdCStr);
                
            // finish
                [[SKPaymentQueue defaultQueue] finishTransaction:transaction];
                break;
            }

        // failed
            case SKPaymentTransactionStateFailed:
            {
            // log
                ostringstream msg;
                msg << "Failed to purchase product " << [productId UTF8String] <<
                    ": " << transaction.error.localizedDescription.UTF8String;
                Log::trace(logTag,msg.str());
                
            // callback
                const char *errorCStr = [[[transaction error] localizedDescription] UTF8String];
                _iapCallback->purchaseFailed(errorCStr);
                break;
            }
            
        // restored
            case SKPaymentTransactionStateRestored:
            {
            // log
                ostringstream msg;
                msg << "Restored product " << [productId UTF8String];
                Log::trace(logTag,msg.str());
                
            // callback
                const char *productIdCStr = [[[transaction payment] productIdentifier] UTF8String];
                _iapCallback->alreadyOwned(productIdCStr);
                
            // finish
                [[SKPaymentQueue defaultQueue] finishTransaction:transaction];
                break;
            }

            default:
            {
                ostringstream msg;
                msg << "Unexpected transaction state " << transaction.transactionState;
                Log::trace(logTag,msg.str());
                break;
            }
        }
    }    
}

/** */
-(void)paymentQueueRestoreCompletedTransactionsFinished:(SKPaymentQueue *)queue {
    _iapCallback->purchasesRestored();
}

/** */
-(void)paymentQueue:(SKPaymentQueue *)queue
    restoreCompletedTransactionsFailedWithError:(NSError *)error {
//
    const char *errorCStr = [[error localizedDescription] UTF8String];
    _iapCallback->restorePurchasesFailed(errorCStr);
}

@end