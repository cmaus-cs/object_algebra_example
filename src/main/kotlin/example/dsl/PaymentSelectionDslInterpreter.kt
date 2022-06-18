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

    override val `advance payment` = PaymentRestriction(AdvancePayment.nel())
    override val `credit card payment` = PaymentRestriction(CreditCardPayment.nel())

    override val `credit score` = PaymentSelectionDsl.UserCreditScore(creditScoreService::creditScore)
    override val `credit card` = PaymentSelectionDsl.Condition { paymentService.creditCardType(it) != null }
    override fun (PaymentSelectionDsl.UserCreditScore).`is worse than`(other: CreditScore) =
        PaymentSelectionDsl.Condition { this(it) < other }

    override fun PaymentSelectionDsl.PaymentRule.otherwise(restriction: PaymentSelectionDsl.PaymentSelection) =
        PaymentSelectionDsl.PaymentSelection {
            this(it) ?: restriction(it)
        }

    override fun provided(condition: PaymentSelectionDsl.Condition) = PaymentSelectionDsl.RuleHead { condition(it) }

    override fun PaymentSelectionDsl.PaymentRule.or(next: PaymentSelectionDsl.PaymentRule) =
        PaymentSelectionDsl.PaymentRule { this(it) ?: next(it) }

    override fun PaymentSelectionDsl.RuleHead.then(selection: PaymentSelectionDsl.PaymentSelection) =
        PaymentSelectionDsl.PaymentRule {
            if (this(it)) selection(it) else null
        }

    override fun allow(paymentMethods: PaymentRestriction) = PaymentSelectionDsl.PaymentSelection {
        paymentMethods
    }

    override fun PaymentRestriction.and(other: PaymentRestriction) = PaymentRestriction(this.value + other.value)

}
