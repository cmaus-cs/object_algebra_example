fun main(args: Array<String>) {

    with(EvalExp()) {
        println(e1().eval())
    }
    with(EvalMul()) {
        println(e2().eval())
    }
}


context(ExpAlg<T>)
fun <T> e1(): T = lit(1) + lit(2) + lit(3)

context(MulAlg<T>)
fun <T> e2(): T = lit(5) + lit(6) * lit(4)

internal interface ExpAlg<T> {
    fun lit(n: Int): T
    operator fun T.plus(value: T): T
}

internal interface MulAlg<T> : ExpAlg<T> {
    operator fun T.times(value: T): T
}

internal interface Eval {
    fun eval(): Int
}

internal open class EvalExp : ExpAlg<Eval> {
    override fun lit(n: Int): Eval = object : Eval {
        override fun eval(): Int = n
    }

    private fun add(x: Eval, y: Eval): Eval = object : Eval {
        override fun eval(): Int = x.eval() + y.eval()
    }

    override fun Eval.plus(value: Eval): Eval = add(this, value)
}

internal class EvalMul : EvalExp(), MulAlg<Eval> {
    private fun multiply(a: Eval, b: Eval) = object : Eval {
        override fun eval(): Int = a.eval() * b.eval()
    }

    override fun Eval.times(value: Eval): Eval = multiply(this, value)
}
