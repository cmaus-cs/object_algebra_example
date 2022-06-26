package example.dsl

import example.model.CreditScore
import example.model.PaymentRestriction
import example.model.User

interface PaymentSelectionDsl {
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

    infix fun RuleHead.then(selection: PaymentSelection): PaymentRule

    fun interface PaymentRule {
        operator fun invoke(user: User): PaymentRestriction?
    }

    fun interface RuleHead {
        operator fun invoke(user: User): Boolean
    }

    fun interface PaymentSelection {
        operator fun invoke(user: User): PaymentRestriction
    }

    fun interface Condition {
        operator fun invoke(user: User): Boolean
    }

    fun interface UserCreditScore {
        operator fun invoke(user: User): CreditScore
    }
}


context(PaymentSelectionDsl)
fun selectPaymentMethods(): PaymentSelectionDsl.PaymentSelection =
    provided(`credit score`.`is worse than`(good)).then(allow(`advance payment`))
        .or(
            provided(`credit card`).then(
                allow(`credit card payment`.and(`advance payment`))
            )
        )
        .otherwise(
            allow(`advance payment`)
        )

