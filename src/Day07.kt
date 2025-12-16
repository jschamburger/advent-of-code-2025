fun main() {
    fun part1(input: List<String>): Long {
        val startIndex = input[0].indexOf('S')
        return input.subList(1, input.size).fold(setOf(startIndex) to 0L) { (indices, splits), line ->
            indices.fold(indices to splits) { acc, i ->
                if (line[i] == '^') {
                    acc.first - setOf(i) + setOf(i - 1, i + 1) to acc.second + 1
                } else {
                    acc
                }
            }
        }.second
    }

    fun part2(input: List<String>): Long {
        val matrix = createMatrix(input)
        // Use priority queue Coordinate to number of paths from there to the bottom
        val queue = ArrayDeque<Coordinate>()
        val startCoordinate = Coordinate(y = 1, x = input[0].indexOf('S'))
        queue.add(startCoordinate)
        // keep number of paths for every calculated coordinate
        val numberOfPathsFromCoordinate = mutableMapOf<Coordinate, Long>()
        // bottom elements each have only one path
        for (i in 0 until input.last().length) {
            numberOfPathsFromCoordinate[Coordinate(y = input.size - 1, x = i)] = 1
        }
        while (queue.isNotEmpty()) {
            val coordinate = queue.removeLast()
            if (matrix.safeGet(coordinate) == '^') {
                // split -> check both elements in next line
                val pathsFromBottomLeftCoordinate = numberOfPathsFromCoordinate[coordinate.bottomLeft()]
                val pathsFromBottomRightCoordinate = numberOfPathsFromCoordinate[coordinate.bottomRight()]

                // both already calculated -> calculate current coordinate
                if (pathsFromBottomLeftCoordinate != null && pathsFromBottomRightCoordinate != null) {
                    numberOfPathsFromCoordinate[coordinate] = pathsFromBottomLeftCoordinate + pathsFromBottomRightCoordinate
                } else {
                    // add coordinates to calculate after re-adding the current coordinate (when the "children" are calculated, the current coordinate can be calculated
                    queue.add(coordinate)
                    if (pathsFromBottomRightCoordinate == null) {
                        queue.add(coordinate.bottomRight())
                    }
                    if (pathsFromBottomLeftCoordinate == null) {
                        queue.add(coordinate.bottomLeft())
                    }
                }
            } else {
                // do not split -> just look at the bottom coordinate
                val pathsFromBottomCoordinate = numberOfPathsFromCoordinate[coordinate.bottom()]
                if (pathsFromBottomCoordinate != null) {
                    // bottom coordinate already calculated -> current coordinate has the same value
                    numberOfPathsFromCoordinate[coordinate] = pathsFromBottomCoordinate
                } else {
                    // add bottom coordinate after re-adding the current coordinate
                    queue.add(coordinate)
                    queue.add(coordinate.bottom())
                }
            }
        }
        return numberOfPathsFromCoordinate[startCoordinate] ?: throw IllegalStateException("No path found")
    }

    val testInput = readInput("Day07_test")
    val input = readInput("Day07")

    // Part 1
    val part1Test = part1(testInput)
    println("Part 1 Test: $part1Test")
    check(part1Test == 21L)

    println("Part 1: ${part1(input)}")

    // Part 2
    val part2Test = part2(testInput)
    println("Part 2 Test: $part2Test")
    check(part2Test == 40L)

    println("Part 2: ${part2(input)}")
}
