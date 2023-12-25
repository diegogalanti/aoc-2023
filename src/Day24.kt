import java.awt.geom.Line2D
import java.awt.geom.Point2D

fun intersection(a: Line2D, b: Line2D): Point2D? {
    val x1 = a.x1
    val y1 = a.y1
    val x2 = a.x2
    val y2 = a.y2
    val x3 = b.x1
    val y3 = b.y1
    val x4 = b.x2
    val y4 = b.y2
    val d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)
    if (d == 0.0) {
        return null
    }
    val xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d
    val yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d
    return Point2D.Double(xi, yi)
}
fun main() {

    fun part1(input: List<String>, boundLow: Long, boundHigh: Long): Int {
        val parsed = input.map { line ->
            val (posS, speedS) = line.split('@')
            val (xPos, yPos) = posS.split(", ").map { it.toDouble() }
            val (xSpeed, ySpeed) = speedS.split(", ").map { it.toDouble() }
            Pair(Point2D.Double(xPos, yPos), Point2D.Double(xSpeed, ySpeed))
        }
        val lines = parsed.map {
            val xSteps: Double
            val ySteps: Double
            xSteps = if (it.second.x < 0) {
                 (it.first.x - boundLow) / (it.second.x * -1)
            } else {
                (boundHigh - it.first.x) / it.second.x
            }
            ySteps = if (it.second.y < 0) {
                (it.first.y - boundLow) / (it.second.y * -1)
            } else {
                (boundHigh - it.first.y) / it.second.y
            }
            var minSteps = minOf(xSteps, ySteps)
            if (minSteps < 0)
                minSteps = 0.0
            Line2D.Double(it.first, Point2D.Double(it.first.x + (minSteps * it.second.x), it.first.y + (minSteps * it.second.y)))
        }
        lines.forEach {
            println("Line = ${it.x1},${it.y1}~${it.x2},${it.y2} ")
        }
        var result = 0
        for (i in lines.indices) {
            for (j in (i + 1)..<lines.size) {
                val inter = intersection(lines[i], lines[j])
                if (inter != null && inter.x >= boundLow && inter.x <= boundHigh && inter.y >= boundLow && inter.y <= boundHigh) {
                    if (lines[i].intersectsLine(lines[j])) {
                        println(inter)
                        result++
                    }
                }
            }
        }
        println("result = $result")
        return result
    }

    fun part2(input: List<String>): Int {
        return 0
    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day24_test")
    check(part1(testInput, 7L, 27L) == 2)
    check(part2(testInput) == 0)

    val input = readInput("Day24")
    part1(input, 200000000000000, 400000000000000).println()
    part2(input).println()
}
