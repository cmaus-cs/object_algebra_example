package example.algebra

@JvmInline
value class PrintCreditScore(val value: String)

@JvmInline
value class PrintCondition(val value: String)

@JvmInline
value class PrintUserCreditScore(val value: String)

@JvmInline
value class PrintPaymentRestriction(val value: String)

@JvmInline
value class PrintPaymentSelection(val value: String)

@JvmInline
value class PrintPaymentRule(val value: String)

@JvmInline
value class PrintRuleHead(val value: String)

object PaymentSelectionAlgebraPrinter :
    PaymentSelectionAlgebra<PrintCreditScore, PrintCondition, PrintUserCreditScore, PrintPaymentRestriction, PrintPaymentSelection, PrintPaymentRule, PrintRuleHead> {

    override val good = PrintCreditScore("good")
    override val average = PrintCreditScore("average")
    override val bad = PrintCreditScore("bad")

    override val `advance payment` = PrintPaymentRestriction("advance payment")
    override val `credit card payment` = PrintPaymentRestriction("credit card payment")
    override val `credit score` = PrintUserCreditScore("credit score")
    override val `credit card` = PrintCondition("credit card")

    override fun PrintUserCreditScore.`is worse than`(other: PrintCreditScore) =
        PrintCondition("${this.value} is worse than ${other.value}")

    override fun allow(paymentMethods: PrintPaymentRestriction) = PrintPaymentSelection("allow ${paymentMethods.value}")

    override fun PrintPaymentRestriction.and(other: PrintPaymentRestriction) =
        PrintPaymentRestriction("${this.value} and ${other.value}")

    override fun PrintPaymentRule.otherwise(restriction: PrintPaymentSelection) =
        PrintPaymentSelection("${this.value}\notherwise\n${restriction.value}")

    override fun provided(condition: PrintCondition) = PrintRuleHead("provided ${condition.value}")

    override fun PrintPaymentRule.or(next: PrintPaymentRule) = PrintPaymentRule("${this.value}\nor\n${next.value}")

    override fun PrintRuleHead.then(restriction: PrintPaymentSelection) =
        PrintPaymentRule("${this.value} then ${restriction.value}")
}
