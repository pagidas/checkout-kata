package org.example.checkout

import org.example.checkout.Checkout.Item
import org.example.checkout.Checkout.SKU
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CheckoutTest {

    val checkout: Checkout = checkoutLogic

    @Test
    fun `total price for no items`() {
        assertEquals(0, checkout(emptyList()))
    }

    @Test
    fun `total price for a single A`() {
        val items = listOf(Item(SKU("A"), 50))
        assertEquals(50, checkout(items))
    }

    @Test
    fun `total price for multiple A`() {
        val items = listOf(
            Item(SKU("A"), 50),
            Item(SKU("A"), 50),
            Item(SKU("A"), 50)
        )
        assertEquals(150, checkout(items))
    }
}

