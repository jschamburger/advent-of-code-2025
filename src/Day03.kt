import kotlin.streams.toList

fun main() {

    fun part1(input: List<String>): Long {
        return input.sumOf { bank ->
            val digits = bank.toCharArray().map { it.digitToInt() }.toList()
            // find the highest number
            val max = digits.subList(0, digits.size - 1).max()
            val maxPos = digits.indexOf(max)
            // find the highest number of the rest
            val maxOfRest = digits.subList(maxPos + 1, digits.size).max()
            "$max$maxOfRest".toLong()
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { bank ->
            val digits = bank.toCharArray().map { it.digitToInt() }.toList()
            (0 until 12).fold(emptyList<Int>() to digits) { (joltage, remainingDigits), index ->
                // find the highest number
                val max = remainingDigits.subList(0, remainingDigits.size - (12 - index) + 1).max()
                val maxPos = remainingDigits.indexOf(max)
                (joltage + max) to remainingDigits.subList(maxPos + 1, remainingDigits.size)
            }.first.joinToString(separator = "").toLong()
        }
    }

    val testInput = readInput("Day03_test")
    val input = readInput("Day03")

    // Part 1
    val part1Test = part1(testInput)
    println("Part 1 Test: $part1Test")
    check(part1Test == 357L)

    println("Part 1: ${part1(input)}")

    // Part 2
    val part2Test = part2(testInput)
    println("Part 2 Test: $part2Test")
    check(part2Test == 3121910778619L)

    println("Part 2: ${part2(input)}")
}
