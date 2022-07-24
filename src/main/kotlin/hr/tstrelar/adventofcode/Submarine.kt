package hr.tstrelar.adventofcode

data class Submarine(
    val position: Int = 0,
    val depth: Int = 0,
    val aim: Int = 0,
) {
    fun getResult(): Int {
        return position * depth
    }
}
