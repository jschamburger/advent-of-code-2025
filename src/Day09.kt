import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun calculateRectangleSize(coordinate1: Coordinate, coordinate2: Coordinate): Long {
        // rectangle size between two points: (|x2 - x1| + 1) * (|y2 - y1| + 1)
        return ((coordinate1.x - coordinate2.x).absoluteValue + 1) * ((coordinate1.y - coordinate2.y).absoluteValue + 1).toLong()
    }

    fun part1(input: List<String>): Long {
        val coordinates = input.map {
            val split = it.split(",")
            Coordinate(x = split[0].toInt(), y = split[1].toInt())
        }
        val rectangleSizes = coordinates.flatMapIndexed { index, coordinate ->
            buildList {
                for (i in index + 1 until coordinates.size) {
                    add(Triple(calculateRectangleSize(coordinate, coordinates[i]), coordinate, coordinates[i]))
                }
            }
        }
        return rectangleSizes.map { it.first }.maxOf { it }
    }

    fun part2(input: List<String>): Long {
        val coordinates = input.map {
            val split = it.split(",")
            Coordinate(x = split[0].toInt(), y = split[1].toInt())
        }
        val rectangleSizes = coordinates.flatMapIndexed { index, coordinate ->
            buildList {
                for (i in index + 1 until coordinates.size) {
                    add(Triple(calculateRectangleSize(coordinate, coordinates[i]), coordinate, coordinates[i]))
                }
            }
        }
        val paths = (coordinates + listOf(coordinates.first())).windowed(2).map { it[0] to it[1] }
        return rectangleSizes.filter { triple ->
            val minX = min(triple.second.x, triple.third.x)
            val maxX = max(triple.second.x, triple.third.x)
            val minY = min(triple.second.y, triple.third.y)
            val maxY = max(triple.second.y, triple.third.y)

            paths.none { (start, end) ->
                // vertical path segment
                if (start.x == end.x) {
                    val xInside = start.x in (minX + 1 until maxX)
                    val pathMinY = minOf(start.y, end.y)
                    val pathMaxY = maxOf(start.y, end.y)

                    // If the vertical line is within the X-bounds, check if it overlaps the Y-span
                    if (xInside && maxOf(pathMinY, minY) < minOf(pathMaxY, maxY)) {
                        return@none true // Intersection found
                    }
                }

                // horizontal path segment
                if (start.y == end.y) {
                    val yInside = start.y in (minY + 1 until maxY)
                    val pathMinX = minOf(start.x, end.x)
                    val pathMaxX = maxOf(start.x, end.x)

                    // If the horizontal line is within the Y-bounds, check if it overlaps the X-span
                    if (yInside && maxOf(pathMinX, minX) < minOf(pathMaxX, maxX)) {
                        return@none true // Intersection found
                    }
                }

                false // No intersection for this path
            }
        }.maxBy { it.first }.also { println(it) }.first
    }

    val testInput = readInput("Day09_test")
    val input = readInput("Day09")

    // Part 1
    val part1Test = part1(testInput)
    println("Part 1 Test: $part1Test")
    check(part1Test == 50L)

    println("Part 1: ${part1(input)}")

    // Part 2
    val part2Test = part2(testInput)
    println("Part 2 Test: $part2Test")
    check(part2Test == 24L)

    println("Part 2: ${part2(input)}")
}
