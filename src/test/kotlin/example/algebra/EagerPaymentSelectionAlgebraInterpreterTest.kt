package example.algebra

import arrow.core.nel
import arrow.core.nonEmptyListOf
import com.github.h0tk3y.betterParse.parser.toParsedOrThrow
import example.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class EagerPaymentSelectionAlgebraInterpreterTest {

    val user = User("detlef")

    @Test
    fun `should use the proper default payment methods`() {

        val interpreter = EagerPaymentSelectionAlgebraInterpreter(
            paymentService = { null },
            creditScoreService = { CreditScore.GOOD },
            user
        )

        with(interpreter) {
            val result = selectPaymentMethods()
            assertEquals(AdvancePayment.nel(), result)
        }
    }

    @Test
    fun `should use credit card advance payment if applicable`() {

        val interpreter = EagerPaymentSelectionAlgebraInterpreter(
            paymentService = { CreditCardType.AMEX },
            creditScoreService = { CreditScore.GOOD },
            user
        )

        with(interpreter) {
            val result = selectPaymentMethods()
            assertEquals(nonEmptyListOf(CreditCardPayment, AdvancePayment), result)
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

        val interpreter = EagerPaymentSelectionAlgebraInterpreter(
            paymentService = { CreditCardType.AMEX },
            creditScoreService = { CreditScore.GOOD },
            user
        )

        with(interpreter) {
            val (tokenizer, parser) = createParser()
            val parseResult = parser.tryParse(tokenizer.tokenize(inputString), 0)
            val result = parseResult.toParsedOrThrow()

            assertEquals(nonEmptyListOf(CreditCardPayment, AdvancePayment), result.value)
        }
    }
}
