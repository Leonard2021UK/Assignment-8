package com.codercampus.async;

import java.util.*;
import java.util.concurrent.*;

public class AsyncApp {
    // here I could use ConcurrentHashMap, thus I could eliminate the synchronised block in the loop
    // holds the number of ech type of number
    public static final Map<Integer, Integer> intCounter = new ConcurrentHashMap<>();

    public static List<CompletableFuture<Void>> generateFutures(int numberOfFutures){
        // instantiate a task provider
        Assignment8 taskProvider = new Assignment8();
        // stores all CompletableFutures
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        // creates a Cached thread pool
        ExecutorService service = Executors.newCachedThreadPool();
        // creates a CompletableFuture for each chunk of data
        for (int i = 0;i < numberOfFutures;i++){
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

        return futures;
    }

    public static void waitForAllFutures(List<CompletableFuture<Void>> futuresList){
        // waits until all CompletableFutures are finished
        for (CompletableFuture<Void> future : futuresList) {
            future.join();
        }

    }

    public static void printResult(){
        // prints the result
        System.out.println(intCounter);
    }

    public static void printNumberOfCountedNumbers(){
        int sum=0;
        for (Integer value:intCounter.values()){
            sum = sum + value;
        }
        System.out.print(" - Counted: " + sum);
    }

    public static void main(String[] args) {

        int repetition = 10;
        int numberOfFutures = 100;
        System.out.println("COUNT 10_000 NUMBER " + repetition +"x");
        for (int i=0; i< repetition; i++){
            List<CompletableFuture<Void>> futuresList = generateFutures(numberOfFutures);
            for (CompletableFuture<Void> future : futuresList) {
                future.join();
            }
            printResult();
            intCounter.clear();
        }
//
        System.out.println("COUNT 500_000 NUMBER " + repetition +"x");
        for (int i=0; i< repetition; i++){
            List<CompletableFuture<Void>> futuresList = generateFutures(numberOfFutures*5);
            for (CompletableFuture<Void> future : futuresList) {
                future.join();
            }
            printResult();
            intCounter.clear();
        }

        System.out.println("COUNT 1_000_000 NUMBER " + repetition +"x");
        for (int i=0; i< repetition; i++){
            List<CompletableFuture<Void>> futuresList = generateFutures(numberOfFutures*10);
            for (CompletableFuture<Void> future : futuresList) {
                future.join();
            }
            printResult();
            intCounter.clear();
        }



    }

}
