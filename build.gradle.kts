plugins {
    java
    idea
    `maven-publish`
    id("com.google.protobuf") version "0.9.4"
    id("com.gradleup.shadow") version "9.2.2"
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

sourceSets {
    main {
        java {
            srcDir("build/generated/source/proto/main/java")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.28.2"
    }
    generateProtoTasks {
        all().configureEach {
            builtins.first { it.name == "java" }.option("lite")
        }
    }
    generatedFilesBaseDir = "$projectDir/src/generated"
}

tasks.shadowJar {
    minimize()
    archiveClassifier.set("")
    includeEmptyDirs = false

    listOf(
        "com.eclipsesource",
        "com.google",
        "javax.annotation",
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
    compileOnly("org.jetbrains:annotations:24.0.0")
    implementation("com.eclipsesource.minimal-json:minimal-json:0.9.5")

    implementation("com.google.protobuf:protobuf-javalite:4.28.2")
    implementation("com.google.protobuf:protobuf-java-util:4.28.2")
    // implementation("tools.profiler:jfr-converter:4.2") // async-profiler - we need to wait until 4.3 cuz x.x.x releases dont get uploaded to maven and we need 4.2.1
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            from(components["shadow"])
        }
    }
}



