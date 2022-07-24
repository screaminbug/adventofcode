import hr.tstrelar.adventofcode.Direction
import hr.tstrelar.adventofcode.Submarine
import hr.tstrelar.adventofcode.UsageException
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

fun main(args: Array<String>) {
    if (args.size != 2) {
        throw UsageException("You didn't provide two arguments")
    }
    val day = args[0].toIntOrNull() ?: throw UsageException("A day should be an integer")
    when(day) {
        1 -> dayOne(args[1])
        2 -> dayTwo(args[1])
        else -> throw UsageException("Day $day is not implemented yet")
    }
}


private fun dayOne(filename: String) {
    val data = readFileAsListOfInts(filename)
    println("Part one: ${dayOnePartOne(data)}")
    println("Part two: ${dayOnePartTwo(data)}")
}

private fun dayOnePartOne(data: List<Int>) =
    data.fold((0 to 0)) {
        acc: Pair<Int, Int>, n: Int ->
        if (n > acc.first) (n to acc.second + 1) else (n to acc.second)
    }.second - 1


private fun dayOnePartTwo(data: List<Int>) =
    dayOnePartOne(data.windowed(3,1).map { it.sum() })


private fun dayTwo(filename: String) {
    val data = readFileAsListOfPairsOfDirectionToInts(filename)
    println("Part one: ${dayTwoPartOne(data)}")
    println("Part two: ${dayTwoPartTwo(data)}")
}

fun dayTwoPartOne(data: List<Pair<Direction, Int>>): Int =
    data.fold(Submarine()) { acc, current ->
        when(current.first) {
            Direction.FORWARD -> Submarine(position = acc.position + current.second, depth = acc.depth)
            Direction.DOWN -> Submarine(position = acc.position, depth = acc.depth + current.second)
            Direction.UP -> Submarine(position = acc.position, depth = acc.depth - current.second)
        }
    }.getResult()

fun dayTwoPartTwo(data: List<Pair<Direction, Int>>): Int =
    data.fold(Submarine()) { acc, current ->
        when(current.first) {
            Direction.FORWARD -> Submarine(
                position = acc.position + current.second,
                depth = acc.depth + (acc.aim * current.second),
                aim = acc.aim
            )
            Direction.DOWN -> Submarine(
                position = acc.position,
                depth = acc.depth,
                aim = acc.aim + current.second
            )
            Direction.UP -> Submarine(
                position = acc.position,
                depth = acc.depth,
                aim = acc.aim - current.second
            )
        }
    }.getResult()

private fun readFileAsListOfInts(filename: String): List<Int> {
    val file = getExistingFile(filename)
    val lines = ArrayList<Int>()
    file.forEachLine {
        lines.add(it.toIntOrNull() ?: throw UsageException("File should contain only integers"))
    }
    return lines
}

private fun readFileAsListOfPairsOfDirectionToInts(filename: String): List<Pair<Direction, Int>> {
    val file = getExistingFile(filename)
    val lines = ArrayList<Pair<Direction, Int>>()
    file.forEachLine {
        val pairStr = it.split(" ").zipWithNext()[0]
        val pairStrInt = (
                Direction.valueOf(pairStr.first.uppercase(Locale.getDefault()))
                        to (pairStr.second.toIntOrNull()
                    ?: throw UsageException(
                        "File should be structured in such a way that each line contains a " +
                                "string and an integer, separated by space")
                        )
                )
        lines.add(pairStrInt)
    }
    return lines
}

private fun getExistingFile(filename: String): File {
    val file = File(filename)
    if (!file.exists()) { throw UsageException("There is no file in the path you provided") }
    return file
}