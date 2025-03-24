package e04

import dev.langchain4j.service.UserMessage

interface JokeService {
    @UserMessage("Tell me a joke about {{subject}}")
    fun joke(subject: String): String
}
