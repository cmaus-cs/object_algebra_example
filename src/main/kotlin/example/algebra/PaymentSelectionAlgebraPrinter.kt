package example.algebra

object PaymentSelectionAlgebraPrinter : PaymentSelectionAlgebra<
        PaymentSelectionAlgebraPrinter.CreditScore,
        PaymentSelectionAlgebraPrinter.Condition,
        PaymentSelectionAlgebraPrinter.UserCreditScore,
        PaymentSelectionAlgebraPrinter.PaymentRestriction,
        PaymentSelectionAlgebraPrinter.PaymentSelection,
        PaymentSelectionAlgebraPrinter.PaymentRule,
        PaymentSelectionAlgebraPrinter.RuleHead> {

    override val good = CreditScore("good")
    override val average = CreditScore("average")
    override val bad = CreditScore("bad")

    override val `advance payment` = PaymentRestriction("advance payment")
    override val `credit card payment` = PaymentRestriction("credit card payment")
    override val `credit score` = UserCreditScore("credit score")
    override val `credit card` = Condition("credit card")

    override fun UserCreditScore.`is worse than`(other: CreditScore) =
        Condition("${this.value} is worse than ${other.value}")

    override fun allow(paymentMethods: PaymentRestriction) = PaymentSelection("allow ${paymentMethods.value}")

    override fun PaymentRestriction.and(other: PaymentRestriction) =
        PaymentRestriction("${this.value} and ${other.value}")

    override fun PaymentRule.otherwise(restriction: PaymentSelection) =
        PaymentSelection("${this.value}\notherwise\n${restriction.value}")

    override fun provided(condition: Condition) = RuleHead("provided ${condition.value}")

    override fun PaymentRule.or(next: PaymentRule) = PaymentRule("${this.value}\nor\n${next.value}")

    override fun RuleHead.then(restriction: PaymentSelection) =
        PaymentRule("${this.value} then ${restriction.value}")

    @JvmInline
    value class CreditScore(val value: String)

    @JvmInline
    value class Condition(val value: String)

    @JvmInline
    value class UserCreditScore(val value: String)

    @JvmInline
    value class PaymentRestriction(val value: String)

    @JvmInline
    value class PaymentSelection(val value: String)

    @JvmInline
    value class PaymentRule(val value: String)

    @JvmInline
    value class RuleHead(val value: String)

}
