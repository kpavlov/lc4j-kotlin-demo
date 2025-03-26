# LangChain4j Kotlin Demo

[![Java CI with Gradle](https://github.com/kpavlov/lc4j-kotlin-demo/actions/workflows/gradle.yml/badge.svg?branch=main)](https://github.com/kpavlov/lc4j-kotlin-demo/actions/workflows/gradle.yml)

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
- **Async1JavaTest** and **Async2JavaTest**: Show asynchronous operations in Java using CompletableFuture
- **AsyncKotlinTest**: Demonstrates basic asynchronous operations in Kotlin using coroutines

### e01: Basic LangChain4j Chat Models
- **BlockingCompletionsTest**: Shows how to use LangChain4j with OpenAI in a blocking (synchronous) manner
- **JavaAsyncWrapperTest**: Demonstrates how to wrap Java-style asynchronous operations in Kotlin coroutines
- **SuspendCompletionsTest**: Shows how to use LangChain4j with OpenAI in a non-blocking (asynchronous) manner using Kotlin coroutines

### e02: Parallel Processing
- **ParallelCompletionsTest**: Demonstrates how to make parallel (concurrent) requests to LangChain4j's chat models using Kotlin coroutines

### e03: Streaming Completions
- **CompletionsStreamingTest**: Shows how to use streaming completions with LangChain4j in a blocking manner
- **SuspendCompletionsStreamingTest**: Demonstrates how to use streaming completions with LangChain4j in a non-blocking manner using Kotlin flows

### e04: AIServices
- **Lc4jChatModelMockTest**: Demonstrates how to use LangChain4j's built-in ChatModelMock for testing
- **AiMocksServiceTest**: Shows how to use MockOpenai to mock LLM responses for testing

### e05: Retrieval Augmented Generation (RAG) with Memory
- **RagWithMemoryTest**: Shows how to use Retrieval Augmented Generation (RAG) with memory in LangChain4j, including document loading, embedding, and retrieval

### e06: Content Moderation
- **ModerationTest**: Demonstrates how to use LangChain4j's moderation capabilities to filter inappropriate content


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
