package com.ad.miningobserver;

public interface EventDesignator<T> {
    
    void handleEvent(T obj);
}
