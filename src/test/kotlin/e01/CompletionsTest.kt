package e01

import dev.langchain4j.data.message.UserMessage.userMessage
import dev.langchain4j.model.chat.chat
import dev.langchain4j.model.chat.request.ChatRequest
import dev.langchain4j.model.openai.OpenAiChatModel
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import me.kpavlov.aimocks.openai.MockOpenai
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

class CompletionsTest {
    private val mockOpenAi = MockOpenai(verbose = false)

    val model =
        OpenAiChatModel
            .builder()
            .baseUrl(mockOpenAi.baseUrl())
            .modelName("o1")
            .temperature(0.7)
            .maxTokens(1000)
            .logResponses(true)
            .logRequests(true)
            .build()

    @Test
    fun `Simple Completion Request`() {
        mockOpenAi.completion {
            userMessageContains("Tell me a joke about LLM")
        } responds {
            assistantContent = "Why did LLM cross road? Hallucination."
        }

        val response =
            model.chat(
                ChatRequest
                    .builder()
                    .messages(
                        userMessage("Tell me a joke about LLM"),
                    ).build(),
            )
        val aiResponse = response.aiMessage().text()

        aiResponse shouldBe "Why did LLM cross road? Hallucination."

        println(aiResponse) // "Why did LLM cross road? Hallucination."
    }

    @Test
    fun `Completion Suspend Function`() {
        mockOpenAi.completion {
            userMessageContains("Request A")
        } responds {
            assistantContent = "Response A"
            delay = 2.seconds
        }

        // runBlocking - we don't want our test to finish earlier
        runBlocking(Dispatchers.IO) {
            val response =
                model.chat {
                    messages(
                        listOf(
                            userMessage("Request A"),
                        ),
                    )
                }

            val aiResponse = response.aiMessage().text()
            aiResponse shouldBe "Response A"
            println("AI Response: $aiResponse")
        }
    }
}
