package priorityqueues;

import edu.princeton.cs.algs4.ST;

import java.util.ArrayList;
import java.util.List;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 1;

    List<PriorityNode<T>> items;
    int size;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        size = 0;
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.

    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> temp = items.get(a);
        items.set(a, items.get(b));
        items.set(b, temp);
    }

    private int leftChildIndex(int index) {
        return 2 * index + 1 - START_INDEX;
    }

    private int rightChildIndex(int index) {
        return 2 * index + 2 - START_INDEX;
    }

    private int parentIndex(int index) {
        if (index == 0) {
            return -1;
        }
        return (index - 1 + START_INDEX) / 2;
    }

    private void percolateUp(PriorityNode<T> node, int currIndex) {
        int parentIndex = parentIndex(currIndex);
        if (parentIndex != -1) {
            PriorityNode<T> parentNode = items.get(parentIndex);
            double myPriority = node.getPriority();
            double parentPriority = parentNode.getPriority();
            boolean needsPercolating = myPriority < parentPriority;
            if (needsPercolating) {
                swap(currIndex, parentIndex);
                percolateUp(node, parentIndex);
            }
        }
    }

    private void percolateDown(PriorityNode<T> node, int currIndex) {
        int leftChildIndex = leftChildIndex(currIndex);
        int rightChildIndex = rightChildIndex(currIndex);

        int lastIndex = size + START_INDEX;
        boolean hasLeftChild = leftChildIndex <= lastIndex;
        boolean hasRightChild = rightChildIndex <= lastIndex;

        if (!hasLeftChild && !hasRightChild) {
            return;
        }

        PriorityNode<T> leftChild = null;
        PriorityNode<T> rightChild = null;

        if (hasLeftChild) {
            leftChild = items.get(leftChildIndex);
        }
        if (hasRightChild) {
            rightChild = items.get(rightChildIndex);
        }

        PriorityNode<T> smallerChild = null;
        if (rightChild == null || leftChild.getPriority() <= rightChild.getPriority()) {
            double priorityDiff = node.getPriority() - leftChild.getPriority();
            if (priorityDiff > 0) {
                swap(currIndex, leftChildIndex);
                percolateDown(node, leftChildIndex);
            }
        } else {
            double priorityDiff = node.getPriority() - rightChild.getPriority();
            if (priorityDiff > 0) {
                swap(currIndex, rightChildIndex);
                percolateDown(node, rightChildIndex);
            }
        }
    }

    @Override
    public void add(T item, double priority) {
        PriorityNode<T> newNode = new PriorityNode<>(item, priority);
        items.add(newNode);
        percolateUp(newNode, size + START_INDEX);
        size++;
    }

    @Override
    public boolean contains(T item) {
        if (item == null) {
            return false;
        }
        return containsHelper(item, items.get(START_INDEX), START_INDEX);
    }

    private boolean containsHelper(T item, PriorityNode<T> currNode, int currIndex) {
        if (currNode == null) {
            return false;
        }
        if (currNode.getItem().equals(item)) {
            return true;
        }
        return containsHelper(item, items.get(leftChildIndex(currIndex)), leftChildIndex(currIndex)) ||
            containsHelper(item, items.get(rightChildIndex(currIndex)), rightChildIndex(currIndex));
    }

    @Override
    public T peekMin() {
        return items.get(START_INDEX).getItem();
    }

    @Override
    public T removeMin() {
        T result = items.get(START_INDEX).getItem();
        items.set(START_INDEX, items.get(size + START_INDEX - 1));
        items.remove(size + START_INDEX - 1);
        percolateDown(items.get(START_INDEX), START_INDEX);
        return result;
    }

    @Override
    public void changePriority(T item, double priority) {
        // TODO: replace this with your code
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public int size() {
        return size;
    }
}
