plugins {
    id("java")
    id("idea")
    id("org.springframework.boot") version "3.1.4"
}
apply(plugin = "io.spring.dependency-management")

group = "dev.maxuz"
version = "1.0-SNAPSHOT"

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.telegram:telegrambots:6.8.0")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springframework.security:spring-security-oauth2-client")
    implementation("org.springframework.security:spring-security-oauth2-jose")

    runtimeOnly("org.postgresql:postgresql")
    implementation("org.liquibase:liquibase-core:4.24.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}