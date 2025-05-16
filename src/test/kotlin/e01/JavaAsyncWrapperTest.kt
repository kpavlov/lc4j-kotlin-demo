package e01

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

class JavaAsyncWrapperTest {

    suspend fun chatAsync(context: CoroutineContext = Dispatchers.IO): String {
        return withContext(context) {
            println("📡 Calling blocking method on thread " + Thread.currentThread())
            Thread.sleep(1.seconds.inWholeMilliseconds)
            return@withContext "Hello, World"
        }
    }

    @Test
    fun `call blocking method async wrapper`() = runTest {
        val result = chatAsync()
        println("✅ Result: $result")
        result shouldBe "Hello, World"
    }

    @Test
    fun `call async wrapper on Virtual Thread`() = runTest {
        val dispatcher =
            Executors.newVirtualThreadPerTaskExecutor().asCoroutineDispatcher()
        val result = chatAsync(dispatcher)
        println("✅ Result: $result")
        result shouldBe "Hello, World"
    }
}
