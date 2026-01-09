import com.microsoft.z3.Context
import com.microsoft.z3.IntNum

data class Machine(
    val indicatorLights: List<Boolean>,
    val toggles: List<List<Int>>,
    val joltages: List<Int>,
)

data class Equation(
    val factors: List<Int>,
    val result: Int,
)

fun main() {

    val patternLights = Regex("\\[([.#]+)]")
    val patternToggles = Regex("\\((.*?)\\)")
    val patternJoltages = Regex("\\{(.*?)}")

    fun parseMachine(input: String): Machine {
        return Machine(
            indicatorLights = patternLights.find(input)!!.groupValues[1].map { it != '.' },
            toggles = patternToggles.findAll(input).toList()
                .map { toggleMatch -> toggleMatch.groupValues[1].split(',').map { it.toInt() } },
            joltages = patternJoltages.find(input)!!.groupValues[1].split(',').map { it.toInt() },
        )
    }

    val permutationLookup = mutableMapOf<Pair<Int, Int>, List<List<Int>>>()
    fun Int.intPermutations(length: Int): List<List<Int>> {
        if (length == 1) return permutationLookup.getOrElse(this to 1) { (0 until this).map { listOf(it) } }
        return (0 until this).flatMap { i ->
            permutationLookup.getOrElse(this to i) { intPermutations(length - 1).map { listOf(i) + it } }
        }
    }

    fun part1(input: List<String>): Long {
        return input.map { line ->
            val machine = parseMachine(line)
            var permutationSize = 1
            while (true) {
                val permutations = machine.toggles.size.intPermutations(permutationSize)
                permutations.forEach { permutation ->
                    // press buttons
                    val indicatorLights = machine.indicatorLights.toMutableList().map { false }.toMutableList()
                    permutation.map { machine.toggles[it] }.forEach { toggle ->
                        toggle.forEach { index ->
                            indicatorLights[index] = !(indicatorLights[index])
                        }
                    }
                    if (indicatorLights == machine.indicatorLights) {
                        println("machine $machine solved in $permutationSize steps")
                        return@map permutationSize
                    }
                }
                permutationSize++
            }
            return@map Int.MAX_VALUE
        }.sum().toLong()
    }

    val context = Context()

    fun part2(input: List<String>): Long {
        return input.map { line ->
            val machine = parseMachine(line)
            println("machine $machine")
            // create linear equation system and solve it using Z3
            val solver = context.mkOptimize()
            val variables = machine.toggles.map { context.mkIntConst("$it") }
            // make sure that every variable is >= 0
            variables.forEach { solver.Add(context.mkGe(it, context.mkInt(0))) }
            machine.joltages.forEachIndexed { index, joltage ->
                val relevantToggles = machine.toggles
                    .withIndex()
                    .filter { it.value.contains(index) }
                    .map { variables[it.index] }
                    .toTypedArray()
                solver.Add(context.mkEq(context.mkAdd(*relevantToggles), context.mkInt(joltage)))
            }
            val totalTogglePresses = context.mkIntConst("total")
            solver.Add(context.mkEq(context.mkAdd(*variables.toTypedArray()), totalTogglePresses))
            // we want the minimum number of presses
            solver.MkMinimize(totalTogglePresses)
            (solver.model.evaluate(totalTogglePresses, false) as IntNum).int

        }.sum().toLong()
    }

    val testInput = readInput("Day10_test")
    val input = readInput("Day10")

    // Part 1
    val part1Test = part1(testInput)
    println("Part 1 Test: $part1Test")
    check(part1Test == 7L)

    println("Part 1: ${part1(input)}")

    // Part 2
    val part2Test = part2(testInput)
    println("Part 2 Test: $part2Test")
    check(part2Test == 33L)

    println("Part 2: ${part2(input)}")
}
