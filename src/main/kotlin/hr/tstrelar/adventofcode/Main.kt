import hr.tstrelar.adventofcode.Direction
import hr.tstrelar.adventofcode.Direction.*
import hr.tstrelar.adventofcode.Navigation
import hr.tstrelar.adventofcode.Submarine
import hr.tstrelar.adventofcode.UsageException
import java.io.File
import java.util.*

fun main(args: Array<String>) {
    if (args.size != 2) {
        throw UsageException("You didn't provide two arguments")
    }
    val day = args[0].toIntOrNull() ?: throw UsageException("A day should be an integer")
    when(day) {
        1 -> dayOne(args[1])
        2 -> dayTwo(args[1])
        3 -> dayThree(args[1])
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
    val data = readFileAsListOfNavigationCommands(filename)
    println("Part one: ${dayTwoPartOne(data)}")
    println("Part two: ${dayTwoPartTwo(data)}")
}

fun dayTwoPartOne(data: List<Navigation>): Int =
    data.fold(Submarine()) { submarine, current ->
        when(current.direction) {
            FORWARD -> Submarine(position = submarine.position + current.amount, depth = submarine.depth)
            DOWN -> Submarine(position = submarine.position, depth = submarine.depth + current.amount)
            UP -> Submarine(position = submarine.position, depth = submarine.depth - current.amount)
        }
    }.getResult()

fun dayTwoPartTwo(data: List<Navigation>): Int =
    data.fold(Submarine()) { submarine, current ->
        when(current.direction) {
            FORWARD -> Submarine(
                position = submarine.position + current.amount,
                depth = submarine.depth + (submarine.aim * current.amount),
                aim = submarine.aim
            )
            DOWN -> Submarine(
                position = submarine.position,
                depth = submarine.depth,
                aim = submarine.aim + current.amount
            )
            UP -> Submarine(
                position = submarine.position,
                depth = submarine.depth,
                aim = submarine.aim - current.amount
            )
        }
    }.getResult()

private fun dayThree(filename: String) {
    val data = readFileFromBinaryStringAsListOfIntsAndWidth(filename)
    println("Part one: ${dayThreePartOne(data)}")
//    println("Part two: ${dayThreePartTwo(data)}")
}

private fun dayThreePartOne(data: Pair<List<Int>, Int>): Int {
    val width = data.second
    val size = data.first.size
    val onesCount = data.first.fold(List(width) { 0 }) { acc: List<Int>, current ->
        IntRange(0, width - 1).map {
            acc[it] + getOneAt(current, it)
        }
    }
    return getGammaRate(onesCount, size) * getEpsilonRate(onesCount, size)
}

private fun getGammaRate(occurence: List<Int>, size: Int) = getRate(occurence, size) {
        count, threshold -> count > threshold
}

private fun getEpsilonRate(occurence: List<Int>, size: Int) = getRate(occurence, size) {
        count, threshold -> count < threshold
}

private fun getRate(occurence: List<Int>, size: Int, condition: (count: Int, threshold: Int) -> Boolean): Int {
    val threshold = size / 2
    return occurence.foldIndexed(0) { idx, acc, n ->
        if (condition(n, threshold)) acc or (1 shl idx) else acc
    }
}

private fun getOneAt(number: Int, pos: Int): Int {
    return (number shr pos) and 1
}

private fun readFileAsListOfInts(filename: String): List<Int> {
    val file = getExistingFile(filename)
    val lines = ArrayList<Int>()
    file.forEachLine {
        lines.add(it.toIntOrNull() ?: throw UsageException("File should contain only integers"))
    }
    return lines
}

private fun readFileAsListOfNavigationCommands(filename: String): List<Navigation> {
    val file = getExistingFile(filename)
    val lines = ArrayList<Navigation>()
    file.forEachLine {
        val pairStr = it.split(" ").zipWithNext()[0]
        val navigation = Navigation(
            direction = Direction.valueOf(pairStr.first.uppercase(Locale.getDefault())),
            amount = pairStr.second.toIntOrNull() ?: throw UsageException(
                        "File should be structured in such a way that each line contains a " +
                                "string and an integer, separated by space")
                        )

        lines.add(navigation)
    }
    return lines
}

private fun readFileFromBinaryStringAsListOfIntsAndWidth(filename: String): Pair<List<Int>, Int> {
    val file = getExistingFile(filename)
    val lines = kotlin.collections.ArrayList<Int>()
    var width = 500 // an arbitrary large number we won't ever get
    file.forEachLine {
        lines.add(it.toIntOrNull(2) ?: throw UsageException(
            "File should contain only binary numbers represented as strings of 1 and 0 in each line"
        ))
        width = if (it.length < width) it.length else width // take the smallest line width
    }
    return lines to width
}
private fun getExistingFile(filename: String): File {
    val file = File(filename)
    if (!file.exists()) { throw UsageException("There is no file in the path you provided") }
    return file
}