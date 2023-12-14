fun main() {
    fun part1(input: List<String>) = input.sumOf { line ->
        "${line.first { it.isDigit() }}${line.last { it.isDigit() }}".toInt()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day01_test")
    check(part1(testInput1) == 142)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
