package example.dsl

import arrow.core.nel
import arrow.core.nonEmptyListOf
import example.model.*
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

internal class PaymentSelectionDslInterpreterTest {

    val user = User("detlef")

    @Test
    fun `should use the proper default payment methods`() {

        val interpreter = PaymentSelectionDslInterpreter(
            paymentService = { null },
            creditScoreService = { CreditScore.GOOD },
        )

        with(interpreter) {
            val program = selectPaymentMethods()
            val result = program(user)
            assertEquals(AdvancePayment.nel(), result.value)
        }
    }

    @Test
    fun `should use credit card advance payment if applicable`() {

        val interpreter = PaymentSelectionDslInterpreter(
            paymentService = { CreditCardType.AMEX },
            creditScoreService = { CreditScore.GOOD },
        )

        with(interpreter) {
            val program = selectPaymentMethods()
            val result = program(user)
            assertEquals(nonEmptyListOf(CreditCardPayment, AdvancePayment), result.value)
        }
    }
}
