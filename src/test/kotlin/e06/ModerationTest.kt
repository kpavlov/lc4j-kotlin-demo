package e06

import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.model.openai.OpenAiModerationModel
import dev.langchain4j.service.AiServices
import dev.langchain4j.service.ModerationException
import io.kotest.matchers.shouldBe
import me.kpavlov.finchly.TestEnvironment
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable
import kotlin.test.Test
import kotlin.test.fail

@TestInstance(Lifecycle.PER_CLASS)
@DisabledIfEnvironmentVariable(named = "CI", matches = "true")
class ModerationTest {
    private val model =
        OpenAiChatModel
            .builder()
            .apiKey(TestEnvironment["OPENAI_API_KEY"])
            .modelName("gpt-4o-mini")
            .maxTokens(100)
            .logRequests(true)
            .logResponses(true)
            .build()

    private var moderationModel =
        OpenAiModerationModel
            .builder()
            .modelName("omni-moderation-latest")
            .apiKey(TestEnvironment["OPENAI_API_KEY"])
            .logRequests(true)
            .logResponses(true)
            .build()

    private val assistant: ModeratedAssistant =
        AiServices
            .builder(ModeratedAssistant::class.java)
            .moderationModel(moderationModel)
            .chatLanguageModel(model)
            .build()

    @Test
    fun `RAG Moderation test`() {
        try {
            assistant
                .chat(
                    userMessage = "I will kill you",
                )
            fail("Should have thrown ModerationException")
        } catch (e: ModerationException) {
            e.message shouldBe "Text \"I will kill you\" violates content policy"
        }
    }
}
