package example.algebra

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PaymentSelectionAlgebraCombinatorTest {

    @Test
    fun `should use credit card advance payment if applicable`() {

        val combinator = PaymentSelectionAlgebraCombinator(PaymentSelectionAlgebraInterpreter, PaymentSelectionAlgebraPrinter)

        val expectedString = """
            provided credit score is worse than good then allow advance payment
            or
            provided credit card then allow credit card payment and advance payment
            otherwise
            allow advance payment
        """.trimIndent()

        val (_, prettyString) = with(combinator) {
             selectPaymentMethods()
        }
        assertEquals(expectedString, prettyString)

    }

}
