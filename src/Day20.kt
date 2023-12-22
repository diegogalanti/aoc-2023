import java.util.*

var rxPrevious = mutableMapOf("sv" to 0, "ng" to 0, "jz" to 0, "ft" to 0)

var current = 0
abstract class Pulse(val origin: Module)
class LowPulse(origin: Module) : Pulse(origin)
class HighPulse(origin: Module) : Pulse(origin)

abstract class Module(val id: String, val targets: MutableList<Module>) {
    var lowSent = 0

    var highSent = 0

    val pulseQueue: Queue<Pair<Int, Pulse>> = LinkedList()
    abstract fun sendPulse()

    open fun registerPulse(pulse: Pulse) {
        pulseQueue.add(Pair(lowSent + highSent, pulse))
        if (pulse is LowPulse)
            lowSent++
        else
            highSent++
    }
}

class TestModule(id: String, targets: MutableList<Module>) : Module(id, targets) {
    override fun sendPulse() {
        pulseQueue.clear()
    }
}

class ConjunctionModule(id: String, targets: MutableList<Module>, inputs: List<String>) : Module(id, targets) {

    private val pulseMap = mutableMapOf(*(inputs.map { it to false }.toTypedArray()))

    override fun sendPulse() {
        if (pulseQueue.isEmpty())
            return
        pulseQueue.poll()
        val pulse = if (pulseMap.all { it.value }) LowPulse(this) else HighPulse(this)
        if (pulse is HighPulse && id in listOf("sv", "ng", "jz", "ft"))
            rxPrevious[id] = current
        targets.forEach { it.registerPulse(pulse) }
    }

    override fun registerPulse(pulse: Pulse) {
        pulseMap[pulse.origin.id] = pulse !is LowPulse
        super.registerPulse(pulse)
    }
}

class FlipFlopModule(id: String, targets: MutableList<Module>) : Module(id, targets) {

    private var isOn = false
    override fun sendPulse() {
        if (pulseQueue.isEmpty())
            return
        if (pulseQueue.poll().second is LowPulse) {
            val newPulse = if (isOn) LowPulse(this) else HighPulse(this)
            targets.forEach { it.registerPulse(newPulse) }
            isOn = !isOn
        }
    }
}

class BroadcasterModule(id: String, targets: MutableList<Module>) : Module(id, targets) {
    override fun sendPulse() {
        if (targets.isEmpty())
            return
        val pulse = pulseQueue.poll()
        targets.forEach { it.registerPulse(pulse.second) }
    }
}

class PulseController(private val broadcaster: BroadcasterModule, val modules: List<Module>) {
    fun pushButton() {
        broadcaster.registerPulse(LowPulse(TestModule("aptly", mutableListOf())))
        while (modules.any { it.pulseQueue.isNotEmpty() })
            modules.filter { it.pulseQueue.isNotEmpty() }.minByOrNull { it.pulseQueue.peek().first }!!.sendPulse()
    }
}

fun main() {

    fun parseInput(input: List<String>): PulseController {
        val parsed = input.map {
            val sides = it.split(" -> ")
            val typeId = if (sides[0][0] == '%' || sides[0][0] == '&')
                Pair(sides[0][0], sides[0].substring(1))
            else
                Pair('b', sides[0])
            val targets = sides[1].split(", ")
            Pair(typeId, targets)
        }
        val parsedIds = parsed.map { it.first.second }
        val mapModules = mutableMapOf<String, Pair<Char, List<String>>>()
        parsed.forEach {
            mapModules[it.first.second] = Pair(it.first.first, it.second)
            it.second.forEach { target ->
                if (target !in parsedIds)
                    mapModules[target] = Pair('t', emptyList())
            }
        }
        val modules: List<Module> = mapModules.map { entry ->
            if (entry.value.first == 't')
                TestModule(entry.key, mutableListOf())
            else if (entry.value.first == '%')
                FlipFlopModule(entry.key, mutableListOf())
            else if (entry.value.first == '&')
                ConjunctionModule(
                    entry.key,
                    mutableListOf(),
                    parsed.filter { it.second.contains(entry.key) }.map { it.first.second })
            else
                BroadcasterModule(entry.key, mutableListOf())
        }
        modules.forEach { module ->
            module.targets.addAll(modules.filter {
                it.id in mapModules[module.id]!!.second
            })
        }
        val broadcaster: BroadcasterModule = modules.first { it is BroadcasterModule } as BroadcasterModule
        return PulseController(broadcaster, modules)
    }

    fun part1(input: List<String>): Int {
        val controller = parseInput(input)
        for (i in 0..999)
            controller.pushButton()
        return controller.modules.sumOf { it.lowSent } * controller.modules.sumOf { it.highSent }
    }


    fun part2(input: List<String>): Long {
        val controller = parseInput(input)
        while (!rxPrevious.all { it.value != 0 }) {
            current++
            controller.pushButton()
        }
        return rxPrevious.map { it.value.toLong() }.reduce { acc, i -> acc * i }
    }

    val testInput1 = readInput("Day20_test1")
    check(part1(testInput1) == 32000000)

    val testInput2 = readInput("Day20_test2")
    check(part1(testInput2) == 11687500)

    val input = readInput("Day20")
    part1(input).println()
    part2(input).println()
}
