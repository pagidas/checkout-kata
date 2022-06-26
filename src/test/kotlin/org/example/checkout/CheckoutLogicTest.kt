package org.example.checkout

import org.example.checkout.Checkout.SKU
import org.example.checkout.GetPricingRules.PricingRule.MealDeal2
import org.example.checkout.GetPricingRules.PricingRule.MultipricedDeal
import org.example.checkout.GetPricingRules.SpecialOffer

class CheckoutLogicTest: CheckoutContract() {
    override val checkout: Checkout =
        checkoutLogic(
            inMemoryGetPricingRules(
                listOf(
                    MultipricedDeal(SKU("B"), SpecialOffer(2, 100)),
                    MultipricedDeal(SKU("C"), SpecialOffer(3, 50)),
                    MealDeal2(Pair(SKU("D"), SKU("E")), 300)
                )
        ))
}

val inMemoryGetPricingRules: (PricingRules) -> GetPricingRules = { pricingRules ->
    GetPricingRules { pricingRules }
}

