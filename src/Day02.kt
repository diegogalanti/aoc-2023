fun main() {
    fun part1(input: List<String>) = input.mapIndexed { i, line ->
        val pairs = line.substringAfter(": ").split(", ", "; ").map {
            val (qty, color) = it.split(' ')
            qty.toInt() to color
        }.sortedBy { it.first }
        val blueMax = pairs.last { it.second == "blue" }.first
        val redMax = pairs.last { it.second == "red" }.first
        val greenMax = pairs.last { it.second == "green" }.first
        if (redMax <= 12 && greenMax <= 13 && blueMax <= 14) i + 1 else 0
    }.sum()

    fun part2(input: List<String>) = input.sumOf { line ->
        val pairs = line.substringAfter(": ").split(", ", "; ").map {
            val (qty, color) = it.split(' ')
            qty.toInt() to color
        }.sortedBy { it.first }
        val blueMax = pairs.last { it.second == "blue" }.first
        val redMax = pairs.last { it.second == "red" }.first
        val greenMax = pairs.last { it.second == "green" }.first
        greenMax * redMax * blueMax
    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
