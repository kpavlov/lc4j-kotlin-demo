package e00;

import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

class Async2JavaTest {
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

    CompletableFuture<String> callApi(@Nullable String apiResult) {
        return CompletableFuture.supplyAsync(() -> {
            sleep();
            return apiResult;
        }, executor1);
    }

    CompletableFuture<String> processData(String data) {
        if (data == null) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.supplyAsync(() -> {
            sleep();
            return data.toUpperCase();
        }, executor2);
    }

    CompletableFuture<UUID> saveToDb(String data) {
        if (data == null) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.supplyAsync(() -> {
            sleep();
            return UUID.randomUUID();
        }, executor3);
    }

    @Test
    void testAsyncFutures() {
        final var result = callApi("Hello, World")
            .thenCompose(this::processData)
            .thenCompose(this::saveToDb)
            .join();

        System.out.println("✅ Result: " + result);
        assertThat(result).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "Hello, World",
        "Bye-bye",
    })
    @NullSource
    void testAsyncWithConditions(@Nullable String apiResult) {
        final var result = callApi(apiResult)
            .thenApply(it -> {
                if (it != null && it.contains("Hello")) {
                    return it;
                } else {
                    return null;
                }
            })
            .thenCompose(this::processData)
            .thenCompose(this::saveToDb)
            .handle((UUID id, Throwable th) -> {
                if (th != null) {
                    // handle exception
                    System.out.println("❌ Error: " + th.getMessage());
                    return null;
                } else {
                    return id;
                }
            })
            .join();

        System.out.println("✅ Result: " + result);

        if (Objects.equals(apiResult, "Hello, World")) {
            assertThat(result).isInstanceOf(UUID.class);
        } else {
            assertThat(result).isNull();
        }
    }

}
