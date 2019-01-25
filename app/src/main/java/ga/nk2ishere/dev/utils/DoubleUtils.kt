package ga.nk2ishere.dev.utils

fun Double.round(places: Int): Double {
    var value = this
    if (places < 0) throw IllegalArgumentException()

    val factor = Math.pow(10.0, places.toDouble()).toLong()
    value *= factor
    val tmp = Math.round(value)
    return tmp.toDouble() / factor
}