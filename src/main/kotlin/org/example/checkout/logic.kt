package org.example.checkout

import org.example.checkout.Checkout.Item

val checkoutLogic: Checkout = Checkout { items ->
    items.map(Item::unitPrice).fold(0, Long::plus)
}

