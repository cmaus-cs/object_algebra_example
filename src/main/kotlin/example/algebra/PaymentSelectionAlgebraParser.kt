package example.algebra

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.lexer.DefaultTokenizer
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser

internal inline fun <reified CreditScore, reified Condition, reified UserCreditScore, reified PaymentRestriction, reified PaymentSelection, reified PaymentRule, reified RuleHead>
        PaymentSelectionAlgebra<CreditScore, Condition, UserCreditScore, PaymentRestriction, PaymentSelection, PaymentRule, RuleHead>.createParser(): Pair<DefaultTokenizer, Parser<PaymentSelection>> {

    val goodToken = literalToken("good")
    val badToken = literalToken("bad")
    val averageToken = literalToken("average")

    val advancePaymentToken = literalToken("advance payment")
    val creditCardPaymentToken = literalToken("credit card payment")
    val userCreditScoreToken = literalToken("credit score")
    val creditCardToken = literalToken("credit card")
    val providedToken = literalToken("provided")
    val isWorseThanToken = literalToken("is worse than")
    val thenToken = literalToken("then")
    val allowToken = literalToken("allow")
    val orToken = literalToken("or")
    val andToken = literalToken("and")
    val otherwiseToken = literalToken("otherwise")

    val ws = regexToken("\\s+", ignore = true)
    val tokenizer = DefaultTokenizer(
        listOf(
            goodToken,
            badToken,
            averageToken,
            advancePaymentToken,
            creditCardPaymentToken,
            userCreditScoreToken,
            creditCardToken,
            providedToken,
            isWorseThanToken,
            thenToken,
            allowToken,
            orToken,
            andToken,
            otherwiseToken,
            ws
        )
    )

    val creditScoreParser = (badToken use { bad }) or
            (goodToken use { good }) or
            (averageToken use { average })

    val paymentRestrictionParser =
        leftAssociative(
            (advancePaymentToken use { `advance payment` }) or
                    (creditCardPaymentToken use { `credit card payment` }),
            andToken
        ) { a, _, b -> a and b }

    val conditionParser =
        creditCardToken use { `credit card` } or
                ((userCreditScoreToken use { `credit score` }) * -isWorseThanToken * creditScoreParser).map { (ucs, cs) -> ucs `is worse than` cs }

    val allowParser = -allowToken * paymentRestrictionParser.map(this::allow)
    val rulesParser = leftAssociative(
        (-providedToken * conditionParser.map(this::provided) * -thenToken * allowParser).map { (ruleHead, selection) -> ruleHead then selection },
        orToken
    ) { rule, _, next -> rule or next }

    val expressionParser =
        allowParser or (rulesParser * -otherwiseToken * allowParser).map { (rule, selection) -> rule otherwise selection }

    return tokenizer to expressionParser
}


