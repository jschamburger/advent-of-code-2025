import java.util.regex.Pattern

private sealed class Token {
    object Plus : Token()
    object Multiply : Token()
    data class Number(val number: Long) : Token()
}

fun main() {
    val pattern = Pattern.compile("\\s+")
    fun part1(input: List<String>): Long {
        val tokens = input.map { line ->
            line.trim().split(pattern).map {
                when (it) {
                    "+" -> Token.Plus
                    "*" -> Token.Multiply
                    else -> Token.Number(it.toLong())
                }
            }
        }
        return (0 until tokens[0].size).sumOf { i ->
            // calculate
            performCalculation(tokens.map { it[i] })
        }
    }

    fun <T> List<T>.split(predicate: (T) -> Boolean): List<List<T>> {
        val index = this.indexOfFirst(predicate)
        return if (index == -1) {
            listOf(this)
        } else {
            listOf(this.take(index)) + this.drop(index + 1).split(predicate)
        }
    }

    fun part2(input: List<String>): Long {
        val charMatrix = createMatrix(input)
        val columns = charMatrix.columns(' ')
        return columns.split { it.all { char -> char == ' ' } }.sumOf { calculation ->
            val operation = calculation[0].last()
            val operators = calculation.reversed().map { list ->
                list.filter { it.digitToIntOrNull() != null }.joinToString(separator = "").toLong()
            }
            when (operation) {
                '+' -> operators.fold(0L) { acc, op -> acc + op }
                '*' -> operators.fold(1L) { acc, op -> acc * op }
                else -> throw IllegalArgumentException("Unrecognized operation: $operation")
            }
        }
    }


    val testInput = readInput("Day06_test")
    val input = readInput("Day06")

    // Part 1
    val part1Test = part1(testInput)
    println("Part 1 Test: $part1Test")
    check(part1Test == 4277556L)

    println("Part 1: ${part1(input)}")

    // Part 2
    val part2Test = part2(testInput)
    println("Part 2 Test: $part2Test")
    check(part2Test == 3263827L)

    println("Part 2: ${part2(input)}")
}

private fun performCalculation(tokens: List<Token>): Long {
    return when (tokens.last()) {
        is Token.Plus -> {
            tokens.subList(0, tokens.size - 1).fold(0) { acc, it -> acc + (it as Token.Number).number }
        }

        is Token.Multiply -> {
            tokens.subList(0, tokens.size - 1).fold(1) { acc, it -> acc * (it as Token.Number).number }
        }

        else -> throw IllegalArgumentException("Unknown token")
    }
}
