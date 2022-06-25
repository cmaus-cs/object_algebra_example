package example.service

import arrow.core.nel
import arrow.core.nonEmptyListOf
import example.model.*
import kotlin.test.Test
import kotlin.test.assertEquals

class CheckoutServiceImplTest {

    private val user = User("detlef")

    @Test
    fun `should use the proper default payment methods`() {

        val paymentMethods = CheckoutServiceImpl(
            paymentService = { null },
            creditScoreService = { CreditScore.GOOD }).selectPaymentMethods(user)

        assertEquals(AdvancePayment.nel(), paymentMethods)
    }

    @Test
    fun `should use credit card advance payment if applicable`() {

        val paymentMethods = CheckoutServiceImpl(
            paymentService = { CreditCardType.AMEX },
            creditScoreService = { CreditScore.GOOD },
        ).selectPaymentMethods(user)

        assertEquals(
            nonEmptyListOf(CreditCardPayment, AdvancePayment),
            paymentMethods
        )
    }
}
