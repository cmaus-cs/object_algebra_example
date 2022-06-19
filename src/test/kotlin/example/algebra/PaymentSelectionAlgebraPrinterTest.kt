package example.algebra

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PaymentSelectionAlgebraPrinterTest {

    @Test
    fun `should render a nice string representation`() {
        val expectedString = """
            provided credit score is worse than good then allow advance payment
            or
            provided credit card then allow credit card payment and advance payment
            otherwise
            allow advance payment
        """.trimIndent()
        with(PaymentSelectionAlgebraPrinter) {
            val result = selectPaymentMethods()
            assertEquals(expectedString, result)
        }
    }
}
