package e05rag

import dev.langchain4j.data.document.loader.FileSystemDocumentLoader
import dev.langchain4j.data.document.parser.TextDocumentParser
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.memory.ChatMemory
import dev.langchain4j.memory.chat.ChatMemoryProvider
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever
import dev.langchain4j.service.AiServices
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import me.kpavlov.finchly.TestEnvironment
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable
import kotlin.test.Test

@TestInstance(Lifecycle.PER_CLASS)
@DisabledIfEnvironmentVariable(named = "CI", matches = "true")
class RagWithMemoryTest {
    private val embeddingStore = InMemoryEmbeddingStore<TextSegment>()

    private val contentRetriever = EmbeddingStoreContentRetriever.from(embeddingStore)

    private val memory: ChatMemory =
        MessageWindowChatMemory
            .builder()
            .chatMemoryStore(InMemoryChatMemoryStore())
            .maxMessages(10)
            .build()

    private val chatMemoryProvider: ChatMemoryProvider =
        object : ChatMemoryProvider {
            override fun get(memoryId: Any): ChatMemory = memory
        }

    private val model =
        OpenAiChatModel
            .builder()
            .apiKey(TestEnvironment.get("OPENAI_API_KEY"))
            .modelName("gpt-4.1-nano")
            .maxTokens(1500)
            .responseFormat("json_schema")
            .strictJsonSchema(true)
            .logRequests(true)
            .logResponses(true)
            .build()

    private val assistant: Assistant =
        AiServices
            .builder(Assistant::class.java)
            .chatModel(model)
            .systemMessageProvider {
                "You are player giving a coin for polite sentence"
            }.chatMemoryProvider(chatMemoryProvider)
            .contentRetriever(contentRetriever)
            .build()

    @BeforeAll
    fun loadKnowledge() {
        val documents =
            FileSystemDocumentLoader.loadDocumentsRecursively(
                System.getProperty("user.dir") + "/src/test/resources/knowledge",
                TextDocumentParser(),
            )

        EmbeddingStoreIngestor.ingest(documents, embeddingStore)
    }

    @Test
    fun `RAG test`() {
        val reply =
            assistant
                .chat(
                    playerName = "Alice",
                    userMessage = "Please summarize a story you know and give me 3 coins?",
                )
        println("Here is ${reply.coins} coins and the reply: \n${reply.text}")

        reply.coins shouldBe 3
        reply.text shouldContain "Ella" // From "The Clock Shop"
    }
}
