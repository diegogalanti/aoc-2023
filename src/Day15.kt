import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: String) = input.split(',').sumOf { token ->
        token.map { it.code }.fold(0) { a, b ->
            (((a+b) * 17) % 256)
        } as Int
    }

    fun part2(input: String): Int {
        val map = HashMap<String, Pair<Int, Int>>()
        input.split(',').forEachIndexed { i, token ->
            val indexOpe = token.indexOfAny(listOf("=", "-"))
            val key = token.substring(0..<indexOpe)
            val operation = token[indexOpe]
            val value = token.substring(indexOpe + 1)
            if (operation == '=') {
                if (map[key] == null)
                    map[key] = Pair(value.toInt(), i)
                else
                    map[key] = Pair(value.toInt(), map[key]!!.second)
            } else
                map.remove(key)
        }
        val boxes = Array(256) { 0 }
        var result = 0
        map.toSortedMap(compareBy {
            map[it]?.second
        }).forEach {
            val hash = part1(it.key)
            boxes[hash]++
            result += (hash + 1) * boxes[hash] * it.value.first
        }
        return result
    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")[0]
    check(part1(testInput) == 1320)
    check(part2(testInput) == 145)

    val input = readInput("Day15")[0]
    println("Results: part 1 = ${part1(input)} | part 2 = ${part2(input)}")
    println("Time to run: part 1 = ${measureTimeMillis {part1(input)}}ms | part 2 = ${measureTimeMillis {part2(input)}}ms")
}
