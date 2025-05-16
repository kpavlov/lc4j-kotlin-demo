package e01

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

class JavaAsyncWrapperTest {

    suspend fun chatAsync(context: CoroutineContext = Dispatchers.IO): String {
        return withContext(context) {
            Thread.sleep(1.seconds.inWholeMilliseconds)
            return@withContext "Hello, World"
        }
    }

    @Test
    fun `call async`() {
        runBlocking {
            val result = chatAsync()
            println("✅ Result: $result")
        }
    }
}
