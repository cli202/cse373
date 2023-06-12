package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ArrayMap<K, V> extends AbstractIterableMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 10;
    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    SimpleEntry<K, V>[] entries;

    // You may add extra fields or helper methods though!

    private int size;

    /**
     * Constructs a new ArrayMap with default initial capacity.
     */
    public ArrayMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Constructs a new ArrayMap with the given initial capacity (i.e., the initial
     * size of the internal array).
     *
     * @param initialCapacity the initial capacity of the ArrayMap. Must be > 0.
     */
    public ArrayMap(int initialCapacity) {
        this.entries = this.createArrayOfEntries(initialCapacity);
        size = 0;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code Entry<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     */
    @SuppressWarnings("unchecked")
    private SimpleEntry<K, V>[] createArrayOfEntries(int arraySize) {
        /*
        It turns out that creating arrays of generic objects in Java is complicated due to something
        known as "type erasure."

        We've given you this helper method to help simplify this part of your assignment. Use this
        helper method as appropriate when implementing the rest of this class.

        You are not required to understand how this method works, what type erasure is, or how
        arrays and generics interact.
        */
        return (SimpleEntry<K, V>[]) (new SimpleEntry[arraySize]);
    }

    @Override
    public V get(Object key) {
        for (int i = 0; i < size; i++) {
            if (java.util.Objects.equals(entries[i].getKey(), key)) {
                return entries[i].getValue();
            }
        }

        return null;
    }

    @Override
    public V put(K key, V value) {
        // loop over array
        for (int i = 0; i < size; i++) {
            // if key exists
            if (java.util.Objects.equals(entries[i].getKey(), key)) {
                // to return what was previously at that key mapping
                V result = entries[i].getValue();
                // override value at that key
                entries[i] = new SimpleEntry<>(key, value);
                // return result
                return result;
            }
        }
        // key does not exist
        // check for resizing
        if (size == entries.length) {
            resize();
        }
        // add new key/value pair to the end of the array
        entries[size] = new SimpleEntry<>(key, value);
        // increment size
        size++;
        // return value
        return null;
    }

    private void resize() {
        SimpleEntry<K, V>[] newArray = createArrayOfEntries(size * 2);
        for (int i = 0; i < size; i++) {
            newArray[i] = entries[i];
        }
        entries = newArray;
    }

    @Override
    public V remove(Object key) {
        // loop through the array
        for (int i = 0; i < size; i++) {
            // if key found
            if (java.util.Objects.equals(entries[i].getKey(), key)) {
                // store value of key
                V result = entries[i].getValue();
                // move last entry to the removed entry
                entries[i] = entries[size - 1];
                // set last entry to null
                entries[size - 1] = null;
                // decrement size
                size--;
                // return result
                return result;
            }
        }
        // return null if key not found
        return null;
    }

    @Override
    public void clear() {
        this.entries = this.createArrayOfEntries(DEFAULT_INITIAL_CAPACITY);
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        // loop through array
        for (int i = 0; i < size; i++) {
            // if key found
            if (java.util.Objects.equals(entries[i].getKey(), key)) {
                // return true
                return true;
            }
        }
        // return false if key not found
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: You may or may not need to change this method, depending on whether you
        // add any parameters to the ArrayMapIterator constructor.
        return new ArrayMapIterator<>(this.entries);
    }

    // // after you implement the iterator, remove this toString implementation
    // // Doing so will give you a better string representation for assertion errors the debugger.
    // @Override
    // public String toString() {
    //     return super.toString();
    // }

    private static class ArrayMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final SimpleEntry<K, V>[] entries;
        private int currIndex;
        // You may add more fields and constructor parameters

        public ArrayMapIterator(SimpleEntry<K, V>[] entries) {
            this.entries = entries;
            currIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return currIndex < entries.length && entries[currIndex] != null;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            SimpleEntry<K, V> result = entries[currIndex];
            currIndex++;
            return result;
        }
    }
}
