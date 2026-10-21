plugins {
    java
    idea
    `maven-publish`
    id("com.google.protobuf") version "0.10.0"
    id("com.gradleup.shadow") version "9.6.1"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

group = "com.github.technove"
version = "4.1.0"

repositories {
    mavenCentral()
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.36.2"
    }
}

tasks.shadowJar {
    minimize()
    archiveClassifier.set("")
    includeEmptyDirs = false

    listOf(
        "com.eclipsesource",
        "com.google",
        "org.checkerframework",
    ).forEach {
        relocate(it, "co.technove.flare.libs/$it")
    }
}

tasks.jar {
    dependsOn(tasks.shadowJar)
    archiveClassifier.set("dev")
}

dependencies {
    compileOnly("org.jspecify:jspecify:1.0.1")
    implementation("com.eclipsesource.minimal-json:minimal-json:0.9.5")

    implementation("com.google.protobuf:protobuf-java:4.36.2")
    implementation("com.google.guava:guava:33.6.0-jre")
    // implementation("tools.profiler:jfr-converter:4.2") // async-profiler - we need to wait until 4.3 cuz x.x.x releases dont get uploaded to maven and we need 4.2.1
}

publishing {
    publications {
        register<MavenPublication>("maven") {
            shadow {
                groupId = project.group.toString()
                artifactId = project.name
                version = project.version.toString()

                artifact(tasks.shadowJar)

                pom {
                    name.set(project.name)
                    description.set("Flare profiler")
                    url.set("https://airplane.gg/")
                }
            }
        }
    }

    repositories {
        mavenLocal()
    }
}
