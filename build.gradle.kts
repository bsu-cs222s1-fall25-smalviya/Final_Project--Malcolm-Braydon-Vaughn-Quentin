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

application {
    mainClass.set("bsu.edu.cs222.FinanceApp")
}

// <<< attach stdin so Scanner.nextLine() works when using 'gradlew run'
tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
