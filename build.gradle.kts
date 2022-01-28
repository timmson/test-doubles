plugins {
    java
}

group = "ru.technicalExcellence.codingDojo"
version = "1.0"

repositories {
    mavenCentral()
}

val servletVersion = "4.0.1"
val junitVersion = "5.8.2"
val mockitoVersion = "4.2.0"

dependencies {
    implementation("javax.servlet:javax.servlet-api:$servletVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:$mockitoVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
}