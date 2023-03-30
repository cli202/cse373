package problems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * See the spec on the website for example behavior.
 */
public class MapProblems {

    /**
     * Returns true if any string appears at least 3 times in the given list; false otherwise.
     */
    public static boolean contains3(List<String> list) {
        Map<String, Integer> counter = new HashMap<>();
        for (String s : list) {
            if (!counter.containsKey(s)) {
                counter.put(s, 0);
            }
            counter.put(s, counter.get(s) + 1);
            if (counter.get(s) == 3) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a map containing the intersection of the two input maps.
     * A key-value pair exists in the output iff the same key-value pair exists in both input maps.
     */
    public static Map<String, Integer> intersect(Map<String, Integer> m1, Map<String, Integer> m2) {
        Map<String, Integer> result = new HashMap<>();
        for (String s1 : m1.keySet()) {
            for (String s2 : m2.keySet()) {
                boolean keyMatch = s1.equals(s2);
                boolean valueMatch = m1.get(s1).equals(m2.get(s2));
                if (keyMatch && valueMatch) {
                    result.put(s1, m1.get(s1));
                }
            }
        }
        return result;
    }
}
