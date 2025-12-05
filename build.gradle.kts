plugins {
    application
    java
}

group = "bsu.edu.cs222"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    // JUnit 5 for your iteration 3 test cases
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")

    // If you use any JSON parsing, you may need something like this:
    // implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
}

application {
    // DEFAULT MAIN CLASS = Console version
    // Switch this to "bsu.edu.cs222.FinanceGuiApp" if you want the GUI to run by default.
    mainClass.set("bsu.edu.cs222.FinanceApp")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17)) // Java 17 recommended
    }
}

tasks.test {
    useJUnitPlatform()
}
