package org.example.checkout

import org.example.checkout.Checkout.Item
import org.example.checkout.Checkout.SKU
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class CheckoutContract {

    abstract val checkout: Checkout

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideItemsAndExpectedTotal")
    fun `checkout provides correct total price`(description: String, items: Items, expectedTotal: Long) {
        assertEquals(expectedTotal, checkout(items))
    }

    private fun provideItemsAndExpectedTotal(): Stream<Arguments> =
        Stream.of(
            `no items`(),
            `a single A`(),
            `multiple A`(),
            `two B with two for one discount`()
        )

    private fun `no items`(): Arguments = Arguments.of(
        "no items",
        emptyList<Item>(),
        0
    )

    private fun `a single A`(): Arguments = Arguments.of(
        "a single A",
        listOf(
            Item(SKU("A"), 50)
        ),
        50
    )

    private fun `multiple A`(): Arguments = Arguments.of(
        "multiple A",
        listOf(
            Item(SKU("A"), 50),
            Item(SKU("A"), 50),
            Item(SKU("A"), 50),
        ),
        150
    )

    private fun `two B with two for one discount`(): Arguments = Arguments.of(
        "two B with 2 for £1 discount",
        listOf(
            Item(SKU("B"), 60),
            Item(SKU("B"), 60)
        ),
        100
    )
}
