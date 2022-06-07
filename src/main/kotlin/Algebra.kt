internal interface ExpAlg<T> {
    fun lit(n: Int): T
    operator fun T.plus(value: T): T
}

internal interface MulAlg<T> : ExpAlg<T> {
    operator fun T.times(value: T): T
}

internal fun interface Eval {
    fun eval(): Int
}

internal open class EvalExp : ExpAlg<Eval> {
    override fun lit(n: Int): Eval = Eval { n }

    private fun add(x: Eval, y: Eval): Eval = Eval { x.eval() + y.eval() }

    override fun Eval.plus(value: Eval): Eval = add(this, value)
}

internal class EvalMul : EvalExp(), MulAlg<Eval> {
    private fun multiply(a: Eval, b: Eval) = Eval { a.eval() * b.eval() }

    override fun Eval.times(value: Eval): Eval = multiply(this, value)
}

internal fun interface Print {
    fun print(): String
}

internal open class PrintExp : ExpAlg<Print> {
    override fun lit(n: Int) = Print { "(lit $n)" }

    private fun add(x: Print, y: Print) = Print { "(+ ${x.print()} ${y.print()})" }

    override fun Print.plus(value: Print) = add(this, value)
}

internal class PrintMul : MulAlg<Print>, PrintExp() {
    private fun multiply(a: Print, b: Print) = Print {
        "(* ${a.print()} ${b.print()})"
    }

    override fun Print.times(value: Print) = multiply(this, value)
}
