plugins {
	java
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "click.dailyfeed"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

extra["springCloudVersion"] = "2025.0.0"

val resilience4jVersion = "1.7.0"
val openFeignVersion = "13.2.1"

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2025.0.0"

dependencies {
    implementation(project(":dailyfeed-code"))

    // spring
	implementation("org.springframework.boot:spring-boot-starter-web")
    // lombok
	compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    // apache commons
    implementation("commons-io:commons-io:2.11.0")

    // resilience4j
    implementation("io.github.resilience4j:resilience4j-feign:${resilience4jVersion}")
    // openfeign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("io.github.openfeign:feign-gson:${openFeignVersion}")
    implementation("io.github.openfeign:feign-core:${openFeignVersion}")
    implementation("io.github.openfeign:feign-jackson:${openFeignVersion}")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
