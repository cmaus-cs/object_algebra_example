package example.algebra

typealias PrintCreditScore = String
typealias PrintCondition = String
typealias PrintUserCreditScore = String
typealias PrintPaymentRestriction = String
typealias PrintPaymentSelection = String
typealias PrintPaymentRule = String
typealias PrintRuleHead = String

object PaymentSelectionAlgebraPrinter :
    PaymentSelectionAlgebra<PrintCreditScore, PrintCondition, PrintUserCreditScore, PrintPaymentRestriction, PrintPaymentSelection, PrintPaymentRule, PrintRuleHead> {

    override val good = "good"
    override val average = "average"
    override val bad = "bad"

    override val `advance payment` = "advance payment"
    override val `credit card payment` = "credit card payment"
    override val `credit score` = "credit score"
    override val `credit card` = "credit card"

    override fun PrintUserCreditScore.`is worse than`(other: PrintCreditScore) =
        "$this is worse than $other"

    override fun allow(paymentMethods: PrintPaymentRestriction) = "allow $paymentMethods"

    override fun PrintPaymentRestriction.and(other: PrintPaymentRestriction) = "$this and $other"

    override fun PrintPaymentRule.otherwise(restriction: PrintPaymentSelection) = "${this}\notherwise\n${restriction}"

    override fun provided(condition: PrintCondition) = "provided $condition"

    override fun PrintPaymentRule.or(next: PrintPaymentRule) = "${this}\nor\n${next}"

    override fun PrintRuleHead.then(restriction: PrintPaymentSelection) = "$this then $restriction"
}
