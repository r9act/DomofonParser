plugins {
    java
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "ru.mishkin"
version = "0.0.1-SNAPSHOT"



java {
    sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // https://mvnrepository.com/artifact/org.mock-server/mockserver-client-java
    implementation("org.mock-server:mockserver-client-java:5.15.0")
    // https://mvnrepository.com/artifact/org.mock-server/mockserver-netty
    testImplementation("org.mock-server:mockserver-netty:5.15.0")

}

tasks.withType<Test> {
    useJUnitPlatform()
}
