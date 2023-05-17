package seamcarving;

import graphs.Edge;
import graphs.EdgeWithData;
import graphs.Graph;
import graphs.shortestpaths.DijkstraShortestPathFinder;
import graphs.shortestpaths.ShortestPathFinder;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DijkstraSeamFinder implements SeamFinder {
    // TODO: replace all 4 references to "Object" on the line below with whatever vertex type
    //  you choose for your graph
    private final ShortestPathFinder<Graph<Double, Edge<Double>>, Double, Edge<Double>> pathFinder;

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
    public List<Integer> findHorizontalSeam(double[][] energies) {
        // TODO: replace this with your code
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public List<Integer> findVerticalSeam(double[][] energies) {
        // TODO: replace this with your code
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    private class Vertex {
        private Point point;
        private double energy;

        public Vertex(Point point, double energy) {
            this.point = point;
            this.energy = energy;
        }
    }

    private class Graf implements Graph<Vertex, Edge<Vertex>> {
        // Set<Vertex> vertices;
        Vertex[][] vertices;
        Map<Vertex, Set<Edge<Vertex>>> edgesTo;

        public Graf(double[][] array) {
            edgesTo = new HashMap<>();
            vertices = new Vertex[array.length][array[0].length];
            for (int y = 0; y < array.length; y++) {
                for (int x = 0; x < array[y].length; x++) {
                    vertices[x][y] = new Vertex(new Point(x, y), array[x][y]);
                }
            }
            int xLast = array[1].length - 1;
            int yLast = array.length - 1;
            for (int y = 0; y < array.length; y++) {
                for (int x = 0; x < array[y].length; x++) {
                    Vertex self = vertices[x][y];
                    if (!(y == 0 || x == xLast)) {
                        if (!edgesTo.containsKey(self)) {
                            edgesTo.put(self, new HashSet<>());
                        }
                        edgesTo.get(self).add(new Edge<>(self, vertices[x + 1][y - 1], self.energy));
                    }
                    if (x != xLast) {
                        if (!edgesTo.containsKey(self)) {
                            edgesTo.put(self, new HashSet<>());
                        }
                        edgesTo.get(self).add(new Edge<>(self, vertices[x + 1][y], self.energy));
                    }
                    if (!(x == xLast || y == yLast)) {
                        if (!edgesTo.containsKey(self)) {
                            edgesTo.put(self, new HashSet<>());
                        }
                        edgesTo.get(self).add(new Edge<>(self, vertices[x + 1][y + 1], self.energy));
                    }
                    if (!(y == yLast)) {
                        if (!edgesTo.containsKey(self)) {
                            edgesTo.put(self, new HashSet<>());
                        }
                        edgesTo.get(self).add(new Edge<>(self, vertices[x][y + 1], self.energy));
                    }
                    if (!(x == 0 || y == yLast)) {
                        if (!edgesTo.containsKey(self)) {
                            edgesTo.put(self, new HashSet<>());
                        }
                        edgesTo.get(self).add(new Edge<>(self, vertices[x - 1][y + 1], self.energy));
                    }
                }
            }
        }

        public Set<Edge<Vertex>> outgoingEdgesFrom(Vertex vertex) {
            return edgesTo.get(vertex);
        }
    }
}
