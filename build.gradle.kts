plugins {
    java
    kotlin("jvm") version Versions.kotlinVersion
    kotlin("plugin.serialization") version Versions.kotlinVersion
}

group = "io.github.starwishsama.twitchwatcher"
version = "0.0.1-Canary"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.charleskorn.kaml:kaml:${Versions.kamlVersion}")
    implementation("org.seleniumhq.selenium:selenium-java:{${Versions.seleniumVersion}}")
}

java {
    targetCompatibility = JavaVersion.VERSION_11
    sourceCompatibility = JavaVersion.VERSION_11
}