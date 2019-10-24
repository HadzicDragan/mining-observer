package com.ad.miningobserver.messaging.control;

/**
 * #TODO refactor or remove
 */
public class EntryHolder<K, V> {
    
    private final K first;
    private final V second;

    public EntryHolder(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public K getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }
}
