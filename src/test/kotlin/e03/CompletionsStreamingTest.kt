package e03

import dev.langchain4j.data.message.UserMessage.userMessage
import dev.langchain4j.model.chat.StreamingChatLanguageModelReply
import dev.langchain4j.model.chat.chatFlow
import dev.langchain4j.model.chat.request.ChatRequest
import dev.langchain4j.model.chat.response.ChatResponse
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler
import dev.langchain4j.model.openai.OpenAiStreamingChatModel
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import me.kpavlov.aimocks.openai.MockOpenai
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CountDownLatch
import kotlin.test.Test

class CompletionsStreamingTest {
    private val mockOpenAi = MockOpenai(verbose = false)

    val model =
        OpenAiStreamingChatModel
            .builder()
            .baseUrl(mockOpenAi.baseUrl())
            .modelName("o1")
            .temperature(0.7)
            .maxTokens(1000)
            .logResponses(true)
            .logRequests(true)
            .build()

    @Test
    @DisabledIfEnvironmentVariable(named = "CI", matches = "true")
    fun `Streaming Completion Request`() {
        mockOpenAi.completion {
            userMessageContains("Tell me a joke about LLM")
        } respondsStream {
            sendDone = true
            responseFlow =
                flow {
                    "Why did LLM cross road? Hallucination."
                        .split(" ")
                        .forEach { word ->
                            emit(" $word")
                            delay(100)
                        }
                }
        }

        val latch = CountDownLatch(1)
        val tokens = ConcurrentLinkedQueue<String>()

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

                        completeResponse.aiMessage().text() shouldBe "Why did LLM cross road? Hallucination."

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

        tokens.joinToString("") shouldBe " Why did LLM cross road? Hallucination."
    }

    @Test
    fun `Streaming Completion Suspend Request`() {
        mockOpenAi.completion {
            userMessageContains("Tell me a joke about LLM")
        } respondsStream {
            responseFlow =
                flow {
                    "Why did LLM cross road? Hallucination."
                        .split(" ")
                        .forEach { word ->
                            emit(word)
                            delay(100)
                        }
                }
        }

        runBlocking {
            model
                .chatFlow {
                    messages(
                        listOf(
                            userMessage("Tell me a joke about LLM"),
                        ),
                    )
                }.collect { reply ->
                    println(reply)
                    when (reply) {
                        is StreamingChatLanguageModelReply.PartialResponse -> {}
                        is StreamingChatLanguageModelReply.CompleteResponse -> {}
                        is StreamingChatLanguageModelReply.Error -> {
                            println("🛑😫 ${reply.cause.message}")
                            reply.cause.printStackTrace()
                        }
                    }
                }
        }
    }
}
