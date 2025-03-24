package e03

import dev.langchain4j.data.message.UserMessage.userMessage
import dev.langchain4j.model.chat.request.ChatRequest
import dev.langchain4j.model.chat.response.ChatResponse
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler
import dev.langchain4j.model.openai.OpenAiStreamingChatModel
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import me.kpavlov.aimocks.openai.MockOpenai
import me.kpavlov.finchly.TestEnvironment
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicReference
import kotlin.test.Test

class CompletionsStreamingTest {
    private val mockOpenAi = MockOpenai(verbose = false)

    val model =
        OpenAiStreamingChatModel
            .builder()
            .apiKey(TestEnvironment.get("OPENAI_API_KEY", "dummy-key-for-tests"))
            .baseUrl(mockOpenAi.baseUrl())
            .modelName("gpt-4o-mini")
            .logResponses(true)
            .logRequests(true)
            .build()

    @Test
    fun `Streaming Completion Request`() {
        val latch = CountDownLatch(1)
        val tokens = ConcurrentLinkedQueue<String>()
        val chatResponseHolder = AtomicReference<ChatResponse>()

        mockOpenAi.completion {
            userMessageContains("Tell me a joke about LLM")
        } respondsStream {
            responseFlow =
                flow {
                    "Why did LLM cross road? Hallucination."
                        .split(" ")
                        .forEach { word ->
                            emit(" $word")
                            delay(1000)
                        }
                }
        }

        model
            .chat(
                ChatRequest
                    .builder()
                    .messages(
                        userMessage("Tell me a joke about LLM"),
                    ).build(),
                object : StreamingChatResponseHandler {
                    override fun onPartialResponse(partialResponse: String?) {
                        println("🔵 \"$partialResponse\"")
                        tokens += partialResponse
                    }

                    override fun onCompleteResponse(completeResponse: ChatResponse) {
                        println("✅ $completeResponse")

                        chatResponseHolder.set(completeResponse)

                        latch.countDown()
                    }

                    override fun onError(error: Throwable) {
                        println("🛑😫 ${error.message}")
                        error.printStackTrace()
                        latch.countDown()
                    }
                },
            )
        latch.await()

        println("Tokens: ${tokens.joinToString(" ")}")
        println("ChatResponse: ${chatResponseHolder.get()}")

        tokens.joinToString("") shouldBe " Why did LLM cross road? Hallucination."
        chatResponseHolder.get().aiMessage().text() shouldBe
            " Why did LLM cross road? Hallucination."
    }
}
