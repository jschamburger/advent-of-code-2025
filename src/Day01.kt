fun main() {
    fun part1(input: List<String>): Int {
        return input.fold(50 to 0) { acc, line ->
            val direction = line[0]
            val distance = line.substring(1, line.length).toInt()
            val next = rotate(acc.first, direction, distance)
            val numberOfZeros = acc.second + if (next == 0) 1 else 0
            next to numberOfZeros
        }.second
    }

    fun part2(input: List<String>): Int {
        return input.fold(50 to 0) { acc, line ->
            val direction = line[0]
            val distance = line.substring(1, line.length).toInt()
            println("rotate ${acc.first} $direction$distance")
            val next = rotateWithZeroes(acc.first, direction, distance)
            println("next: $next")
            val numberOfZeros = acc.second + next.second
            next.first to numberOfZeros
        }.second
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 3)

    part2(testInput).println()
    check(part2(testInput) == 6)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    print("Part 1: ")
    part1(input).println()
    print("Part 2: ")
    part2(input).println()
}

fun rotate(dial: Int, direction: Char, distance: Int): Int {
    return when (direction) {
        'L' -> (dial - distance).mod(100)
        'R' -> (dial + distance).mod(100)
        else -> throw IllegalArgumentException()
    }
}

fun rotateWithZeroes(dial: Int, direction: Char, distance: Int): Pair<Int, Int> {
    return when (direction) {
        'L' -> {
            val newDial = (dial - distance).mod(100)
            val didPassZero = (dial < newDial || newDial == 0) && dial != 0
            val entireTurns = distance.floorDiv(100)
            newDial to entireTurns + if (didPassZero) 1 else 0
        }

        'R' -> {
            val newDial = (dial + distance).mod(100)
            val didPassZero = (dial > newDial || newDial == 0) && dial != 0
            val entireTurns = distance.floorDiv(100)
            newDial to entireTurns + if (didPassZero) 1 else 0
        }

        else -> throw IllegalArgumentException()
    }
}
