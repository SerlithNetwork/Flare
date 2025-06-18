plugins {
    java
    idea
    `maven-publish`
    id("com.google.protobuf") version "0.9.4"
    id("com.gradleup.shadow") version "8.3.0"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

group = "com.github.technove"
version = "4.0-SNAPSHOT"

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
        artifact = "com.google.protobuf:protoc:3.8.0"
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

    implementation("com.google.protobuf:protobuf-javalite:3.17.3")
    implementation("com.google.protobuf:protobuf-java-util:3.14.0")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            shadow {
                project.shadow.component(this@create)
            }
        }
    }
}



