package e03

import dev.langchain4j.data.message.UserMessage.userMessage
import dev.langchain4j.kotlin.model.chat.StreamingChatModelReply
import dev.langchain4j.kotlin.model.chat.chatFlow
import dev.langchain4j.model.chat.response.ChatResponse
import dev.langchain4j.model.openai.OpenAiStreamingChatModel
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import me.kpavlov.aimocks.openai.MockOpenai
import me.kpavlov.finchly.TestEnvironment
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicReference
import kotlin.test.Test

class SuspendCompletionsStreamingTest {
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
    fun `Streaming Completion With Flow`() {
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

        val tokens = ConcurrentLinkedQueue<String>()
        val chatResponseHolder = AtomicReference<ChatResponse>()
        runBlocking {
            model
                .chatFlow {
                    messages(
                        listOf(
                            userMessage("Tell me a joke about LLM"),
                        ),
                    )
                }.collect { reply ->
                    when (reply) {
                        is StreamingChatModelReply.PartialResponse -> {
                            println("🔵 \"${reply.partialResponse}\"")
                            tokens += reply.partialResponse
                        }
                        is StreamingChatModelReply.CompleteResponse -> {
                            println("✅ \"${reply.response.aiMessage().text()}\"")
                            chatResponseHolder.set(reply.response)
                            reply.response.aiMessage().text() shouldBe
                                " Why did LLM cross road? Hallucination."
                        }
                        is StreamingChatModelReply.Error -> {
                            println("🛑😫 ${reply.cause.message}")
                            reply.cause.printStackTrace()
                        }
                    }
                }
        }
        tokens.joinToString("") shouldBe " Why did LLM cross road? Hallucination."
        chatResponseHolder.get().aiMessage().text() shouldBe
            " Why did LLM cross road? Hallucination."
    }
}
