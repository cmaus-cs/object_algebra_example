import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.parser
import com.github.h0tk3y.betterParse.lexer.DefaultTokenizer
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser
import com.github.h0tk3y.betterParse.parser.toParsedOrThrow

internal inline fun <reified T> MulAlg<T>.createParser(): Pair<DefaultTokenizer, Parser<T>> {

    val int = regexToken("[0-9]+")
    val plus = literalToken("+")
    val mult = literalToken("*")
    val openp = literalToken("(")
    val closep = literalToken(")")
    val ws = regexToken("\\s+", ignore = true)
    val tokenizer = DefaultTokenizer(listOf(int, plus, mult, openp, closep, ws))

    lateinit var termFactory: () -> Parser<T>
    val multExprFactory = { leftAssociative(parser(termFactory), mult) { a, _, b -> a * b } }
    val sumExprFactory = { leftAssociative(parser(multExprFactory), plus) { a, _, b -> a + b } }
    termFactory = {
        (int use { lit(text.toInt()) }) or (-openp * parser(sumExprFactory) * -closep)
    }

    return tokenizer to sumExprFactory()
}

fun main(args: Array<String>) {
    val exps = listOf(
        "42" to 42,
        "3 * 6" to 18,
        "2 * 3 * 4" to 24,
        "2 + 3 * 4" to 14,
        "(2 + 3) * 4" to 20
    )

    with(EvalMul()) {
        val (tokenizer, parser) = createParser()
        exps.forEach { (exp, expected) ->
            val parseResult = parser.tryParse(tokenizer.tokenize(exp), 0)
            val evalResult = parseResult.toParsedOrThrow().value.eval()
            println("$exp evaluates to $evalResult. Expected $expected")
        }
    }

    with(PrintMul()) {
        val (tokenizer, parser) = createParser()
        exps.forEach { (exp, expected) ->
            val parseResult = parser.tryParse(tokenizer.tokenize(exp), 0)
            val evalResult = parseResult.toParsedOrThrow().value.print()
            println("$exp pretty prints to $evalResult")
        }
    }
}
