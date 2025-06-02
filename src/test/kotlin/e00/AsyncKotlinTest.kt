package e00

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

class AsyncKotlinTest {

    suspend fun callApi(apiResponse: String? = "Hello World"): String? {
        delay(1.seconds) // simulate slow operation
        return apiResponse
    }

    suspend fun processData(data: String): String {
        delay(1.seconds) // simulate slow operation
        return data.uppercase()
    }

    suspend fun saveToDb(data: String): UUID {
        delay(1.seconds) // simulate slow operation
        return UUID.randomUUID()
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "Hello, World",
        ]
    )
    @NullSource
    fun `Chain of calls with conditions`(apiResponse: String?) =
        runBlocking {
            val apiResult = callApi(apiResponse)
            if (apiResult != null) {
                val processedData = processData(apiResult)
                val persistentId = saveToDb(processedData)
                println("✅ Result: $persistentId")
            } else {
                println("✅ Result is null")
            }
        }
}
