package org.example.checkout

import org.example.checkout.Checkout.Item
import org.example.checkout.Checkout.SKU
import org.example.checkout.GetPricingRules.PricingRule

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
        items.groupingBy { it }.eachCount()
            .map { (item, quantity) -> discount(item, quantity, pricingRules.findBySku(item.sku)) }
            .fold(0, Long::plus)
    } }

private val discount: (Item, Int, PricingRule?) -> Long = { item, quantity, maybePricingRule ->
    val unitsOffer = maybePricingRule?.specialOffer?.units ?: 0
    val subtotal = item.unitPrice * quantity
    if (unitsOffer == quantity)
        subtotal - maybePricingRule!!.specialOffer.price
    else 0
}

private fun PricingRules.findBySku(sku: SKU): PricingRule? = find { rule -> rule.sku == sku }

