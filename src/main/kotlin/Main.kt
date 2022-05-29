fun main(args: Array<String>) {

    with(EvalExp()) {
        println(e1().eval())
    }
}


context(ExpAlg<T>)
fun <T> e1(): T = lit(1) + lit(2) + lit(3)


internal interface ExpAlg<T> {
    fun lit(n: Int): T
    operator fun T.plus(value: T): T
}

internal interface Eval {
    fun eval(): Int
}

internal class EvalExp : ExpAlg<Eval> {
    override fun lit(n: Int): Eval = object : Eval {
        override fun eval(): Int = n
    }

    private fun add(x: Eval, y: Eval): Eval = object : Eval {
        override fun eval(): Int = x.eval() + y.eval()
    }

    override fun Eval.plus(value: Eval): Eval = add(this, value)
}
