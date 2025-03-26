package e00;

import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Async2JavaTest {
    final Executor executor1 = Executors.newFixedThreadPool(10);
    final Executor executor2 = Executors.newFixedThreadPool(20);
    final Executor executor3 = Executors.newFixedThreadPool(10);

    private static void sleep() {
        try {
            Thread.sleep(1000); // sleep for 1 second
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    CompletableFuture<String> callApi() {
        return CompletableFuture.supplyAsync(() -> {
            sleep();
            return "Hello World";
        }, executor1);
    }

    CompletableFuture<String> processData(String data) {
        return CompletableFuture.supplyAsync(() -> {
            sleep();
            return data.toUpperCase();
        }, executor2);
    }

    CompletableFuture<UUID> saveToDb(String data) {
        return CompletableFuture.supplyAsync(() -> {
            sleep();
            return UUID.randomUUID();
        }, executor3);
    }

    @Test
    void testAsyncFutures() {
        final var result = callApi()
            .thenCompose(this::processData)
            .thenCompose(this::saveToDb)
            .join();

        System.out.println("✅ Result: " + result);
    }

    @Test
    void testAsyncWithConditions() {
        final var result = callApi()
            .thenApply(it -> {
                if (it.contains("Bye")) {
                    return it;
                } else {
                    throw new IllegalStateException("No 'Bye' in the response");
                }
            })
            .thenCompose(this::processData)
            .thenCompose(this::saveToDb)
            .handle((UUID id, Throwable th) -> {
                if (th != null) {
                    // handle exception
                    return null;
                } else {
                    return id;
                }
            })
            .join();

        System.out.println("✅ Result: " + result);
    }

}
