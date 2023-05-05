package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 1;

    List<PriorityNode<T>> items;
    HashMap<T, Integer> itemToIndex;
    int size;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        size = 0;
        itemToIndex = new HashMap<T, Integer>();
        for (int i = 0; i < START_INDEX; i++) {
            items.add(new PriorityNode<>(null, -1));
        }
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
        itemToIndex.put(items.get(b).getItem(), b);
        itemToIndex.put(items.get(a).getItem(), a);
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
        if (parentIndex >= START_INDEX) {
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

        int lastIndex = size + START_INDEX - 1;
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
        if (itemToIndex.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        PriorityNode<T> newNode = new PriorityNode<>(item, priority);
        items.add(size + START_INDEX, newNode);
        itemToIndex.put(item, size + START_INDEX);
        percolateUp(newNode, size + START_INDEX);
        size++;
    }

    @Override
    public boolean contains(T item) {
        return itemToIndex.containsKey(item);
        // if (item == null || size == 0) {
        //     return false;
        // }
        // return containsHelper(item, items.get(START_INDEX), START_INDEX);
    }

    private boolean containsHelper(T item, PriorityNode<T> currNode, int currIndex) {
        if (currNode == null) {
            return false;
        }
        if (currNode.getItem().equals(item)) {
            return true;
        }

        int leftChildIndex = leftChildIndex(currIndex);
        int rightChildIndex = rightChildIndex(currIndex);

        int lastIndex = size + START_INDEX - 1;
        boolean hasLeftChild = leftChildIndex <= lastIndex;
        boolean hasRightChild = rightChildIndex <= lastIndex;

        //return false if no children itemToIndex (no more to check)
        if (!hasLeftChild && !hasRightChild) {
            return false;
        }

        if (!hasRightChild) {
            return containsHelper(item, items.get(leftChildIndex), leftChildIndex);
        }
        return containsHelper(item, items.get(leftChildIndex), leftChildIndex) ||
            containsHelper(item, items.get(rightChildIndex), rightChildIndex);
    }

    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return items.get(START_INDEX).getItem();
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public T removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T result = items.get(START_INDEX).getItem();
        itemToIndex.remove(result);
        items.set(START_INDEX, items.get(size + START_INDEX - 1));
        items.remove(size + START_INDEX - 1);
        size--;
        if (size != 0) {
            percolateDown(items.get(START_INDEX), START_INDEX);
        }

        return result;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }
        int indexOfItem = itemToIndex.get(item);
        PriorityNode<T> targetNode = items.get(indexOfItem);
        targetNode.setPriority(priority);
        percolateUp(targetNode, indexOfItem);
        percolateDown(targetNode, itemToIndex.get(item));
    }

    @Override
    public int size() {
        return size;
    }
}
