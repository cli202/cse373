package graphs.shortestpaths;

import priorityqueues.ExtrinsicMinPQ;
import priorityqueues.NaiveMinPQ;
import graphs.BaseEdge;
import graphs.Graph;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new NaiveMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        Map<V, E> sptMap = new HashMap<>();
        Set<V> visitedVertices = new HashSet<>();
        Map<V, Double> distancesFromStart = new HashMap<>();
        NaiveMinPQ<V> unknownVertices = new NaiveMinPQ<>();
        distancesFromStart.put(start, 0.0);
        unknownVertices.add(start, 0.0);
        while (!visitedVertices.contains(end)) {
            if (unknownVertices.isEmpty()) {
                return null;
            }
            V closestVertex = unknownVertices.removeMin();
            visitedVertices.add(closestVertex);
            for (E edge : graph.outgoingEdgesFrom(closestVertex)) {
                V nextVertex = edge.to();
                if (!distancesFromStart.containsKey(nextVertex)) {
                    distancesFromStart.put(nextVertex, Double.POSITIVE_INFINITY);
                }
                double oldDistance = distancesFromStart.get(nextVertex);
                double newDistance = distancesFromStart.get(closestVertex) + edge.weight();
                if (newDistance < oldDistance) {
                    distancesFromStart.put(nextVertex, newDistance);
                    sptMap.put(nextVertex, edge);
                    unknownVertices.add(nextVertex, newDistance);
                }
            }
        }
        return sptMap;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }

        if (Objects.equals(spt, null)) {
            return new ShortestPath.Failure<>();
        }
        E edge = spt.get(end);
        if (edge == null) {
            return new ShortestPath.Failure<>();
        }

        E mostRecentEdge = spt.get(end);
        V fromMostRecentEdge = mostRecentEdge.from();

        List<E> edges = new ArrayList<>();
        while (!Objects.equals(fromMostRecentEdge, start)) {
            edges.add(mostRecentEdge);
            mostRecentEdge = spt.get(fromMostRecentEdge);
            fromMostRecentEdge = mostRecentEdge.from();
        }

        edges.add(mostRecentEdge);
        // if runtime is bad, FIX THIS
        Collections.reverse(edges);
        return new ShortestPath.Success<>(edges);
    }

}
