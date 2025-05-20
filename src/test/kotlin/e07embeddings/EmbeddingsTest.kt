package e07embeddings

import dev.langchain4j.data.embedding.Embedding
import dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel
import dev.langchain4j.model.embedding.onnx.allminilml6v2q.AllMiniLmL6V2QuantizedEmbeddingModel
import dev.langchain4j.model.openai.OpenAiEmbeddingModel
import io.kotest.matchers.comparables.shouldBeGreaterThan
import me.kpavlov.finchly.TestEnvironment
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import kotlin.math.sqrt
import kotlin.test.Test


@TestInstance(Lifecycle.PER_CLASS)
internal class EmbeddingsTest {

    private val embeddingsModel: DimensionAwareEmbeddingModel
        get() {
            val openAiApiKey = TestEnvironment["OPENAI_API_KEY"]
            return if (openAiApiKey != null) {
                OpenAiEmbeddingModel
                    .builder()
                    .modelName("text-embedding-3-small")
                    .apiKey(openAiApiKey)
                    .logRequests(true)
                    .logResponses(true)
                    .build()
            } else {
                AllMiniLmL6V2QuantizedEmbeddingModel()
            }
        }


    @Test
    fun `Embeddings test`() {

        val text1 = """
            Embeddings are numerical representations of concepts
            converted to number sequences, which make it easy
            for computers to understand the relationships
            between those concepts"""

        val text2 = """
            Stronger performance. Comparing text-embedding-ada-002 to text-embedding-3-small,
            the average score on a commonly used benchmark for multi-language retrieval
            (MIRACL) has increased from 31.4% to 44.0%, while the average score
            on a commonly used benchmark for English tasks (MTEB) has increased
            from 61.0% to 62.3%.
            """

        val cinderellaText = """
            And when it was evening Cinderella wanted to go home, and the prince was about
            to go with her, when she ran past him so quickly that he could not follow her.
            But he had laid a plan, and had caused all the steps to be spread with pitch,
            so that as she rushed down them the left shoe of the maiden remained
            sticking in it. The prince picked it up, and saw that it was of gold,
            and very small and slender.
            """

        embeddingsModel.embed(
            """
            Embeddings are numerical representations of concepts
            converted to number sequences, which make it easy
            for computers to understand the relationships
            between those concepts"""
        )

        val embedding1 =
            embeddingsModel.embed(text1).content().toDoubleArray()
        val embedding2 =
            embeddingsModel.embed(text2).content().toDoubleArray()
        val embedding3 =
            embeddingsModel.embed(cinderellaText).content().toDoubleArray()

        val similarity12 = cosineSimilarity(
            embedding1,
            embedding2
        )

        val similarity13 = cosineSimilarity(
            embedding1,
            embedding3
        )

        val similarity23 = cosineSimilarity(
            embedding2,
            embedding3
        )

        similarity12 shouldBeGreaterThan similarity13
        similarity12 shouldBeGreaterThan similarity23
    }

    /**
     * See https://en.wikipedia.org/wiki/Cosine_similarity#Definition
     */
    fun cosineSimilarity(vector1: DoubleArray, vector2: DoubleArray): Double {
        require(vector1.size == vector2.size) { "Vectors must have the same dimension" }

        val dotProduct = vector1.zip(vector2).sumOf { (a, b) -> a * b }
        val norm1 = sqrt(vector1.sumOf { it * it })
        val norm2 = sqrt(vector2.sumOf { it * it })

        return dotProduct / norm1 / norm2
    }

    fun Embedding.toDoubleArray(): DoubleArray {
        return this.vector().map { it.toDouble() }.toDoubleArray()
    }
}

