import kotlin.system.measureTimeMillis

fun main() {
    fun part1(input: String) = input.split(',').sumOf { token ->
        token.fold(0) { a, b ->
            (((a+b.code) * 17) % 256)
        } as Int
    }

    fun part2(input: String): Int {
        val map = HashMap<String, Pair<Int, Int>>()
        input.split(',').forEachIndexed { i, token ->
            val (key, value) = token.split('=','-')
            if (value.isNotEmpty())
                map[key] = if (map[key] == null) Pair(value.toInt(), i) else Pair(value.toInt(), map[key]!!.second)
            else
                map.remove(key)
        }
        val boxes = Array(256) { 0 }
        var result = 0
        map.toSortedMap(compareBy {map[it]?.second}).forEach {
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
