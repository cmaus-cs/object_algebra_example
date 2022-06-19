package example.algebra

import arrow.core.nel
import arrow.core.nonEmptyListOf
import com.github.h0tk3y.betterParse.parser.toParsedOrThrow
import example.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PaymentSelectionAlgebraCombinatorTest {

    val user = User("detlef")


    @Test
    fun `should use credit card advance payment if applicable`() {

        val interpreter = PaymentSelectionAlgebraInterpreter(
            paymentService = { CreditCardType.AMEX },
            creditScoreService = { CreditScore.GOOD }
        )

        val combinator = PaymentSelectionAlgebraCombinator(interpreter, PaymentSelectionAlgebraPrinter)

        val expectedString = """
            provided credit score is worse than good then allow advance payment
            or
            provided credit card then allow credit card payment and advance payment
            otherwise
            allow advance payment
        """.trimIndent()

        with(combinator) {
            val (program, prettyString) = selectPaymentMethods()
            assertEquals(expectedString, prettyString)

        }

    }

}
