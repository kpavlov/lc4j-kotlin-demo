package e08structured

import dev.langchain4j.data.message.SystemMessage.systemMessage
import dev.langchain4j.data.message.UserMessage.userMessage
import dev.langchain4j.kotlin.model.chat.chat
import dev.langchain4j.model.chat.request.ResponseFormat
import dev.langchain4j.model.chat.request.ResponseFormatType
import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.model.openai.OpenAiChatRequestParameters
import dev.langchain4j.model.output.structured.Description
import dev.langchain4j.service.output.JsonSchemas
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.nulls.shouldNotBeNull
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import me.kpavlov.finchly.TestEnvironment
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable
import kotlin.test.Test

@TestInstance(Lifecycle.PER_CLASS)
@DisabledIfEnvironmentVariable(named = "CI", matches = "true")
internal class StructuredResponseCompletionsTest {

    private val model: OpenAiChatModel =
        OpenAiChatModel
            .builder()
            .apiKey(TestEnvironment.get("OPENAI_API_KEY", "dummy-key-for-tests"))
            .modelName("gpt-4.1-nano")
            .temperature(0.7)
            .maxCompletionTokens(100)
            .logResponses(true)
            .logRequests(true)
            .build()

    @JvmRecord
    @Serializable
    data class Person(
        @Description("Person's name")
        val name: String,
        @Description("Person's age")
        val age: Int,
        @Description("Weight in kilograms")
        val weight: Float,
        @Description("Height in meters")
        val height: Float,
        val married: Boolean
    )

    @Test
    fun `JsonSchema response`() = runTest {
        val response =
            model.chat {
                messages += systemMessage("Convert person to JSON")
                messages += userMessage(
                    """
                        Bob is 25 years old and weighs 0.075 tonnes.
                        His height is one meter eighty-five centimeters.
                        He is married.
                        """.trimIndent()
                )

                parameters(OpenAiChatRequestParameters.builder()) {
                    val personSchema = JsonSchemas
                        .jsonSchemaFrom(Person::class.java)
                        .orElseThrow()

                    responseFormat = ResponseFormat.builder()
                        .type(ResponseFormatType.JSON)
                        .jsonSchema(personSchema)
                        .build()
                }
            }

        val aiResponse = response.aiMessage()
        aiResponse.text() shouldNotBeNull {
            val person = Json.parseToJsonElement(this)
            println("👦🏻 The Person is: $person")
            person shouldBeEqualToComparingFields Person(
                name = "Bob",
                age = 25,
                weight = 75.0f,
                height = 1.85f,
                married = true
            )
        }
    }
}
