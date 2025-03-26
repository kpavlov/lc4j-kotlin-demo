package e00

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

class AsyncKotlinTest {
    suspend fun callApi(): String {
        delay(1.seconds) // simulate delay
        return "Hello World"
    }

    suspend fun processData(data: String): String {
        delay(1.seconds) // simulate delay
        return data.uppercase()
    }

    suspend fun saveToDb(data: String): UUID {
        delay(1.seconds) // simulate delay
        return UUID.randomUUID()
    }

    @Test
    fun `Chain of calls`() =
        runBlocking {
            val apiResult = callApi()
            val processedData = processData(apiResult)
            val persistentId = saveToDb(processedData)

            println("✅ Result: $persistentId")
        }

    @Test
    fun `Chain of calls with conditions`() =
        runBlocking {
            val persistentId: UUID?
            val apiResult = callApi()
            if (apiResult.contains("Bye")) {
                persistentId = null
            } else {
                val processedData = processData(apiResult)
                persistentId = saveToDb(processedData)
            }

            println("✅ Result: $persistentId")
        }
}
