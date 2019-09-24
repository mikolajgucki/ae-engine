#import <Foundation/Foundation.h>
#include "Log.h"
#include "AEiOSLuaLibIAP.h"

using namespace std;
using namespace ae;
using namespace ae::iap;

/// The log tag.
static const char *logTag = "AEIAP";

/** */
SKProduct *AEiOSLuaLibIAP::findProduct(NSString *nsProductId) {
    for (SKProduct *product in products) {
        if ([[product productIdentifier] isEqualToString:nsProductId]) {
            return product;
        }
    }
    
    return nil;
}

/** */
void AEiOSLuaLibIAP::init() {    
    Log::trace(logTag,"AEiOSLuaLibIAP::init()");
    
    int capacity = (int)productIds.size();
    NSMutableArray *nsProductIdArray = [NSMutableArray arrayWithCapacity:capacity];
// identifier array
    vector<const string>::iterator itr;
    for (itr = productIds.begin(); itr != productIds.end(); ++itr) {
        const char *productIdCStr = (*itr).c_str();
        NSString *nsProductId = [NSString stringWithUTF8String:productIdCStr];
        [nsProductIdArray addObject:nsProductId];
    }
    
// identifier set
    NSSet *nsProductIdSet = [NSSet setWithArray:nsProductIdArray];
    
// callback
    IAPCallback *callback = getCallback();
    
// products request delegate
    productsRequestDelegate = [[AESKProductsRequestDelegate alloc] init];
    [productsRequestDelegate setProductsListener:this];
    
// transaction observer
    transactionObserver = [[AESKPaymentTransactionObserver alloc] init];
    [transactionObserver setIapCallback:callback];
    [[SKPaymentQueue defaultQueue] addTransactionObserver:transactionObserver];
    
// request products
    SKProductsRequest *request = [[SKProductsRequest alloc]
        initWithProductIdentifiers:nsProductIdSet];
    request.delegate = productsRequestDelegate;
    [request start];
}

/** */
bool AEiOSLuaLibIAP::isSupported() {
    Log::trace(logTag,"AEiOSLuaLibIAP::isSupported()");
    
    if ([SKPaymentQueue canMakePayments] == YES) {
        return true;
    }    
    return false;
}

/** */
void AEiOSLuaLibIAP::purchase(const string& productId) {
// identifier
    const char *productIdCStr = productId.c_str();
    NSString *nsProductId = [NSString stringWithUTF8String:productIdCStr];
    
// log
    ostringstream msg;
    msg << "AEiOSLuaLibIAP::purchase(" << nsProductId.UTF8String << ")";
    Log::trace(logTag,msg.str());
    
// find product
    SKProduct *product = findProduct(nsProductId);
    if (product == nil) {
        getCallback()->purchaseFailed("Product not found");
        return;
    }
    
// add payment to the default queue
    SKPayment *payment = [SKPayment paymentWithProduct:product];
    [[SKPaymentQueue defaultQueue] addPayment:payment];
}

/** */
bool AEiOSLuaLibIAP::getProducts(vector<IAPProduct *> &iapProducts) {
    Log::trace(logTag,"AEiOSLuaLibIAP::getProducts()");
    
    for (SKProduct *product in products) {
        const char *productIdCStr = [[product productIdentifier] UTF8String];
        const char *titleCStr = [[product localizedTitle] UTF8String];
        
    // price in cents
        long priceInCents = [[[product price] decimalNumberByMultiplyingByPowerOf10:2] longValue];
        
    // currency
        NSString *nsCurrency = (NSString *)[[product priceLocale]
            objectForKey:NSLocaleCurrencyCode];
        const char *currencyCStr = [nsCurrency UTF8String];
        
    // format price
        NSNumberFormatter *formatter = [[NSNumberFormatter alloc] init];
        [formatter setFormatterBehavior:NSNumberFormatterBehavior10_4];
        [formatter setNumberStyle:NSNumberFormatterCurrencyStyle];
        [formatter setLocale:product.priceLocale];
        NSString *priceStr = [formatter stringFromNumber:product.price];
        const char *priceCStr = [priceStr UTF8String];
        
    // create product
        IAPProduct *iapProduct = new (nothrow) IAPProduct(
            productIdCStr,titleCStr,priceCStr,priceInCents,currencyCStr);
        if (iapProduct == (IAPProduct *)0) {
            setNoMemoryError();
            return false;
        }
        
        iapProducts.push_back(iapProduct);
    }
    
    return true;
}

/** */
void AEiOSLuaLibIAP::receivedProducts(SKProductsResponse *response) {
    Log::trace(logTag,"AEiOSLuaLibIAP::receivedProducts()");
    products = response.products;
    
// log the unknown products
    for (int index = 0; index < [response.invalidProductIdentifiers count]; index++) {
        NSString *id = response.invalidProductIdentifiers[index];
        
    // log
        ostringstream msg;
        msg << "Invalid product: " << [id UTF8String];
        Log::error(logTag,msg.str());
    }
    
// log the products
    for (int index = 0; index < [products count]; index++) {
        SKProduct *product = products[index];
        NSString *id = [product productIdentifier];
        NSString *title = [product localizedTitle];
        NSString *currency = (NSString *)[[product priceLocale] objectForKey:NSLocaleCurrencyCode];
        
    // log
        ostringstream msg;
        msg << "product: " << [id UTF8String];
        msg << ", title: " << [title UTF8String];
        msg << ", price: " << [[product price] doubleValue];
        msg << ", currency: " << [currency UTF8String];
        Log::info(logTag,msg.str());
    }
    
    getCallback()->started();
}

/** */
void AEiOSLuaLibIAP::restorePurchases() {
    Log::trace(logTag,"AEiOSLuaLibIAP::restorePurchases()");
    [[SKPaymentQueue defaultQueue] restoreCompletedTransactions];
}

/** */
void AEiOSLuaLibIAP::purchased(NSString *productId) {
// log
    ostringstream msg;
    msg << "AEiOSLuaLibIAP::purchased(" << [productId UTF8String] << ")";
    Log::trace(logTag,msg.str());
    
// callback
    const char *productIdCStr = [productId UTF8String];
    getCallback()->purchased(productIdCStr);
    
}
 
/** */
void AEiOSLuaLibIAP::purchaseFailed(NSString *reason) {
// log
    ostringstream msg;
    msg << "AEiOSLuaLibIAP::purchaseFailed(" << reason.UTF8String << ")";
    Log::trace(logTag,msg.str());
    
// callback
    const char *reasonCStr = [reason UTF8String];
    getCallback()->purchaseFailed(reasonCStr);
}
