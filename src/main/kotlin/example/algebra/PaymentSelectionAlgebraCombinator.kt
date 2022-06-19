package example.algebra

class PaymentSelectionAlgebraCombinator<
        CreditScoreA,
        CreditScoreB,
        ConditionA,
        ConditionB,
        UserCreditScoreA,
        UserCreditScoreB,
        PaymentRestrictionA,
        PaymentRestrictionB,
        PaymentSelectionA,
        PaymentSelectionB,
        PaymentRuleA,
        PaymentRuleB,
        RuleHeadA,
        RuleHeadB>(
    private val algebraA: PaymentSelectionAlgebra<
            CreditScoreA,
            ConditionA,
            UserCreditScoreA,
            PaymentRestrictionA,
            PaymentSelectionA,
            PaymentRuleA,
            RuleHeadA
            >,
    private val algebraB: PaymentSelectionAlgebra<
            CreditScoreB,
            ConditionB,
            UserCreditScoreB,
            PaymentRestrictionB,
            PaymentSelectionB,
            PaymentRuleB,
            RuleHeadB>
) : PaymentSelectionAlgebra<
        Pair<CreditScoreA, CreditScoreB>,
        Pair<ConditionA, ConditionB>,
        Pair<UserCreditScoreA, UserCreditScoreB>,
        Pair<PaymentRestrictionA, PaymentRestrictionB>,
        Pair<PaymentSelectionA, PaymentSelectionB>,
        Pair<PaymentRuleA, PaymentRuleB>,
        Pair<RuleHeadA, RuleHeadB>
        > {
    override val good: Pair<CreditScoreA, CreditScoreB>
        get() = algebraA.good to algebraB.good
    override val average: Pair<CreditScoreA, CreditScoreB>
        get() = algebraA.average to algebraB.average
    override val bad: Pair<CreditScoreA, CreditScoreB>
        get() = algebraA.bad to algebraB.bad
    override val `advance payment`: Pair<PaymentRestrictionA, PaymentRestrictionB>
        get() = algebraA.`advance payment` to algebraB.`advance payment`
    override val `credit card payment`: Pair<PaymentRestrictionA, PaymentRestrictionB>
        get() = algebraA.`credit card payment` to algebraB.`credit card payment`
    override val `credit score`: Pair<UserCreditScoreA, UserCreditScoreB>
        get() = algebraA.`credit score` to algebraB.`credit score`
    override val `credit card`: Pair<ConditionA, ConditionB>
        get() = algebraA.`credit card` to algebraB.`credit card`

    override fun Pair<UserCreditScoreA, UserCreditScoreB>.`is worse than`(other: Pair<CreditScoreA, CreditScoreB>): Pair<ConditionA, ConditionB> {
        val (fst, snd) = this
        val (o1, o2) = other

        val c1 = algebraA.run {
            fst `is worse than` o1
        }
        val c2 = algebraB.run {
            snd `is worse than` o2
        }

        return c1 to c2
    }

    override fun allow(paymentMethods: Pair<PaymentRestrictionA, PaymentRestrictionB>): Pair<PaymentSelectionA, PaymentSelectionB> {
        val (pr1, pr2) = paymentMethods
        return algebraA.run { allow(pr1) } to algebraB.run { allow(pr2) }
    }

    override fun Pair<PaymentRestrictionA, PaymentRestrictionB>.and(other: Pair<PaymentRestrictionA, PaymentRestrictionB>): Pair<PaymentRestrictionA, PaymentRestrictionB> {
        val (pr1, pr2) = this
        val (next1, next2) = other

        return algebraA.run { pr1 and next1 } to algebraB.run { pr2 and next2 }
    }

    override fun Pair<PaymentRuleA, PaymentRuleB>.otherwise(restriction: Pair<PaymentSelectionA, PaymentSelectionB>): Pair<PaymentSelectionA, PaymentSelectionB> {
        val (pr1, pr2) = this
        val (r1, r2) = restriction
        return algebraA.run { pr1 otherwise r1 } to algebraB.run { pr2 otherwise r2 }
    }

    override fun provided(condition: Pair<ConditionA, ConditionB>): Pair<RuleHeadA, RuleHeadB> =
        algebraA.run { provided(condition.first) } to algebraB.run { provided(condition.second) }


    override fun Pair<PaymentRuleA, PaymentRuleB>.or(next: Pair<PaymentRuleA, PaymentRuleB>): Pair<PaymentRuleA, PaymentRuleB> {
        val (fst, snd) = this
        return algebraA.run { fst or next.first } to algebraB.run { snd or next.second }
    }

    override fun Pair<RuleHeadA, RuleHeadB>.then(restriction: Pair<PaymentSelectionA, PaymentSelectionB>): Pair<PaymentRuleA, PaymentRuleB> {
        val (fst, snd) = this
        return algebraA.run { fst then restriction.first } to algebraB.run { snd then restriction.second }
    }
}
