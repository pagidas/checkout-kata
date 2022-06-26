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
            `two B with two for one discount`(),
            `two B with discount and multiple A`(),
            `three B with two for one discount`(),
            `four B with two for one discount`(),
            `three B with two for one discount and one A`(),
            `three C with buy three get one free discount`(),
            `four C with buy three get one free discount`(),
            `six C with buy three get one free discount`(),
            `D E with meal deal discount`(),
            `D E D with meal deal discount`(),
            `D E D E with meal deal discount`(),
            `multiple discounts`()
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

    private fun `two B with discount and multiple A`(): Arguments = Arguments.of(
        "two B with 2 for £1 discount and multiple A",
        listOf(
            Item(SKU("B"), 60),
            Item(SKU("B"), 60),
            Item(SKU("A"), 50),
            Item(SKU("A"), 50),
            Item(SKU("A"), 50)
        ),
        250
    )

    private fun `three B with two for one discount`(): Arguments = Arguments.of(
        "three B with 2 for £1 discount",
        listOf(
            Item(SKU("B"), 60),
            Item(SKU("B"), 60),
            Item(SKU("B"), 60)
        ),
        160
    )

    private fun `four B with two for one discount`(): Arguments = Arguments.of(
        "four B with 2 for £1 discount",
        listOf(
            Item(SKU("B"), 60),
            Item(SKU("B"), 60),
            Item(SKU("B"), 60),
            Item(SKU("B"), 60)
        ),
        200
    )

    private fun `three B with two for one discount and one A`(): Arguments = Arguments.of(
        "three B with 2 for £1 discount and one A",
        listOf(
            Item(SKU("B"), 60),
            Item(SKU("B"), 60),
            Item(SKU("B"), 60),
            Item(SKU("A"), 50)
        ),
        210
    )

    private fun `three C with buy three get one free discount`(): Arguments = Arguments.of(
        "three C with buy 3 get one free discount",
        listOf(
            Item(SKU("C"), 25),
            Item(SKU("C"), 25),
            Item(SKU("C"), 25)
        ),
        50
    )

    private fun `four C with buy three get one free discount`(): Arguments = Arguments.of(
        "four C with buy 3 get one free discount",
        listOf(
            Item(SKU("C"), 25),
            Item(SKU("C"), 25),
            Item(SKU("C"), 25),
            Item(SKU("C"), 25)
        ),
        75
    )

    private fun `six C with buy three get one free discount`(): Arguments = Arguments.of(
        "six C with buy 3 get one free discount",
        listOf(
            Item(SKU("C"), 25),
            Item(SKU("C"), 25),
            Item(SKU("C"), 25),
            Item(SKU("C"), 25),
            Item(SKU("C"), 25),
            Item(SKU("C"), 25)
        ),
        100
    )

    private fun `D E with meal deal discount`(): Arguments = Arguments.of(
        "D, E with meal deal discount",
        listOf(
            Item(SKU("D"), 150),
            Item(SKU("E"), 200)
        ),
        300
    )

    private fun `D E D with meal deal discount`(): Arguments = Arguments.of(
        "D, E, D with meal deal discount",
        listOf(
            Item(SKU("D"), 150),
            Item(SKU("E"), 200),
            Item(SKU("D"), 150)
        ),
        450
    )

    private fun `D E D E with meal deal discount`(): Arguments = Arguments.of(
        "D, E, D, E with meal deal discount",
        listOf(
            Item(SKU("D"), 150),
            Item(SKU("E"), 200),
            Item(SKU("D"), 150),
            Item(SKU("E"), 200),
        ),
        600
    )

    private fun `multiple discounts`(): Arguments = Arguments.of(
        "multiple items with multiple discounts",
        listOf(
            Item(SKU("C"), 25),
            Item(SKU("C"), 25),
            Item(SKU("C"), 25),
            Item(SKU("B"), 60),
            Item(SKU("B"), 60),
            Item(SKU("D"), 150),
            Item(SKU("E"), 200),
            Item(SKU("A"), 50),
            Item(SKU("A"), 50),
        ),
        550
    )
}

