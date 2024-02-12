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
    jcenter()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // https://mvnrepository.com/artifact/com.github.zakgof/velvet-video
    implementation("com.github.zakgof:velvet-video:0.5.1")

    // https://mvnrepository.com/artifact/com.github.zakgof/velvet-video
    implementation("com.github.zakgof:velvet-video-natives:0.2.8.full")




}

tasks.withType<Test> {
    useJUnitPlatform()
}
