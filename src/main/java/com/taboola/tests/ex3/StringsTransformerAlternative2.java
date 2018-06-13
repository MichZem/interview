package com.taboola.tests.ex3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class StringsTransformerAlternative2 {
    /**
     * Arbitrary number of threads, to be redefine according to the product requirement (either statically or by configuration)
     */
    private static final int THREAD_NUMBER = 10;

    private List<String> data = new ArrayList<String>();

    public StringsTransformerAlternative2(List<String> startingData) {
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
     * The solution here is to iterate over each StringFunction, and then to have a ThreadPool
     * that process (transform) all the string in the data <br/>
     * Once all string in data[] are transformed by the StringFunction, we pass to the next StringFunction and repeat it (ThreadPool etc ...)
     *
     * The advantage here is that our data[] array is transformed in the same pace , so if for some reason, we have to stop the execution,
     * our data will still look the same way <br/>
     *
     *
     * @param functions
     * @return
     * @throws InterruptedException
     */
    public List<String> transform(final List<StringFunction> functions) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBER);
        for (final StringFunction stringFunction : functions) {
            final CountDownLatch latch = new CountDownLatch(data.size());

            for (final String str : data) {
                executor.execute(new Runnable() {
                    public void run() {
                        stringFunction.transform(str);
                        latch.countDown();
                    }
                });
            }
            latch.await();

        }

        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);
        return data;
    }

    public static interface StringFunction {
        public String transform(String str);
    }
}