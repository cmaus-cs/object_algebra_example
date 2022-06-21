package example.algebra

import arrow.core.nel
import arrow.core.nonEmptyListOf
import com.github.h0tk3y.betterParse.parser.toParsedOrThrow
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

    @Test
    fun `should evaluate a parsed expression`() {
        val inputString = """
            provided credit score is worse than good then allow advance payment
            or
            provided credit card then allow credit card payment and advance payment
            otherwise
            allow advance payment
        """.trimIndent()

        val interpreter = PaymentSelectionAlgebraInterpreter(
            paymentService = { CreditCardType.AMEX },
            creditScoreService = { CreditScore.GOOD },
        )

        with(interpreter) {
            val (tokenizer, parser) = createParser()
            val parseResult = parser.tryParse(tokenizer.tokenize(inputString), 0)
            val program = parseResult.toParsedOrThrow().value

            val result = program(user)
            assertEquals(nonEmptyListOf(CreditCardPayment, AdvancePayment), result.value)
        }
    }
}
