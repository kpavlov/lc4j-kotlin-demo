import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.kotlin.test)
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
    implementation(platform(libs.langchain4j.bom))
    implementation(libs.langchain4j.kotlin)
    implementation(libs.langchain4j.openai)
    implementation(libs.langchain4j.embedding.inprocess)
    implementation(libs.aimocks.openai)
    runtimeOnly(libs.slf4j.simple)
    testImplementation(libs.finchly)
    testImplementation(libs.assertj.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(group = "dev.langchain4j", name = "langchain4j-core", classifier = "tests")
}
