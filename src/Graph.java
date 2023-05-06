import java.util.*;

public class Graph<T> {
    private Set<Vertex<T>> vertices;
    private Set<Edge<T>> edges;

    public class Vertex<T> {
        private final T value;

        public Vertex(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vertex<?> vertex = (Vertex<?>) o;
            return Objects.equals(value, vertex.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    public class Edge<T> {
        private final Vertex<T> source;
        private final Vertex<T> destination;
        private final double weight;

        public Edge(Vertex<T> source, Vertex<T> destination, double weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        public Vertex<T> getSource() {
            return source;
        }

        public Vertex<T> getDestination() {
            return destination;
        }

        public double getWeight() {
            return weight;
        }
    }

    public Graph() {
        this.vertices = new HashSet<>();
        this.edges = new HashSet<>();
    }

    public void addVertex(Vertex<T> vertex) {
        vertices.add(vertex);
    }

    public void addEdge(Vertex<T> source, Vertex<T> destination, double weight) {
        Edge<T> edge = new Edge<>(source, destination, weight);
        edges.add(edge);
    }

    public List<Vertex<T>> getNeighbors(Vertex<T> vertex) {
        List<Vertex<T>> neighbors = new ArrayList<>();
        for (Edge<T> edge : edges) {
            if (edge.getSource().equals(vertex)) {
                neighbors.add(edge.getDestination());
            }
        }
        return neighbors;
    }

    public List<Vertex<T>> breadthFirstSearch(Vertex<T> start) {
        List<Vertex<T>> visited = new ArrayList<>();
        Queue<Vertex<T>> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);
        while (!queue.isEmpty()) {
            Vertex<T> vertex = queue.poll();
            for (Vertex<T> neighbor : getNeighbors(vertex)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return visited;
    }

    public List<Vertex<T>> depthFirstSearch(Vertex<T> start) {
        List<Vertex<T>> visited = new ArrayList<>();
        Stack<Vertex<T>> stack = new Stack<>();
        stack.push(start);
        while (!stack.isEmpty()) {
            Vertex<T> vertex = stack.pop();
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                for (Vertex<T> neighbor : getNeighbors(vertex)) {
                    stack.push(neighbor);
                }
            }
        }
        return visited;
    }
}