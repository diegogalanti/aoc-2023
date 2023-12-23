fun main() {
    data class GardenNode(val row: Int, val col: Int, var value: Char)

    fun lagrangeInterpolation(xValues: List<Int>, yValues: List<Int>, xInput: Int): Long {
        val n = xValues.size
        var yOutput = 0L

        for (i in 0 until n) {
            var term = yValues[i]
            for (j in 0 until n) {
                if (j != i) {
                    term *= (xInput - xValues[j]) / (xValues[i] - xValues[j])
                }
            }
            yOutput += term
        }
        return yOutput
    }

    fun executeStepArray(map : Array<Array<GardenNode>>, steps : List<GardenNode> ) : List<GardenNode>{
        val outList = mutableSetOf<GardenNode>()
        for (step in steps) {
            if (step.row > 0 && map[step.row - 1][step.col].value != '#')
                outList.add(map[step.row - 1][step.col])
            if (step.col > 0 && map[step.row][step.col - 1].value != '#')
                outList.add(map[step.row][step.col - 1])
            if (step.row < map.size - 1 && map[step.row + 1][step.col].value != '#')
                outList.add(map[step.row + 1][step.col])
            if (step.col < map[0].size - 1 && map[step.row][step.col + 1].value != '#')
                outList.add(map[step.row][step.col + 1])
        }
        return outList.toList()
    }

    fun part1(input: List<String>, steps: Int): Int {
        lateinit var initial : GardenNode
        val map = input.mapIndexed { row, line ->
            line.toCharArray().mapIndexed { col, c ->
                if (c == 'S') {
                    initial = GardenNode(row, col, 'O')
                    initial
                }
                else
                    GardenNode(row, col, c)
            }.toTypedArray()
        }.toTypedArray()
        var list = listOf(initial)
        for (i in 1..steps)
            list = executeStepArray(map, list)
        return list.count()
    }

    fun part2(input: List<String>, steps: Int): Long {
        lateinit var initial : GardenNode
        val inputTriple = input.map { line ->
            val replaced = line.replace('S', '.')
            replaced + replaced + replaced + replaced + replaced
        } + input.map { line ->
            val replaced = line.replace('S', '.')
            replaced + replaced + replaced + replaced + replaced
        } + input.map { line ->
            val replaced = line.replace('S', '.')
            replaced + replaced + line + replaced + replaced
        } + input.map { line ->
            val replaced = line.replace('S', '.')
            replaced + replaced + replaced + replaced + replaced
        } + input.map { line ->
            val replaced = line.replace('S', '.')
            replaced + replaced + replaced + replaced + replaced
        }
        val map = inputTriple.mapIndexed { row, line ->
            line.toCharArray().mapIndexed { col, c ->
                if (c == 'S') {
                    initial = GardenNode(row, col, 'O')
                    initial
                }
                else
                    GardenNode(row, col, c)
            }.toTypedArray()
        }.toTypedArray()
        val xList = listOf(65, 196, 327)
        val yList = mutableListOf<Int>()
        var list = listOf(initial)
        for (i in 1..65)
            list = executeStepArray(map, list)
        yList.add(list.count())
        for (i in 66..196)
            list = executeStepArray(map, list)
        yList.add(list.count())
        for (i in 197..327)
            list = executeStepArray(map, list)
        yList.add(list.count())
        return  lagrangeInterpolation(xList, yList, steps)
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    check(part1(testInput, 6) == 16)

    val input = readInput("Day21")
    part1(input, 64).println()
    part2(input, 26501365).println()
}
