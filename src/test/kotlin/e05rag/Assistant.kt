package e05rag

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import dev.langchain4j.model.output.structured.Description
import dev.langchain4j.service.MemoryId
import dev.langchain4j.service.UserMessage

@JsonIgnoreProperties(ignoreUnknown = true)
class Reply
    @JsonCreator
    constructor(
        @JsonProperty("text")
        @Description("Text response from the AI Character")
        val text: String = "",
        @JsonProperty("coins", defaultValue = "0")
        @Description(
            "Number of coins the AI Character gives to the Player when positive, " +
                "or takes from the Player when negative. " +
                "If no coins are given or taken then 0",
        )
        val coins: Int = 0,
    )

interface Assistant {
    fun chat(
        @MemoryId playerName: String,
        @UserMessage userMessage: String,
    ): Reply
}
