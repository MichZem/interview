package com.taboola.tests.ex3;

import java.util.*;

/**
 * <b><i>Question 3</i></b>: <br/>
 * Jimmy was tasked with writing a class that takes a base list of strings and a series of transformations
 * and applies them, returning the end result.<br/>
 *  To better utilize all available resources, the solution was done in a multi-threaded fashion.<br/>
 * Explain the problems with this solution, and offer 2 alternatives. Discuss the advantages of each approach.

 */
public class StringsTransformer {
    private List<String> data = new ArrayList<String>();

    public StringsTransformer(List<String> startingData) {
        this.data = startingData;
    }

    private void forEach(StringFunction function) {
        List<String> newData = new ArrayList<String>();
        for (String str : data) {
            newData.add(function.transform(str));
        }
        data = newData;
    }

    /**
     * THE Problem with this implementation is that you don't have control about the order of the transformation. <br/>
     * You may execute "StringFunction_4" before "StringFunction_1 / 2 / 3 " <br/>
     * @param functions
     * @return
     * @throws InterruptedException
     */
    public List<String> transform(List<StringFunction> functions) throws InterruptedException {
        List<Thread> threads = new ArrayList<Thread>();
        for (final StringFunction f : functions) {
            threads.add(new Thread(new Runnable() {
                // @Override
                public void run() {
                    forEach(f);
                }
            }));
        }
        for (Thread t : threads) {
            t.join();
        }
        return data;
    }

    public static interface StringFunction {
        public String transform(String str);
    }
}