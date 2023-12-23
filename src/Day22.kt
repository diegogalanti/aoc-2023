fun main() {
    data class Brick(
        val xRange: IntRange,
        val yRange: IntRange,
        var zRange: IntRange,
        var canShoot: Boolean = false,
        var felt: Boolean = false
    )

    fun IntRange.intersect(otherRange: IntRange): Boolean {
        return otherRange.any { it in this }
    }

    fun getBricksUnder(brick: Brick, bricks: List<Brick>): List<Brick> {
        return bricks.filter { current ->
            current.zRange.last == brick.zRange.first - 1 && brick.xRange.intersect(current.xRange) && brick.yRange.intersect(
                current.yRange
            )
        }
    }

    fun getBricksAbove(brick: Brick, bricks: List<Brick>): List<Brick> {
        return bricks.filter { current ->
            current.zRange.first == brick.zRange.last + 1 && brick.xRange.intersect(current.xRange) && brick.yRange.intersect(
                current.yRange
            )
        }
    }

    fun part1(input: List<String>): Int {
        val bricks = input.map { line ->
            val (low, high) = line.split('~')
            val lowDigits = low.split(',').map { it.toInt() }
            val highDigits = high.split(',').map { it.toInt() }
            Brick(lowDigits[0]..highDigits[0], lowDigits[1]..highDigits[1], lowDigits[2]..highDigits[2])
        }.sortedBy { it.zRange.first }
        var found = true
        while (found) {
            found = false
            for (brick in bricks) {
                if (brick.zRange.first == 1)
                    continue
                else {
                    val under = getBricksUnder(brick, bricks)
                    if (under.isEmpty()) {
                        found = true
                        brick.zRange = brick.zRange.first - 1..<brick.zRange.last
                    }
                }
            }
        }
        for (brick in bricks) {
            val aboves = getBricksAbove(brick, bricks)
            if (aboves.isEmpty())
                brick.canShoot = true
            else {
                val allAboveHaveOtherDown = aboves.all { above ->
                    getBricksUnder(above, bricks).size > 1
                }
                if (allAboveHaveOtherDown)
                    brick.canShoot = true
            }
        }
        return bricks.count { it.canShoot }
    }

    fun chainDrop(toDrop: List<Brick>, allBricks: List<Brick>) {
        toDrop.forEach { it.felt = true }
        val aboveDropped = mutableSetOf<Brick>()
        toDrop.forEach { dropped ->
            aboveDropped.addAll(getBricksAbove(dropped, allBricks))
        }
        val list = aboveDropped.filter { a ->
            val under = getBricksUnder(a, allBricks)
            under.all {
                it.felt
            }
        }
        if (list.isNotEmpty())
            chainDrop(list, allBricks)
    }

    fun part2(input: List<String>): Int {
        var bricks = input.map { line ->
            val (low, high) = line.split('~')
            val lowDigits = low.split(',').map { it.toInt() }
            val highDigits = high.split(',').map { it.toInt() }
            Brick(lowDigits[0]..highDigits[0], lowDigits[1]..highDigits[1], lowDigits[2]..highDigits[2])
        }.sortedBy { it.zRange.first }
        var found = true
        while (found) {
            found = false
            for (brick in bricks) {
                if (brick.zRange.first == 1)
                    continue
                else {
                    val under = getBricksUnder(brick, bricks)
                    if (under.isEmpty()) {
                        found = true
                        brick.zRange = brick.zRange.first - 1..<brick.zRange.last
                    }
                }
            }
        }
        for (brick in bricks) {
            val aboves = getBricksAbove(brick, bricks)
            if (aboves.isEmpty())
                brick.canShoot = true
            else {
                val allAboveHaveOtherDown = aboves.all { above ->
                    getBricksUnder(above, bricks).size > 1
                }
                if (allAboveHaveOtherDown)
                    brick.canShoot = true
            }
        }
        bricks = bricks.sortedBy { it.zRange.first }
        var result = 0
        for (i in bricks.indices) {
            if (bricks[i].canShoot)
                continue
            chainDrop(getBricksAbove(bricks[i], bricks).filter { getBricksUnder(it,bricks).size == 1 }, bricks)
            result += bricks.count { it.felt }
            bricks.forEach { it.felt = false }
        }
        return result
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 7)

    val input = readInput("Day22")
    part1(input).println()
    part2(input).println()
}
