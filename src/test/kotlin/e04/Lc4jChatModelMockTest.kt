@file:Suppress("HasPlatformType")

package e04

import dev.langchain4j.model.chat.mock.ChatModelMock
import dev.langchain4j.service.AiServices
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class Lc4jChatModelMockTest {
    val model =
        ChatModelMock.thatAlwaysResponds(
            "Yes, Sir",
        )

    val service: JokeService =
        AiServices.create(
            JokeService::class.java,
            model,
        )

    @Test
    fun testService() {
        val result = service.joke("LLM")
        result shouldBe "Yes, Sir"
        println(service.joke("LLM")) // "Yes, Sir"
    }
}
