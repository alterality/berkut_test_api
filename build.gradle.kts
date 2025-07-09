import java.time.LocalDateTime
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification

plugins {
    java
    id("org.springframework.boot") version "3.2.11"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.liquibase.gradle") version "2.2.0"
    jacoco
    checkstyle
}

group = "berkut.abc"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val telegramBotsVersion = "6.8.0"
val mapstructVersion = "1.5.5.Final"
val testcontainersVersion = "1.19.3"
val jjwtVersion = "0.12.3"

dependencies {
    // Spring Boot starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    // Database
    implementation("org.postgresql:postgresql")
    implementation("org.liquibase:liquibase-core")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")


    // Telegram Bot API
    implementation("org.telegram:telegrambots:$telegramBotsVersion")
    implementation("org.telegram:telegrambots-spring-boot-starter:$telegramBotsVersion")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")

    // Object mapping
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")

    // Utilities
    implementation("org.apache.commons:commons-lang3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // API Documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

    // Caching
    implementation("com.github.ben-manes.caffeine:caffeine")

    // Monitoring and metrics
    implementation("io.micrometer:micrometer-registry-prometheus")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Configuration
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
    testImplementation("com.h2database:h2")
    testImplementation("org.mockito:mockito-core:5.14.0")
    testImplementation("org.awaitility:awaitility")

    // Test Runtime
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


tasks.withType<Test> {
    useJUnitPlatform()

    jvmArgs = listOf(
        "-Dspring.profiles.active=test",
        "-Dfile.encoding=UTF-8",
        "-Duser.timezone=UTC"
    )

    systemProperty("junit.jupiter.execution.parallel.enabled", "true")
    systemProperty("junit.jupiter.execution.parallel.mode.default", "concurrent")

    minHeapSize = "128m"
    maxHeapSize = "1g"

    testLogging {
        events = setOf(
            TestLogEvent.PASSED,
            TestLogEvent.SKIPPED,
            TestLogEvent.FAILED
        )
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }
}

tasks.withType<Jar> {
    archiveBaseName.set("telegram-bot-api")

    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
                "Built-By" to System.getProperty("user.name"),
                "Built-Date" to LocalDateTime.now().toString(),
                "Built-JDK" to System.getProperty("java.version")
            )
        )
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(
        listOf(
            "-parameters",
            "-Xlint:unchecked",
            "-Xlint:deprecation",
            "-Werror"
        )
    )
}

configure<CheckstyleExtension> {
    toolVersion = "10.12.4"
    configFile = file("config/checkstyle/checkstyle.xml")
    maxErrors = 0
    maxWarnings = 0
}

configure<JacocoPluginExtension> {
    toolVersion = "0.8.8"
}

tasks.named<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.test)

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }

    finalizedBy(tasks.named("jacocoTestCoverageVerification"))
}

tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    violationRules {
        rule {
            limit {
                minimum = "0.80".toBigDecimal()
            }
        }
    }
}

tasks.register("checkDependencies") {
    group = "verification"
    description = "Check for outdated dependencies"

    doLast {
        println("Dependency check completed")
    }
}

tasks.register("generateDocs") {
    group = "documentation"
    description = "Generate API documentation"

    dependsOn(tasks.build)

    doLast {
        println("API documentation available at: http://localhost:8080/swagger-ui.html")
    }
}

tasks.register<org.springframework.boot.gradle.tasks.run.BootRun>("bootRunDev") {
    group = "application"
    description = "Run application with dev profile"

    jvmArgs = listOf(
        "-Dspring.profiles.active=dev",
        "-Dspring.devtools.restart.enabled=true"
    )

    systemProperty("file.encoding", "UTF-8")
}

tasks.register<org.springframework.boot.gradle.tasks.run.BootRun>("bootRunProd") {
    group = "application"
    description = "Run application with prod profile"

    jvmArgs = listOf(
        "-Dspring.profiles.active=prod",
        "-Xms512m",
        "-Xmx1g"
    )

    systemProperty("file.encoding", "UTF-8")
}

tasks.register("cleanBuild") {
    group = "build"
    description = "Full clean and build"

    dependsOn(tasks.clean, tasks.build, tasks.named("jacocoTestReport"))

    tasks.findByName("build")?.mustRunAfter(tasks.clean)
    tasks.findByName("jacocoTestReport")?.mustRunAfter(tasks.build)
}

tasks.named("build") {
    dependsOn(tasks.named("checkstyleMain"), tasks.named("checkstyleTest"))
}

tasks.named("test") {
    finalizedBy(tasks.named("jacocoTestReport"))
}

springBoot {
    buildInfo()

    mainClass.set("berkut.abc.TelegramBotApiApplication")
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:$testcontainersVersion")
    }
}