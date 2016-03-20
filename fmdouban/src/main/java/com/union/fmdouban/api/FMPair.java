package com.union.fmdouban.api;

/**
 * Created by zhouxiaming on 16/3/20.
 */
public class FMPair<K, V> {
    public final K key;
    public final V value;
    public FMPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

}
