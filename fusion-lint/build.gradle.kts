/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 by Proton Technologies A.G. (Switzerland) Email: contact@protonmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
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
