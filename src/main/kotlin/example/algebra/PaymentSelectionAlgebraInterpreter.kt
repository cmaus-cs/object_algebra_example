package example.algebra

import arrow.core.nel
import example.model.*
import example.service.CreditScoreService
import example.service.PaymentService

fun interface EvalPaymentRule {
    operator fun invoke(user: User): PaymentRestriction?
}

fun interface EvalRuleHead {
    operator fun invoke(user: User): Boolean
}

fun interface EvalCondition {
    operator fun invoke(user: User): Boolean
}

fun interface EvalUserCreditScore {
    operator fun invoke(user: User): CreditScore
}

fun interface EvalPaymentSelection {
    operator fun invoke(user: User): PaymentRestriction
}

class PaymentSelectionAlgebraInterpreter(
    private val paymentService: PaymentService,
    private val creditScoreService: CreditScoreService
) :
    PaymentSelectionAlgebra<CreditScore, EvalCondition, EvalUserCreditScore, PaymentRestriction, EvalPaymentSelection, EvalPaymentRule, EvalRuleHead> {
    override val good = CreditScore.GOOD
    override val average = CreditScore.AVERAGE
    override val bad = CreditScore.BAD

    override val `advance payment` = PaymentRestriction(AdvancePayment.nel())
    override val `credit card payment` = PaymentRestriction(CreditCardPayment.nel())

    override val `credit score` = EvalUserCreditScore(creditScoreService::creditScore)
    override val `credit card` = EvalCondition { paymentService.creditCardType(it) != null }

    override fun EvalUserCreditScore.`is worse than`(other: CreditScore) = EvalCondition { this(it) < other }

    override fun EvalRuleHead.then(restriction: EvalPaymentSelection) = EvalPaymentRule {
        if (this(it)) restriction(it) else null
    }

    override fun allow(paymentMethods: PaymentRestriction) = EvalPaymentSelection { paymentMethods }

    override fun PaymentRestriction.and(other: PaymentRestriction) = PaymentRestriction(this.value + other.value)

    override fun EvalPaymentRule.otherwise(restriction: EvalPaymentSelection) = EvalPaymentSelection {
        this(it) ?: restriction(it)
    }

    override fun provided(condition: EvalCondition): EvalRuleHead = EvalRuleHead { condition(it) }

    override fun EvalPaymentRule.or(next: EvalPaymentRule): EvalPaymentRule = EvalPaymentRule {
        this(it) ?: next(it)
    }
}


