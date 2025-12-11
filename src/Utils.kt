import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

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
    TOP, RIGHT, BOTTOM, LEFT;

    fun rotateClockwise(): Direction =
        when (this) {
            TOP -> RIGHT
            RIGHT -> BOTTOM
            BOTTOM -> LEFT
            LEFT -> TOP
        }

    fun rotateCounterclockwise(): Direction =
        when (this) {
            TOP -> LEFT
            RIGHT -> TOP
            BOTTOM -> RIGHT
            LEFT -> BOTTOM
        }

    companion object {
        fun fromTo(from: Coordinate, position: Coordinate): Direction =
            when (position) {
                from.top() -> TOP
                from.right() -> RIGHT
                from.bottom() -> BOTTOM
                else -> LEFT
            }

        fun fromChar(char: Char): Direction =
            when (char) {
                '^' -> TOP
                '>' -> RIGHT
                'v' -> BOTTOM
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
            Direction.TOP -> top()
            Direction.RIGHT -> right()
            Direction.BOTTOM -> bottom()
            Direction.LEFT -> left()
        }
}

typealias Matrix<T> = MutableList<MutableList<T>>

fun <T> Matrix<T>.safeGet(coordinate: Coordinate): T? {
    return if (coordinate.x < 0 || coordinate.y < 0 || coordinate.x >= this[coordinate.y].size || coordinate.y >= this.size) {
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

fun <T>Matrix<T>.copy(): Matrix<T> {
    return this.map { it.toMutableList() }.toMutableList()
}

fun <T> Matrix<T>.println() {
    this.forEach {
        println(it.joinToString(""))
    }
}
