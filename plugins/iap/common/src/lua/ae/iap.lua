-- @module iap
-- @group IAP
-- @brief Handles in-app events.

-- check if the library is loaded
if not iap then
    error('IAP library not loaded')
end

-- indicates if IAP is supported
local supported

-- the received products
local products

-- @brief Callback function called when IAP has started.
--   Never call this function directly.
function iap.started()
    ae.log.trace('iap.started()')
    
    -- check supported
    supported = iap.isSupported()
    if supported then
        ae.log.trace('IAP is supported')
    else
        ae.log.trace('IAP is not supported')
    end
    
    -- get products
    products = iap.getProducts()
    ae.log.trace('Received products:')
    for _,product in pairs(products) do
        ae.log.trace('\n  Id: %s\n  Title: %s\n  Price-in-cents: %d\n' ..
            '  Currency: %s',product.id,product.title,product.priceInCents,
            product.currencyCode)
    end    
end

-- @brief Callback function called when IAP has failed to start.
--   Never call this function directly.
function iap.failedToStart()
    ae.log.trace('iap.failedToStart()')
end

-- @brief Callback function called when a product is being purchased.
--   Never call this function directly.
-- @param productId The product identifier.
function iap.purchasing(productId)
    ae.log.trace('iap.purchasing('.. productId .. ')')
end

-- @brief Callback function called when a purchase has been deferred.
--   Never call this function directly.
-- @param productId The product identifier.
function iap.deferred(productId)
    ae.log.trace('iap.deferred('.. productId .. ')')
end

-- @brief Callback function called when a product has been purchased.
--   Never call this function directly.
-- @param productId The product identifier.
function iap.purchased(productId)
    ae.log.trace('iap.purchased('.. productId .. ')')
end

-- @brief Callback function called when a purchased has failed.
--   Never call this function directly.
-- @param reason The failure reason.
function iap.purchaseFailed(reason)
    ae.log.trace('iap.purchaseFailed(' .. reason .. ')')
end

-- @brief Callback function called when an already owned product has been
--   purchased. Never call this function directly.
-- @param productId The product identifier.
function iap.alreadyOwned(productId)
    ae.log.trace('iap.alreadyOwned('.. productId .. ')')
    iap.purchased(productId)
end

-- @brief Callback function called when purchases have been restored.
--   Never call this function directly.
function iap.purchasesRestored()
    ae.log.trace('iap.purchasesRestored()')
end

-- @brief Callback function called when it failed to restore purchases.
--   Never call this function directly.
-- @param reason The failure reason.
function iap.restorePurchasesFailed(reason)
    ae.log.trace('iap.restorePurchasesFailed(' .. reason .. ')')
end

-- @brief Gets a product.
-- @param id The product identifier.
-- @return The product or `nil` if there is no such product or no information
--   on products have been received.
function iap.getProduct(id)
    if not products then
        return nil
    end

    for _,product in pairs(products) do
        if id == product.id then
            return product
        end
    end

    return nil
end