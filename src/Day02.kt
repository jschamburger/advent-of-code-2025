fun main() {
    val pattern = "(\\d+)(\\1)+"
    val regex = Regex(pattern)

    fun part1(input: List<String>): Long {
        return input[0].split(',').map {
            val indices = it.split('-')
            val start = indices[0].toLong()
            val end = indices[1].toLong()
            // find invalid ids
            (start .. end).filter { id ->
                val idString = id.toString()
                idString.length % 2 == 0 && idString.take(idString.length / 2) == idString.substring(idString.length / 2)
            }
        }.flatten().sum()
    }

    fun part2(input: List<String>): Long {
        return input[0].split(',').map {
            val indices = it.split('-')
            val start = indices[0].toLong()
            val end = indices[1].toLong()
            // find invalid ids
            (start .. end).filter { id ->
                val idString = id.toString()
                idString.matches(regex)
            }
        }.flatten().sum()
    }

    val testInput = readInput("Day02_test")
    val input = readInput("Day02")

    // Part 1
    val part1Test = part1(testInput)
    println("Part 1 Test: $part1Test")
    check(part1Test == 1227775554L)

    println("Part 1: ${part1(input)}")

    // Part 2
    val part2Test = part2(testInput)
    println("Part 2 Test: $part2Test")
    check(part2Test == 4174379265L)

    println("Part 2: ${part2(input)}")
}
