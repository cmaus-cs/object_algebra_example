package example.algebra

import arrow.core.nel
import arrow.core.nonEmptyListOf
import com.github.h0tk3y.betterParse.parser.toParsedOrThrow
import example.model.*
import example.service.CreditScoreService
import example.service.PaymentService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PaymentSelectionAlgebraInterpreterTest {

    val user = User("detlef")

    val program = with(PaymentSelectionAlgebraInterpreter) {
        selectPaymentMethods()
    }

    @Test
    fun `should use the proper default payment methods`() {

        val result = with(PaymentService { null }) {
            with(CreditScoreService { CreditScore.GOOD }) {
                program.eval(user)
            }
        }

        assertEquals(AdvancePayment.nel(), result.value)
    }

    @Test
    fun `should use credit card advance payment if applicable`() {

        val result = with(PaymentService { CreditCardType.AMEX }) {
            with(CreditScoreService { CreditScore.GOOD }) {
                program.eval(user)
            }
        }

        assertEquals(nonEmptyListOf(CreditCardPayment, AdvancePayment), result.value)
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

        val program = with(PaymentSelectionAlgebraInterpreter) {
            val (tokenizer, parser) = createParser()
            val parseResult = parser.tryParse(tokenizer.tokenize(inputString), 0)
            parseResult.toParsedOrThrow().value
        }

        val result = with(PaymentService { CreditCardType.AMEX }) {
            with(CreditScoreService { CreditScore.GOOD }) {
                program.eval(user)
            }
        }

        assertEquals(nonEmptyListOf(CreditCardPayment, AdvancePayment), result.value)
    }
}
