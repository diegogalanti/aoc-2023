import kotlin.math.abs



fun main() {
    data class PointForest(val x: Int, val y: Int)
    class Graph {
        val adjacencyList: MutableMap<PointForest, MutableList<PointForest>> = mutableMapOf()

        fun addVertex(vertex: PointForest) {
            adjacencyList[vertex] = mutableListOf()
        }

        fun addEdge(source: PointForest, destination: PointForest) {
            adjacencyList[source]?.add(destination)
            adjacencyList[source]?.sortByDescending {
                abs(it.y - 140) + abs(it.x - 139)
            }
            //adjacencyList[destination]?.add(source) // Assuming an undirected graph
        }

        fun longestPath(startVertex: PointForest, endVertex: PointForest): List<PointForest> {
            val visited = mutableSetOf<PointForest>()
            val visitedMap = mutableMapOf<PointForest, Int>()
            val currentPath = mutableListOf<PointForest>()
            val longestPath = mutableListOf<PointForest>()

            longestPathRecursive(startVertex, endVertex, visited, visitedMap, currentPath, longestPath)

            return longestPath
        }

        private fun longestPathRecursive(
            currentVertex: PointForest,
            endVertex: PointForest,
            visited: MutableSet<PointForest>,
            visitedMap: MutableMap<PointForest, Int>,
            currentPath: MutableList<PointForest>,
            longestPath: MutableList<PointForest>
        ) {
            visited.add(currentVertex)
            currentPath.add(currentVertex)
            if (currentVertex == endVertex && currentPath.size > longestPath.size) {
                longestPath.clear()
                longestPath.addAll(currentPath)
            }

            adjacencyList[currentVertex]?.forEach { neighbor ->
                if (!visited.contains(neighbor)) {
                    longestPathRecursive(neighbor, endVertex, visited, visitedMap, currentPath, longestPath)
                }
            }
            visited.remove(currentVertex)
            currentPath.removeAt(currentPath.size - 1)
        }
    }

    fun part1(input: List<String>): Int {
        val graph = Graph()
        lateinit var startVertex: PointForest
        lateinit var endVertex: PointForest
        val vertexMap = input.mapIndexed { row, line ->
            line.mapIndexed { col, c ->
                val point = PointForest(row, col)
                if (row == 0 && c == '.')
                    startVertex = point
                else if (row == input.size - 1 && c == '.')
                    endVertex = point
                Pair(point, c)
            }
        }

        vertexMap.forEachIndexed { row, line ->
            line.forEachIndexed { col, vertex ->
                if (vertex.second == '#')
                    return@forEachIndexed
                graph.addVertex(vertex.first)
                if (vertex.second == '.') {
                    if (col > 0)
                        graph.addEdge(vertex.first, vertexMap[row][col - 1].first)
                    if (col < vertexMap[0].size - 1)
                        graph.addEdge(vertex.first, vertexMap[row][col + 1].first)
                    if (row < vertexMap.size - 1)
                        graph.addEdge(vertex.first, vertexMap[row + 1][col].first)
                    if (row > 0)
                        graph.addEdge(vertex.first, vertexMap[row - 1][col].first)

                } else if (vertex.second == '<') {
                    graph.addEdge(vertex.first, vertexMap[row][col - 1].first)
                } else if (vertex.second == '>') {
                    graph.addEdge(vertex.first, vertexMap[row][col + 1].first)
                } else if (vertex.second == 'v') {
                    graph.addEdge(vertex.first, vertexMap[row + 1][col].first)
                } else if (vertex.second == '^') {
                    graph.addEdge(vertex.first, vertexMap[row - 1][col].first)
                }
            }
        }
        val longestPath = graph.longestPath(startVertex, endVertex)
        return longestPath.size - 1
    }

    fun part2(input: List<String>): Int {
        val graph = Graph()
        lateinit var startVertex: PointForest
        lateinit var endVertex: PointForest
        val vertexMap = input.mapIndexed { row, line ->
            line.mapIndexed { col, c ->
                val point = PointForest(row, col)
                if (row == 0 && c == '.')
                    startVertex = point
                else if (row == input.size - 1 && c == '.')
                    endVertex = point
                Pair(point, c)
            }
        }

        vertexMap.forEachIndexed { row, line ->
            line.forEachIndexed { col, vertex ->
                if (vertex.second == '#')
                    return@forEachIndexed
                graph.addVertex(vertex.first)
                if (col > 0)
                    graph.addEdge(vertex.first, vertexMap[row][col - 1].first)
                if (col < vertexMap[0].size - 1)
                    graph.addEdge(vertex.first, vertexMap[row][col + 1].first)
                if (row < vertexMap.size - 1)
                    graph.addEdge(vertex.first, vertexMap[row + 1][col].first)
                if (row > 0)
                    graph.addEdge(vertex.first, vertexMap[row - 1][col].first)
            }
        }

        val longestPath = graph.longestPath(startVertex, endVertex)
        return longestPath.size - 1
    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23_test")
    check(part1(testInput) == 94)
    check(part2(testInput) == 154)

    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}
