#include "DesktopIAPCfg.h"

using namespace std;

namespace ae {

namespace iap {

/** */
void DesktopIAPCfg::deleteProducts() {
    vector<DesktopIAPProduct *>::iterator itr;
    
    for (itr = products.begin(); itr != products.end(); ++itr) {
        DesktopIAPProduct *product = *itr;
        delete product;
    }
}

/** */
DesktopIAPProduct *DesktopIAPCfg::getProduct(const string &id) {
    vector<DesktopIAPProduct *>::iterator itr;
    
// for each product
    for (itr = products.begin(); itr != products.end(); ++itr) {
        DesktopIAPProduct *product = *itr;
        if (product->getId() == id) {
            return product;
        }
    }
    
    return (DesktopIAPProduct *)0;
}

/** */
bool DesktopIAPCfg::isOwned(const std::string& productId) {
    vector<DesktopIAPProduct *>::iterator itr = products.begin();
    for (;itr != products.end(); ++itr) {
        DesktopIAPProduct *product = *itr;
        if (product->getId() == productId) {
            return product->getOwned();
        }
    }
    
    return false;
}

/** */
void DesktopIAPCfg::getProducts(vector<IAPProduct *> &products_) {
    vector<DesktopIAPProduct *>::iterator itr;
    
// for each product
    for (itr = products.begin(); itr != products.end(); ++itr) {
        IAPProduct *product = *itr;
        products_.push_back(new IAPProduct(*product));
    }
}

} // namespace

} // namespace