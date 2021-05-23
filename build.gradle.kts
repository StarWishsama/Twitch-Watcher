/*
 * Copyright (c) 2021-2021 StarWishsama.
 *
 * Class created by StarWishsama on 2021-5-23
 *
 * 此源代码的使用受 GNU General Public License v3.0 许可证约束, 欲阅读此许可证, 可在以下链接查看.
 *
 * https://github.com/StarWishsama/Twitch-Watcher/blob/master/LICENSE
 */

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
    // Selenium WebDriver @ https://www.selenium.dev/
    implementation("org.seleniumhq.selenium:selenium-java:{${Versions.seleniumVersion}}")
}

java {
    targetCompatibility = JavaVersion.VERSION_11
    sourceCompatibility = JavaVersion.VERSION_11
}