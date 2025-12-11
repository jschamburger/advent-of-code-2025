fun main() {

    val regex = Regex("""(\d+)-(\d+)""")
    fun part1(input: List<String>): Long {
        val separator = input.indexOf("")
        val ranges = input.subList(0, separator).map {
            val start = regex.matchEntire(it)?.groups[1]?.value?.toLong() ?: 0
            val end = regex.matchEntire(it)?.groups[2]?.value?.toLong() ?: 0
            start ..end
        }
        val ids = input.subList(separator + 1, input.size).map {
            it.toLong()
        }
        return ids.count { id -> ranges.any { it.contains(id) } }.toLong()
    }

    fun part2(input: List<String>): Long {
        val separator = input.indexOf("")
        val ranges = input.subList(0, separator).map {
            val start = regex.matchEntire(it)?.groups[1]?.value?.toLong() ?: 0
            val end = regex.matchEntire(it)?.groups[2]?.value?.toLong() ?: 0
            start ..end
        }
        return generateSequence(ranges) { previousRanges ->
            val newRanges = previousRanges.fold(emptyList<LongRange>()) { longRanges, nextRange ->
                // remove overlaps
                // ####----------#########--------########
                // ------------------##-------------------
                val completelyContained = longRanges.filter { it.first <= nextRange.first && it.last >= nextRange.last }
                // ####----------#########--------########
                // -------------#############-------------
                val completelyContains = longRanges.filter { it.first >= nextRange.first && it.last <= nextRange.last }
                // ####----------#########--------########
                // ------------------########-------------
                val overlapsEnd = longRanges.filter { it.first <= nextRange.first && it.last >= nextRange.first }
                // ####----------#########--------########
                // ----------########---------------------
                val overlapsStart = longRanges.filter { it.first <= nextRange.last && it.first >= nextRange.first }
                when {
                    completelyContained.isNotEmpty() -> {
                        longRanges
                    }
                    completelyContains.isNotEmpty() -> {
                        longRanges - listOf(completelyContains.first()).toSet() + listOf(nextRange)
                    }
                    overlapsEnd.isNotEmpty() -> {
                        val overlappingRange = overlapsEnd.first()
                        val newRange = overlappingRange.first ..nextRange.last
                        longRanges - listOf(overlappingRange).toSet() + listOf(newRange)
                    }
                    overlapsStart.isNotEmpty() -> {
                        val overlappingRange = overlapsStart.first()
                        val newRange = nextRange.first ..overlappingRange.last
                        longRanges - listOf(overlappingRange).toSet() + listOf(newRange)
                    }
                    else -> {
                        longRanges + listOf(nextRange)
                    }
                }
            }
            if (previousRanges == newRanges) {
                // terminate sequence
                null
            } else {
                newRanges
            }
        }.last().sumOf { it.last - it.first + 1 }
    }

    val testInput = readInput("Day05_test")
    val input = readInput("Day05")

    // Part 1
    val part1Test = part1(testInput)
    println("Part 1 Test: $part1Test")
    check(part1Test == 3L)

    println("Part 1: ${part1(input)}")

    // Part 2
    val part2Test = part2(testInput)
    println("Part 2 Test: $part2Test")
    check(part2Test == 14L)

    println("Part 2: ${part2(input)}")
}
