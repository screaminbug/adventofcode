import hr.tstrelar.adventofcode.UsageException
import java.io.File

fun main(args: Array<String>) {
    if (args.size != 2) {
        throw UsageException("You didn't provide two arguments")
    }
    val day = args[0].toIntOrNull() ?: throw UsageException("A day should be an integer")
    when(day) {
        1 -> dayOne(args[1])
        else -> throw UsageException("Day $day is not implemented yet")
    }
}


private fun dayOne(filename: String) {
    val data = readFileAsList(filename)
    data.forEach {
        println(it)
    }
}


private fun readFileAsList(filename: String): List<Int> {
    val file = File(filename)
    if (!file.exists()) { throw UsageException("There is no file in the path you provided") }
    val lines = ArrayList<Int>()
    file.forEachLine {
        lines.add(it.toIntOrNull() ?: throw UsageException("File should contain only integers"))
    }
    return lines
}
