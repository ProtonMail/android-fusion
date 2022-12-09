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

val nexusUser = System.getenv("NEXUS_USER") ?: "${privateProperties["NEXUS_USER"]}"
val nexusPwd = System.getenv("NEXUS_PWD") ?: "${privateProperties["NEXUS_PWD"]}"
val nexusUrl = System.getenv("NEXUS_URL") ?: "${privateProperties["NEXUS_URL"]}"
val gitLabSSHPrefix =
    System.getenv("GITLAB_SSH_PREFIX") ?: "${privateProperties["GITLAB_SSH_PREFIX"]}"
val gitLabDomain = System.getenv("GITLAB_DOMAIN") ?: "${privateProperties["GITLAB_DOMAIN"]}"
val mavenUser = System.getenv("MAVEN_USER") ?: "${privateProperties["MAVEN_USER"]}"
val mavenPassword = System.getenv("MAVEN_PASSWORD") ?: "${privateProperties["MAVEN_PASSWORD"]}"
val mavenSigningKey =
    System.getenv("MAVEN_SIGNING_KEY") ?: "${privateProperties["MAVEN_SIGNING_KEY"]}"
val mavenSigningKeyPassword = System.getenv("MAVEN_SIGNING_KEY_PASSWORD")
    ?: "${privateProperties["MAVEN_SIGNING_KEY_PASSWORD"]}"

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 23
        targetSdk = 33

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
        viewBinding = true
        buildConfig = false
    }
}

mavenPublishing {
    group = "me.proton"
    version = "0.9.0"
    pom {
        scm {
            connection.set("scm:${gitLabSSHPrefix}:proton/android/shared/fusion")
            developerConnection.set("${gitLabDomain}android/shared/fusion.git")
            url.set("${gitLabDomain}android/shared/fusion")
        }
    }
}

publishing {
    repositories {
        maven {
            url = uri(nexusUrl)
            name = "ProtonNexus"
            credentials {
                username = nexusUser
                password = nexusPwd
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(mavenSigningKey, mavenSigningKeyPassword)
}

dependencies {
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.compose.ui:ui:1.2.1")
    implementation("androidx.compose.ui:ui-test:1.2.0")
    implementation("androidx.compose.ui:ui-test-junit4:1.2.1")
    implementation("androidx.test.ext:junit:1.1.3")
    implementation("androidx.test.espresso:espresso-core:3.4.0")
    implementation("androidx.test.espresso:espresso-contrib:3.4.0")
    implementation("androidx.test.espresso:espresso-intents:3.4.0")
    implementation("androidx.test.uiautomator:uiautomator:2.2.0")
    implementation("androidx.test:core-ktx:1.4.0")
}
