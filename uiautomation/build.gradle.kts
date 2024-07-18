import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.codeborne:selenide-appium:2.8.1")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
    implementation("io.appium:java-client:8.6.0")
    implementation("io.github.ashwithpoojary98:appium_flutterfinder_java:1.0.5")
    implementation("io.rest-assured:rest-assured:5.4.0")
    implementation("org.junit.jupiter:junit-jupiter:5.10.2")
    implementation("org.junit-pioneer:junit-pioneer:2.2.0")
    implementation("org.junit.platform:junit-platform-suite-engine:1.10.2")
    implementation("org.slf4j:slf4j-simple:2.0.12")
}

// Test config args and default/fallback values
val testConfigMap = mapOf<String, Any>(
    "test.config.app.identifier" to "nl.ictu.edi.wallet.latest",
    "test.config.device.name" to "emulator-5554",
    "test.config.platform.name" to "Android",
    "test.config.platform.version" to 14.0,
    "test.config.remote" to false,
)

// Set system properties for test config
fun configureTestTask(task: Test) {
    testConfigMap.forEach { (key, value) ->
        task.systemProperty(key, System.getProperty(key, value.toString()))
    }
}

tasks.test {
    configureTestTask(this)
    useJUnitPlatform()
}

tasks.register<Test>("smokeTest") {
    configureTestTask(this)
    useJUnitPlatform {
        includeTags("smoke")

        // Exclude all test suites/wrappers; when using 'includeTags' this is needed to prevent
        // duplicated test executions and ensure only the actual tagged tests are run.
        exclude("suite/**")
    }
}

kotlin {
    jvmToolchain(11)
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "11"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "11"
}
