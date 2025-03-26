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


## Running the Examples

You can run the examples using Gradle:

```bash
./gradlew test
```

Or run a specific test:

```bash
./gradlew test --tests "e05.RagWithMemoryTest"
```

## Resources

For more information about LangChain4j and related topics, check out the [LINKS.md](LINKS.md) file.
