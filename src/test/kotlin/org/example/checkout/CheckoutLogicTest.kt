package org.example.checkout

import org.example.checkout.Checkout.SKU
import org.example.checkout.GetPricingRules.PricingRule
import org.example.checkout.GetPricingRules.SpecialOffer

class CheckoutLogicTest: CheckoutContract() {
    override val checkout: Checkout =
        checkoutLogic(
            inMemoryGetPricingRules(
                listOf(
                    PricingRule(SKU("B"), SpecialOffer(2, 100))
                )
        ))
}

val inMemoryGetPricingRules: (PricingRules) -> GetPricingRules = { pricingRules ->
    GetPricingRules { pricingRules }
}

