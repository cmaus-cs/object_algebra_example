package example.algebra

object EagerPaymentSelectionAlgebraPrinter : PaymentSelectionAlgebra<
        String,
        String,
        String,
        String,
        String,
        String,
        String> {

    override val good = "good"
    override val average = "average"
    override val bad = "bad"

    override val `advance payment` = "advance payment"
    override val `credit card payment` = "credit card payment"
    override val `credit score` = "credit score"
    override val `credit card` = "credit card"

    override fun String.`is worse than`(other: String) =
        "$this is worse than $other"

    override fun allow(paymentMethods: String) = "allow $paymentMethods"

    override fun String.and(other: String) = "$this and $other"

    override fun String.otherwise(restriction: String) = "${this}\notherwise\n${restriction}"

    override fun provided(condition: String) = "provided $condition"

    override fun String.or(next: String) = "${this}\nor\n${next}"

    override fun String.then(restriction: String) = "$this then $restriction"
}
