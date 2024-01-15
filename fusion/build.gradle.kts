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

plugins {
    id("com.android.library")
    kotlin("android")
    id("signing")
    id("com.vanniktech.maven.publish") version "0.22.0"
}

android {
    compileSdk = 34

    defaultConfig {
        namespace = "me.proton.test.fusion"
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    lint {
        checkTestSources = true
    }
}

mavenPublishing {
    group = "me.proton.test"
    version = "0.9.94"
    pom {
        scm {
            connection.set(GITHUB_PROTONMAIL_DOMAIN)
            developerConnection.set(GITHUB_PROTONMAIL_DOMAIN)
            url.set(GITHUB_PROTONMAIL_DOMAIN)
        }
    }
}

publishing {
    repositories {
        maven {
            url = uri(MAVEN_URL)
            name = "ProtonNexus"
            credentials {
                username = MAVEN_CENTRAL_USERNAME
                password = MAVEN_CENTRAL_PASSWORD
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(MAVEN_SIGNING_KEY, MAVEN_SIGNING_KEY_PASSWORD)
}

dependencies {
    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0") {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }

    implementation(libs.bundles.compose)
    implementation(libs.bundles.espresso)
    implementation(libs.bundles.coreKtx)
    implementation(libs.uiautomator)

    debugImplementation(libs.compose.ui.test.manifest)

    androidTestImplementation(libs.compose.material)
    androidTestImplementation(libs.navigation.testing)
    androidTestImplementation(libs.navigation.compose)
    lintChecks(project(":fusion-lint"))
}
