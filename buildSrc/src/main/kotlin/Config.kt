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

import org.gradle.api.Project
import java.io.FileInputStream
import java.io.IOException
import java.util.Properties


val Project.privateProperties get() = Properties().apply {
    try {
        load(FileInputStream("${projectDir.path}/../private.properties"))
    } catch (ex: IOException) {
        logger.warn("private.properties file doesn't exist. Full error message: $ex")
    }
}

fun Project.fromVariable(string: String): String {
    val value = System.getenv(string) ?: privateProperties?.getProperty(string, "")
    if (value.isEmpty()) {
        logger.debug("Variable $string is not set!")
    }
    return value
}

val Project.NEXUS_USER: String get() = fromVariable("NEXUS_USER")
val Project.NEXUS_PWD: String get() = fromVariable("NEXUS_PWD")
val Project.NEXUS_URL: String get() = fromVariable("NEXUS_URL")
val Project.GITLAB_SSH_PREFIX: String get() = fromVariable("GITLAB_SSH_PREFIX")
val Project.GITLAB_DOMAIN: String get() = fromVariable("GITLAB_DOMAIN")
val Project.GITHUB_PROTONMAIL_DOMAIN: String get() = fromVariable("GITHUB_PROTONMAIL_DOMAIN")
val Project.MAVEN_URL: String get() = fromVariable("MAVEN_URL")
val Project.MAVEN_CENTRAL_USERNAME: String get() = fromVariable("MAVEN_CENTRAL_USERNAME")
val Project.MAVEN_CENTRAL_PASSWORD: String get() = fromVariable("MAVEN_CENTRAL_PASSWORD")
val Project.MAVEN_SIGNING_KEY: String get() = fromVariable("MAVEN_SIGNING_KEY")
val Project.MAVEN_SIGNING_KEY_PASSWORD: String get() = fromVariable("MAVEN_SIGNING_KEY_PASSWORD")
