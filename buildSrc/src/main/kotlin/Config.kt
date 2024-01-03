/*
 * Copyright (c) 2022 Proton Technologies AG
 * This file is part of Proton Technologies AG and ProtonCore.
 *
 * ProtonCore is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProtonCore is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProtonCore.  If not, see <https://www.gnu.org/licenses/>.
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
