import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.max
import kotlin.math.min

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

enum class Direction {
    UP, RIGHT, DOWN, LEFT;

    fun rotateClockwise(): Direction =
        when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }

    fun rotateCounterclockwise(): Direction =
        when (this) {
            UP -> LEFT
            RIGHT -> UP
            DOWN -> RIGHT
            LEFT -> DOWN
        }

    companion object {
        fun fromTo(from: Coordinate, position: Coordinate): Direction =
            when (position) {
                from.top() -> UP
                from.right() -> RIGHT
                from.bottom() -> DOWN
                else -> LEFT
            }

        fun fromChar(char: Char): Direction =
            when (char) {
                '^' -> UP
                '>' -> RIGHT
                'v' -> DOWN
                else -> LEFT
            }
    }
}

data class Coordinate(val y: Int, val x: Int) {
    operator fun plus(other: Coordinate): Coordinate = Coordinate(y + other.y, x + other.x)
    operator fun minus(other: Coordinate): Coordinate = Coordinate(y - other.y, x - other.x)
    fun top() = Coordinate(y - 1, x)
    fun right() = Coordinate(y, x + 1)
    fun bottom() = Coordinate(y + 1, x)
    fun left() = Coordinate(y, x - 1)
    fun topLeft() = Coordinate(y - 1, x - 1)
    fun topRight() = Coordinate(y - 1, x + 1)
    fun bottomLeft() = Coordinate(y + 1, x - 1)
    fun bottomRight() = Coordinate(y + 1, x + 1)
    fun neighbours() = setOf(top(), right(), bottom(), left())
    fun adjacent() = setOf(topLeft(), top(), topRight(), right(), bottomRight(), bottom(), bottomLeft(), left())
    fun neighbour(direction: Direction) =
        when (direction) {
            Direction.UP -> top()
            Direction.RIGHT -> right()
            Direction.DOWN -> bottom()
            Direction.LEFT -> left()
        }
}

fun Coordinate.isInsideRectangle(a: Coordinate, b: Coordinate): Boolean {
    val minY = minOf(a.y, b.y)
    val maxY = maxOf(a.y, b.y)
    val minX = minOf(a.x, b.x)
    val maxX = maxOf(a.x, b.x)
    return this.x in minX + 1 until maxX && this.y in minY + 1 until maxY
}

fun Coordinate.pathTo(other: Coordinate): List<Coordinate> =
    buildList {
        for (yi in min(y, other.y) .. max(y, other.y)) {
            for (xi in min(x, other.x) .. max(x, other.x)) {
                add(Coordinate(yi, xi))
            }
        }
    }

fun Coordinate.isOnPath(a: Coordinate, b: Coordinate): Boolean {
    val direction = when {
        a.x < b.x -> Direction.RIGHT
        a.x > b.x -> Direction.LEFT
        a.y < b.y -> Direction.DOWN
        else -> Direction.UP
    }
    val minY = minOf(a.y, b.y)
    val maxY = maxOf(a.y, b.y)
    val minX = minOf(a.x, b.x)
    val maxX = maxOf(a.x, b.x)
    return when (direction) {
        Direction.UP,
        Direction.DOWN -> this.x == b.x && this.y in (minY..maxY)
        Direction.RIGHT,
        Direction.LEFT -> this.y == b.y && this.x in (minX..maxX)
    }
}

typealias Matrix<T> = MutableList<MutableList<T>>

fun <T> Matrix<T>.safeGet(coordinate: Coordinate): T? {
    return if (coordinate.x < 0 || coordinate.y < 0 || coordinate.y >= this.size || coordinate.x >= this[coordinate.y].size) {
        null
    } else {
        this[coordinate.y][coordinate.x]
    }
}

fun createMatrix(input: List<String>): Matrix<Char> {
    val charMatrix = input.map {
        it.toMutableList()
    }.fold(listOf<MutableList<Char>>()) { acc, chars ->
        acc + mutableListOf(chars)
    }.toMutableList()
    return charMatrix
}

fun <T> Matrix<T>.sizeY() = size
fun <T> Matrix<T>.sizeX() = this.maxOf { it.size }

fun <T> Matrix<T>.coordinates(): List<Coordinate> {
    return (0 until sizeY()).map { y ->
        (0 until sizeX()).map { x ->
            Coordinate(y, x)
        }.toList()
    }.toList().flatten()
}

fun <T> Matrix<T>.columns(default: T): List<List<T>> {
    return (0 until sizeX()).map { x ->
        (0 until sizeY()).map { y ->
            safeGet(Coordinate(y, x)) ?: default
        }.toList()
    }.toList()
}

fun <T> Matrix<T>.set(coordinate: Coordinate, value: T) {
    this[coordinate.y][coordinate.x] = value
}

fun <T>Matrix<T>.copy(): Matrix<T> {
    return this.map { it.toMutableList() }.toMutableList()
}

fun <T> Matrix<T>.println() {
    this.forEach {
        println(it.joinToString(""))
    }
}
