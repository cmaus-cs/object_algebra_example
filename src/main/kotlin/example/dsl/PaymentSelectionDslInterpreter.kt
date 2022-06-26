package example.dsl

import arrow.core.nel
import example.model.AdvancePayment
import example.model.CreditCardPayment
import example.model.CreditScore
import example.model.PaymentRestriction
import example.service.CreditScoreService
import example.service.PaymentService

class PaymentSelectionDslInterpreter(
    private val paymentService: PaymentService,
    private val creditScoreService: CreditScoreService
) : PaymentSelectionDsl {
    override val good = CreditScore.GOOD
    override val average = CreditScore.AVERAGE
    override val bad = CreditScore.BAD

    override val `advance payment`: PaymentRestriction = TODO()
    override val `credit card payment`: PaymentRestriction = TODO()

    override val `credit score`: PaymentSelectionDsl.UserCreditScore = TODO()
    override val `credit card`: PaymentSelectionDsl.Condition = TODO()
    override fun (PaymentSelectionDsl.UserCreditScore).`is worse than`(other: CreditScore): PaymentSelectionDsl.Condition =
        TODO()

    override fun PaymentSelectionDsl.PaymentRule.otherwise(restriction: PaymentSelectionDsl.PaymentSelection): PaymentSelectionDsl.PaymentSelection =
        TODO()

    override fun provided(condition: PaymentSelectionDsl.Condition): PaymentSelectionDsl.RuleHead = TODO()

    override fun PaymentSelectionDsl.PaymentRule.or(next: PaymentSelectionDsl.PaymentRule): PaymentSelectionDsl.PaymentRule =
        TODO()

    override fun PaymentSelectionDsl.RuleHead.then(selection: PaymentSelectionDsl.PaymentSelection): PaymentSelectionDsl.PaymentRule =
        TODO()

    override fun allow(paymentMethods: PaymentRestriction): PaymentSelectionDsl.PaymentSelection = TODO()

    override fun PaymentRestriction.and(other: PaymentRestriction): PaymentRestriction = TODO()

}
