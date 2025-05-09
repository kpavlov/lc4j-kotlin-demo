package e02

import dev.langchain4j.data.message.UserMessage.userMessage
import dev.langchain4j.kotlin.model.chat.chat
import dev.langchain4j.model.openai.OpenAiChatModel
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import me.kpavlov.aimocks.openai.MockOpenai
import java.util.concurrent.Executors
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTimedValue

class ParallelCompletionsTest {
    private val mockOpenAi = MockOpenai(verbose = false)

    val model: OpenAiChatModel =
        OpenAiChatModel
            .builder()
            .apiKey("dummy-key-for-tests")
            .baseUrl(mockOpenAi.baseUrl())
            .modelName("o1")
            .temperature(0.7)
            .maxCompletionTokens(1000)
            .logResponses(false)
            .logRequests(false)
            .build()

    @Test
    fun `Parallel Requests`() {
        mockOpenAi.completion {
            userMessageContains("Concurrent request #")
        } responds {
            assistantContent = "Concurrent response"
            delay = 1.seconds
        }

        runBlocking(Executors.newVirtualThreadPerTaskExecutor().asCoroutineDispatcher()) {
            val timedValue =
                measureTimedValue {
                    (1..64) // expensive with OpenAI API 🤑
                        .map { index ->
                            async {
                                val aiMessageText =
                                    model
                                        .chat {
                                            messages(
                                                listOf(userMessage("Concurrent request #$index")),
                                            )
                                        }.aiMessage()
                                        .text()
                                // println("Request $index finished")
                                aiMessageText
                            }
                        }.awaitAll()
                }
            val duration = timedValue.duration
            println("-----------")
            println("🟢Samples: ${timedValue.value.size} ⏱️Total time: $duration") // N % 64
            println("-----------")
        }
    }
}
