package com.taboola.tests.ex4;

import java.util.HashMap;

/**
 * <b>Question 4</b>
 * You're tasked with writing a spec for a generic local cache with the following property: <br/>
 * If the cache is asked for a key that it doesn't contain,
 * it should fetch the data using an externally provided function that reads the data from another source (database or similar).
 * What features do you think such a cache should offer? How, in general lines, would you implement it?
 *
 * <b><i>My Answer: </i></b> what feature ? I didn't fully understand that question <br/>Anyway, the cache should define a way LoadingFunction
 * that will be called whenever the value is not present in the in-memory cache <br/>
 */
public class MyCache<KEY, VALUE> {

    private final LoadingFunction<KEY, VALUE> loadingFunction;
    private HashMap<KEY, VALUE> cache = new HashMap<KEY, VALUE>();

    public MyCache(LoadingFunction<KEY, VALUE> loadingFunction) {
        this.loadingFunction = loadingFunction;
    }

    public VALUE getFromCache(KEY key) {
        VALUE value = cache.get(key);
        if(value == null) {
            value = loadingFunction.readDataFromExternalSource(key);
        }
        return value;
    }

    public static interface LoadingFunction<KEY, VALUE> {
        VALUE readDataFromExternalSource(KEY key);
    }
}
