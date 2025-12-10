fun main() {

    fun part1(input: List<String>): Long {
        val matrix = createMatrix(input)
        return matrix.coordinates().count { coordinate ->
            val adjacentRolls = coordinate.adjacent().filter { (matrix.safeGet(it) == '@') }
            matrix.safeGet(coordinate) == '@' && adjacentRolls.size < 4
        }.toLong()
    }

    fun part2(input: List<String>): Long {
        val matrix = createMatrix(input)
        val matrixSequence = generateSequence(matrix to 0) { (oldMatrix, acc) ->
            val newMatrix = oldMatrix.copy()
            val coordinates = oldMatrix.coordinates().filter { coordinate ->
                // find coordinates that can be removed
                val adjacentRolls = coordinate.adjacent().filter { (oldMatrix.safeGet(it) == '@') }
                val canBeRemoved = oldMatrix.safeGet(coordinate) == '@' && adjacentRolls.size < 4
                if (canBeRemoved) {
                    newMatrix[coordinate.y][coordinate.x] = 'x'
                }
                canBeRemoved
            }
            if (newMatrix == oldMatrix) {
                // terminate sequence
                null
            } else {
                newMatrix to acc + coordinates.size
            }
        }
        return matrixSequence.last().second.toLong()
    }

    val testInput = readInput("Day04_test")
    val input = readInput("Day04")

    // Part 1
    val part1Test = part1(testInput)
    println("Part 1 Test: $part1Test")
    check(part1Test == 13L)

    println("Part 1: ${part1(input)}")

    // Part 2
    val part2Test = part2(testInput)
    println("Part 2 Test: $part2Test")
    check(part2Test == 43L)

    println("Part 2: ${part2(input)}")
}
