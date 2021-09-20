package com.codercampus.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncApp {
    Assignment8 task = new Assignment8();
    ExecutorService service = Executors.newCachedThreadPool();

    CompletableFuture.supplyAsync(Assignment8::new);

    CompletableFuture<Assignment8> future
        = CompletableFuture.supplyAsync(Assignment8::new);

}
