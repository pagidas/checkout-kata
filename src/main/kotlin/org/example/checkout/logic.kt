package org.example.checkout

import org.example.checkout.Checkout.Item
import org.example.checkout.Checkout.SKU
import org.example.checkout.GetPricingRules.PricingRule.MealDeal2
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
                is MealDeal2 -> mealDealDiscount(items, rule)
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

private val mealDealDiscount: (Items, MealDeal2) -> Long = { items, mealDeal ->
    // counts the number of times a pair of SKUs occurred in a given list of items.
    fun Items.countByPairOfSKUs(compositeSku: Pair<SKU, SKU>): Int =
        filter(hasSku(compositeSku.first))
            .zip(filter(hasSku(compositeSku.second)))
            .count()
    // will not throw an exception since it is used when discountable items will contain the deal's SKUs.
    fun Items.fetchBySku(sku: SKU): Item = first(hasSku(sku))

    val offeredSkus = mealDeal.compositeSku.toList()
    val discountable = items.filter(hasSkuIn(offeredSkus))

    if (discountable.map(Item::sku).containsAll(offeredSkus)) {
        val firstItemDeal = discountable.fetchBySku(mealDeal.compositeSku.first)
        val secondItemDeal = discountable.fetchBySku(mealDeal.compositeSku.second)
        val baseDiscount = firstItemDeal.unitPrice + secondItemDeal.unitPrice - mealDeal.price
        val numOfDiscountApplied = discountable.countByPairOfSKUs(mealDeal.compositeSku)
        numOfDiscountApplied * baseDiscount
    }
    else 0
}

private val hasSku: (SKU) -> (Item) -> Boolean = { requiredSku -> { item -> item.sku == requiredSku } }
private val hasSkuIn: (List<SKU>) -> (Item) -> Boolean = { requiredSkus -> { item -> item.sku in requiredSkus } }

