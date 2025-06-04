package e03streaming

import dev.langchain4j.data.message.UserMessage.userMessage
import dev.langchain4j.kotlin.model.chat.request.chatRequest
import dev.langchain4j.model.chat.StreamingChatModel
import dev.langchain4j.model.chat.response.ChatResponse
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler
import dev.langchain4j.model.openai.OpenAiStreamingChatModel
import io.kotest.matchers.string.shouldContainIgnoringCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import me.kpavlov.aimocks.openai.MockOpenai
import me.kpavlov.finchly.TestEnvironment
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicReference
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds

class CompletionsStreamingTest {
    private val mockOpenAi = MockOpenai(verbose = true)

    private val model: StreamingChatModel =
        OpenAiStreamingChatModel
            .builder()
            .apiKey(TestEnvironment.get("OPENAI_API_KEY", "dummy-key-for-tests"))
            .baseUrl(mockOpenAi.baseUrl())
            .modelName("gpt-4.1-nano")
            .logResponses(true)
            .logRequests(true)
            .build()

    @Test
    fun `Streaming Completion Request`() {
        val latch = CountDownLatch(1)
        val tokens = ConcurrentLinkedQueue<String>()
        val chatResponseHolder = AtomicReference<ChatResponse>()

        mockOpenAi.completion {
            userMessageContains("What is the color of the sky?")
        } respondsStream {
            responseFlow =
                flow {
                    "The color of the sky typically appears blue during the day."
                        .split(" ")
                        .forEach { word ->
                            emit(" $word")
                            delay(500.milliseconds)
                        }
                }
        }

        model
            .chat(
                chatRequest {
                    messages += userMessage("What is the color of the sky?")
                },
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

        println("Tokens: ${tokens.joinToString()}")
        println("ChatResponse: ${chatResponseHolder.get()}")

        tokens.joinToString(" ") shouldContainIgnoringCase "blue"

//        tokens.joinToString("") shouldBe " Why did LLM cross road? Hallucination."
//        chatResponseHolder.get().aiMessage().text() shouldBe " Why did LLM cross road? Hallucination."
    }
}
