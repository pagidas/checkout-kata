package org.example.checkout

import org.example.checkout.Checkout.Item
import org.example.checkout.Checkout.SKU
import org.example.checkout.GetPricingRules.PricingRule.MultipricedDeal

val checkoutLogic: (GetPricingRules) -> Checkout = { getPricingRules ->
    val discountedPrice = discountedPriceLogic(getPricingRules())
    Checkout { items ->
        val subtotal = items.map(Item::unitPrice).fold(0, Long::plus)
        val discount = discountedPrice(items)
        subtotal - discount
    }
}

private val discountedPriceLogic: (PricingRules) -> (Items) -> Long =
    { pricingRules -> { items ->
        pricingRules.map { rule ->
            when (rule) {
                is MultipricedDeal -> multipricedDiscount(items, rule)
            }
        }.fold(0, Long::plus)
    } }

private val multipricedDiscount: (Items, MultipricedDeal) -> Long = { items, multipricedDeal ->
    val discountable = items.filter(hasSku(multipricedDeal.sku))
    if (discountable.isEmpty()) 0
    else with(multipricedDeal) {
        val item = discountable.first()
        val quantity = discountable.count()

        val baseDiscount = item.unitPrice * specialOffer.units - specialOffer.price
        (quantity / specialOffer.units) * baseDiscount
    }
}

private val hasSku: (SKU) -> (Item) -> Boolean = { requiredSku -> { item -> item.sku == requiredSku } }

