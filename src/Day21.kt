fun main() {
    data class GardenNode(val row: Int, val col: Int, var value: Char)

    fun executeStep(map: MutableMap<GardenNode, List<GardenNode>>) {
        val steps = map.filter { it.key.value == 'O' }

        for (step in steps) {
            step.key.value = '.'
            for (neig in step.value) {
                neig.value = 'O'
            }
        }
    }

    fun part1(input: List<String>, steps: Int): Int {
        val map = input.map { it.toCharArray().map { c -> if (c == 'S') 'O' else c } }

        val listNodes = mutableSetOf<GardenNode>()
        for (row in map.indices) {
            for (col in map[row].indices) {
                if (map[row][col] == '#')
                    continue
                listNodes.add(GardenNode(row, col, map[row][col]))
            }
        }
        val mapNodes = mutableMapOf<GardenNode, List<GardenNode>>()
        for (listNode in listNodes) {
            mapNodes[listNode] = listNodes.filter {
                it.row == listNode.row && it.col == listNode.col + 1 ||
                it.row == listNode.row && it.col == listNode.col - 1 ||
                it.row == listNode.row - 1 && it.col == listNode.col ||
                it.row == listNode.row + 1 && it.col == listNode.col
            }
        }
        for (i in 1..steps) {
            executeStep(mapNodes)
//            print("$i ")
//            mapNodes.count { it.key.value == 'O' }.println()
        }
        return mapNodes.count { it.key.value == 'O' }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    check(part1(testInput, 6) == 16)
    check(part2(testInput) == 0)

    val input = readInput("Day21")
    part1(input, 64).println()
    part2(input).println()
}

