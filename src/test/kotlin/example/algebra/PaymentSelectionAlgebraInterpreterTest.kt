package example.algebra

import arrow.core.nel
import arrow.core.nonEmptyListOf
import example.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PaymentSelectionAlgebraInterpreterTest {

    val user = User("detlef")

    @Test
    fun `should use the proper default payment methods`() {

        val interpreter = PaymentSelectionAlgebraInterpreter(
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

        val interpreter = PaymentSelectionAlgebraInterpreter(
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
