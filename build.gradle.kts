/*
 * Copyright (c) 2021-2021 StarWishsama.
 *
 * Class created by StarWishsama on 2021-5-23
 *
 * 此源代码的使用受 GNU General Public License v3.0 许可证约束, 欲阅读此许可证, 可在以下链接查看.
 *
 * https://github.com/StarWishsama/Twitch-Watcher/blob/master/LICENSE
 */

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    kotlin("jvm") version Versions.kotlinVersion
    kotlin("plugin.serialization") version Versions.kotlinVersion
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("com.github.gmazzo.buildconfig") version "3.0.0"
}

group = "io.github.starwishsama.twitchwatcher"
version = "0.0.1-Canary"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.charleskorn.kaml:kaml:${Versions.kamlVersion}")
    // Selenium WebDriver @ https://www.selenium.dev/
    implementation("org.seleniumhq.selenium:selenium-java:${Versions.seleniumVersion}")

    // Jline a Java library for handling console input @ https://github.com/jline/jline3
    implementation("org.jline:jline:${Versions.jlineVersion}")

    // Jansi needed by JLine
    implementation("org.fusesource.jansi:jansi:${Versions.jansiVersion}")
}

java {
    targetCompatibility = JavaVersion.VERSION_11
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks.withType<ShadowJar> {

}