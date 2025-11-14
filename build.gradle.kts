plugins {
    java
    application
}

repositories { mavenCentral() }

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(23)) }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xlint:all"))
}

dependencies {

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}


application {
    mainClass.set("bsu.edu.cs222.FinanceApp")
}

// <<< attach stdin so Scanner.nextLine() works when using 'gradlew run'
tasks.named<JavaExec>("run") {
    standardInput = System.`in`
    val key = System.getenv("FMP_API_KEY") ?: ""
    if (key.isNotBlank()) jvmArgs("-Dfmp.apiKey=$key")
}

tasks.test {
    useJUnitPlatform()

    // Optional: nicer console output
    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showStandardStreams = false
    }
}

