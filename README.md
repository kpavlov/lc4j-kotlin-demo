# LangChain4j Kotlin Demo

[![Java CI with Gradle](https://github.com/kpavlov/lc4j-kotlin-demo/actions/workflows/build.yml/badge.svg)](https://github.com/kpavlov/lc4j-kotlin-demo/actions/workflows/build.yml)

This repository contains a collection of examples demonstrating how to use [LangChain4j](https://github.com/langchain4j/langchain4j) with Kotlin. LangChain4j is a popular Java framework for building applications with Large Language Models (LLMs).

Also checkout [LINKS.md](LINKS.md).

## Overview

The examples in this repository showcase various features and capabilities of LangChain4j, including:

- **Retrieval Augmented Generation (RAG)**: Enhance LLM responses with external knowledge
- **Streaming Completions**: Process LLM responses as they are generated
- **Moderation**: Filter inappropriate content
- **Structured Outputs**: Parse LLM responses into structured data
- **Memory**: Maintain conversation context
- **Testing Utilities**: Mock LLM responses for testing

## Prerequisites

- JDK 23 or later
- Kotlin 2.1.20 or later
- Gradle
- OpenAI API key (set in environment variable `OPENAI_API_KEY`)

## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/kpavlov/lc4j-kotlin-demo.git
   cd lc4j-kotlin-demo
   ```

2. Set your OpenAI API key:
   ```bash
   export OPENAI_API_KEY=your-api-key
   ```

   Or create a `.env` file in the project root with:
   ```
   OPENAI_API_KEY=your-api-key
   ```

3. Build the project:
   ```bash
   ./gradlew build
   ```

## Examples

The examples are organized into different packages in the `src/test/kotlin` directory:

### e00: Asynchronous Programming Basics
- **[Async1JavaTest](src/test/java/e00/Async1JavaTest.java)** and **Async2JavaTest**: Show asynchronous operations in Java using CompletableFuture
- **[AsyncKotlinTest](src/test/java/e00/Async2JavaTest.java)**: Demonstrates basic asynchronous operations in Kotlin using coroutines

### e01: Basic LangChain4j Chat Models
- **[BlockingCompletionsTest](src/test/kotlin/e01/BlockingCompletionsTest.kt)**: Shows how to use LangChain4j with OpenAI in a blocking (synchronous) manner
- **JavaAsyncWrapperTest**: Demonstrates how to wrap Java-style asynchronous operations in Kotlin coroutines
- **SuspendCompletionsTest**: Shows how to use LangChain4j with OpenAI in a non-blocking (asynchronous) manner using Kotlin coroutines

### e02: Parallel Processing
- **[ParallelCompletionsTest.kt](src/test/kotlin/e02parallel/ParallelCompletionsTest.kt)**: Demonstrates how to make parallel (concurrent) requests to LangChain4j's chat models using Kotlin coroutines

### e03: Streaming Completions
- **[CompletionsStreamingTest](src/test/kotlin/e03streaming/CompletionsStreamingTest.kt)**: Shows how to use streaming completions with LangChain4j in a blocking manner
- **[SuspendCompletionsStreamingTest](src/test/kotlin/e03streaming/SuspendCompletionsStreamingTest.kt)**: Demonstrates how to use streaming completions with LangChain4j in a non-blocking manner using Kotlin flows

### e04: AIServices
- **[AiMocksServiceTest](src/test/kotlin/e04/AiMocksServiceTest.kt)**: Demonstrates how to use LangChain4j's built-in ChatModelMock for testing
- **[AiMocksServiceTest.kt](src/test/kotlin/e04/AiMocksServiceTest.kt)**: Shows how to use MockOpenai to mock LLM responses for testing

### e05: Retrieval Augmented Generation (RAG) with Memory
- **[RagWithMemoryTest](src/test/kotlin/e05rag/RagWithMemoryTest.kt)**: Shows how to use Retrieval Augmented Generation (RAG) with memory in LangChain4j, including document loading, embedding, and retrieval

### e06: Content Moderation
- **[ModerationTest](src/test/kotlin/e06moderation/ModerationTest.kt)**: Demonstrates how to use LangChain4j's moderation capabilities to filter inappropriate content

### e07: Embeddings
- **[EmbeddingsTest](src/test/kotlin/e07embeddings/EmbeddingsTest.kt)**: The test demonstrates that two texts about embeddings and AI performance have higher cosine similarity to each other than either has to a completely unrelated Cinderella fairy tale text, proving that the embedding model successfully groups semantically related content together in the vector space.




## Running the Examples

You can run the examples using Gradle:

```bash
./gradlew test
```

Or run a specific test:

```bash
./gradlew test --tests "e05.RagWithMemoryTest"
```

## License

This project is licensed under the GNU General Public License v3.0 (GPLv3) - see the [LICENSE](LICENSE) file for details.

## Resources

For more information about LangChain4j and related topics, check out the [LINKS.md](LINKS.md) file.
