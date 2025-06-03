package e04

import dev.langchain4j.model.chat.ChatModel
import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.service.AiServices
import dev.langchain4j.service.UserMessage
import io.kotest.matchers.shouldBe
import me.kpavlov.aimocks.openai.MockOpenai
import kotlin.test.Test

class AiMocksServiceTest {
    val mockOpenAi = MockOpenai(verbose = true)

    val model: ChatModel =
        OpenAiChatModel
            .builder()
            .baseUrl(mockOpenAi.baseUrl())
            .modelName("gpt-4.1-nano")
            .logRequests(true)
            .logResponses(true)
            .build()

    interface JokeService {
        @UserMessage("Tell me a joke about {{subject}}")
        fun joke(subject: String): String
    }

    val service: JokeService =
        AiServices.create(
            JokeService::class.java,
            model,
        )

    @Test
    fun testService() {
        mockOpenAi.completion {
            userMessageContains("Tell me a joke about LLM")
        } responds {
            assistantContent = "Why did LLM cross road? Hallucination."
        }

        val result = service.joke("LLM")
        result shouldBe "Why did LLM cross road? Hallucination."
        println(service.joke("LLM")) // "Why did LLM cross road? Hallucination."
    }
}
