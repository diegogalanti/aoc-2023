fun main() {
    fun part1(input: String) = input.split(',').sumOf { token ->
        (token.map { it.code }.reduce { a, b ->
            ((a * 17) % 256) + b
        } * 17) % 256
    }

    fun part2(input: String): Int {
        return 0
    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")[0]
    check(part1(testInput) == 1320)

    val input = readInput("Day15")[0]
    part1(input).println()
}
