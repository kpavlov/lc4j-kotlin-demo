package e01

import dev.langchain4j.data.message.UserMessage.userMessage
import dev.langchain4j.kotlin.model.chat.chat
import dev.langchain4j.model.openai.OpenAiChatModel
import io.kotest.matchers.string.shouldContainIgnoringCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import me.kpavlov.aimocks.openai.MockOpenai
import me.kpavlov.finchly.TestEnvironment
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

internal class SuspendCompletionsTest {
    private val mockOpenAi = MockOpenai(verbose = false)

    private val model: OpenAiChatModel =
        OpenAiChatModel
            .builder()
            .apiKey(TestEnvironment.get("OPENAI_API_KEY", "dummy-key-for-tests"))
            .baseUrl(mockOpenAi.baseUrl())
            .modelName("gpt-4.1-nano")
            .temperature(0.7)
            .maxCompletionTokens(100)
            .logResponses(true)
            .logRequests(true)
            .build()

    @Test
    fun `Completion Request (Non-Blocking)`() {
        mockOpenAi.completion {
            userMessageContains("What does LLM stand for?")
        } responds {
            assistantContent = "LLM typically stands for \"Large Language Model.\""
            delay = 2.seconds
        }

        // runBlocking - we don't want our test to finish earlier
        runBlocking(Dispatchers.IO) {
            val response =
                model.chat {
                    messages(
                        listOf(
                            userMessage("What does LLM stand for?"),
                        ),
                    )
                }

            val aiResponse = response.aiMessage().text()
            aiResponse shouldContainIgnoringCase "Large Language Model"
            println("AI Response: $aiResponse")
        }
    }
}
