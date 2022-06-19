package example.algebra

interface PaymentSelectionAlgebra<CreditScore, Condition, UserCreditScore, PaymentRestriction, PaymentSelection, PaymentRule, RuleHead> {
    val good: CreditScore
    val average: CreditScore
    val bad: CreditScore

    val `advance payment`: PaymentRestriction
    val `credit card payment`: PaymentRestriction

    val `credit score`: UserCreditScore
    val `credit card`: Condition

    infix fun UserCreditScore.`is worse than`(other: CreditScore): Condition

    fun allow(paymentMethods: PaymentRestriction): PaymentSelection

    infix fun PaymentRestriction.and(other: PaymentRestriction): PaymentRestriction

    infix fun PaymentRule.otherwise(restriction: PaymentSelection): PaymentSelection

    fun provided(condition: Condition): RuleHead

    infix fun PaymentRule.or(next: PaymentRule): PaymentRule

    infix fun RuleHead.then(restriction: PaymentSelection): PaymentRule
}


context(PaymentSelectionAlgebra<CreditScore, Condition, UserCreditScore, PaymentRestriction, PaymentSelection, PaymentRule, RuleHead>)
fun <CreditScore, Condition, UserCreditScore, PaymentRestriction, PaymentSelection, PaymentRule, RuleHead> selectPaymentMethods(): PaymentSelection =
    provided(`credit score` `is worse than` good) then allow(`advance payment`) or
            (provided(`credit card`) then allow(`credit card payment` and `advance payment`)) otherwise
            allow(`advance payment`)


