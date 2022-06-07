fun main(args: Array<String>) {

    with(EvalExp()) {
        println(e1().eval())
    }
    with(EvalMul()) {
        println(e2().eval())
        println(e3().eval())
    }
    with (PrintMul()) {
        println(e2().print())
        println(e3().print())
    }
}


context(ExpAlg<T>)
fun <T> e1(): T = lit(1) + lit(2) + lit(3)

context(MulAlg<T>)
fun <T> e2(): T = lit(5) + lit(6) * lit(4)

context(MulAlg<T>)
fun <T> e3(): T = (lit(5) + lit(6)) * lit(4)

