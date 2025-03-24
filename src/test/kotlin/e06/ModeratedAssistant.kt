package e06

import dev.langchain4j.service.Moderate
import dev.langchain4j.service.SystemMessage
import dev.langchain4j.service.UserMessage

interface ModeratedAssistant {
    @SystemMessage("Hello! How can I help you?")
    @Moderate
    fun chat(
        @UserMessage userMessage: String,
    ): String
}
