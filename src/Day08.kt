import java.util.PriorityQueue
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {

    fun calculateDistance(box1: JunctionBox, box2: JunctionBox): Double {
        return sqrt((box2.x - box1.x).pow(2) + (box2.y - box1.y).pow(2) + (box2.z - box1.z).pow(2))
    }

    fun createCircuitQueue(input: List<String>): Pair<PriorityQueue<Triple<Double, JunctionBox, JunctionBox>>, MutableList<Circuit>> {
        val junctionBoxes = input.map {
            val split = it.split(",")
            JunctionBox(split[0].toDouble(), split[1].toDouble(), split[2].toDouble())
        }
        val queue =
            PriorityQueue<Triple<Double, JunctionBox, JunctionBox>>(Comparator { e1, e2 -> e1.first.compareTo(e2.first) })
        val distances = junctionBoxes.flatMapIndexed { index, box ->
            buildList {
                for (i in index + 1 until junctionBoxes.size) {
                    add(Triple(calculateDistance(box, junctionBoxes[i]), box, junctionBoxes[i]))
                }
            }
        }
        queue.addAll(distances)
        val circuits = mutableListOf<Circuit>()
        circuits.addAll(junctionBoxes.map { listOf(it) })
        return Pair(queue, circuits)
    }

    fun part1(input: List<String>, numberOfConnections: Int): Long {
        val (queue, circuits) = createCircuitQueue(input)

        repeat((0 until numberOfConnections).count()) {
            val triple = queue.remove()
            // connect two circuits if necessary
            val circuit1 = circuits.find { it.contains(triple.second) }!!
            val circuit2 = circuits.find { it.contains(triple.third) }!!
            if (circuit1 != circuit2) {
                circuits.remove(circuit1)
                circuits.remove(circuit2)
                circuits.add(circuit1 + circuit2)
            }
        }
        return circuits.map { it.size }.sortedDescending().subList(0, 3).fold(1L) { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Long {
        val (queue, circuits) = createCircuitQueue(input)

        while (queue.isNotEmpty()) {
            val triple = queue.remove()
            // connect two circuits if necessary
            val circuit1 = circuits.find { it.contains(triple.second) }!!
            val circuit2 = circuits.find { it.contains(triple.third) }!!
            if (circuit1 != circuit2) {
                circuits.remove(circuit1)
                circuits.remove(circuit2)
                circuits.add(circuit1 + circuit2)
                if (circuits.size == 1) {
                    return triple.second.x.toLong() * triple.third.x.toLong()
                }
            }
        }
        return -1
    }

    val testInput = readInput("Day08_test")
    val input = readInput("Day08")

    // Part 1
    val part1Test = part1(testInput, 10)
    println("Part 1 Test: $part1Test")
    check(part1Test == 40L)

    println("Part 1: ${part1(input, 1000)}")

    // Part 2
    val part2Test = part2(testInput)
    println("Part 2 Test: $part2Test")
    check(part2Test == 25272L)

    println("Part 2: ${part2(input)}")
}

private data class JunctionBox(val x: Double, val y: Double, val z: Double)
private typealias Circuit = List<JunctionBox>
