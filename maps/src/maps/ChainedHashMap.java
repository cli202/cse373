package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ChainedHashMap<K, V> extends AbstractIterableMap<K, V> {
    // these numbers seemed reasonable
    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 0.9;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 10;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 5;

    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    AbstractIterableMap<K, V>[] chains;

    // You're encouraged to add extra fields (and helper methods) though!

    int size;
    double resizeLoadFactor;
    int numberOfChains;
    int chainLength;

    /**
     * Constructs a new ChainedHashMap with default resizing load factor threshold,
     * default initial chain count, and default initial chain capacity.
     */
    public ChainedHashMap() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    /**
     * Constructs a new ChainedHashMap with the given parameters.
     *
     * @param resizingLoadFactorThreshold the load factor threshold for resizing. When the load factor
     *                                    exceeds this value, the hash table resizes. Must be > 0.
     * @param initialChainCount the initial number of chains for your hash table. Must be > 0.
     * @param chainInitialCapacity the initial capacity of each ArrayMap chain created by the map.
     *                             Must be > 0.
     */
    public ChainedHashMap(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        // create hash table of ArrayMaps with desired capacity
        chains = createArrayOfChains(initialChainCount);
        // initialize data
        size = 0;
        resizeLoadFactor = resizingLoadFactorThreshold;
        numberOfChains = initialChainCount;
        chainLength = chainInitialCapacity;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code AbstractIterableMap<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     * @see ArrayMap createArrayOfEntries method for more background on why we need this method
     */
    @SuppressWarnings("unchecked")
    private AbstractIterableMap<K, V>[] createArrayOfChains(int arraySize) {
        return (AbstractIterableMap<K, V>[]) new AbstractIterableMap[arraySize];
    }

    /**
     * Returns a new chain.
     *
     * This method will be overridden by the grader so that your ChainedHashMap implementation
     * is graded using our solution ArrayMaps.
     *
     * Note: You do not need to modify this method.
     */
    protected AbstractIterableMap<K, V> createChain(int initialSize) {
        return new ArrayMap<>(initialSize);
    }

    @Override
    public V get(Object key) {
        AbstractIterableMap<K, V> chain = findCorrectChain(key);
        // if chain does not exist, there is nothing to get
        if (chain == null) {
            return null;
        }
        // otherwise actually get it
        return chain.get(key);
    }

    @Override
    public V put(K key, V value) {
        // check for when to resize
        if ((double) ((size + 1) / chains.length) >= resizeLoadFactor) {
            resize();
        }

        int bucket = 0;
        if (key != null) {
            bucket = Math.abs(key.hashCode()) % chains.length;
        }


        // need to write this one this way because we are using the new keyword in create chain
        if (chains[bucket] == null) {
            chains[bucket] = createChain(chainLength);
        }
        // see if the size of chains increased or not
        V result  = chains[bucket].put(key, value);
        if (result == null) {
            size++;
        }
        //return result
        return result;
    }

    private void resize() {
        int newTableSize = 2 * chains.length;
        AbstractIterableMap<K, V>[] newTable = createArrayOfChains(newTableSize);
        Iterator<Map.Entry<K, V>> itr = this.iterator();
        while (itr.hasNext()) {
            Map.Entry<K, V> nextEntry = itr.next();
            int bucket = Math.abs(nextEntry.getKey().hashCode()) % newTableSize;
            if (newTable[bucket] == null) {
                newTable[bucket] = createChain(chainLength);
            }
            newTable[bucket].put(nextEntry.getKey(), nextEntry.getValue());
        }
        chains = newTable;
    }

    @Override
    public V remove(Object key) {
        // find correct chain
        AbstractIterableMap<K, V> chain = findCorrectChain(key);
        int bucket = 0;
        if (key != null) {
            bucket = Math.abs(key.hashCode()) % chains.length;
        }
        // if chain is null, there is no ArrayMap, so there is nothing to remove
        if (chain == null) {
            return null;
        }
        // otherwise, actually do the remove
        V result = chain.remove(key);

        if (result != null) {
            size--;
        }
        if (chains[bucket].size() == 0) {
            chains[bucket] = null;
        }
        return result;
    }

    @Override
    public void clear() {
        // create new hash table of ArrayMaps
        chains = createArrayOfChains(numberOfChains);
        // reset size
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        // find correct chain
        AbstractIterableMap<K, V> chain = findCorrectChain(key);
        // if no chain exists, it certainly does not contain the key
        if (chain == null) {
            return false;
        }
        // otherwise, see if it does contain the key
        return chain.containsKey(key);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: you won't need to change this method (unless you add more constructor parameters)
        return new ChainedHashMapIterator<>(this.chains);
    }

    // // Doing so will give you a better string representation for assertion errors the debugger.
    // @Override
    // public String toString() {
    //     return super.toString();
    // }

    private AbstractIterableMap<K, V> findCorrectChain(Object key) {
        int bucket = 0;
        if (key != null) {
            bucket = Math.abs(key.hashCode()) % chains.length;
        }
        // if key was null, bucket is still 0
        return chains[bucket];
    }

    /*
    See the assignment webpage for tips and restrictions on implementing this iterator.
     */
    private static class ChainedHashMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private AbstractIterableMap<K, V>[] chains;
        // You may add more fields and constructor parameters

        int currBucket;
        SimpleEntry<K, V> currEntry;

        Iterator<Map.Entry<K, V>> currItr;
        public ChainedHashMapIterator(AbstractIterableMap<K, V>[] chains) {
            this.chains = chains;
            currBucket = 0;

            // find the first non-null bucket
            while (currBucket < chains.length && chains[currBucket] == null) {
                currBucket++;
            }

            // if you could not find a non-null bucket, the whole hash table is null
            //
            if (currBucket == chains.length) {
                currItr = null;
            }
            else {
                currItr = chains[currBucket].iterator();
            }

            // obtain initial iterator  no in hasNext()

        }

        @Override
        public boolean hasNext() {
            // if iterator has a next, return true
            if (currBucket >= chains.length) {
                return false;
            }

            if (currItr.hasNext()) {
                return true;
            }
            // if not, there may be entries in other buckets
            // jump to the next bucket
            currBucket++;
            // find next non-null bucket
            while (currBucket < chains.length && chains[currBucket] == null) {
                currBucket++;
            }


            // run tests? ok
            // if iterator looked at entire hash table and reached the end, it did not have a next
            if (currBucket == chains.length) {
                return false;
            }

            // STATE: currBucket has found a non-null bucket
            // create iterator
            currItr = chains[currBucket].iterator();
            // use that iterator to call hasNext()
            return currItr.hasNext();
        }

        @Override
        public Map.Entry<K, V> next() {
            // if there is no next
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            // STATE: there is a next
            return currItr.next();
        }
    }
}
