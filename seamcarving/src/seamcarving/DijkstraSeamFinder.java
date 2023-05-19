package seamcarving;

import graphs.Edge;
import graphs.Graph;
import graphs.shortestpaths.DijkstraShortestPathFinder;
import graphs.shortestpaths.ShortestPath;
import graphs.shortestpaths.ShortestPathFinder;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DijkstraSeamFinder implements SeamFinder {
    private final ShortestPathFinder<Graph<Vertex, Edge<Vertex>>, Vertex, Edge<Vertex>> pathFinder;

    public DijkstraSeamFinder() {
        this.pathFinder = createPathFinder();
    }

    protected <G extends Graph<V, Edge<V>>, V> ShortestPathFinder<G, V, Edge<V>> createPathFinder() {
        /*
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
        */
        return new DijkstraShortestPathFinder<>();
    }

    @Override
    public List<Integer> findVerticalSeam(double[][] energies) {
        GrafH g = new GrafH(energies);
        ShortestPath<Vertex, Edge<Vertex>> spt = pathFinder.findShortestPath(g, g.dummyStart, g.dummyEnd);
        List<Integer> result = new ArrayList<>();
        for (Vertex v : spt.vertices()) {
            result.add((int) v.point.getY());
        }
        result.remove(0);
        result.remove(result.size() - 1);
        return result;
    }

    @Override
    public List<Integer> findHorizontalSeam(double[][] energies) {
        GrafV g = new GrafV(energies);
        ShortestPath<Vertex, Edge<Vertex>> spt = pathFinder.findShortestPath(g, g.dummyStart, g.dummyEnd);
        List<Integer> result = new ArrayList<>();
        for (Vertex v : spt.vertices()) {
            result.add((int) v.point.getX());
        }
        result.remove(0);
        result.remove(result.size() - 1);
        return result;
    }

    private class Vertex {
        private Point point;
        private double energy;

        public Vertex(Point point, double energy) {
            this.point = point;
            this.energy = energy;
        }
    }

    private class GrafV implements Graph<Vertex, Edge<Vertex>> {
        // Set<Vertex> vertices;
        Vertex[][] vertices;
        Map<Vertex, Set<Edge<Vertex>>> edgesTo;

        public Vertex dummyStart;
        public Vertex dummyEnd;

        public GrafV(double[][] array) {
            edgesTo = new HashMap<>();
            vertices = new Vertex[array.length][array[0].length];
            dummyStart = new Vertex(new Point(-1, -1), 0);
            dummyEnd = new Vertex(new Point(-1, -1), 0);
            edgesTo.put(dummyStart, new HashSet<>());
            int xLast = array[1].length - 1;
            int yLast = array.length - 1;

            for (int y = 0; y < array.length; y++) {
                for (int x = 0; x < array[y].length; x++) {
                    vertices[y][x] = new Vertex(new Point(x, y), array[y][x]);
                    if (y == 0) {
                        edgesTo.get(dummyStart).add(new Edge<>(dummyStart, vertices[y][x], vertices[y][x].energy));
                    }
                }
            }

            for (int y = 0; y < array.length; y++) {
                for (int x = 0; x < array[y].length; x++) {
                    Vertex self = vertices[y][x];
                    // if (!(y == 0 || x == xLast)) {
                    //     if (!edgesTo.containsKey(self)) {
                    //         edgesTo.put(self, new HashSet<>());
                    //     }
                    //     edgesTo.get(self).add(new Edge<>(self, vertices[x + 1][y - 1], self.energy));
                    // }
                    // if (x != xLast) {
                    //     if (!edgesTo.containsKey(self)) {
                    //         edgesTo.put(self, new HashSet<>());
                    //     }
                    //     edgesTo.get(self).add(new Edge<>(self, vertices[x + 1][y], self.energy));
                    // }
                    if (!(x == xLast || y == yLast)) {
                        if (!edgesTo.containsKey(self)) {
                            edgesTo.put(self, new HashSet<>());
                        }
                        edgesTo.get(self).add(new Edge<>(self, vertices[y + 1][x + 1], vertices[y + 1][x + 1].energy));
                    }
                    if (!(y == yLast)) {
                        if (!edgesTo.containsKey(self)) {
                            edgesTo.put(self, new HashSet<>());
                        }
                        edgesTo.get(self).add(new Edge<>(self, vertices[y + 1][x], vertices[y + 1][x].energy));
                    }
                    if (!(x == 0 || y == yLast)) {
                        if (!edgesTo.containsKey(self)) {
                            edgesTo.put(self, new HashSet<>());
                        }
                        edgesTo.get(self).add(new Edge<>(self, vertices[y + 1][x - 1], vertices[y + 1][x - 1].energy));
                    }
                    if (y == yLast) {
                        edgesTo.put(self, new HashSet<>());
                        edgesTo.get(self).add(new Edge<>(self, dummyEnd, 0));
                    }
                }
            }
        }

        public Set<Edge<Vertex>> outgoingEdgesFrom(Vertex vertex) {
            return edgesTo.get(vertex);
        }

    }

    private class GrafH implements Graph<Vertex, Edge<Vertex>> {
        // Set<Vertex> vertices;
        Vertex[][] vertices;
        Map<Vertex, Set<Edge<Vertex>>> edgesTo;

        public Vertex dummyStart;
        public Vertex dummyEnd;

        public GrafH(double[][] array) {
            edgesTo = new HashMap<>();
            vertices = new Vertex[array.length][array[0].length];
            dummyStart = new Vertex(new Point(-1, -1), 0);
            dummyEnd = new Vertex(new Point(-1, -1), 0);
            int xLast = array[0].length - 1;
            int yLast = array.length - 1;

            edgesTo.put(dummyStart, new HashSet<>());
            for (int y = 0; y < array.length; y++) {
                for (int x = 0; x < array[y].length; x++) {
                    vertices[y][x] = new Vertex(new Point(x, y), array[y][x]);
                    if (x == 0) {
                        edgesTo.get(dummyStart).add(new Edge<>(dummyStart, vertices[y][x], vertices[y][x].energy));
                    }
                }
            }

            for (int y = 0; y < array.length; y++) {
                for (int x = 0; x < array[y].length; x++) {
                    Vertex self = vertices[y][x];
                    if (!(y == 0 || x == xLast)) {
                        if (!edgesTo.containsKey(self)) {
                            edgesTo.put(self, new HashSet<>());
                        }
                        edgesTo.get(self).add(new Edge<>(self, vertices[y - 1][x + 1],
                            vertices[y - 1][x + 1].energy));
                    }
                    if (x != xLast) {
                        if (!edgesTo.containsKey(self)) {
                            edgesTo.put(self, new HashSet<>());
                        }
                        edgesTo.get(self).add(new Edge<>(self, vertices[y][x + 1], vertices[y][x + 1].energy));
                    }
                    if (!(x == xLast || y == yLast)) {
                        if (!edgesTo.containsKey(self)) {
                            edgesTo.put(self, new HashSet<>());
                        }
                        edgesTo.get(self).add(new Edge<>(self, vertices[y + 1][x + 1],
                            vertices[y + 1][x + 1].energy));
                    }
                    // if (!(y == yLast)) {
                    //     if (!edgesTo.containsKey(self)) {
                    //         edgesTo.put(self, new HashSet<>());
                    //     }
                    //     edgesTo.get(self).add(new Edge<>(self, vertices[x][y + 1], self.energy));
                    // }
                    // if (!(x == 0 || y == yLast)) {
                    //     if (!edgesTo.containsKey(self)) {
                    //         edgesTo.put(self, new HashSet<>());
                    //     }
                    //     edgesTo.get(self).add(new Edge<>(self, vertices[x - 1][y + 1], self.energy));
                    // }
                    if (x == xLast) {
                        edgesTo.put(self, new HashSet<>());
                        edgesTo.get(self).add(new Edge<>(self, dummyEnd, 0));
                    }
                }
            }
        }

        public Set<Edge<Vertex>> outgoingEdgesFrom(Vertex vertex) {
            return edgesTo.get(vertex);
        }
    }
}
