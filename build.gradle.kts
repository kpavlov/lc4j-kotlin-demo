import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.1.21"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
    compilerOptions {
        jvmTarget = JvmTarget.JVM_23
        javaParameters = true
    }
}

dependencies {
    implementation(platform("dev.langchain4j:langchain4j-bom:1.0.1"))
    implementation(group = "dev.langchain4j", name = "langchain4j-kotlin")
    implementation(group = "dev.langchain4j", name = "langchain4j-open-ai")
    implementation(group = "dev.langchain4j", name = "langchain4j-easy-rag")
    implementation("me.kpavlov.aimocks:ai-mocks-openai:0.4.2")
    runtimeOnly("org.slf4j:slf4j-simple:2.0.17")
    testImplementation(group = "dev.langchain4j", name = "langchain4j-core", classifier = "tests")
    testImplementation("me.kpavlov.finchly:finchly:0.1.1")
    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    testImplementation("io.kotest:kotest-assertions-json:5.9.1")
}
