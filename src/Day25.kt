import java.util.*


fun main() {
    data class Node(val name: String) {
        var parent: Node? = null
    }

    class Graph {
        val adjacencyList: MutableMap<Node, MutableSet<Node>> = mutableMapOf()

        fun addVertex(vertex: Node) {
            adjacencyList[vertex] = mutableSetOf()
        }

        fun addEdge(source: Node, destination: Node) {
            adjacencyList[source]?.add(destination)
            adjacencyList[destination]?.add(source) // Assuming an undirected graph
        }

        fun countPath(startVertex: Node, endVertex: Node): Map<Node, Int> {
            val visited = mutableSetOf<Node>()
            startVertex.parent = null
            var count = 0
            val queue: Queue<Node> = LinkedList()
            val visitedMap = mutableMapOf<Node, Int>()
            queue.add(startVertex)
            while (queue.isNotEmpty()) {
                val currentVertex = queue.poll()
                if (currentVertex == endVertex) {
                    var curr = currentVertex
                    while (curr != null) {
                        visitedMap[curr] = if (visitedMap[curr] == null) 0 else visitedMap[curr]!! + 1
                        curr = curr.parent
                    }
                    count++
                }
                visited.add(currentVertex)
                adjacencyList[currentVertex]?.forEach { neighbor ->
                    if (!visited.contains(neighbor)) {
                        neighbor.parent = currentVertex
                        queue.add(neighbor)
                    }
                }
            }
            if (count == 3) {
                return visitedMap
            }
            return emptyMap()
        }

        fun countGraphNodes(first: Node): Int {
            val visited = mutableSetOf<Node>()
            val queue: Queue<Node> = LinkedList()
            queue.add(first)
            while (queue.isNotEmpty()) {
                val currentVertex = queue.poll()
                visited.add(currentVertex)
                adjacencyList[currentVertex]?.forEach { neighbor ->
                    if (!visited.contains(neighbor)) {
                        queue.add(neighbor)
                    }
                }
            }
            return visited.size
        }

        fun disconnect(first: Node, second: Node) {
            adjacencyList[first]?.remove(second)
            adjacencyList[second]?.remove(first)
        }
    }

    fun part1(input: List<String>): Int {
        val graph = Graph()
        val nodes = input.map { line ->
            val (origin, sourcesS) = line.split(": ")
            val sourcesSL = sourcesS.split(" ")
            val sources = sourcesSL.map { Node(it) }
            Pair(Node(origin), sources)
        }
        nodes.forEach { nodePair ->
            graph.addVertex(nodePair.first)
            nodePair.second.forEach {
                graph.addVertex(it)
            }
        }
        nodes.forEach { nodePair ->
            nodePair.second.forEach {
                graph.addEdge(nodePair.first, it)
            }
        }
        val countOcurrence = mutableMapOf<Node, Int>()
        for (i in nodes.indices) {
            for (j in i + 1..<nodes.size) {
                val result = graph.countPath(nodes[i].first, nodes[j].first)
                result.forEach {
                    countOcurrence[it.key] =
                        if (countOcurrence[it.key] == null) 0 else countOcurrence[it.key]!! + it.value
                }
            }
            println(countOcurrence.maxBy { it.value }.value)
            if (countOcurrence.maxBy { it.value }.value > 2000) //2000 is enough
                break
        }
        val occurrencesSorted = countOcurrence.toList().sortedByDescending { it.second }
        graph.disconnect(occurrencesSorted[0].first, occurrencesSorted[1].first)
        graph.disconnect(occurrencesSorted[2].first, occurrencesSorted[3].first)
        graph.disconnect(occurrencesSorted[4].first, occurrencesSorted[5].first)

        return graph.countGraphNodes(nodes.first { it.first.name == occurrencesSorted[0].first.name }.first) * graph.countGraphNodes(nodes.first { it.first.name == occurrencesSorted[1].first.name }.first)
    }

    val input = readInput("Day25")
    part1(input).println()
}
