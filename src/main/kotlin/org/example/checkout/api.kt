package org.example.checkout

import org.example.checkout.Checkout.Item
import org.example.checkout.Checkout.SKU
import org.example.checkout.GetPricingRules.PricingRule

typealias Items = Collection<Item>
typealias PricingRules = Collection<PricingRule>

/**
 * The driving application interface.
 */
fun interface Checkout: (Items) -> Long {
    data class Item(val sku: SKU, val unitPrice: Long)
    data class SKU(val value: String)
}

/**
 * The driven application interface.
 */
fun interface GetPricingRules: () -> PricingRules {
    sealed interface PricingRule {
        data class MultipricedDeal(val sku: SKU, val specialOffer: SpecialOffer): PricingRule
        data class MealDeal2(val compositeSku: Pair<SKU, SKU>, val price: Long): PricingRule
    }

    data class SpecialOffer(val units: Int, val price: Long)
}

