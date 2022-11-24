import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    application
}

group = "dev.sihan"
version = "1.0"

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.majorVersion
}

application {
    mainClass.set("MainKt")
}

dependencies {
    // config
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.41")
    implementation("com.typesafe:config:1.4.2")
    implementation("io.github.config4k:config4k:0.5.0")

    // log
    implementation("org.apache.logging.log4j:log4j-api:2.19.0")
    implementation("org.apache.logging.log4j:log4j-core:2.19.0")
    implementation("com.lmax:disruptor:3.4.4")
}