package com.taboola.tests.ex3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class StringsTransformerAlternative1 {
    /**
     * Arbitrary number of threads, to be redefine according to the product requirement (either statically or by configuration)
     */
    private static final int THREAD_NUMBER = 10;

    private List<String> data = new ArrayList<String>();

    public StringsTransformerAlternative1(List<String> startingData) {
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
     * The solution here is to have a ThreadPool and a task (Runnable) that given a text, iterate over all the StringFunctions
     * and transform in the right order the text. <br/>
     * At the end, once all the data were executed, we return from the shutdown and return the list of string (data) <br/>
     *
     * This method is supposed to be more efficient than the alternative n.2 , as we don't wait for anything <br/>
     * @param functions
     * @return
     * @throws InterruptedException
     */
    public List<String> transform(final List<StringFunction> functions) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBER);

        for (String str : data) {
            final String textToTransform = str;
            executor.execute(new Runnable() {
                public void run() {
                    for (StringFunction stringFunction : functions) {
                        stringFunction.transform(textToTransform);
                    }
                }

            });

        }
        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);
        return data;
    }

    public static interface StringFunction {
        public String transform(String str);
    }
}