package example.algebra

import arrow.core.nel
import example.model.*
import example.service.CreditScoreService
import example.service.PaymentService

fun interface EvalPaymentRule {
    context (PaymentService, CreditScoreService)
    fun eval(user: User): PaymentRestriction?
}

fun interface EvalRuleHead {
    context (PaymentService, CreditScoreService)
    fun eval(user: User): Boolean
}

fun interface EvalCondition {
    context (PaymentService, CreditScoreService)
    fun eval(user: User): Boolean
}

fun interface EvalUserCreditScore {
    context(CreditScoreService)
    fun eval(user: User): CreditScore
}

fun interface EvalPaymentSelection {
    context (PaymentService, CreditScoreService)
    fun eval(user: User): PaymentRestriction
}


object PaymentSelectionAlgebraInterpreter :
    PaymentSelectionAlgebra<CreditScore, EvalCondition, EvalUserCreditScore, PaymentRestriction, EvalPaymentSelection, EvalPaymentRule, EvalRuleHead> {
    override val good = CreditScore.GOOD
    override val average = CreditScore.AVERAGE
    override val bad = CreditScore.BAD

    override val `advance payment` = PaymentRestriction(AdvancePayment.nel())
    override val `credit card payment` = PaymentRestriction(CreditCardPayment.nel())

    override val `credit score` = object : EvalUserCreditScore {
        context(CreditScoreService) override fun eval(user: User): CreditScore =
            creditScore(user)

    }

    override val `credit card` = object : EvalCondition {

        context(PaymentService, CreditScoreService) override fun eval(user: User): Boolean =
            creditCardType(user) != null
    }

    override fun EvalUserCreditScore.`is worse than`(other: CreditScore) = object : EvalCondition {
        context(PaymentService, CreditScoreService) override fun eval(user: User) =
            this@EvalUserCreditScore.eval(user) < other

    }

    override fun EvalRuleHead.then(restriction: EvalPaymentSelection) = object : EvalPaymentRule {
        context (PaymentService, CreditScoreService) override fun eval(user: User): PaymentRestriction? =
            if (this@EvalRuleHead.eval(user)) restriction.eval(user) else null
    }

    override fun allow(paymentMethods: PaymentRestriction) = object : EvalPaymentSelection {
        context(PaymentService, CreditScoreService) override fun eval(user: User): PaymentRestriction = paymentMethods
    }

    override fun PaymentRestriction.and(other: PaymentRestriction) = PaymentRestriction(this.value + other.value)

    override fun EvalPaymentRule.otherwise(restriction: EvalPaymentSelection) = object : EvalPaymentSelection {
        context(PaymentService, CreditScoreService) override fun eval(user: User): PaymentRestriction =
            this@EvalPaymentRule.eval(user) ?: restriction.eval(user)

    }

    override fun provided(condition: EvalCondition) = object : EvalRuleHead {
        context(PaymentService, CreditScoreService) override fun eval(user: User) =
            condition.eval(user)
    }

    override fun EvalPaymentRule.or(next: EvalPaymentRule) = object : EvalPaymentRule {
        context(PaymentService, CreditScoreService) override fun eval(user: User): PaymentRestriction? =
            this@EvalPaymentRule.eval(user) ?: next.eval(user)
    }
}


