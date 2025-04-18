plugins {
    kotlin("jvm") version "2.1.20"
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
        javaParameters = true
    }
}

dependencies {
    implementation(platform("dev.langchain4j:langchain4j-bom:1.0.0-beta3"))
    implementation(group = "dev.langchain4j", name = "langchain4j")
    implementation(group = "dev.langchain4j", name = "langchain4j-open-ai")
    implementation(group = "dev.langchain4j", name = "langchain4j-easy-rag")
    implementation("me.kpavlov.aimocks:ai-mocks-openai:0.3.1")
    runtimeOnly("org.slf4j:slf4j-simple")
    testImplementation(group = "dev.langchain4j", name = "langchain4j-core", classifier = "tests")
    testImplementation("me.kpavlov.finchly:finchly:0.1.1")
    testImplementation("io.kotest:kotest-assertions-json:5.9.1")
}
