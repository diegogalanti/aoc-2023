import java.util.HashSet
import java.util.LinkedList
import java.util.Queue
import kotlin.system.measureTimeMillis

enum class Direction {
    LEFT,
    RIGHT,
    UP,
    DOWN,
    SPLIT_LR,
    SPLIT_UD
}

fun main() {
    data class Cell(val type: Char, var energized: Boolean = false)

    data class Beam(var direction: Direction, var posX: Int, var posY: Int) {
        fun adjustDirection(cell: Cell) {
            direction = when (cell.type) {
                '-' -> when (direction) {
                    Direction.DOWN -> Direction.SPLIT_LR
                    Direction.LEFT -> direction
                    Direction.RIGHT -> direction
                    Direction.UP -> Direction.SPLIT_LR
                    else -> direction
                }
                '|' -> when (direction) {
                    Direction.DOWN -> direction
                    Direction.LEFT -> Direction.SPLIT_UD
                    Direction.RIGHT -> Direction.SPLIT_UD
                    Direction.UP -> direction
                    else -> direction
                }
                '\\' -> when (direction) {
                    Direction.DOWN -> Direction.RIGHT
                    Direction.LEFT -> Direction.UP
                    Direction.RIGHT -> Direction.DOWN
                    Direction.UP -> Direction.LEFT
                    else -> direction
                }
                '/' -> when (direction) {
                    Direction.DOWN -> Direction.LEFT
                    Direction.LEFT -> Direction.DOWN
                    Direction.RIGHT -> Direction.UP
                    Direction.UP -> Direction.RIGHT
                    else -> direction
                }
                else -> direction
            }
        }
    }

    fun part1(input: List<String>, dir : Direction, posY : Int, posX : Int): Int {
        val cavern = input.map { line ->
            line.toCharArray().map { Cell(it) }
        }
        val beamQueue: Queue<Beam> = LinkedList()
        beamQueue.add(Beam(dir, posX, posY))
        val setDirectionPos = HashSet<Triple<Direction, Int, Int>>()
        while (beamQueue.isNotEmpty()) {
            val beam = beamQueue.first()
            if (setDirectionPos.contains(Triple(beam.direction, beam.posX, beam.posY)) || beam.posY < 0 || beam.posX < 0 || beam.posX >= cavern[0].size || beam.posY >= cavern.size) {
                beamQueue.remove(beam)
                continue
            }
            setDirectionPos.add(Triple(beam.direction, beam.posX, beam.posY))
            cavern[beam.posY][beam.posX].energized = true
            beam.adjustDirection(cavern[beam.posY][beam.posX])
            when (beam.direction) {
                Direction.DOWN -> beam.posY++
                Direction.UP -> beam.posY--
                Direction.RIGHT -> beam.posX++
                Direction.LEFT -> beam.posX--
                Direction.SPLIT_LR -> {
                    beamQueue.remove(beam)
                    beamQueue.add(Beam(Direction.LEFT, beam.posX - 1, beam.posY))
                    beamQueue.add(Beam(Direction.RIGHT, beam.posX + 1, beam.posY))
                }
                Direction.SPLIT_UD -> {
                    beamQueue.remove(beam)
                    beamQueue.add(Beam(Direction.UP, beam.posX, beam.posY - 1))
                    beamQueue.add(Beam(Direction.DOWN, beam.posX, beam.posY + 1))
                }
            }
        }
        return cavern.sumOf { line -> line.count{ it.energized }}
    }

    fun part2(input: List<String>): Int {
        val possibilities = mutableListOf<Int>()
        for (i in input.indices) {
            possibilities.add(part1(input, Direction.RIGHT, i, 0))
            possibilities.add(part1(input, Direction.LEFT, i, input[i].length - 1))
        }
        for (i in input[0].indices) {
            possibilities.add(part1(input, Direction.DOWN, 0, i))
            possibilities.add(part1(input, Direction.UP, 0, input.size - 1))
        }
        return possibilities.max()
    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput, Direction.RIGHT, 0, 0) == 46)
    check(part2(testInput) == 51)

    val input = readInput("Day16")
    println("Results: part 1 = ${part1(input, Direction.RIGHT, 0, 0)} | part 2 = ${part2(input)}")
    println("Time to run: part 1 = ${measureTimeMillis { part1(input, Direction.RIGHT, 0, 0) }}ms | part 2 = ${measureTimeMillis { part2(input) }}ms")
}
