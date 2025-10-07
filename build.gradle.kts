val springCloudVersion = "2025.0.0"
val resilience4jVersion = "1.7.0"
val openFeignVersion = "13.2.1"

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}")
    }
}

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
    implementation("io.github.openfeign:feign-slf4j:${openFeignVersion}")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
