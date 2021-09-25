package com.codercampus.async;

        import java.util.*;
        import java.util.concurrent.*;
        import java.util.concurrent.atomic.AtomicInteger;

public class AsyncApp {

    public static void main(String[] args) {
        // instantiate a task provider
        Assignment8 taskProvider = new Assignment8();
        // holds the number of ech type of number
        // here I could use ConcurrentHashMap, thus I could eliminate the synchronised block in the loop
        Map<Integer, Integer> intCounter = new ConcurrentHashMap<>();
        // stores all CompletableFutures
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        // creates a Cached thread pool
        ExecutorService service = Executors.newFixedThreadPool(100);
        // creates a CompletableFuture for each chunk of data
        for (int i = 0;i < 1000;i++){
            // processes a chunk of data (1000 numbers)
            CompletableFuture<Void> future =  CompletableFuture.supplyAsync(()->taskProvider,service)
                    .thenApply(Assignment8::getNumbers)
                    .thenAccept((List<Integer> set) ->
                            {
                                synchronized(intCounter){
                                    set.forEach(num->{
                                        intCounter.merge(num,1,Integer::sum);
                                    });
                                };
                            }
                    );

            // collects the created CompletableFuture
            futures.add(future);
        }

        // waits until all CompletableFuture are finished
        for (CompletableFuture<Void> future : futures) {
            future.join();
        }

        // prints the result
        System.out.println(intCounter);
        int sum=0;
        for (Integer value:intCounter.values()){
            sum = sum + value;
        }

        System.out.println("Total number of numbers counted: " + sum);
    }

}

