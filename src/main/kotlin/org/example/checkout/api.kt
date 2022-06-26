package org.example.checkout

import org.example.checkout.Checkout.Item

typealias Items = Collection<Item>

/**
 * The driving application interface.
 */
fun interface Checkout: (Items) -> Long {
    data class Item(val sku: SKU, val unitPrice: Long)
    data class SKU(val value: String)
}

