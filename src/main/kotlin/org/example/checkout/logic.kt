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
            .map { (item, quantity) -> discount(item.unitPrice, quantity, pricingRules.findBySku(item.sku)) }
            .fold(0, Long::plus)
    } }

private val discount: (Long, Int, PricingRule?) -> Long = { itemUnitPrice, quantity, maybePricingRule ->
    val unitsOffer = maybePricingRule?.specialOffer?.units ?: 0
    if (unitsOffer > 0)
        with(maybePricingRule!!) {
            val baseDiscount = itemUnitPrice * specialOffer.units - specialOffer.price
            (quantity / specialOffer.units) * baseDiscount
        }
    else 0
}

private fun PricingRules.findBySku(sku: SKU): PricingRule? = find { rule -> rule.sku == sku }

