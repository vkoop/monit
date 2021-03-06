plugins {
    java

    id("com.google.cloud.tools.jib") version "2.7.0"
    id("org.springframework.boot") version "2.3.7.RELEASE"
    id("com.github.ben-manes.versions") version "0.28.0"

    id("org.sonarqube") version "3.0"
}

repositories {
    mavenCentral()
    jcenter()
}

sonarqube {
    properties {
        property("sonar.projectKey", "vkoop_monit")
        property("sonar.organization", "vkoop")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

dependencies {
    val lombokVersion = "1.18.16"
    val springVersion = "2.3.7.RELEASE"

    implementation("org.springframework.boot:spring-boot-starter")

    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    implementation("org.springframework.boot:spring-boot-starter-mail:$springVersion")
    implementation("org.springframework.boot:spring-boot-configuration-processor:$springVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")

    implementation("io.reactivex.rxjava2:rxjava:2.2.10")
    implementation("org.telegram:telegrambots:4.3.1")
    implementation("org.hibernate:hibernate-validator:5.4.2.Final")
    implementation("io.vavr:vavr:0.10.2")

    testImplementation("com.icegreen:greenmail:1.5.10")
}

group = "de.vkoop"
version = "0.0.6-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
