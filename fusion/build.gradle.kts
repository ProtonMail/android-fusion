/*
 * Copyright (c) 2022 Proton Technologies AG
 * This file is part of Proton Technologies AG and Proton Mail.
 *
 * Proton Mail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Proton Mail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Proton Mail. If not, see <https://www.gnu.org/licenses/>.
 */

import java.util.Properties
import java.io.FileInputStream
import java.io.IOException

plugins {
    id("com.android.library")
    kotlin("android")
    id("signing")
    id("com.vanniktech.maven.publish") version "0.22.0"
}

val privateProperties = Properties().apply {
    try {
        load(FileInputStream("${rootProject.projectDir}/private.properties"))
    } catch (e: IOException) {
        logger.warn("private.properties file doesn't exist. Full error message: $e")
    }
}

val nexusUser = "NEXUS_USER".fromVariable()
val nexusPwd = "NEXUS_PWD".fromVariable()
val nexusUrl = "NEXUS_URL".fromVariable()
val gitLabSSHPrefix = "GITLAB_SSH_PREFIX".fromVariable()
val gitLabDomain = "GITLAB_DOMAIN".fromVariable()
val gitHubDomain = "GITHUB_PROTONMAIL_DOMAIN".fromVariable()
val mavenUrl = "MAVEN_URL".fromVariable()
val mavenUser = "MAVEN_USER".fromVariable()
val mavenPassword = "MAVEN_PASSWORD".fromVariable()
val mavenSigningKey = "MAVEN_SIGNING_KEY".fromVariable()
val mavenSigningKeyPassword = "MAVEN_SIGNING_KEY_PASSWORD".fromVariable()

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }
}

mavenPublishing {
    group = "me.proton.test"
    version = "0.9.62"
    pom {
        scm {
            connection.set(gitHubDomain)
            developerConnection.set(gitHubDomain)
            url.set(gitHubDomain)
        }
    }
}

publishing {
    repositories {
        maven {
            url = uri(mavenUrl)
            name = "ProtonNexus"
            credentials {
                username = mavenUser
                password = mavenPassword
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(mavenSigningKey, mavenSigningKeyPassword)
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.compose.ui:ui:1.3.3")
    implementation("androidx.compose.ui:ui-test:1.3.3")
    implementation("androidx.compose.ui:ui-test-junit4:1.3.3")
    implementation("androidx.test.ext:junit:1.1.5")
    implementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.test.espresso:espresso-contrib:3.5.1")
    implementation("androidx.test.espresso:espresso-intents:3.5.1")
    implementation("androidx.test.uiautomator:uiautomator:2.2.0")
    implementation("androidx.test:core-ktx:1.5.0")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.compose.material:material:1.3.1")
    implementation("androidx.activity:activity-compose:1.6.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.3.3")
    androidTestImplementation("androidx.compose.material:material:1.3.1")
}

fun String.fromVariable(): String {
    val value = System.getenv(this) ?: "${privateProperties[this]}"
    if (value.isEmpty() || value == "null") {
        logger.warn("Variable $this is not set!")
    }
    return value
}