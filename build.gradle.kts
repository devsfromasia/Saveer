/*
 * Saveer
 * Copyright (C) 2019  Yannick Seeger, Leon Kappes, Michael Rittmeister
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import com.github.spotbugs.SpotBugsTask
import com.github.spotbugs.SpotBugsXmlReport
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.spotbugs") version "2.0.0"
    checkstyle
    pmd
    application
    java
    kotlin("jvm") version "1.3.40"
}

group = "bot.saveer"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
}

dependencies {
    compile("net.dv8tion:JDA:4.BETA.0_10") {
        exclude(module = "opus-java")
    }
    compile("io.github.cdimascio", "java-dotenv", "5.1.0")
    compile("ch.qos.logback", "logback-classic", "1.3.0-alpha4")
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter", "junit-jupiter", "5.4.2")
}

application {
    mainClassName = "bot.saveer.saveer.Launcher"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

pmd {
    ruleSets = listOf("$rootProject.projectDir/config/pmd/ruleset.xml")
}

tasks {
    "jar"(Jar::class) {
        archiveClassifier += "original"
    }
    "spotbugsMain"(SpotBugsTask::class) {
        reports {
            xml.enabled = false
            html.enabled = true
        }
    }
    "checkstyleMain"(Checkstyle::class) {
        configFile = project.file("config/checkstyle/google_checks.xml")
    }
}

val compileKotlin: KotlinCompile by tasks

compileKotlin.kotlinOptions {
    jvmTarget = "11"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "11"
}

/**
 * @see SingleFileReport.enabled
 */
var SingleFileReport.enabled: Boolean
    get() = this.isEnabled
    set(value) {
        this.isEnabled = value
    }

/**
 * @see SpotBugsXmlReport.enabled
 */
var SpotBugsXmlReport.enabled: Boolean
    get() = this.isEnabled
    set(value) {
        this.isEnabled = value
    }

operator fun <T> Property<T>.plusAssign(value: T) = this.set(value)
