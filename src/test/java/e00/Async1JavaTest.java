package e00;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

class Async1JavaTest {
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

    @Test
    void testAsyncFutures() {
         final var result = CompletableFuture.supplyAsync(() -> {
                sleep();
                return "Hello World";
            }, executor1)
            .thenApplyAsync(text -> {
                sleep();
                return text.toUpperCase();
            }, executor2)
            .thenApplyAsync(text -> {
                sleep();
                return text.replace("L", "￡");
            }, executor3)
            .join();

         System.out.println("✅ Result: " + result);
         assertThat(result).isEqualTo("HE￡￡O WOR￡D");
    }


}
