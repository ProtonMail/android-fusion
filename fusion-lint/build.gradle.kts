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
    kotlin("jvm")
    id("signing")
    id("com.vanniktech.maven.publish") version "0.22.0"
}

dependencies {
    compileOnly(libs.bundles.lint)
}

tasks.jar {
    manifest {
        attributes(
            "Manifest-Version" to "1.0",
            "Implementation-Title" to "Fusion Lint Rules",
            "Implementation-Version" to "1.0"
        )
    }
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Lint-Registry-v2"] = "me.proton.test.fusion.lint.RobotIssueRegistry"
    }
}

mavenPublishing {
    group = "me.proton.test"
    version = "1.0.2"
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
