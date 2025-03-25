package e01

import dev.langchain4j.data.message.UserMessage.userMessage
import dev.langchain4j.model.chat.request.ChatRequest
import dev.langchain4j.model.openai.OpenAiChatModel
import io.kotest.matchers.shouldBe
import me.kpavlov.aimocks.openai.MockOpenai
import me.kpavlov.finchly.TestEnvironment
import kotlin.test.Test

internal class BlockingCompletionsTest {
    private val mockOpenAi = MockOpenai(verbose = false)

    private val model: OpenAiChatModel =
        OpenAiChatModel
            .builder()
            .apiKey(TestEnvironment.get("OPENAI_API_KEY", "dummy-key-for-tests"))
            .baseUrl(mockOpenAi.baseUrl())
            .modelName("gpt-4o-mini")
            .temperature(0.7)
            .maxCompletionTokens(100)
            .logResponses(true)
            .logRequests(true)
            .build()

    @Test
    fun `Completion Request (Classic)`() {
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
}
