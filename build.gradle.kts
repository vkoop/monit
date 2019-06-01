plugins {
	java
	id("org.springframework.boot") version "2.1.5.RELEASE"
}

repositories {
	mavenCentral()
	jcenter()
}

dependencies {
	val lombokVersion ="1.18.8"
	val springVersion ="2.1.5.RELEASE"

	compile("org.springframework.boot:spring-boot-starter")

	compileOnly("org.projectlombok:lombok:$lombokVersion")
	annotationProcessor("org.projectlombok:lombok:$lombokVersion")

	compile("org.springframework.boot:spring-boot-starter-mail:$springVersion")
	compile("org.springframework.boot:spring-boot-configuration-processor:$springVersion")
	testCompile("org.springframework.boot:spring-boot-starter-test:$springVersion")

	compile("io.reactivex.rxjava2:rxjava:2.2.7")
	compile("org.telegram:telegrambots:4.1.2")
	compile("org.hibernate:hibernate-validator:5.4.2.Final")
	compile ("io.vavr:vavr:0.9.2")

	testCompile ("com.icegreen:greenmail:1.5.7")
}

//compileJava.dependsOn(processResources)

group = "de.vkoop"
version = "0.0.4-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_11
	targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
}
