package problems;

import datastructures.IntTree;
// Checkstyle will complain that this is an unused import until you use it in your code.
import datastructures.IntTree.IntTreeNode;

/**
 * See the spec on the website for tips and example behavior.
 * Also note: you may want to use private helper methods to help you solve these problems.
 * YOU MUST MAKE THE PRIVATE HELPER METHODS STATIC, or else your code will not compile.
 * This happens for reasons that aren't the focus of this assignment and are mostly skimmed over in
 * 142 and 143. If you want to know more, you can ask on the discussion board or at office hours.
 * REMEMBER THE FOLLOWING RESTRICTIONS:
 * - do not call any methods on the `IntTree` objects
 * - do not construct new `IntTreeNode` objects (though you may have as many `IntTreeNode` variables
 *      as you like).
 * - do not construct any external data structures such as arrays, queues, lists, etc.
 * - do not mutate the `data` field of any node; instead, change the tree only by modifying
 *      links between nodes.
 */

public class IntTreeProblems {

    /**
     * Computes and returns the sum of the values multiplied by their depths in the given tree.
     * (The root node is treated as having depth 1.)
     */
    public static int depthSum(IntTree tree) {
        return depthSumHelper(1, tree.overallRoot);
    }

    private static int depthSumHelper(int depth, IntTreeNode node) {
        if (node == null) {
            return 0;
        }
        int myValue = node.data * depth;
        int myLeftSum = depthSumHelper(depth + 1, node.left);
        int myRightSum = depthSumHelper(depth + 1, node.right);
        return myValue + myLeftSum + myRightSum;
    }

    /**
     * Removes all leaf nodes from the given tree.
     */
    public static void removeLeaves(IntTree tree) {
        if (isLeaf(tree.overallRoot)) {
            tree.overallRoot = null;
        } else {
            removeLeavesHelper(tree.overallRoot);
        }
    }

    private static void removeLeavesHelper(IntTreeNode node) {
        if (node != null) {
            if (isLeaf(node.left)) {
                node.left = null;
            } else {
                removeLeavesHelper(node.left);
            }
            if (isLeaf(node.right)) {
                node.right = null;
            } else {
                removeLeavesHelper(node.right);
            }
        }
    }

    private static boolean isLeaf(IntTreeNode node) {
        return node == null || (node.left == null && node.right == null);
    }

    /**
     * Removes from the given BST all values less than `min` or greater than `max`.
     * (The resulting tree is still a BST.)
     */
    public static void trim(IntTree tree, int min, int max) {
        tree.overallRoot = trimChildren(tree.overallRoot, min, max);
    }

    private static IntTreeNode trimChildren(IntTreeNode node, int min, int max) {
        if (node != null) {
            node.left = trimChildren(node.left, min, max);
            node.right = trimChildren(node.right, min, max);
            if (node.data < min) {
                node = node.right;
            } else if (node.data > max) {
                node = node.left;
            }
        }
        return node;
    }
}
